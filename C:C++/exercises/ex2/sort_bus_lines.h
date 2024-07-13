#ifndef EX2_REPO_SORTBUSLINES_H
#define EX2_REPO_SORTBUSLINES_H
// write only between #define EX2_REPO_SORTBUSLINES_H
// and #endif //EX2_REPO_SORTBUSLINES_H
#include <string.h>
#define NAME_LEN 21
/**
 * TODO add documentation
 */
typedef struct BusLine
{
    char name[NAME_LEN];
    int distance, duration;
} BusLine;
typedef enum SortType
{
    DISTANCE,
    DURATION,
} SortType;

/**
 * the function sorts a given array of BusLine object by name using bubblesort
 */
void bubble_sort (BusLine *start, BusLine *end);

/**
 * the function sorts a given array of BusLine
 * object by distance/duration using quicksort
 */
void quick_sort (BusLine *start, BusLine *end, SortType sort_type);

/**
 * this is part of the quicksort function.
 * It sorting the objects in the array according to a pivot, which is the last object.
 */
BusLine *partition (BusLine *start, BusLine *end, SortType sort_type);
// write only between #define EX2_REPO_SORTBUSLINES_H
// and #endif //EX2_REPO_SORTBUSLINES_H
#endif //EX2_REPO_SORTBUSLINES_H
