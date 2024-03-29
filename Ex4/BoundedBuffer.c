//Ido Michael 201157138
//Dana Erlich 200400950

#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>
#include "BoundedBuffer.h"


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
	buff->tail = 0; //buff->tail = capacity - 1;
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
	pthread_mutex_lock(&(buff->mutex)); // Begin critical section
    if (buff->finished == 1) {
        pthread_mutex_unlock(&(buff->mutex));
		return 0;
	}

	while (buff->size == buff->capacity) { // Wait until it is not full
		pthread_cond_wait(&(buff->cv_empty), &(buff->mutex));
        if (buff->finished) {
            pthread_mutex_unlock(&(buff->mutex));
            return 0;
        }
	}

	// enqueue data and set to 'not empty'
	buff->buffer[buff->tail] = data; // reverse rows?
	buff->tail = ((buff->tail) + 1) % buff->capacity;
	buff->size++;

	pthread_cond_signal(&(buff->cv_full)); // Signal for any thread waiting for a value

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
    pthread_mutex_lock(&(buff->mutex)); // Begin critical section
    if (buff->finished == 1) {
        pthread_mutex_unlock(&(buff->mutex));
		return 0;
	}

	while (buff->size == 0) { // Wait until it is not empty
		pthread_cond_wait(&(buff->cv_full), &(buff->mutex));
        if (buff->finished) {
            pthread_mutex_unlock(&(buff->mutex));
            return NULL;
        }
	}

	// dequeue data and set to 'not full'
    char *data = buff->buffer[buff->head];
    buff->head = (buff->head + 1) % buff->capacity;
    buff->size--;
    pthread_cond_signal(&(buff->cv_empty));// Signal for any thread waiting for a value

	pthread_mutex_unlock(&(buff->mutex));    //exit critical section

    return data;
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
	pthread_mutex_destroy(&(buff->mutex)); // Destroy mutex
	pthread_cond_destroy(&(buff->cv_empty)); // Destroy empty condition
	pthread_cond_destroy(&(buff->cv_full));  // Destroy full condition
	free(buff->buffer);
}

