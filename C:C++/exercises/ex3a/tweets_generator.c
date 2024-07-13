#include "markov_chain.h"
#include "linked_list.h"
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>


int str_to_int(const char s[]);
bool check_path(const char path[], FILE ** file);
int fill_database(FILE *fp, int words_to_read, MarkovChain *markov_chain);

#define USAGE_ERROR "USAGE: YOU NEED TO ENTER - SEED NUMBER,"\
"NUM OF TWEETS, PATH TO SENTENCES FILE, NUM OF WORDS TO READ"
#define PATH_ERROR "THE PATH TO SENTENCES FILE IS INVALID"
#define MAX_SENT_LEN 1000
#define MAX_TWEET_LEN 20
#define NUM_ARGS_OPTION_1 4
#define NUM_ARGS_OPTION_2 5

int main(int argc, char *argv[])
{
    if (argc == NUM_ARGS_OPTION_1 || argc == NUM_ARGS_OPTION_2)
    {
        int seed = str_to_int(argv[1]);
        srand(seed);
        int tweets_num = str_to_int(argv[2]);
        FILE* file;
        if(!check_path(argv[3], &file)){
            printf(PATH_ERROR);
            return EXIT_FAILURE;
        }
        int num_to_read;
        if(argc == NUM_ARGS_OPTION_2){
            num_to_read = str_to_int(argv[4]);
        }
        else{
            num_to_read = 0;
        }
        LinkedList* database = malloc(sizeof(LinkedList));
        database->first = NULL;
        database->last = NULL;
        database->size = 0;
        MarkovChain* mark_ch = malloc(sizeof(MarkovChain));
        mark_ch->database = database;
        if(fill_database(file, num_to_read, mark_ch)){
            return EXIT_FAILURE;
        }
        fclose(file);
        int count = 0;
        MarkovNode * first_node;
        while (count < tweets_num) {
            first_node = get_first_random_node(mark_ch);
            printf("Tweet %d: ", count+1);
            generate_tweet(mark_ch, first_node, MAX_TWEET_LEN);
            printf("\n");
            count++;
        }
        free_database(&mark_ch);
    }
    else{
        printf(USAGE_ERROR);
        return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
}

int fill_database(FILE *fp, int words_to_read, MarkovChain *markov_chain)
{
    int count = 0;
    int step = 1;
    if (words_to_read == 0)
    {
        step = 0; //makes sure the loop won't stop till the end.
        count = -1;
    }
    char line[MAX_SENT_LEN];
    while(count < words_to_read)
    {
        if(!fgets(line, MAX_SENT_LEN, fp))
        {
            break;
        }
        strtok(line, "\n"); // removing the \n
        char* word = strtok(line, " ");
        Node * node1 = add_to_database(markov_chain, word);
        count = count + step;
        if(node1 == NULL)
        {
            free_database(&markov_chain);
            return EXIT_FAILURE;
        }
        word = strtok(NULL, " ");
        Node * node2;
        while (word != NULL && count < words_to_read)
        {
            node2 = add_to_database(markov_chain, word);
            count = count + step;
            if(node2 == NULL)
            {
                free_database(&markov_chain);
                return EXIT_FAILURE;
            }
            add_node_to_frequencies_list(node1->data, node2->data);
            word = strtok(NULL, " ");
            node1 = node2;
        }
    }
    return EXIT_SUCCESS;
}

bool check_path(const char path[], FILE ** file)
{
    FILE* temp = fopen(path, "r");
    if (temp)
    {
        *file = temp;
        return true;
    }
    return false;
}

int str_to_int(const char s[])
{
    int d;
    sscanf(s, "%d", &d);
    return d;
}


