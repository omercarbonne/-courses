#include "sort_bus_lines.h"
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


//TODO add implementation here
void switch_buses(BusLine *bus1, BusLine *bus2);
void bubble_sort (BusLine *start, BusLine *end)
{
    int len = end-start+1;
    for (int i = 0; i<len; i++)
    {
        for (int j = 0; j<len-1-i; j++)
        {
           int res = strcmp((*(start+j)).name,(*(start+j+1)).name);
            if (res > 0)
            {
                switch_buses(start+j, start+j+1);
            }
        }
    }

}

void quick_sort(BusLine *start, BusLine *end, SortType sort_type)
{
    if(end-start <= 0)
    {
        return;}
    else
    {
        BusLine *pivot = partition(start, end, sort_type);
        quick_sort(start, pivot-1, sort_type);
        quick_sort(pivot+1, end, sort_type);
    }
}

BusLine *partition(BusLine *start, BusLine *end, SortType sort_type)
{
    BusLine *i = start - 1;
    for (BusLine *j = start; end - j > 0; j++)
    {
        if (sort_type == DURATION)
        {
            if ((*j).duration < (*end).duration)
            {
                i++;
                switch_buses(i, j);
            }
        }
        if (sort_type == DISTANCE)
        {
            if ((*j).distance < (*end).distance)
            {
                i++;
                switch_buses(i, j);
            }
        }
    }
    i++;
    switch_buses(i, end);
    return i;
}

void print_buses_lines2(int n, BusLine *ptr)
{
    for (int i = 0; i < n; i++)
    {
        printf("%s,%d,%d\n", ptr[i].name, ptr[i].distance, ptr[i].duration);
    }
}


void switch_buses(BusLine *bus1, BusLine *bus2)
{
    BusLine temp = *bus1;
    *bus1 = *bus2;
    *bus2 = temp;
}