//
//  BoundedBuffer.c
//  
//
//  Created by Ido Michael on 5/9/15.
//
//

#include "BoundedBuffer.h"


/*
 * Initializes the buffer with the specified capacity.
 * This function should allocate the buffer, initialize its properties
 * and also initialize its mutex and condition variables.
 * It should set its finished flag to 0.
 */
void bounded_buffer_init(BoundedBuffer *buff, int capacity) {
    
    // Initialize buffer and check malloc
   	buff->buffer = (char**) malloc(capacity * sizeof(char*));
    if (buff->buffer == NULL) {
        fprintf(stderr, "Cannot allocate buffer.\n");
        exit (1);
    }
    buff->size = 0;
    buff->capacity = capacity;
    buff->head = 0;
    buff->tail = capacity - 1;
    buff->mutex = NULL;
    buff->cv_empty = NULL;
    buff->cv_full = NULL;
    // Should i check allocation? also how to initialize?
    buff->finished = 0;
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
int bounded_buffer_enqueue(BoundedBuffer *buff, char *data) {
    sem_t sem_one;
    if(buff->size == buff->capacity) {

        // Wait buff full.
        // Case of buffer finished - return 0.
        if(buff->finished == 1) {
            return 0;
        }
    }
    
    // Enqueue succeded
    buff->buffer->head = data;
    buff->head += 1;
    return 1;
    
    // buff has finished.
    if(buff->finished == 1) {
        return 0;
    }
}
