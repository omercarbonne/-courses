CC = gcc
CXX_FLAGS = -sdt=c99 -Wextra -Wvla -Werror -g

snake: snakes_and_ladders.o markov_chain.o linked_list.o
	${CC} -o snakes_and_ladders.o markov_chain.o linked_list.o

tweets: tweets_generator.o markov_chain.o linked_list.o
	${CC} -o tweets_generator.o markov_chain.o linked_list.o

snakes_and_ladders.o: snakes_and_ladders.c markov_chain.h linked_list.h
	${CC} ${CXX_FLAGS} -c snakes_and_ladders.c

tweets_generator.o: tweets_generator.c markov_chain.h linked_list.h
	${CC} ${CXX_FLAGS} -c tweets_generator.c

markov_chain.o: markov_chain.c markov_chain.h linked_list.h
	${CC} ${CXX_FLAGS} -c markov_chain.c

linked_list.o: linked_list.h linked_list.c
	${CC} ${CXX_FLAGS} linked_list.c
