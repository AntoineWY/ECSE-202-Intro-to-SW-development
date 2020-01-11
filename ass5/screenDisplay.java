import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextField;

/**
 * screenDisplay class refers to the textFields on the calculator. In this class,
 * an additional function for the TextField is added which is to add the command string of the button 
 * on it every time the button is clicked.
 * @author Antoine Wang 260766084
 */
@SuppressWarnings("serial")
public class screenDisplay extends JTextField{
		
	/**
	 * Create a constructor for a screenDisplay, which is essentially a non-editable textField 
	 * with a particular font.
	 * @param initValue The initial String in the display
	 * @param column The maximum column allowed in the display
	 */
		public screenDisplay(String initValue, int column){
			setFont(new Font ("SansSerif", Font.PLAIN,20));
			setEditable(false);
			setBackground(Color.WHITE);
		}
		
		/**
		 * addDight method allows the String in the display to extend every time a button is clicked
		 * @param buttonInput The new number or operator (as a String) added on the display
		 */
		public void addDigit(String buttonInput){
			String currentDisplay = getText();
			setText(currentDisplay + buttonInput);
		}		
	}