import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import acm.gui.TableLayout;
import acm.program.Program;

public class JCalc extends Program implements ChangeListener, ActionListener {

	String expression = ""; //string that stores the input 
	JTextField input = new JTextField(""); // input text field 
	JTextField output = new JTextField(""); // output text field
	JTextField precision = new JTextField(""); // precision text field
	JSlider slider;
	static int default_dec = 6;
	int decPlace; 
	double result = 0.0;

	//Method that returns the rank of an operator
	 public static int precedence(String operator){
			char op = operator.charAt(0);
			switch(op){
			case '+': return 2;
			case '-': return 2;
			case '*': return 3;
			case '/': return 3;
			case '^': return 4;
			case 'u': return 5;
			case '%': return 5;
			case 'p': return 5;
			default:  return 0;
			}
		}
	    
	//Method that checks if the string is an operator
	public static boolean isOperator(String str){
		String op = "+-*/()^u%p";
		//Comparing the string to array op
		for(int i=0; i< op.length();i++){
			for(int j=0; j<str.length();j++)
				if(str.charAt(j)==op.charAt(i)){
				return true;
			}
		}
		return false;
	} 
	
	//Method that reformats string (replaces unary minus by u and removes unary plus)
	//unary minus/plus if first elem or following operator including parenthesis
	public static String checkUnary(String str){
		String newStr="";
		for(int i=0; i<str.length();i++){
			if(str.charAt(i)=='-'){
				if(i==0){
					newStr+="u";
				}
				else if(isOperator(Character.toString(str.charAt(i-1)))){
					newStr+="u";
				}
				else{
					newStr+="-";
				}
			}
			else if(str.charAt(i)=='\u03C0'){
				newStr+="p";
			}
			else if(str.charAt(i)=='+'){
				if(i==0){
					continue;	
				}
				else if(isOperator(Character.toString(str.charAt(i-1)))){
					continue;
				}
				else {
					newStr+="+";
				}
			}
			else{
				newStr+=Character.toString(str.charAt(i));
			}
		}
		return newStr;	
	}
	
	//Method putting queue in postfix notation(Shunting Yard Algorithm)
	public static Queue postfix(StringTokenizer st, Queue queue, Stack stack){
		String token;
	
		while (st.hasMoreTokens()) { 
			token = st.nextToken(); 
			
			if (Character.isDigit(token.charAt(0))){
				queue.enqueue(token);	
			}
			
			
			else if(isOperator(token)){
				if(token.contains("(")){
					stack.push(token);	
				}	
	
				else if(token.contains(")")){
					while(stack.top.val != null && !stack.top.val.contains("(")){
						queue.enqueue(stack.pop());
						}	
					stack.pop(); 
				}
				else{ 
					while (stack.top != null && precedence(token) <= precedence(stack.top.val)){ 
						queue.enqueue(stack.pop());
					}
					stack.push(token);	
				}
			}
			else{
				//if invalid characters(other than number and operator) are present
				return null;
			}
		}
		while (stack.top != null){
			queue.enqueue(stack.pop());
		}
			return queue;
	}
		
	//Method for basic math operations(+-*/^)
	public static Double operation(String operator,Double num1, Double num2){
		char op = operator.charAt(0);
		switch(op){
			case '^': return Math.pow(num1,num2);
			case '+': return num1+num2;
			case '-': return num1-num2;
			case '*': return num1*num2;
			case '/': return num1/num2;
			case 'u': return -num2;
			case '%': return num2*0.01;
			case 'p': return num2*Math.PI;
			default:  return 0.0;
			}
	}
		
	//Method that calculates the expression using the postfix notation 
	public static double calculation(Queue queue){
		Stack answer = new Stack(); 
		double num1, num2;
		
		while (queue.top != null){
			if (Character.isDigit(queue.top.val.charAt(0))){
				answer.push(queue.dequeue());
			}
			else {
				if(queue.top.val.contains("u")){
					num2=Double.parseDouble(answer.pop());
					num1=0.0;
				}
				else if(queue.top.val.contains("%")){
					num2=Double.parseDouble(answer.pop());
					num1=0.0;
				}
				else if(queue.top.val.contains("p")){
					if(answer.top==null){
						num2=1.0;
						num1=0.0;
					}
					else{
						num2=Double.parseDouble(answer.pop());
						num1=0.0;
					}
				}
				else if (answer.top == null){
					num1 = 0.0;
					num2 = 0.0;
				}
				else {
					num2 = Double.parseDouble(answer.pop());
					if (answer.top == null){
						num1 = 0;
					}
					else {
						num1 = Double.parseDouble(answer.pop());
					}
				}	
				answer.push(String.valueOf(operation(queue.top.val,num1,num2)));
				queue.dequeue();
			}
		}
		
		return (Double.parseDouble(answer.pop()));
	}
	
