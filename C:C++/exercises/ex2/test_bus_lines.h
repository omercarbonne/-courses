#ifndef EX2_REPO_TESTBUSLINES_H
#define EX2_REPO_TESTBUSLINES_H
// write only between #define EX2_REPO_TESTBUSLINES_H and
// #endif //EX2_REPO_TESTBUSLINES_H
#include "sort_bus_lines.h"

/**
 * the function check if a given array of
 * buslines objects is sorted by distance
 * returns true if yes, and false if not
 */
int is_sorted_by_distance (BusLine *start, BusLine *end);

/**
 * the function check if a given array of
 * buslines objects is sorted by duration
 * returns true if yes, and false if not
 */
int is_sorted_by_duration (BusLine *start, BusLine *end);

/**
 *the function check if a given array of
 * buslines objects is sorted by name
 * returns true if yes, and false if not
 */
int is_sorted_by_name (BusLine *start, BusLine *end);
void print_buses_lines3(int n, BusLine *ptr);

/**
 * the function checks if two arrays of
 * BusLine objects have the same objects.
 * (the order does not matter
 */
int is_equal (BusLine *start_sorted,
              BusLine *end_sorted, BusLine *start_original,
              BusLine *end_original);

void apply_tests(BusLine *start, BusLine *end);

// write only between #define EX2_REPO_TESTBUSLINES_H
// and #endif //EX2_REPO_TESTBUSLINES_H
#endif //EX2_REPO_TESTBUSLINES_H
