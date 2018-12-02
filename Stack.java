public class Stack{
	//Variables
	public listNode top;
	
	//Constructor
	public Stack(){
		super();
		top = null;
	}

	//Adding operator to the top of the stack
	public void push(String newStr){
		listNode elem = new listNode(newStr);
		if(top != null){		
			top.next = elem;
			elem.prev = top;		
		}
		top = elem;
	}
	
	//Removing the operator at the top of the stack(last operator added)
	public String pop(){
		if(top == null){
			System.out.println("Error: empty stack");
			return null;
		}
		
		String result_str = top.val;		
		if(top.prev == null){
			top = null;  
		}
		else{
			listNode pre_top = top.prev;
			pre_top.next = null;
			top = pre_top;
		}			
		return result_str;
	}	
	
	//Print stack
	public void print(){
		listNode temp=top;
		while(temp!=null){
			System.out.print(temp.val+" ");
			temp=temp.prev;
		}
		System.out.println();
	}
	
}
	
	
