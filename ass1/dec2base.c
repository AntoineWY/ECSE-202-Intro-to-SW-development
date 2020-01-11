/*
 * plus.c
 *
 *  Created on: Sep 18, 2017
 *  Antoine Wang 260766084
 */
#include <Stdio.h>

	int main(int argc, char *argv[]){
		int base;
		long lastQ;                                     //Number to be converted
		long Q;
		int initial=0, reverse;                         //Indicates the digit number
		if (argc !=3 && argc !=2){
			printf("Wrong number of arguments!");
		    return (0);
		}
		else if (argc ==2){                             //Situation 1: Default(base 2)
			int arr[sizeof (int)*8];
			sscanf(argv[1], "%d",&lastQ);
			printf("The Base-2 form of %d is: ", lastQ);
			while (lastQ>0){
				Q = (int) lastQ/2;
				arr[initial]= lastQ%2;
				lastQ=Q;
			    initial=initial+1;
			}
			for (reverse=initial-1; reverse>=0; reverse=reverse-1){
				printf("%d", arr[reverse]);
				}
			printf("\n");
	}
		else if (argc ==3){                             //Situation 2: Random base
		char result[36]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		sscanf(argv[1], "%d",&lastQ);
		sscanf(argv[2], "%d",&base);
		char arr[sizeof (int)*8];
		  if (base<=35 && base>1){
		     printf("The Base-%d form of %d is: ", base, lastQ);
			  while (lastQ>0){
			   Q = (int) lastQ/base;
			   arr[initial]= result[lastQ%base];
			   lastQ=Q;
			   initial=initial+1;
		}
              for (reverse = initial-1; reverse>=0; reverse=reverse-1){
        	   printf("%c", arr[reverse]);
        }
              printf("\n");
        }
		  else {
			   printf("wrong base entered!");
			   return (0);
		}
		}
	}


