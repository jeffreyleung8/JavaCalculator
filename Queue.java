public class Queue{
	//Variables
	public listNode top;
	public listNode rear;

	//Constructor
	public Queue(){}
	
	//Add element to queue
	public void enqueue(String newStr){
		listNode elem = new listNode(newStr);

		if(rear == null){
			rear=elem;
			top=elem;
		}
		else{
			elem.next = rear;
			rear.prev = elem;
			rear = elem;
		}

	}
	
	//Removing the first element added
	public String dequeue(){
		if(top == null){
			System.out.println("Error: the queue is empty");
			return null;
		}

		String result_str = top.val;
		
		if(top.prev == null){			
			top = null;
			rear = null;
		}
		else{
			listNode pre_front = top.prev;
			pre_front.next = null;
			top = pre_front;
		}
		
		return result_str;
	}
	
	//Print queue
	public void print(){
		listNode temp=top;
		if(top!=null){
			while(temp!=null){
				System.out.print(temp.val+" ");
				temp=temp.prev;
			}
		}
		else{
			System.out.println("Error: the queue is empty");
		}
	} 
		
}

