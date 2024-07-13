#include <string.h> // For strlen(), strcmp(), strcpy()
#include "markov_chain.h"

#define MAX(X, Y) (((X) < (Y)) ? (Y) : (X))

#define EMPTY -1
#define BOARD_SIZE 100
#define MAX_GENERATION_LENGTH 60
#define DICE_MAX 6
#define NUM_OF_TRANSITIONS 20

/**
 * represents the transitions by ladders and snakes in the game
 * each tuple (x,y) represents a ladder from x to if x<y or a snake otherwise
 */
const int transitions[][2] = {{13, 4},
                              {85, 17},
                              {95, 67},
                              {97, 58},
                              {66, 89},
                              {87, 31},
                              {57, 83},
                              {91, 25},
                              {28, 50},
                              {35, 11},
                              {8,  30},
                              {41, 62},
                              {81, 43},
                              {69, 32},
                              {20, 39},
                              {33, 70},
                              {79, 99},
                              {23, 76},
                              {15, 47},
                              {61, 14}};

/**
 * struct represents a Cell in the game board
 */
typedef struct Cell {
    int number; // Cell number 1-100
    int ladder_to;  // ladder_to represents the jump of the ladder
    // in case there is one from this square
    int snake_to;  // snake_to represents the jump of the snake in
    // case there is one from this square
    //both ladder_to and snake_to should be -1 if the Cell doesn't have them
} Cell;

/** Error handler **/
static int handle_error(char *error_msg, MarkovChain **database)
{
    printf("%s", error_msg);
    if (database != NULL)
    {
        free_database(database);
    }
    return EXIT_FAILURE;
}


static int create_board(Cell *cells[BOARD_SIZE])
{
    for (int i = 0; i < BOARD_SIZE; i++)
    {
        cells[i] = malloc(sizeof(Cell));
        if (cells[i] == NULL)
        {
            for (int j = 0; j < i; j++) {
                free(cells[j]);
            }
            handle_error(ALLOCATION_ERROR_MASSAGE,NULL);
            return EXIT_FAILURE;
        }
        *(cells[i]) = (Cell) {i + 1, EMPTY, EMPTY};
    }

    for (int i = 0; i < NUM_OF_TRANSITIONS; i++)
    {
        int from = transitions[i][0];
        int to = transitions[i][1];
        if (from < to)
        {
            cells[from - 1]->ladder_to = to;
        }
        else
        {
            cells[from - 1]->snake_to = to;
        }
    }
    return EXIT_SUCCESS;
}

/**
 * fills database
 * @param markov_chain
 * @return EXIT_SUCCESS or EXIT_FAILURE
 */
static int fill_database(MarkovChain *markov_chain)
{
    Cell* cells[BOARD_SIZE];
    if(create_board(cells) == EXIT_FAILURE)
    {
        return EXIT_FAILURE;
    }
    MarkovNode *from_node = NULL, *to_node = NULL;
    size_t index_to;
    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        add_to_database(markov_chain, cells[i]);
    }

    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        from_node = get_node_from_database(markov_chain,cells[i])->data;

        if (cells[i]->snake_to != EMPTY || cells[i]->ladder_to != EMPTY)
        {
            index_to = MAX(cells[i]->snake_to,cells[i]->ladder_to) - 1;
            to_node = get_node_from_database(markov_chain, cells[index_to])
                    ->data;
            add_node_to_frequencies_list(to_node,
                                         from_node, markov_chain);
        }
        else
        {
            for (int j = 1; j <= DICE_MAX; j++)
            {
                index_to = ((Cell*) (from_node->data))->number + j - 1;
                if (index_to >= BOARD_SIZE)
                {
                    break;
                }
                to_node = get_node_from_database(markov_chain, cells[index_to])
                        ->data;
                add_node_to_frequencies_list(to_node,
                                             from_node, markov_chain);
            }
        }
    }
    // free temp arr
    for (size_t i = 0; i < BOARD_SIZE; i++)
    {
        free(cells[i]);
    }
    return EXIT_SUCCESS;
}


static int str_to_int(const char s[])
{
    int d;
    sscanf(s, "%d", &d);
    return d;
}

static void print_cell(void* cell)
{
    Cell* temp = (Cell*)cell;
    printf(" [%d]", temp->number);
    if(temp->ladder_to != EMPTY)
    {
        printf("-ladder to %d", temp->ladder_to);
    }
    if(temp->snake_to != EMPTY)
    {
        printf("-snake to %d", temp->snake_to);
    }
    if(temp->number != BOARD_SIZE)
    {
        printf(" ->");
    }
}

static bool is_last(void* cell)
{
    Cell* temp = (Cell*)cell;
    if(temp->number == BOARD_SIZE)
    {
        return true;
    }
    return false;
}


static int cell_cmp(void* cell1, void* cell2)
{
    Cell * temp1 = (Cell*)cell1;
    Cell * temp2 = (Cell*)cell2;
    return temp1->number-temp2->number;
}

static void* get_copy_cell(void* cell)
{
    Cell* new_cell = malloc(sizeof(Cell));
    if(!new_cell)
    {
        return NULL;
    }
    Cell* temp = (Cell*)cell;
    new_cell->number = temp->number;
    new_cell->snake_to = temp->snake_to;
    new_cell->ladder_to = temp->ladder_to;
    return (void*)new_cell;
}


static void generate_route(MarkovChain* markov_chain, int route_num)
{
    int count = 1;
    //Cell temp = {1, -1, -1};
    //MarkovNode* first = get_node_from_database(markov_chain, &temp);
    printf("Random Walk %d:", route_num);
    MarkovNode * first = markov_chain->database->first->data;
    while((!is_last((Cell*)first->data))&&(count<MAX_GENERATION_LENGTH))
    {
        print_cell(first->data);
        first = get_next_random_node(first);
        count++;
    }
    print_cell(first->data);
    printf("\n");
}

/**
 * @param argc num of arguments
 * @param argv 1) Seed
 *             2) Number of sentences to generate
 * @return EXIT_SUCCESS or EXIT_FAILURE
 */
int main(int argc, char *argv[])
{
    if(argc != 3)
    {
        //printf(USAGE_ERROR);
        return EXIT_FAILURE;
    }
    int seed = str_to_int(argv[1]);
    srand(seed);
    int num_of_routs = str_to_int(argv[2]);
    LinkedList* database = malloc(sizeof(LinkedList));
    if(!database)
    {
        //printf(ALLOCATION_ERROR_MASSAGE);
        return EXIT_FAILURE;
    }
    database->first = NULL;
    database->last = NULL;
    database->size = 0;
    MarkovChain* markov_chain = malloc(sizeof(MarkovChain));
    if(!markov_chain)
    {
        //printf(ALLOCATION_ERROR_MASSAGE);
        return EXIT_FAILURE;
    }
    markov_chain->database = database;
    markov_chain->print_func = print_cell;
    markov_chain->is_last = is_last;
    markov_chain->free_data = free;
    markov_chain->comp_func = cell_cmp;
    markov_chain->copy_func = get_copy_cell;
    if(fill_database(markov_chain))
    {
        return EXIT_FAILURE;
    }
    for (int i = 0; i < num_of_routs; i++)
    {
        generate_route(markov_chain, i+1);
    }
    return EXIT_SUCCESS;
}
