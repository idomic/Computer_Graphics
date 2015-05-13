/*
 * WordCounter.h
 *
 *  Created on: Apr 26, 2013
 *      Author: yotam
 */

#ifndef WORDCOUNTER_H_
#define WORDCOUNTER_H_

#include "BoundedBuffer.h"

#define READ_BUFF_SIZE 1024
#define STDIN_READ_BUFF_SIZE 80
#define FILE_READ_BUFFER_SIZE 1024
#define FILE_QUEUE_SIZE 10

#define FILE_ACCESS_RW 0666

typedef struct {
	BoundedBuffer *buff;
	FILE *log_file;
} WordCounterData;

typedef struct {
	BoundedBuffer *buff;
	char *pipe;
} ListenerData;

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
void *run_listener(void *param);

/*
 * WordCounter thread starting point.
 * The word counter reads file names from the files queue, one by one, and counts the words in them.
 * It will write the result to a log file.
 * Then it should free the memory of the dequeued file name string (it was allocated by the Listener thread).
 * If the dequeue operation fails (returns NULL), it means that the application is trying
 * to exit and therefore the thread should simply terminate.
 */
void *run_wordcounter(void *param);

/*
 * A word-counting function. Counts the words in file_name and returns the number.
 */
int count_words_in_file(char *file_name);

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


#endif /* WORDCOUNTER_H_ */
