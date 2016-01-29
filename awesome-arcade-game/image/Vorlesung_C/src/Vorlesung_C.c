/*
 ============================================================================
 Name        : Vorlesung_C.c
 Author      : 
 Version     :
 Copyright   : Your copyright notice
 Description : Hello World in C, Ansi-style
 ============================================================================
 */

#include <stdio.h>
#include <stdlib.h>

int main(void) {

	static int zaehler=0;

	void inkrementiere(void){zaehler++;}
	int abfrage(void){return zaehler;}

}
