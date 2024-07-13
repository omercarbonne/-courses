#include "test_bus_lines.h"
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>


BusLine* get_a_copy(BusLine *start, BusLine *end);
bool is_name_exist(char name[], BusLine *start, BusLine *end);
//TODO add implementation here

int is_sorted_by_distance (BusLine *start, BusLine *end)
{
    int len = end - start + 1;
    for (int i = 0; i < len - 1; i++)
    {
        if (start[i].distance > start[i+1].distance)
        {
            return 0;
        }
    }
    return 1;
}

/**
 * TODO add documentation
 */
int is_sorted_by_duration (BusLine *start, BusLine *end)
{
    int len = end - start + 1;
    for (int i = 0; i < len - 1; i++)
    {
        if (start[i].duration > start[i+1].duration)
        {
            return 0;
        }
    }
    return 1;
}

/**
 * TODO add documentation
 */
int is_sorted_by_name (BusLine *start, BusLine *end)
{
    int len = end - start + 1;
    for (int i = 0; i < len - 1; i++)
    {
        if (strcmp(start[i].name, start[i+1].name) > 0)
        {
            return 0;
        }
    }
    return 1;
}

/**
 * TODO add documentation
 */
int is_equal (BusLine *start_sorted, BusLine *end_sorted,
              BusLine *start_original, BusLine *end_original)
{
    int len_org = end_original - start_original + 1;
    int len_sort = end_sorted - start_sorted + 1;
    if (len_org != len_sort)
    {
        return 0;
    }
    for (int i = 0; i < len_sort - 1; i++)
    {
        if (!is_name_exist(start_sorted[i].name, start_original, end_original))
        {
            return 0;
        }
    }
    return 1;
}

bool is_name_exist(char name[], BusLine *start, BusLine *end)
{
    int len = end - start + 1;
    for (int i = 0; i < len; i++)
    {
        if (strcmp(name, start[i].name) == 0)
        {
            return true;
        }
    }
    return false;


}

BusLine* get_a_copy(BusLine *start, BusLine *end)
{
    int arr_size = end - start + 1;
    BusLine* cpy = malloc(arr_size* sizeof(BusLine));
    memcpy(cpy, start, arr_size* sizeof(BusLine));
    return cpy;
}


void apply_tests(BusLine *start, BusLine *end)
{
    int arr_size = end-start + 1;
    BusLine * cpy_start = get_a_copy(start, end);
    BusLine * cpy_end = cpy_start + arr_size - 1;
    quick_sort(cpy_start, cpy_end, DISTANCE);
    if(is_sorted_by_distance(cpy_start, cpy_end))
    {
        printf("TEST 1 PASSED: checks if the "
               "array is sorted by distance\n");
    }
    else
    {
        printf("TEST 1 FAILED:  checks if the "
               "array is sorted by distance\n");
    }
    if(is_equal(start, end, cpy_start, cpy_end))
    {
        printf("TEST 2 PASSED: checks if the org "
               "array and the edited one are equal\n");
    }
    else
    {
        printf("TEST 2 FAILED: checks if the org "
               "array and the edited one are equal\n");
    }
    quick_sort(cpy_start, cpy_end, DURATION);
    if(is_sorted_by_duration(cpy_start, cpy_end))
    {
        printf("TEST 3 PASSED: checks if the "
               "array is sorted by duration\n");
    }
    else
    {
        printf("TEST 3 FAILED: checks if the "
               "array is sorted by duration\n");
    }
    if(is_equal(start, end, cpy_start, cpy_end))
    {
        printf("TEST 4 PASSED: checks if the org "
               "array and the edited one are equal\n");
    }
    else
    {
        printf("TEST 4 FAILED: checks if the org "
               "array and the edited one are equal\n");
    }
    bubble_sort(cpy_start, cpy_end);
    if(is_sorted_by_name(cpy_start, cpy_end))
    {
        printf("TEST 5 PASSED: checks if the "
               "array is sorted by name\n");
    }
    else
    {
        printf("TEST 5 FAILED: checks if "
               "the array is sorted by name\n");
    }
    if(is_equal(start, end, cpy_start, cpy_end))
    {
        printf("TEST 6 PASSED: checks if the org "
               "array and the edited one are equal\n");
    }
    else
    {
        printf("TEST 6 FAILED: checks if the org "
               "array and the edited one are equal\n");
    }
    free(cpy_start);
    cpy_start = NULL;
    cpy_end = NULL;
}