// Dana Erlich 200400950

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/stat.h>
#include "WordCounter.h"

#define MAX_LOG_LINE_SIZE 1024
#define MAX_DATE_SIZE 25
#define DATE_FORMAT "%Y-%m-%d %H:%M:%S"

// TODO: Add more defines as required

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
	int fd, num, len;
	char message[READ_BUFF_SIZE];
	char *token;
	char *token_filename;

	data = (ListenerData*)param;
	buff = data->buff;

	mknod(data->pipe_file, S_IFIFO | FILE_ACCESS_RW, 0);

	while (1) {
		// Wait for a connection on the pipe
		fd = open(data->pipe_file, O_RDONLY);
		if (fd <= 0) {
			perror("open");
			exit(1);		
		}

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
					if(remove(data->pipe_file) != 0) {
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

}
/*
 * A word-counting function. Counts the words in file_name and returns the number.
 */
int count_words_in_file(char *file_name){
	unsigned char words_buffer[FILE_READ_BUFFER_SIZE];
	FILE *file;
	int len = 0;	
	int word_counter;
	if (file_name == NULL) {
		return 0;
	}

	file = fopen(file_name, "r");
	if (file == NULL) {
		return 0;
	}

	while ((len = fread(words_buffer, 1, FILE_READ_BUFFER_SIZE, file)) > 0) {
		int current_char = 0;
		while((words_buffer[current_char] != EOF) && (current_char != (FILE_READ_BUFFER_SIZE - 1))){
			if(!is_alphabetic([current_char])) {
				word_counter++;
		}
			current_char++;
	}

    fclose(file);
	return word_counter;
}

/*
 * logs the number of words in the file to the output log file.
 */
void log_count(WordCounterData *counter_data, char *file_name, int count);



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
int main(int argc, char *argv[]);


