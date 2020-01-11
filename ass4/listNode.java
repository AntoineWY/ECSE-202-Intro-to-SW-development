/**
 * The listNode class defines the property of listNode object, 
 * the basic elements in Queue and Stack structure. Its payload variable 
 * holds the information it carries as a String. It also has two pointer 
 * pointer to the next and last listNode. The information is set public to all other classes.
 * @author Antoine Wang 260766084
 *
 */
public class listNode {
	/* public instance variables for object listNode */
	public String payLoad;      /* The data listNode carries as a String */
	public listNode next;       /* Pointer to the next node */
	public listNode previous;   /* Pointer to the previous node */
	
	/**
	 * Create a new listNode object carrying the payload string
	 * @param payLoad The data listNode carries as a String
	 */
	public listNode(String payLoad) 
	{
		super();
		this.payLoad = payLoad;
	}
}
