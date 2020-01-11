/*
Antoine Wang <yinuo.wang@mail.mcgill.ca>
260766084
 */


#define MAXRECORDS 100
#define MAXNAMELENGTH 15
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct StudentRecord                      //Construct the datatype for students' recoed
{
	char FirstNames[MAXNAMELENGTH];
	char LastNames[MAXNAMELENGTH];
	int IDNums;
	int Marks;
};

void swap (int indx1, int indx2, struct StudentRecord *SRecords[])
{
	struct StudentRecord* temp = SRecords[indx1];
	SRecords[indx1] = SRecords[indx2];
	SRecords[indx2] = temp;
}


int partitioning (struct StudentRecord *SRecords[],int start, int finish)
{
	char* lmpivot=SRecords[start]->LastNames;                               //Choose a pivot randomly
	int i =start;
	int j=finish;
	while(1)
	{
		while(strcmp(SRecords[i]->LastNames,lmpivot)<0)                     //Compare the left elements of the Pivot and If they are smaller, meaning that no switch is needed
		{
			i=1+i;                                                          //jump to the next
		}
		while(strcmp(SRecords[j]->LastNames, lmpivot)>0)
		{
			j=j-1;                                                          //If there is no need to switch (right one is originally larger), jump to the next
		}
		if(i>=j)                                                            //When index meet midway, swap them
			return i;
		swap(i,j,SRecords);
	}
}
void quicksort(struct StudentRecord *SRecords[],int start, int finish)
{
	if(start < finish)
	{
		int p = partitioning (SRecords,start,finish);
		quicksort(SRecords,start,p);                                        //Recursion: the array keeps dividing itself until only the pivot left, meaning everything sorted
		quicksort(SRecords,p+1,finish);
	}
}

int binsearch(char qlm[MAXNAMELENGTH],struct StudentRecord *SRecords[],int length )
{
	unsigned int L=0, R=length,m;
	while (R>L)
	{
		m=(L+R-1)/2;                                                        //Choose a middle one, serves as a pivot
		if (strcmp(qlm,SRecords[m]->LastNames)==0)
		{
			printf("The following record was found:\n");                     //Case 1: find the last name with match with the query and print the record
			printf("Name: %s %s\n",SRecords[m]->FirstNames,SRecords[m]->LastNames);
			printf("Student ID: %d\n",SRecords[m]->IDNums);
			printf("Student Grade: %d\n",SRecords[m]->Marks);
			return 0;
		}
		else if (strcmp(qlm,SRecords[m]->LastNames)>0)                      //Case 2: the query is larger than the middle one, so the left bound is set to m+1
		{                                                                   //which restricted the range to the right half of the array
			L=m+1;
		}
		else                                                                //Case 3: the query is smaller than the middle one, so the right bound is set to m
		{                                                                   //which restricted the range to the left half of the array
			R=m;
		}
	}
    printf("No record found for student with last name %s.\n",qlm);
}


int main(int argc, char * argv[]) {

	struct StudentRecord *pSRecords[MAXRECORDS];
	struct StudentRecord SRecords[MAXRECORDS];
	char lun[MAXNAMELENGTH];
    int numrecords, nummarks, recordnum,a;
	
    //Read in Names and ID data
    FILE * NamesIDsDataFile;
	if((NamesIDsDataFile = fopen(argv[1], "r")) == NULL){
		printf("Can't read from file %s\n", argv[1]);
		exit(1);
	}
	
	numrecords=0;
    	while (fscanf(NamesIDsDataFile,"%s%s%d",&(SRecords[numrecords].FirstNames[0]),
		      				&(SRecords[numrecords].LastNames[0]),
		      				&(SRecords[numrecords].IDNums)) != EOF) {
	  numrecords++;
 	}
	  
	fclose(NamesIDsDataFile);
	
	//Read in marks data
	FILE * MarksDataFile;
	if((MarksDataFile = fopen(argv[2], "r")) == NULL){
		printf("Can't read from file %s\n", argv[2]);
		exit(1);
	}
	nummarks=0;
	while(fscanf(MarksDataFile,"%d",&(SRecords[nummarks].Marks)) != EOF) {
	    nummarks++;
	}
	
	fclose(MarksDataFile);

	for (int i=0; i<numrecords;i++){                                         //Creating array of pointers to pass to each function
		pSRecords[i]=&SRecords[i];                                           //each pointer is assigned with the address of one element in the array of struct by the for loop
	}
	quicksort(pSRecords,0,numrecords-1);                                     //Use pointer array to sort the data according to the lastname first

	if(argc== 4){
		sscanf(argv[3],"%s",&lun);                                           //Input the last name (query) to look up
	    binsearch(lun,pSRecords,numrecords);                                 //If a last name is actually input, call binsearch function
	}
	else
		printf("Please input a last name to look up!");
	return EXIT_SUCCESS;
}

