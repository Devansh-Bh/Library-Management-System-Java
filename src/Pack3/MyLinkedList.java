package Pack3;

public class MyLinkedList {

    private Node head;

    public void add(String data){

        Node newNode = new Node(data);

        if(head == null){
            head = newNode;
            return;
        }

        Node temp = head; // start frm head

        // mve to last node
        while (temp.next != null){
            temp = temp.next;
        }

        // connect last node to new node
        temp.next = newNode;
    }

    public void display(){

        if(head == null){
            System.out.println("No History Available!");
            return;
        }
        Node temp = head;

        while (temp != null){
            System.out.println(temp.data);
            temp = temp.next;
        }
    }
}
