/*
Antoine Wang <yinuo.wang@mail.mcgill.ca>
260766084
 */


#define MAXRECORDS 100
#define MAXNAMELENGTH 15
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct stdre                         //The data structure which holds the information of the students.
{
	char fn[MAXNAMELENGTH];
	char ln[MAXNAMELENGTH];
	int IDn;
	int Mk;
};

struct tnode                         //The data structure used in building the binary tree.
{
	struct stdre info;
	struct tnode* left;
	struct tnode* right;
};


struct tnode* addnote (struct tnode* root, struct stdre info)
{
	if (root == NULL)
	{
		root = (struct tnode*)  malloc (sizeof (struct tnode));                       //Reading in the information (payload) in the nodes of the tree.
		strcpy (root -> info.fn, info.fn);
		strcpy (root -> info.ln, info.ln);
		root -> info.IDn = info.IDn;
		root -> info.Mk = info.Mk;
		root -> left = NULL;
		root -> right = NULL;
	}
	else if (strcmp (info.ln,root->info.ln)<0)                                     //Whether the newly added node should be sorted in front of the root or behind the root by last name in alphabetical order
	{
		root -> left = addnote (root -> left,info);                                //last name smaller (closer to the beginning of the alphabet) than the root is attached to the left of the root
	}

	else
	{
		root -> right = addnote (root -> right, info);                             //Larger last names (closer to the end of the alphabet) than the root attached to the right
	}
}

void traversetree (struct tnode* root, struct stdre** pinfo)                 //read the tree's data in a traversed order and create the pointer array for sorted data
{
	static int indx=-1;                                     //state the initial value of indx to be -1 is because I put the increment before getting the pointer

	if (root -> left != NULL)                               //so that the first address of students' record in the tree's node will be stored in pinfo[0]
		traversetree(root -> left,pinfo);                   //The traversing goes straight down to the left, get the pointer to the left node (information component)

    indx++;                                                 //Then traverse root
	pinfo[indx]= &root->info;

	if (root->right != NULL)                                //Then traverse right node
		traversetree(root -> right, pinfo);

}


struct stdre* binsearch(struct stdre* pinfo[], char* lun, int length)       //binary search routine in a SORTED array of pointers
{
    unsigned int left = 0, right = length, middle;                          //"left", "right" and "middle" are all indexes of the sorted array
    while (right > left)
    {
    	middle = (left + right - 1)/2;                                      //Case 1: find record which matches with the input "look up name", return that pointer
    	if (strcasecmp (lun, pinfo [middle] -> ln) == 0)
    		return pinfo[middle];
    	else if (strcasecmp (lun, pinfo[middle] -> ln) > 0)                 //If the input name is larger than the middle, move the left bound to (middle+1)
    		left = middle + 1;                                              //and get the right half of the array
    	else                                                                //input name is smaller, move the right bound to get the left half
    	    right = middle;
    }
    return NULL;                                                            //Case 2: No correct information is found. Return a NULL pointer.
}

void main(int argc, char * argv[]) {

	struct tnode* root = NULL;
	struct stdre* info = NULL;
	struct stdre** pinfo = NULL;
	char* lun;                 //char string of "look up name"
    int numrecords, nummarks, recordnum;

    FILE * NamesIDsDataFile;
    FILE * MarksDataFile;

    if((NamesIDsDataFile = fopen(argv[1], "r")) == NULL)      //Testing if the correct files are input in the commander line
	{
		printf("Can't read from file %s\n", argv[1]);
		exit(1);
	}
	if((MarksDataFile = fopen(argv[2], "r")) == NULL)
	{
		printf("Can't read from file %s\n", argv[2]);
		exit(1);
	}

	numrecords=0;
	nummarks=0;
	info = (struct stdre*) malloc (MAXRECORDS* sizeof(struct stdre));
    while (fscanf(NamesIDsDataFile,"%s%s%d",&(info[numrecords].fn[0]),      //A Loop which reads two files at the same time
		      				&(info[numrecords].ln[0]),
		      				&(info[numrecords].IDn)) != EOF &&
    		fscanf(MarksDataFile,"%d",&(info[nummarks].Mk)) != EOF)
    {
    	root = addnote(root,info[numrecords]);                              //for every student record structure (stdre) it reads, adds to the binary tree

    	numrecords++;
    	nummarks++;
    }
	fclose(NamesIDsDataFile);
	fclose(MarksDataFile);

	pinfo = (struct stdre**) malloc (numrecords*sizeof(struct stdre**));    //Create a array of pointers which records the pointer as the binary tree is being traversed
    traversetree(root, pinfo);                                              //Pass the array of pointers to traverse function


    lun = (char*) malloc (MAXNAMELENGTH*sizeof (char));                     //Allocate the memory for "look up name" as a dynamic array of char
    if (argc == 4)                                                          //Check whether a name is input in the commander line
    {
    	sscanf(argv[3],"%s",lun);
    	info = binsearch (pinfo, lun, numrecords);
    	if (info != NULL)
    	{
    		printf("The following record is found:\n");                     //Print the information from the binary search function which matches with the input "look up name"
    		printf("Name: %s %s\n", info->fn, info->ln);
    		printf("Student ID: %d\n", info->IDn);
    		printf("Student Grade: %d\n", info->Mk);
    	}
    	else
    	{
    		printf("No record found for student with last name %s.\n", lun);
    	}
    }
    else
    	printf("Please input a last name in the commander line to look up!\n");
}

