import acm.gui.TableLayout;
import acm.program.Program;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * This program is a simple 4-operation graphic calculator.
 * It can only handle addition, subtraction, multiplication and division.
 * In case the user wants to change the priority of calculation, please use parentheses "()".
 * This version incorporates a slider to change precision.
 * @author Antoine Wang 260766084
 */ 
@SuppressWarnings("serial")
public class JGraphicCal extends Program implements ChangeListener, ActionListener{
	public void init(){
		resize(900,600); // Resize the canvas
		setLayout(new TableLayout(8,4,15,15));  // Define the table Layout
		expressionDisplay = new screenDisplay("",20); // add the text field for expression display at the first row
		answerDisplay = new screenDisplay("",20);  // add the text field for answer display at the second row
		digitDisplay = new screenDisplay(DEFAULT_DIGIT+"",2);
		digitSlider = new JSlider (0,10, DEFAULT_DIGIT);
		add(expressionDisplay, "gridwidth=4 height=" + BUTTON_SIZE/2);
		add(answerDisplay, "gridwidth=4 height=" + BUTTON_SIZE/2);
		addButtons();
		add(digitDisplay,"gridwidth=1 height=" + BUTTON_SIZE/2);
		digitDisplay.setText(DEFAULT_DIGIT+"");
		digitSlider.addChangeListener(this);  // Add a ChangeListener to the slider
		addActionListeners();
	}
	
	/**
	 * add buttons and Label to fit in 8*4 grids of the table layout with 15 pixels of separation between each element 
	 */
	private void addButtons(){
		String constraint = "width=" + BUTTON_SIZE + " height=" + BUTTON_SIZE/2;
		add(new JButton("7"), constraint);
		add(new JButton("8"), constraint);
		add(new JButton("9"), constraint);
		add(new JButton("+"), constraint);
		add(new JButton("4"), constraint);
		add(new JButton("5"), constraint);
		add(new JButton("6"), constraint);
		add(new JButton("-"), constraint);
		add(new JButton("1"), constraint);
		add(new JButton("2"), constraint);
		add(new JButton("3"), constraint);
		add(new JButton("x"), constraint);
		add(new JButton("0"), constraint);
		add(new JButton("."), constraint);
		add(new JButton("c"), constraint);
		add(new JButton("/"), constraint);
		add(new JButton("("), constraint);
		add(new JButton(")"), constraint);
		add(new JButton("="), "gridwidth= 2" + " height=" + BUTTON_SIZE/2);
		JLabel digitLabel = new JLabel ("Precision");
		digitLabel.setFont(new Font("Calibri",Font.PLAIN,20));
		add(digitLabel,constraint);
		add(digitSlider,"gridwidth= 2");
	}
	
	/** 
	 * Set the calculator's behavior when certain button is pressed
	 */
	public void actionPerformed(ActionEvent e){
		String action = e.getActionCommand();
		/* When a number or operator button is clicked */
		if (!action.equals("c") && !action.equals("="))
			expressionDisplay.addDigit(action);
		/* When the equal (=) sign is clicked, the programs calls the calculating methods */
		else if (action.equals("=")){
			resultValue = calWrapper(expressionDisplay.getText());
			result = String.format("%."+digitSlider.getValue()+"f", resultValue);
		    answerDisplay.setText(result);
		}
		/* when the clear button (c) is clicked, reset all the display and change the digit setting to 6 digits (default) */
		else{
		    digitDisplay.setText(DEFAULT_DIGIT+"");
		    digitSlider.setValue(DEFAULT_DIGIT);
		    expressionDisplay.setText("");
		    answerDisplay.setText("");
		}
	}
	
	/**
	 * Set the slider and the digit field's behavior when the value of the slider is changed
	 */
	public void stateChanged(ChangeEvent arg0){
		digitDisplay.setText(digitSlider.getValue() + "");
		result = String.format("%."+digitSlider.getValue()+"f", resultValue);
		answerDisplay.setText(result);
	}

	
	/**
	 * A wrapper method to encapsulate the core methods for the 
	 * implementation of the calculation, return a double for display purpose
	 * @param input The String typed in the JTextField of the calculator display
	 * @return A double as the result of the calculation result
	 */
	public double calWrapper(String input){
		StringTokenizer st = new StringTokenizer (input, "+-x/()",true);  
		
		while(st.hasMoreTokens()){
		/* Enqueue all the tokens to the input queue. */
		inputQueue.enqueue(st.nextToken());
		}
		outputQueue = postFix(inputQueue);
		return Double.valueOf(getResult(outputQueue));
	}
	
