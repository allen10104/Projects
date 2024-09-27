/*=============================================================================
| Assignment: pa01 - Encrypting a plaintext file using the Vigenere cipher
|
| Author: Allen Su
| Language: c, c++, Java, go, python
|
| To Compile: javac pa01.java
| gcc -o pa01 pa01.c
| g++ -o pa01 pa01.cpp
| go build pa01.go
|
| To Execute: java -> java pa01 kX.txt pX.txt
| or c++ -> ./pa01 kX.txt pX.txt
| or c -> ./pa01 kX.txt pX.txt
| or go -> ./pa01 kX.txt pX.txt
| or python -> python3 pa01.py kX.txt pX.txt
| where kX.txt is the keytext file
| and pX.txt is plaintext file
|
| Note: All input files are simple 8 bit ASCII input
|
| Class: CIS3360 - Security in Computing - Spring 2023
| Instructor: McAlpin
| Due Date: 3/06/2023
|
+=============================================================================*/

#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdlib.h>
#define ARRAYMAX 10000


int main(int argc, char **argvc)
{
  char *fname_1 = argvc[1];
  char *fname_2 = argvc[2];
  int rows;
  int columns;
  char text[ARRAYMAX];
  char ciphertext[ARRAYMAX];
  char padding[2] = {'x', '\0'};
  int counter = 0;
  int sum;
  FILE  *key = fopen(fname_1, "r");
  FILE *plaintext = fopen(fname_2, "r");

  fscanf(key, "%d", &rows);
  columns = rows;
  int keyMatrix[rows][columns];
  
  for(int i = 0; i < rows; i++)
    {
      for (int j = 0; j < columns; j++)
        {
          fscanf(key, "%d", &keyMatrix[i][j]);
        }
    }
  while(!feof(plaintext))
    {
      fscanf(plaintext, " %c", &text[counter]);
      if(text[counter] > 96 && text[counter] < 123)
      {
        counter++;
      }
      else if (text[counter] > 64 && text[counter] < 91)
      {
        text[counter] = tolower(text[counter]);
        counter++;
      }
      else
      {
        text[counter] = 'x';
      }
    }

    if((strlen(text)) % rows != 0)
    {
      for(int i = 0; i < ((strlen(text)) % rows); i++)
        strcat(text, padding);
    }

  counter = 0;
  int stringCounter = 0;
  while(counter < strlen(text))
  {
    for(int i = 0; i < rows; i++)
      {
        for(int j = 0; j < columns; j++)
          {
            sum += keyMatrix[i][j] * (text[counter] - 97);
            counter++;
          }
        sum = (sum % 26) + 97;
        ciphertext[stringCounter] = sum;
        sum = 0;
        stringCounter++;
        counter -= rows;
      }
    counter += rows;
  }
  printf("\nKey matrix:\n");
  for (int i = 0; i < rows; i ++)
    {
      for(int j = 0; j < columns; j++)
        {
          printf(" ");
          printf("%d\t", keyMatrix[i][j]);
        }
      printf("\n");
    }
  printf("\n");
  printf("Plaintext:\n");
  counter = 1;
  for(int i = 0; i < strlen(text) + 1; i++)
    {
      printf("%c", text[i]);
      if(counter == 80)
      {
        printf("\n");
        counter = 0;
      }
      counter++;
    }
  printf("\n\nCiphertext:\n");
  counter = 1;
  for(int i = 0; i < strlen(ciphertext) + 1; i++)
    {
      printf("%c", ciphertext[i]);
      if(counter == 80)
      {
        printf("\n");
        counter = 0;
      }
      counter++;
    }
  printf("\n");
  fclose(key);
  fclose(plaintext);
  return 0;
}

/*=============================================================================
| I [Allen Su] ([al424676]) affirm that this program is
| entirely my own work and that I have neither developed my code together with
| any another person, nor copied any code from any other person, nor permitted
| my code to be copied or otherwise used by any other person, nor have I
| copied, modified, or otherwise used programs created by others. I acknowledge
| that any violation of the above terms will be treated as academic dishonesty.
+=============================================================================*/