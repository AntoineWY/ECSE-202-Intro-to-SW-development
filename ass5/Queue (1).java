/**
 * The Queue class creates a data structure. It contains enqueue (put information in) and dequeue 
 * (release information) methods which handle the data following the sequence of "First in first out"
 * @author Antoine Wang 260766084
 */
public class Queue{
	
	/* Declare two pointers, pointing the first and the last element of the Queue respectively */
	public listNode back;
	public listNode front;
	
	/**
	 * Creates a empty Queue with nothing inside
	 */
	public Queue() {
		super();
		back = null;
		front = null;
	}
	
	/**
	 * Checks whether the Queue is empty
	 * @return Whether the Queue is empty
	 */
	public boolean isEmpty(){
		return back==null;
	}
	
	/**
	 * Adds a new node carrying the payload (String) to the back of the Queue
	 * @param inputStr The data which is going to be attached to the back of the Queue as a String
	 */
	public void enqueue(String inputStr){
		/* Create a new listNode object to hold the payload string */
		listNode addNote = new listNode (inputStr);
		
		/* If the Queue is empty */
		if (back== null){
			/* addNote is the only element. It is both the front and the back. */ 
			back = addNote;
			front = addNote;  
		}
		/* If Queue is not empty, adding a new node at the back */
		else
		{
			/* Link the newly added note to the back and move the back pointer to the new back node */
			addNote.next=back;
			back.previous = addNote;
			back = addNote;
		}
	}
	
	/**
	 * Gets the information from the Queue which is input first
	 * @return The data enqueued first in the Queue as a String
	 */
	public String dequeue(){
		/* If the Queue is empty, get nothing and report error */
		if (front == null){
			System.out.println("Error: Empty");
			return null;
		}
		
		/* Declares a stirng to hold the data */
		String result = front.payLoad;
		
		/* If there is only one element in the queue */
		if (front.previous==null)
		{
			front = null;
			back = null;
		}
		/* If there is multiply elements in the queue */
		else
		{
			/* Gets the first enqueued node at the front,
			 * break the link and move the front pointer one node to the back
			 */
			listNode beforeFront = front.previous;
			beforeFront.next = null;
			front = beforeFront;
		}
		return result;
	}
	

}