	/**
	 * The postFix method implements the switch of operators in the original expression.
	 * @param input The series of token strings to be sorted to post-fix form as a Queue
	 * @return A Queue containing the sorted token strings as post-fix expression
	 */
	private Queue postFix(Queue input){
		/* Declare a output Queue to hold the resultant strings and a operator 
	     * stack to pop operators based on their precedence */
		Queue output = new Queue();
		Stack operator = new Stack();
		while (!input.isEmpty())
		{
			/* Declare a string which hold the information whenever something is dequeued from the input queue*/
			String dqInput = input.dequeue(); 
			if (!isOperator(dqInput) && !isLp(dqInput) && !isRp(dqInput))
			{
				/*Put the operand (numbers) directly to the output Queue*/
				output.enqueue(dqInput);
			}
			/* If the string is an operator */
			else if (isOperator (dqInput))
			{
				while (!operator.isEmpty())
				{
					/* Check if the top of the operator stack has a higher precedence than the newly dequeued operator
					 * In the following two cases the top of the stack is popped to the output queue:
					 * 1. top has higher precedence
					 * 2. they have same precedence and the dequeued operator is left associative
					 */
					if (isOperator(operator.top.payLoad)&& 
							(isHigherpre(operator.top.payLoad,dqInput) || isEqualpre(operator.top.payLoad, dqInput)))
						output.enqueue(operator.pop());
					/* exit the loop to push the newly dequeued input operator into the stack */
					else 
						break;
				}
			operator.push(dqInput);
			}
			else if (isLp(dqInput))
				/* If the token is a left parentheses, push it on the stack directly */
				operator.push(dqInput);
			else{
				/* If the token is a right parentheses, keep popping out all the operators until 
				 * the left parentheses is met 
				 */
				while(!isLp(operator.top.payLoad)){
					output.enqueue(operator.pop());
				}
				/* Then discard the left parentheses */
				operator.pop();
			}
		}
		while (!operator.isEmpty()){
			/* After all the tokens from input is dequeued, pop the rest of the operators in the stack to the output queue */
			output.enqueue(operator.pop());
		}
		return output;
	}
		
	/**
	 * getResult method evaluates the postFix queue of a certain math expression. 
	 * By using another Stack to pop out operands for each step and hold intermediate results,
	 * it eventually returns a String containing the value (Double) of the result.
	 * @param postFix The post-fix from of a math expression as a group of Strings embedded in a Queue
	 * @return A String representing the value of the result
	 */
	private String getResult (Queue postFix){
		Stack hold = new Stack();
		while (!postFix.isEmpty()){
			if (!isOperator(postFix.front.payLoad))
				/* Dequeue the operands (numbers) to the stack */
				hold.push(postFix.dequeue());
			else{
				/* If the next token String is a operator, pop out two numbers recently pushed and perform calculation */
				String num1 = hold.pop();
				String num2 = hold.pop();
				/* Since the Stack follows "last in first out", the sequence of the 
				 * operands need to be switched when being passed the doCal method */
				String result = doCal(num2, num1, postFix.dequeue());
				hold.push(result);			
			}
		}
		return hold.top.payLoad;
	}
		
	/**
	 * doCal method serves as the interpreter that evaluate the expression in the post-fix form
	 * @param num1 The operand on the left side of the operator during the calculation as a String
	 * @param num2 The operand on the right side of the operator during the calculation as a String
	 * @param sign The operator which defines which kind of calculation is performed as a String
	 * @return A String which express the value of a double (the calculated result)
	 */
	private String doCal(String num1, String num2, String sign){
		double calResult;
		String calResultStr;
		/* Those four if statements specify four types of operators ("+", "-", "*", "/") 
		and their corresponding operations */
		if (sign.equals("+")){
			/* Convert numeric String into double and calculate the sum in this case */
			calResult = Double.valueOf(num1) + Double.valueOf(num2);
			/* Convert the result (double) back to the String and return it */
			calResultStr = String.valueOf(calResult);
			return calResultStr;
		}
		else if (sign.equals("-")){
			/* Performs subtraction */
			calResult = Double.valueOf(num1) - Double.valueOf(num2);
			calResultStr = String.valueOf(calResult);
			return calResultStr;
		}
		else if (sign.equals("x")){
			/* Performs multiplication */
			calResult = Double.valueOf(num1) * Double.valueOf(num2);
			calResultStr = String.valueOf(calResult);
			return calResultStr;
		}
		else{
			/* Performs division */
			calResult = Double.valueOf(num1) / Double.valueOf(num2);
			calResultStr = String.valueOf(calResult);
			return calResultStr;
		}
	}
		