	//Method that returns a string with the correct decimal force
	//2 decimal -> "#.00"
	public static String decFormat(int value){
		String str="#0";
		if(value !=0){
			str+=".";
			for(int i=0; i<value;i++){
				str+=0;
			}
		}
		return str;
	}
		
	public void init(){
		
		setSize(325, 350);
		setLayout(new TableLayout(9, 4));
		
		output.setEditable(false);
		output.setBackground(Color.WHITE);
		
		add(input,"gridwidth = 4 height = 25");
		add(output,"gridwidth = 4 height = 25");
		
		
		String BUTTON_WIDTH = "70";
		String BUTTON_HEIGHT = "35";
		String button_label[]= {"C","+/-","(", ")","DEL", "%","^","/","7","8","9","*",
								"4","5","6","-","1","2","3","+","0",".","\u03C0","="};
		
		String constraint = "width=" + BUTTON_WIDTH + " height=" + BUTTON_HEIGHT;
		
		//Create buttons with an actionlistener and add in the applet
		for (int i = 0; i < button_label.length; i++) {
			JButton cur_button = new JButton(button_label[i]);
			cur_button.addActionListener(this);
			add(cur_button, constraint);
		}
		
		//Label beside the slider
		add(new JLabel("Precision"));
		
		//Slider
		decPlace = default_dec;
		String default_val_str = decPlace + "";
		slider = new JSlider(0,10,decPlace);
	
		slider.addChangeListener(this);
		add(slider,"gridwidth = 2");
		
		//Precision text field
		precision.setText(default_val_str);
		precision.setEditable(false);
		precision.setBackground(Color.WHITE);
		add(precision);	
	}

	//Output changes when slider is moved
	@Override
	public void stateChanged(ChangeEvent arg0) {
		
		decPlace=slider.getValue();
		
		//Display precision
	    precision.setText(Integer.toString(decPlace));
	    
	    //Format output
	    String format = decFormat(decPlace);
		DecimalFormat df= new DecimalFormat(format);
		String finalresult = df.format(result);

		if(result==0.0){
			output.setText("");
		}
		else{
			output.setText(finalresult);
		}
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand()=="="){
			expression = input.getText();

			Queue queue = new Queue(); 
			Stack stack_op = new Stack(); 

			//Reformat string 
			String newStr=checkUnary(expression);
			
			//Separate string in token	
			StringTokenizer st = new StringTokenizer(newStr, "+-/*^()u%p", true);
				
			//Postfix notation 
			queue = postfix(st, queue, stack_op);
		
			//Format String
			String format = decFormat(decPlace);
			DecimalFormat df= new DecimalFormat(format);
			
			result = calculation(queue);
			
			//Display answer with correct format according to string format 
			output.setText(df.format(result));						
		}
		
		//Remove a character from the input
		else if(e.getActionCommand()=="DEL"){
			if(input.getText()=="" || expression==""){
				output.setText("ERROR");
			}
			else{
				int lastPosition=input.getText().length()-1;
				String substr = input.getText().substring(0, lastPosition);
				expression=substr;
				input.setText(substr); 
				output.setText(null);
			}
		}
		//Reset everything(input, output, result,expression)
		else if(e.getActionCommand()=="C"){
			input.setText(null);
			output.setText(null);
			expression ="";
			result=0.0;
		}
		//Adds a unary minus or removes it
		else if(e.getActionCommand()=="+/-"){
			if(input.getText()=="" || expression==""){
				expression += "-";
				input.setText(expression);
			}
			else{
				int lastPosition=input.getText().length()-1;
					if(input.getText().charAt(lastPosition)=='-'){
						String substr = input.getText().substring(0, lastPosition);
						input.setText(substr);
					}
					else{
						expression+="-";
						input.setText(input.getText()+"-");    		
					}
			}
		}
		//Display other buttons' value directly in the output
		else{
			expression += e.getActionCommand();
			input.setText(expression);
		}
	}
}
