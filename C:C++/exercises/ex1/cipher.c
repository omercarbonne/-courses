#include "cipher.h"
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>
#define BUTTON_CAP 65
#define TOP_CAP 90
#define BUTTON_LOW 97
#define TOP_LOW 122
#define NUM_OF_LETTERS 26

/// IN THIS FILE, IMPLEMENT EVERY FUNCTION THAT'S DECLARED IN cipher.h.
bool is_capital(char c);
bool is_lowercase(char c);
bool is_valid_letter(char c);
int get_encoded_num(char c, int k);

// See full documentation in header file
void encode (char s[], int k)
{
  int i = 0;
  while (s[i++] != '\0')
  {
      s[i-1] = get_encoded_num(s[i-1], k);
  }
}

int get_encoded_num(char c, int k)
{
    if (is_capital(c))
    {
        int temp = ((int)c - BUTTON_CAP + k)%NUM_OF_LETTERS;
        if (temp>=0) {return (char)(temp + BUTTON_CAP);}
        else {return (char)(temp + TOP_CAP + 1);}

    }
    else if (is_lowercase(c))
    {
        int temp = ((int)c - BUTTON_LOW + k)%NUM_OF_LETTERS;
        if (temp>=0) {return (char)(temp + BUTTON_LOW);}
        else {return (char)(temp + TOP_LOW + 1);}

    }
    else {return c;}
}


bool is_capital(char c)
{
    if ((BUTTON_CAP <= c) && (c <= TOP_CAP)) {return true;}
    return false;
}

bool is_lowercase(char c)
{
    if ((BUTTON_LOW <= c) && (c <= TOP_LOW)) {return true;}
    return false;
}

bool is_valid_letter(char c)
{
    if (is_capital(c) || is_lowercase(c)) {return true;}
    return false;
}


// See full documentation in header file
void decode (char s[], int k)
{
    return encode(s,-k);
}