	/**
	 * Check whether the first operator has a higher precedence than the second
	 * If the first one has a higher precedence, return true. 
	 * @param op1 The first operator token from input to be compared as a String
	 * @param op2 The second operator token from input to be compared as a String
	 * @return Whether the first operator has a higher precedence
	 */
	private boolean isHigherpre (String op1 , String op2){
		Operator first = new Operator(op1);
		Operator second = new Operator(op2);
		
		first.defineOperator(first);
		second.defineOperator(second);
		
		return (first.precedence > second.precedence);
	}
	
	
	/**
	 * Check whether the first operator has a higher precedence than the second
	 * If both operators have the same precedence, return true. 
	 * @param op1 The first operator token from input to be compared as a String
	 * @param op2 The second operator token from input to be compared as a String
	 * @return Whether the both operators have equal precedence
	 */
	private boolean isEqualpre (String op1 , String op2){
		Operator first = new Operator(op1);
		Operator second = new Operator(op2);
		
		first.defineOperator(first);
		second.defineOperator(second);
		
		return (first.precedence == second.precedence);
	}
	
	/**
 	 * Check whether a input token from the origin input expression is a operator
 	 * by comparing it to a string which is actually defined as "+","-","x" or "/"
 	 * @param inputTk The input token string, after being segmented from the input expression string
 	 * @return Whether the token is a operator ("+","-","x" or "/")
 	 */
	private boolean isOperator (String inputTk){
		return (inputTk.equals("+") || inputTk.equals("-") || inputTk.equals("x") || inputTk.equals("/"));
	}
	
	/**
	 * Check whether the input token String is a left parentheses ("(").
	 * @param inputTk Token String to be checked
	 * @return Whether this String is a left parentheses
	 */
	private boolean isLp(String inputTk){
		return (inputTk.equals("("));
	}
	
	/**
	 * Check whether the input token String is a right parentheses (")").
	 * @param inputTk Token String to be checked
	 * @return Whether this String is a right parentheses
	 */
	private boolean isRp(String inputTk){
		return (inputTk.equals(")"));
	}

	
	/**
	 * The Operator class adds additional information (precedence and associativity)
	 * to the input operators which are actually strings.
	 */
	public class Operator {
		/* Declare a string as the content of the object operator */
		String operator;
		/* Declare a integer number to define the precedence of the operator. Higher precedence, larger the value of the integer */
		int precedence;
			
		/**
		 * Creates a new Operator object with the input operator token (string) 
		 * and also adds the properties as precedence and associativity
		 * @param inputOp The operator token read from the input expression as a String
		 */
		public Operator(String inputOp){
			super();
			operator = inputOp;
		}	
		/**
		 * Defines the operator's precedence by a switch loop 
		 * @param operator The object created which contains the token and its properties as a Operator object
		 */
		public void defineOperator (Operator operator){
			switch(operator.operator){
			case "+" : case "-":
				/* Lower precedence for plus and minus */
				operator.precedence = 0;
				break;
			case "x" : case "/":
				/* Higher precedence for multiple and divide */
				operator.precedence = 1;
				break;
			}
		}	
	}
	private static final int BUTTON_SIZE = 100; // Set the dimension for each button
	private static final int DEFAULT_DIGIT = 6; // Set the default number of decimal digits to 6 
	/* Elements on the calculator including 3 displays and a slider */
	screenDisplay expressionDisplay;            
	screenDisplay answerDisplay;
	screenDisplay digitDisplay;
	JSlider digitSlider;
	/* Declare two Queues to receive the tokenized Strings and pass to the postFix and calculation methods */ 
	private Queue inputQueue = new Queue();
	private Queue outputQueue = new Queue();
	/* Declare a String and double to be passed to both actionPerformed (doing calculation)
	 * and stateChanged method (changes digit) */
	String result;
	double resultValue;
}
