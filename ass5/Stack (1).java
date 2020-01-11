/** 
 * The Stack class creates a data structure which stores the information. It can also pop 
 * out the information, following the sequence of "last in first out". The push method get the information into the stack
 * and the pop method eject the information and release the memory.  
 * @author Antoine Wang 260766084
 */
public class Stack {
	/* Declare a pointer pointing to the top of the stack */
	public listNode top;

	/**
	 * Creates a new Stack object (an empty stack) with a top pointer pointing to null
	 */
	public Stack(){
		super();
		top = null;
	}
	
	/** 
	 * Checks if the stack is empty
	 * @return Whether the stack is empty
	 */
	public boolean isEmpty()
	{
		return top == null;
	}
	
	/** 
	 * Pushes a new String into the stack 
	 * @param inputStr The data (String) which is going to be stored in the stack
	 */
	public void push(String inputStr)
	{
		/* Create an new listNode object which carries the input String as its payload */ 
		listNode addNote = new listNode(inputStr);
		
		/* If the stack is originally empty, simply set the newly addNote as the top of the stack */
		if (top == null){
			top = addNote;	
		}
		
		/* If the stack is not empty */ 
		else
		{
			/* Set the next pointer of the stack top pointing to the newly added note. 
			 * Then the newly added note points back to the original top node.
			 * Move the top pointer up to the newly added node.
			 */
			top.next = addNote;
			addNote.previous = top;
			top = addNote;
		}
	}
	
	/** 
	 * Pops out the top node in the stack and release the memory
	 * @return The data released from the stack as a String
	 */
	public String pop()
	{
		/* If the stack is empty, report error and return null */
		if (top == null)
		{
			System.out.println("Error: Empty stack");
			return null;
		}
		
		/* Declare a result string to hold popped data */
		String result = top.payLoad;
		
		/* If there is only one element inside the stack */
		if (top.previous== null)
		{
			top= null;
		}
		/* If there are multiple elements inside the stack */
		else
		{ 
			/* Break the connection between the top of the stack and second top,
			 * Then move the top pointer one node down 
			 */
			listNode beforeNode = top.previous;
			beforeNode.next= null;
			top = beforeNode;
		}
		return result;
	}
	
	/**
	 * Check the data at the top of the Stack which is the next to be popped
	 * This method is not used during the shunting yard algorithm
	 * but it can be called when doing check and debugging
	 * @return The data stored at the top of the stack
	 */
	public String checkTop(){
		if(top == null)
		{
			System.out.println("Empty Stack");
			return null;
		}
		else
			return top.payLoad;
	}

}
