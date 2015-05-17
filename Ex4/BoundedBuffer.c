//Ido Michael 201157138
//Dana Erlich 200400950

#ifndef BOUNDEDBUFFER_H_
#define BOUNDEDBUFFER_H_

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

typedef struct {
	char **buffer;
	int size;
	int capacity;
	int head;
	int tail;
	pthread_mutex_t mutex;
	pthread_cond_t cv_empty;
	pthread_cond_t cv_full;
	int finished;
} BoundedBuffer;

/*
 * Initializes the buffer with the specified capacity.
 * This function should allocate the buffer, initialize its properties
 * and also initialize its mutex and condition variables.
 * It should set its finished flag to 0.
 */
void bounded_buffer_init(BoundedBuffer *buff, int capacity){
	buff->buffer = (char**) malloc (capacity * sizeof(char*));
	if (buff->buffer == NULL) {
		fprintf(stderr, "Error: Cannot allocate memory.\n");
		exit (1);
	}
	buff->size = 0;
	buff->capacity = capacity;
	buff->head = 0;
	buff->tail = capacity - 1;
	buff->finished = 0;
	pthread_mutex_init(&(buff->mutex), NULL); 	 // Init mutex
	pthread_cond_init(&(buff->cv_empty), NULL); // Init empty condition
	pthread_cond_init(&(buff->cv_full), NULL);  // Init full condition

}

/*
 * Enqueue a string (char pointer) to the buffer.
 * This function should add an element to the buffer. If the buffer is full,
 * it should wait until it is not full, or until it has finished.
 * If the buffer has finished (either after waiting or even before), it should
 * simply return 0.
 * If the enqueue operation was successful, it should return 1. In this case it
 * should also signal that the buffer is not empty.
 * This function should be synchronized on the buffer's mutex!
 */
int bounded_buffer_enqueue(BoundedBuffer *buff, char *data){
	if (buff->finished == 1) {
		return 0;
	}

	pthread_mutex_lock(&(buff->mutex)); // Begin critical section
	
	if (buff->size == buff->capacity) { // Wait until it is not full
		pthread_cond_wait(&(buff->cv_full), &(buff->mutex));
	}
	
	// enqueue data and set to 'not empty'
	buff->tail = ((buff->tail) + 1) % buff->capacity;
	buff->buffer[buff->tail] = data;
	buff->size++;
	buff->empty = FALSE;

	if (buff->finished == 1) {
		return 0;
	}
	
	pthread_cond_signal(&(buff->cv_empty)); // Signal for any thread waiting for a value
	
	pthread_mutex_unlock(&(buff->mutex)); // Exit critical section

	return 1;
}
/*
 * Dequeues a string (char pointer) from the buffer.
 * This function should remove the head element of the buffer and return it.
 * If the buffer is empty, it should wait until it is not empty, or until it has finished.
 * If the buffer has finished (either after waiting or even before), it should
 * simply return NULL.
 * If the dequeue operation was successful, it should signal that the buffer is not full.
 * This function should be synchronized on the buffer's mutex!
 */
char *bounded_buffer_dequeue(BoundedBuffer *buff){
	if (buff->finished == 1) {
		return 0;
	}

	pthread_mutex_lock(&(buff->mutex)); // Begin critical section
	
	if (buff->size == 0) { // Wait until it is not empty
		pthread_cond_wait(&(buff->cv_empty), &(buff->mutex));
	}
	
	// dequeue data and set to 'not full'
	data = buff->buffer[buff->tail];
	buff->tail = ((buff->tail) - 1) % buff->capacity;
	buff->size--;
	buff->full = FALSE;

	if (buff->finished == 1) {
		return 0;
	}

	pthread_cond_signal(&(buff->cv_full)); // Signal for any thread waiting for a value
	
	pthread_mutex_unlock(&(buff->mutex)); // Exit critical section

	return 1;
}

/*
 * Sets the buffer as finished.
 * This function sets the finished flag to 1 and then wakes up all threads that are
 * waiting on the condition variables of this buffer.
 * This function should be synchronized on the buffer's mutex!
 */
void bounded_buffer_finish(BoundedBuffer *buff){
	
	pthread_mutex_lock(&(buff->mutex)); // Begin critical section
	
	buff->finished = 1;
	
	pthread_cond_broadcast(&(buff->cv_full));
	pthread_cond_broadcast(&(buff->cv_empty));

	pthread_mutex_unlock(&(buff->mutex)); // Exit critical section
}

/*
 * Frees the buffer memory and destroys mutex and condition variables.
 */
void bounded_buffer_destroy(BoundedBuffer *buff){
	int i;
	for (i = 0; i < capacity; i++)
	{
		res = buff->buffer[i];
		free(res);
	}
	pthread_mutex_destroy(&(cubbyHole->mutex)); // Destroy mutex
	pthread_cond_destroy(&(cubbyHole->cv_empty)); // Destroy empty condition
	pthread_cond_destroy(&(cubbyHole->cv_full));  // Destroy full condition
	free(buff);
}

#endif /* BOUNDEDBUFFER_H_ */
