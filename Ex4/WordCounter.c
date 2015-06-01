//Ido Michael 201157138
//Dana Erlich 200400950

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "WordCounter.h"
#include "BoundedBuffer.h"


#define MAX_LOG_LINE_SIZE 1024
#define MAX_DATE_SIZE 25
#define DATE_FORMAT "%Y-%m-%d %H:%M:%S"
#define CMD_EXIT "exit\n"

/*
 * A helper function that prints current time to a string.
 * Time is printed in the format required by exercise specification.
 */
void dateprintf(char *buff, int max_size, const char *format) {
	time_t timer;
	struct tm *tm_info;
	time(&timer);
	tm_info = localtime(&timer);
	strftime(buff, max_size, format, tm_info);
}

/*
 * Function to determine if a character is alphabetic
 * returns 1 if alphabetic, 0 if not
 */
int is_alphabetic (unsigned char c) {
    if ( (c >= 'a' && c <= 'z') || ( c >= 'A' && c <= 'Z')) {
        return 1;
    }
    else {
        return 0;
    }
}

/*
 * Listener thread starting point.
 * Creates a named pipe (the name should be supplied by the main function) and waits for
 * a connection on it. Once a connection has been received, reads the data from it and
 * parses the file names out of the data buffer. Each file name is copied to a new string
 * and then enqueued to the files queue.
 * If the enqueue operation fails (returns 0), it means that the application is trying to exit.
 * Therefore the Listener thread should stop. Before stopping, it should remove the pipe file and
 * free the memory of the filename it failed to enqueue.
 */
 void *run_listener(void *param) {
	ListenerData *data;
	BoundedBuffer *buff;
	int fd, num, len, enqueue_result;
	char message[READ_BUFF_SIZE];
	char *token;
	char *token_filename;

	data = (ListenerData*)param;
	buff = data->buff;

	mknod(data->pipe, S_IFIFO | FILE_ACCESS_RW, 0);

	while (1) {
		// Wait for a connection on the pipe
		printf("waiting for connection\n");
		fd = open(data->pipe, O_RDONLY);
		if (fd <= 0) {
            fprintf(stderr, "Error: cannot open pipe.\n");
			exit(0);
		}
		printf("connection recieved.\n");
		// Read data from pipe
		while ((num = read(fd, message, READ_BUFF_SIZE)) > 0) {
			message[num-1] = '\0';
			// Parse seperate file names by new line
			token = strtok(message,"\n");

			while (token != NULL) {
				len = strlen(token);
				// Allocate memory for a file name
				token_filename = (char*) malloc (len + 1);
				if (token_filename == NULL) {
					fprintf(stderr, "Error: Cannot allocate memory.\n");
					exit (1);
				}
				// Copy  file name from token to token_filename
				strncpy(token_filename, token, len);
				token_filename[len] = '\0';
				// Enqueue the file name to the files queue
				enqueue_result = bounded_buffer_enqueue(buff, token_filename);
				if (enqueue_result == 0) { // Enqueue has failed
					// Free the last allocated file name buffer
					free(token_filename);
					// Remove the pipe file
					if(remove(data->pipe) != 0) {
						fprintf(stderr, "Error: pipe file could not be removed.\n");
					}
					return NULL;
				}
				//File name has enqueud successfully.
				token = strtok(NULL, "\n");
			}
		}
		close(fd);
	}
}

/*
 * WordCounter thread starting point.
 * The word counter reads file names from the files queue, one by one, and counts the words in them.
 * It will write the result to a log file.
 * Then it should free the memory of the dequeued file name string (it was allocated by the Listener thread).
 * If the dequeue operation fails (returns NULL), it means that the application is trying
 * to exit and therefore the thread should simply terminate.
 */
void *run_wordcounter(void *param){
    BoundedBuffer *buff;
    char *file;
    int word_counter;
    WordCounterData *data;
    data = (WordCounterData*)param;
    buff = data->buff;
    while (1) {
        file = bounded_buffer_dequeue(buff);
        if (file == NULL) {
            return NULL;
        }
        //1. Count the words in the file and output the operation result to the screen.
		word_counter = count_words_in_file(file);
        if (word_counter == -1) {
           printf("file %s %s\n",file ,"doesn't exist");
           log_count(data, file , word_counter);
        } else {
           printf("Counting words for file: ../ %s\n",file);
           printf("File: ../ %s %s %d\n",file," || Words: ",word_counter);
            //2. Log the result to the file.
			log_count(data, file , word_counter);
			//3. Free the buffer that holds the file name (it was allocated by the Listener thread - this is the place to free it).
			free(file);
        }
    }
}
/*
 * A word-counting function. Counts the words in file_name and returns the number.
 */
