all: wordcounter
clean:
	rm wordcounter *.o

wordcounter: WordCounter.o BoundedBuffer.o
	gcc -pthread -o wordcounter WordCounter.o BoundedBuffer.o

BoundedBuffer.o: BoundedBuffer.c BoundedBuffer.h
	gcc -c BoundedBuffer.c

WordCounter.o: WordCounter.c WordCounter.h BoundedBuffer.c BoundedBuffer.h
	gcc -c WordCounter.c
