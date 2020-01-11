import acm.program.ConsoleProgram;
import java.util.StringTokenizer;

/**
 * This program is a simple 4-operation calculator.
 * It can only handle addition, subtraction, multiplication and division.
 * In case the user wants to change the priority of calculation, please use parentheses "()".
 * @author Antoine Wang 260766084
 */ 
@SuppressWarnings("serial")
public class JCal extends ConsoleProgram{
	
	public void run() {
		/* Declare input queues to hold the tokenized strings and pass to methods*/
		Queue inputQueue = new Queue();
		/* Declare output queues to receive the sorted strings as a queue */
		Queue outputQueue = new Queue();
		
        resize(900,600);
        setFont("calibri-30");
		
        /* Introduction to the program */
		println("This program calculate a simple math expression which uses only binary operators.");
		println("Supported operators including \" + \", \" - \",\" * \", \" / \",\" ( \" and \" ) \".");
		println("Please type CLOSE to exit the program.");
		
		/* An infinite loop which keeps executing the calculation until the sentinel (CLOSE) is input */
        while(true)
		{
			String str = readLine ("Enter string: ");
			/* Test whether the program should be ended by exit the loop if the sentinel is input*/
			if (str.equals(SENTINEL))
				break;
			else{
				/* Segment the input string (the infix expression) into operators and operands*/ 
				StringTokenizer st = new StringTokenizer (str, "+-*/()",true);  
				
				while(st.hasMoreTokens()){
					/* Enqueue all the tokens to the input queue. */
					inputQueue.enqueue(st.nextToken());
				}
				/* Transform into post-fix form. */
				outputQueue = postFix(inputQueue);
				/* Print out the original expression and the calculated value */
				println(str + " = " + getResult(outputQueue));
			}
		}
		System.exit(1);
	}
	
	/**
	 * The postFix method implements the switch of operators in the original expression.
	 * @param input The series of token strings to be sorted to post-fix form as a Queue
	 * @return A Queue containing the sorted token strings as post-fix expression
	 */
		public Queue postFix(Queue input){
			/* Declare a output Queue to hold the resultant strings and a operator 
	         * stack to pop operators based on their precedence
			 */
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
		public String getResult (Queue postFix){
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
		public String doCal(String num1, String num2, String sign){
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
			else if (sign.equals("*")){
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
	public boolean isHigherpre (String op1 , String op2){
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
	public boolean isEqualpre (String op1 , String op2){
		Operator first = new Operator(op1);
		Operator second = new Operator(op2);
		
		first.defineOperator(first);
		second.defineOperator(second);
		
		return (first.precedence == second.precedence);
	}
	
	/**
 	 * Check whether a input token from the origin input expression is a operator
 	 * by comparing it to a string which is actually defined as "+","-","*" or "/"
 	 * @param inputTk The input token string, after being segmented from the input expression string
 	 * @return Whether the token is a operator ("+","-","*" or "/")
 	 */
	public boolean isOperator (String inputTk){
		return (inputTk.equals("+") || inputTk.equals("-") || inputTk.equals("*") || inputTk.equals("/"));
	}
	
	/**
	 * Check whether the input token String is a left parentheses ("(").
	 * @param inputTk Token String to be checked
	 * @return Whether this String is a left parentheses
	 */
	public boolean isLp(String inputTk){
		return (inputTk.equals("("));
	}
	
	/**
	 * Check whether the input token String is a right parentheses (")").
	 * @param inputTk Token String to be checked
	 * @return Whether this String is a right parentheses
	 */
	public boolean isRp(String inputTk){
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
	 * Creates a new Student object with the input operator token (string) 
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
				case "*" : case "/":
					/* Higher precedence for multiple and divide */
					operator.precedence = 1;
					break;
				}
			}	
		}

	/* The String Sentinel which exit the console program */ 
	private static final String SENTINEL = "CLOSE";
}

		


