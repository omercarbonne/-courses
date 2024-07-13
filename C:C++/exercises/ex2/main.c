#include "sort_bus_lines.h"
#include "test_bus_lines.h"
#include <stdbool.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>


#define MAX_INFO_LEN 60
#define MAX_NAME_LEN 20
#define TOP_LETTER 122
#define LOW_LETTER 97
#define MIN_DISTANCE 0
#define MAX_DISTANCE 1000
#define MIN_DURATION 10
#define MAX_DURATION 100
#define ERROR_1 "USAGE: ENTER <TEST> / <BY_DURATION> / <BY_DISTANCE> / <BY_NAME> ONLY"
#define ERROR_2 "ERROR: ENTER A POSITIVE INTEGER REPRESENT THE NUMBER OF BUSES, TRY AGAIN\n"
#define ERROR_3 "ERROR: THE RIGHT FORMAT IS: <name>,<distance>,<duration> PLEASE TRY AGAIN\n"
#define ERROR_4 "ERROR: THE NAME MUST CONTAIN ONLY 0-9 DIGITS OR LOWERCASE LETTERS, TRY AGAIN\n"
#define ERROR_5 "ERROR: THE DISTANCE MUST BE AN INTEGER BETWEEN 0-1000, TRY AGAIN\n"
#define ERROR_6 "ERROR: THE DURATION MUST BE AN INTEGER BETWEEN 10-100, TRY AGAIN\n"

int get_num_of_buses();
BusLine get_new_bus_line();
bool check_duration(int duration);
bool check_distance(int distance);
bool check_name(char name[]);
bool is_a_to_z(char c);
bool is_digit(char c);
void get_buses_lines(int n, BusLine *ptr);
void print_buses_lines(int n, BusLine *ptr);

/**
 * TODO add documentation
 */
int main (int argc, char *argv[])
{
    if (argc != 2)
    {
        printf(ERROR_1);
        return EXIT_FAILURE;
    }
    else
    {
        if ((!strcmp(argv[1], "by_duration"))||(!strcmp(argv[1], "by_distance"))||
        (!strcmp(argv[1], "by_name"))||(!strcmp(argv[1], "test")))
        {
            int num_of_buses = get_num_of_buses();
            BusLine *start = malloc(sizeof(BusLine)*num_of_buses);
            get_buses_lines(num_of_buses, start);
            if ((!strcmp(argv[1], "test")))
            {
                apply_tests(start,start + num_of_buses - 1);
            }
            else
            {
                if (!strcmp(argv[1], "by_duration"))
                {
                    SortType sort_type = DURATION;
                    quick_sort(start, start + num_of_buses - 1, sort_type);
                }
                else if (!strcmp(argv[1], "by_distance"))
                {
                    SortType sort_type = DISTANCE;
                    quick_sort(start, start + num_of_buses - 1, sort_type);
                }
                else if (!strcmp(argv[1], "by_name"))
                {
                    bubble_sort(start, start + num_of_buses - 1);
                }
                print_buses_lines(num_of_buses, start);
            }
            free(start);
            start = NULL;
        }
        else
        {
            printf(ERROR_1);
            return EXIT_FAILURE;
        }
    }
}

void print_buses_lines(int n, BusLine *ptr)
{
    for (int i = 0; i < n; i++)
    {
        printf("%s,%d,%d\n", ptr[i].name, ptr[i].distance, ptr[i].duration);
    }
}


int get_num_of_buses()
{
    char ans[MAX_INFO_LEN];
    int num;
    int res;
    while (true)
    {
        printf("Enter number of lines. Then enter\n");
        fgets(ans, MAX_INFO_LEN, stdin);
        res = sscanf(ans, "\n%d", &num);
        if (!res)
        {
            printf(ERROR_2);
        }
        else if (num < 1)
        {
            printf(ERROR_2);
        }
        else
        {
            return num;
            break;
        }
    }
}

void get_buses_lines(int n, BusLine *ptr)
{
    for (int i = 0; i < n; i++)
    {
        ptr[i] = get_new_bus_line();
    }
}

BusLine get_new_bus_line()
{
    char info[MAX_INFO_LEN];
    char name[MAX_NAME_LEN];
    int distance;
    int duration;
    int res = 0;
    printf("Enter line info. Then enter\n");
    while (true)
    {
        fgets(info, MAX_INFO_LEN, stdin);
        res = sscanf(info, "%[^,],%d,%d", name, &distance, &duration);
        if (res!= 3)
        {
            printf(ERROR_3);
            printf("Enter line info. Then enter\n");
        }
        else if (!check_name(name))
        {
            printf(ERROR_4);
            printf("Enter line info. Then enter\n");
        }
        else if(!check_distance(distance))
        {
            printf(ERROR_5);
            printf("Enter line info. Then enter\n");
        }
        else if(!check_duration(duration))
        {
            printf(ERROR_6);
            printf("Enter line info. Then enter\n");
        }
        else
        {
            break;
        }
    }
    BusLine new_bus;
    strcpy(new_bus.name, name);
    new_bus.distance = distance;
    new_bus.duration = duration;
    return new_bus;
}


bool check_name(char name[])
{
    int i = 0;
    while (name[i] != '\0')
    {
        if (!((is_digit(name[i])) || (is_a_to_z(name[i]))))
        {
            return false;
        }
        i++;
    }
    return true;
}

bool is_a_to_z(char c)
{
    if ((c >= LOW_LETTER) && (c <= TOP_LETTER))
    {
        return true;}
    return false;
}

bool check_distance(int distance)
{
    if((distance >= MIN_DISTANCE) && (distance <= MAX_DISTANCE))
    {return true;}
    return false;
}

bool check_duration(int duration)
{
    if ((duration >= MIN_DURATION) && (duration <= MAX_DURATION))
    {return true;}
    return false;
}

bool is_digit(char c)
{
    if((c>='0') && (c<='9'))
    {return true;}
    return false;
}


