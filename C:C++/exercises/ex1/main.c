#include "cipher.h"
#include "tests.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#define OPTION_1 5
#define OPTION_2 2
#define MAX_LENGTH 1024
#define MINUS_SIGN 45
#define DECIMAL 10

bool is_k_valid(const char k[]);
bool is_command_valid(char cmd[]);
bool is_k_valid(const char k[]);
const char* is_argvs_valid(char argv1[], char argv2[], char argv3[], char argv4[]);
bool is_files_ok(const char path1[], const char path2[]);
void apply_encode(char path1[], char path2[], int k);
void apply_decode(char path1[], char path2[], int k);
bool run_all_tests();
bool is_digit(char c);
// your code goes here

int main (int argc, char *argv[])
{
    if (argc == OPTION_1)
    {
        char* text = is_argvs_valid(argv[1], argv[2], argv[3], argv[4]);
        if (!strcmp(text, "ok"))
        {
            int k = strtol(argv[2], NULL, DECIMAL);
            if (!strcmp(argv[1], "encode"))
            {

                apply_encode(argv[3], argv[4], k);

            }
            else
            {
                apply_decode(argv[3], argv[4], k);
            }
        }
        else
        {
            fprintf(stderr, text);
            return EXIT_FAILURE;
        }
    }
    else if (argc == OPTION_2)
    {
        if (!strcmp(argv[1], "test"))
        {
            if(run_all_tests())
            {return EXIT_SUCCESS;}
            else
            {return EXIT_FAILURE;}
        }
        else
        {
            fprintf(stderr, "Usage: cipher test\n" );
            return EXIT_FAILURE;
        }
    }
    else
    {
        fprintf(stderr, "The program receives 1 or 4 arguments only.\n");
        return EXIT_FAILURE;
    }
  return EXIT_SUCCESS;
}

const char* is_argvs_valid(char argv1[], char argv2[], char argv3[], char argv4[])
{
    if (!is_command_valid(argv1)) {return "The given command is invalid.\n";}
    else if (!is_k_valid(argv2)) {return "The given shift value is invalid.\n";}
    else if(!is_files_ok(argv3, argv4)) {return "The given file is invalid.\n";}
    else {return "ok";}
}

bool is_command_valid(char cmd[])
{
    if ((!strcmp(cmd, "encode"))|| (!strcmp(cmd, "decode"))) {return true;}
    else {return false;}
}

bool is_k_valid(const char k[])
{
    for (int i = 0; k[i]!= '\0'; i++)
    {
        if (!is_digit(k[i]))
        {
            if ((i == 0) && ((int)k[i] == MINUS_SIGN))
            {continue;}
            return false;
        }
    }
        return true;
}

bool is_digit(char c)
{
    if((c>='0') && (c<='9'))
    {return true;}
    return false;
}

bool is_files_ok(const char path1[], const char path2[])
{
    FILE* file1 = fopen(path1, "r");
    if (file1 == NULL) {return false;}
    else { fclose(file1);}
    FILE* file2 = fopen(path2, "w");
    if (file2 == NULL) {return false;}
    else { fclose(file2);}
    return true;
}

void apply_encode(char path1[], char path2[], int k)
{
    FILE* file1 = fopen(path1, "r");
    FILE* file2 = fopen(path2, "w");
    char line[MAX_LENGTH];
    while (fgets(line, MAX_LENGTH, file1))
    {
        encode(line, k);
        fputs(line, file2);
    }
    fclose(file1);
    fclose(file2);
}

void apply_decode(char path1[], char path2[], int k)
{

    FILE* file1 = fopen(path1, "r");
    FILE* file2 = fopen(path2, "w");
    char line[MAX_LENGTH];
    while (fgets(line, MAX_LENGTH, file1))
    {
        decode(line, k);
        fputs(line, file2);
    }
    fclose(file1);
    fclose(file2);
}

bool run_all_tests()
{
    int ans = 0;
    ans += test_encode_non_cyclic_lower_case_positive_k ();
    ans += test_encode_cyclic_lower_case_special_char_positive_k ();
    ans += test_encode_non_cyclic_lower_case_special_char_negative_k ();
    ans += test_encode_cyclic_lower_case_negative_k ();
    ans += test_encode_cyclic_upper_case_positive_k ();
    ans += test_decode_non_cyclic_lower_case_positive_k ();
    ans += test_decode_cyclic_lower_case_special_char_positive_k ();
    ans += test_decode_non_cyclic_lower_case_special_char_negative_k ();
    ans += test_decode_cyclic_lower_case_negative_k ();
    ans += test_decode_cyclic_upper_case_positive_k ();
    if(ans == 0) {return true;}
    return false;
}