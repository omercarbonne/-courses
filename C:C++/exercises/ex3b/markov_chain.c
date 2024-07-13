#include "markov_chain.h"
#include <stdbool.h>
#include <stdlib.h>
#include <string.h>


#define MAX_CHAIN_LEN 20
#define MIN_CHAIN_LEN 2
#define DOT_SIGN 46
Node* add_to_database(MarkovChain *markov_chain, void *data_ptr)
{
    Node * try_new_node = get_node_from_database(markov_chain, data_ptr);
    if (try_new_node) // if the data is already exist
    {
        return try_new_node;
    }
    else
    {
        // first try to allocate all memory needed
        void* new_data_ptr =
                markov_chain->copy_func(data_ptr);
        MarkovNode * new_mark_node_ptr = malloc(sizeof(MarkovNode));
        if ((!new_data_ptr) || (!new_mark_node_ptr))
        { // if memory allocation didn't work
            free(new_data_ptr);
            free(new_mark_node_ptr);
            return NULL;
        }
        //  assigning all the information in the memory.
        new_mark_node_ptr->data = new_data_ptr;
        new_mark_node_ptr->frequencies_list = NULL;
        new_mark_node_ptr->list_len = 0;
        if(!add(markov_chain->database, new_mark_node_ptr))
        {
            return markov_chain->database->last;
        }
        else
        {
            free(new_data_ptr);
            free(new_mark_node_ptr);
            return NULL;
        }
    }
}
Node* get_node_from_database(MarkovChain *markov_chain, void *data_ptr)
{
    Node *temp_ptr = markov_chain->database->first;
    while (temp_ptr != NULL)
    {
        if (!markov_chain->comp_func(temp_ptr->data->data, data_ptr))
        {
            return temp_ptr;
        }
        temp_ptr = temp_ptr->next;
    }
    return NULL;
}
MarkoveNodeFrequencies *
is_data_exist(MarkoveNodeFrequencies *freq_list, int list_len,
              void *data, MarkovChain *markov_chain)
{
    if (freq_list == NULL)
    {
        return NULL;
    }
    for (int i = 0; i < list_len; i++)
    { // looking for matching data
        if (!markov_chain->comp_func(freq_list[i].markov_node->data, data))
        {
            return &freq_list[i];
        }
    }
    return NULL;
}

bool add_node_to_frequencies_list(MarkovNode *second_node,
                                  MarkovNode *first_node,
                                  MarkovChain *markov_chain)
{
    if(markov_chain->is_last((*first_node).data))
    {
        return true;
    }
    if (first_node->frequencies_list == NULL) // the first node
    {
        MarkoveNodeFrequencies * temp_alloc =
                malloc(sizeof(MarkoveNodeFrequencies));
        if(!temp_alloc)
        {
            return false;
        }
        first_node->frequencies_list=temp_alloc;
        first_node->list_len = 1;
        first_node->frequencies_list->markov_node = second_node;
        first_node->frequencies_list->frequency = 1;
        return true;
    }
    else
    {
        MarkoveNodeFrequencies * temp = is_data_exist
                (first_node->frequencies_list,
                 first_node->list_len, second_node->data, markov_chain);
        if(temp) // if data is exist
        {
            temp->frequency += 1;
            return true;
        }
        else // data doesn't exist yet
        {
            MarkoveNodeFrequencies * temp_alloc =realloc(
                    first_node->frequencies_list,
                    sizeof(MarkoveNodeFrequencies)*(first_node->list_len+1));
            if(!temp_alloc) // realloc failed
            {
                return false;
            }
            first_node->list_len += 1;
            first_node->frequencies_list = temp_alloc;
            first_node->frequencies_list
            [(first_node->list_len)-1].frequency = 1;
            first_node->frequencies_list[(first_node->list_len)-1]
                    .markov_node = second_node;
            return true;
        }
    }

}
void free_database(MarkovChain ** markov_chain)
{
    Node* temp = (*markov_chain)->database->first;
    for (int i = 0; i<(*markov_chain)->database->size; i++)
    {
        free_node(&temp, *markov_chain);
        Node * sec_temp = temp;
        temp = temp->next;
        free(sec_temp);
    }
    free((*markov_chain)->database);
    free(*markov_chain);
    *markov_chain = NULL;
}
void free_node(Node **node, MarkovChain *markov_chain)
{
    free_mark_node(&(*node)->data, markov_chain);
}
void free_mark_node(MarkovNode **mark_node, MarkovChain *markov_chain)
{
    free((*mark_node)->frequencies_list);
    markov_chain->free_data((*mark_node)->data);
    free(*mark_node);
    *mark_node = NULL;
}
bool is_end_of_sentence(char* word)
{
    char last = 'a';
    while (*word != '\0')
    {
        last = *word;
        word++;
    }
    if(last == DOT_SIGN)
    {
        return true;
    }
    return false;
}
int get_random_number(int max_number)
{
    return rand() % max_number;
}

MarkovNode* get_first_random_node(MarkovChain *markov_chain)
{
    int max_num = markov_chain->database->size;
    int num = get_random_number(max_num);
    Node* first_node = get_node(markov_chain->database, num);
    while (markov_chain->is_last(first_node->data->data))
    {
        num = get_random_number(max_num);
        //printf("final random num: %d\n", num);
        first_node = get_node(markov_chain->database, num);
    }
    return first_node->data;
}
Node* get_node(LinkedList* linked_list, int i)
{
    Node * temp = linked_list->first;
    for (int j = 0; j < i; j++)
    {
        temp = temp->next;
    }
    return temp;
}
MarkovNode* get_next_random_node(MarkovNode *state_struct_ptr)
{
    int lst_len = state_struct_ptr->list_len;
    int max_num = get_num_of_nexts_words(state_struct_ptr);
    int num = get_random_number(max_num);
    MarkoveNodeFrequencies *temp = state_struct_ptr->frequencies_list;
    int count = temp->frequency;
    for (int i = 1; i < lst_len; i++)
    {
        if(count > num)
        {
            return temp->markov_node;
        }
        temp++;
        count += temp->frequency;
    }
    return temp->markov_node;
}
int get_num_of_nexts_words(MarkovNode* mark_node)
{
    if (mark_node->frequencies_list == NULL)
    {
        return 0;
    }
    int lst_len = mark_node->list_len;
    MarkoveNodeFrequencies *temp = mark_node->frequencies_list;
    int count = temp->frequency;
    for (int i = 1; i < lst_len; i++)
    {
        temp++;
        count += temp->frequency;
    }
    return count;
}
void generate_tweet(MarkovChain *markov_chain,
                    MarkovNode *first_node, int max_length)
{
    if(first_node == NULL)
    {
        first_node = get_first_random_node(markov_chain);
    }
    if(max_length > MAX_CHAIN_LEN)
    {
        max_length = MAX_CHAIN_LEN;
    }
    if(max_length < MIN_CHAIN_LEN)
    {
        max_length = MIN_CHAIN_LEN;
    }
    markov_chain->print_func(first_node->data);
    int count = 1;
    while ((count < max_length) && (first_node->frequencies_list != NULL))
    {
        first_node = get_next_random_node(first_node);
        markov_chain->print_func(first_node->data);
        count++;
    }
}