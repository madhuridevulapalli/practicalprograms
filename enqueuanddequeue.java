public class SimpleQueue(){
	int size=10;
	int[] queue= new int[size];
	int front=-1;
	int rare=-1;
}
void enqueue(int value){
	if(rare == size-1){
		System.out.print("queue is full");
	}
	else{
		if(front==-1)front=0;
			rare++;
			queue[rare]=value;
			System.out.print(value + "inserted");
	}
	void dequeue(){
		if(front=-1||front>rare){
			System.out.print("queue is empty");
		}
		else{
			System.out.print(queue[front]+"removed");
			front++;
		}
	}
	void display(){
		if(front=-1||front>rare){
			System.out.print("queue is empty");
		}
		else{
			System.out.print("queue elements");
			for(int i=front;i<=rare;i++){
				System.out.print(queue[i]+"");
			}
			System.out.println();
		}
	}
	public static void main(string args[]){
		SimpleQueue q=new SimpleQueue();
		q.enqueue(10);
		q.enqueue(20);
		q.enqueue(30);
		q.enqueue(40);
		q.display();
		q.dequeue();
		q.display();
		q.enqueue(50);
	}
}
				
		