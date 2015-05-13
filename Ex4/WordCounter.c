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
	int fd, num;
	char message[READ_BUFF_SIZE];

	data = (ListenerData*)param;
	buff = data->buff;

	mknod(data->pipe_file, S_IFIFO | FILE_ACCESS_RW, 0);

	while (1) {
		// Wait for a connection on the pipe
		fd = open(data->pipe_file, O_RDONLY);
		if (fd <= 0) {
			// Handle error
		}

		// Read data from pipe
		while ((num = read(fd, message, READ_BUFF_SIZE)) > 0) {
			// Parse read data into messages, enqueue them, etc...
			// TODO: Complete this part
			// TODO: Make sure the thread exits at some point
		}
		close(fd);
	}
}

// TODO: Implement the other required functions
