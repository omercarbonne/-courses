#include "markov_chain.h"
#include "linked_list.h"
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>



static int str_to_int(const char s[]);
static bool check_path(const char path[], FILE ** file);
static int fill_database(FILE *fp, int words_to_read,
                         MarkovChain *markov_chain);

#define USAGE_ERROR "USAGE: YOU NEED TO ENTER - SEED NUMBER,"\
"NUM OF TWEETS, PATH TO SENTENCES FILE, NUM OF WORDS TO READ"
#define PATH_ERROR "THE PATH TO SENTENCES FILE IS INVALID"
#define MAX_SENT_LEN 1000
#define MAX_CHAIN_LEN 20
#define NUM_ARGS_OPTION_1 4
#define NUM_ARGS_OPTION_2 5

static bool is_last(void* word)
{
    word = (char*)word;
    return is_end_of_sentence(word);
}

static void print_func(void * word)
{
    printf("%s ", (char*)word);
}

static int comp_func(void* first, void* second)
{
    first = (char*)first;
    second = (char*)second;
    return strcmp(first, second);
}

static void* copy_func(void* origin)
{
    origin = (char*)origin;
    int len = strlen(origin)+1;
    char* new = malloc(sizeof(char)* len);
    if(!new)
    {
        return NULL;
    }
    //strcpy(new, origin);
    memcpy(new, origin, len);
    return (void*)new;
}


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
        MarkovChain* markov_chain = malloc(sizeof(MarkovChain));
        markov_chain->database = database;
        markov_chain->print_func = &print_func;
        markov_chain->is_last = &is_last;
        markov_chain->free_data = &free;
        markov_chain->comp_func = &comp_func;
        markov_chain->copy_func = &copy_func;
        if(fill_database(file, num_to_read, markov_chain)){
            return EXIT_FAILURE;
        }
        fclose(file);
        int count = 0;
        MarkovNode * first_node;
        while (count < tweets_num) {
            first_node = get_first_random_node(markov_chain);
            printf("Tweet %d: ", count+1);
            generate_tweet(markov_chain, first_node, MAX_CHAIN_LEN);
            printf("\n");
            count++;
        }
        free_database(&markov_chain);
    }
    else{
        printf(USAGE_ERROR);
        return EXIT_FAILURE;
    }
    return EXIT_SUCCESS;
}

static int fill_database(FILE *fp, int words_to_read,
                         MarkovChain *markov_chain)
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
            add_node_to_frequencies_list(node2->data,
                                         node1->data, markov_chain);
            word = strtok(NULL, " ");
            node1 = node2;
        }
    }
    return EXIT_SUCCESS;
}

static bool check_path(const char path[], FILE ** file)
{
    FILE* temp = fopen(path, "r");
    if (temp)
    {
        *file = temp;
        return true;
    }
    return false;
}

static int str_to_int(const char s[])
{
    int d;
    sscanf(s, "%d", &d);
    return d;
}