int count_words_in_file(char *file_name){
	unsigned char words_buffer[FILE_READ_BUFFER_SIZE];
	FILE *file;
	int len = 0;
	int word_counter = 0;
	int was_last_alpha = 0; //Flag that indicates if last char was alphabetic
	if (file_name == NULL) {
		return -1;
	}

	file = fopen(file_name, "r");
	if (file == NULL) {
		return -1;
	}

	while ((len = fread(words_buffer, 1, FILE_READ_BUFFER_SIZE, file)) > 0) {
		int current_char = 0;
		while((words_buffer[current_char] != EOF) && (current_char < len)){
			if(!is_alphabetic(words_buffer[current_char]) && was_last_alpha) {
				word_counter++;
				was_last_alpha = 0;
			}
			if(is_alphabetic(words_buffer[current_char])){
				was_last_alpha = 1;
			}
			current_char++;
		}
	}
    fclose(file);
	return word_counter;
}
/*
 * logs the number of words in the file to the output log file.
 */
void log_count(WordCounterData *counter_data, char *file_name, int count){
	FILE *log_file;
    log_file = counter_data->log_file;
	//create buffer for log data
    char insert_to_log[FILE_READ_BUFFER_SIZE];
	//create buffer for casting int to string
    char number_to_string[FILE_READ_BUFFER_SIZE];
    if (count == -1) {
        strcat(insert_to_log, " File: ");
        strcat(insert_to_log, file_name);
        strcat(insert_to_log, " doesnt exist");
        strcat(insert_to_log, "\n");
    } else {
    	//insert date string into buffer
        dateprintf(insert_to_log, FILE_READ_BUFFER_SIZE, DATE_FORMAT);
        strcat(insert_to_log, " File: ");
        strcat(insert_to_log, file_name);
        strcat(insert_to_log, "|| Number of words: ");
        sprintf(number_to_string, "%d", count);
        strcat(insert_to_log, number_to_string);
        strcat(insert_to_log, "\n");
    }
	    //write log data to log file
        fputs(insert_to_log, log_file);
}



/*
 * Main function.
 * Reads command line arguments in the format:
 * 		./WordCounter pipe_name destination_log_file_name
 * Where pipe_name is the name of FIFO pipe that the Listener should create and
 * destination_log_file_name is the destination file name where the log should be written.
 * This function should create the files queue and prepare the parameters to the Listener and
 * WordCounter threads. Then, it should create these threads.
 * After threads are created, this function should control them as follows:
 * it should read input from user, and if the input line is "exit" (or "exit\n"), it should
 * set the files queue as "finished". This should make the threads terminate (possibly only
 * when the next connection is received).
 * At the end the function should join the threads and exit.
 */
int main(int argc, char *argv[]){
	char *user_input_buff;
    BoundedBuffer buff;
    WordCounterData word_counter_data;
    ListenerData listener_data;
    pthread_t listener_thread, word_counter_thread;

	//Arguments check
	if(argc < 3 || argc > 3){
		printf("Usage: ./WordCounter pipe_file_name destination_log_file_name");
		exit(1);
	}
	char *pipe_file_name = argv[1];
	char *destination_log_file_name = argv[2];

	//Create the files queue
	bounded_buffer_init(&buff, FILE_QUEUE_SIZE);

	//Create and start the two threads
	word_counter_data.buff = &buff;
    word_counter_data.log_file = fopen(destination_log_file_name, "w");
    if (word_counter_data.log_file == NULL) {
    		// Ignore (return) if can't open the dest file
    		exit(1);
    	}

    listener_data.buff = &buff;
    listener_data.pipe = pipe_file_name;
    pthread_create(&listener_thread, NULL, run_listener, (void*)(&listener_data));
    pthread_create(&word_counter_thread, NULL, run_wordcounter, (void*)(&word_counter_data));
    printf("Logging results to file %s\n",destination_log_file_name);

	//Read input (lines) from the user (in a loop)
	user_input_buff = (char*) malloc (STDIN_READ_BUFF_SIZE);
	if (user_input_buff == NULL) {
		fprintf(stderr, "Error: Cannot allocate memory.\n");
		exit (1);
	}
	while (1) {
        if (fgets (user_input_buff, STDIN_READ_BUFF_SIZE, stdin) == NULL) {
            fprintf(stderr, "Error: Read error has occurred.\n");
            exit(1);
        }
        //Check if input is equal to CMD_EXIT
		if (strcmp (user_input_buff, CMD_EXIT) == 0) {
            bounded_buffer_finish(&buff);
            break;
        }
    }

	//Join the threads
	pthread_join(listener_thread, NULL);
    pthread_join(word_counter_thread, NULL);
	free(user_input_buff);
	bounded_buffer_destroy(&buff);
    printf("WordCounter has exited successfully.\n");
    fclose(word_counter_data.log_file);
    return 0;
}

