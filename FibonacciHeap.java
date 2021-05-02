/**
 * 
 * @author Chitranshu Raj
 */
 
class Node {
	
	String hashtag;					//Hashtag Value
	int key;						//Key Value
	int Degree;						//Node Degree
	boolean childcutval;			//Child Cut Value of Node 
	Node cnode;						//Child Node to Node
	Node pnode;						//Parent Node to Node
	Node lnode;						//Left Pointer 										
	Node rnode;						//Right Pointer 			

	public Node() {
	}
	
	/*
	 * Initialize the Node Constructor with @param=key
	 */
	 
	public Node(int key) {
		rnode = this;
		lnode = this;
		this.key = key;
	}
	
	/*
	 *Initialize the Node Constructor with @param=key and @param=hashtag
	 */
	 
	public Node(int key, String hashtag) {
		rnode = this;
		lnode = this;
		this.key = key;
		this.hashtag = hashtag;
	}
	
	/*
	 * Returns key value
	 */
	 
	public final int GET_KEY() {
		return key;
	}
	
	/*
	 * Returns hashtag value
	 */
	 
	public final String GET_TAG() {
		return hashtag;
	}
}

class FibonacciHeap {
	
	/* MAX NODE */
	private Node MaxNode;
	/* NODE COUNT */
	private int ncount;

	/*
	 * The check_Empty operation checks the heap for its
	 * empty or not; if it is then it returns true 
	 * else false
	 */
	public boolean check_Empty() {
		if(MaxNode == null){
			return true;
		}
		else{
			return false;
		}
	}


	/*
	 * The increment_key operation increases the key value of the
	 * given current node to the new key value that is passed as
	 * a parameter.
	 */
	public Node increment_key(Node current_node, int new_key) {
		Node parent_node=null;
		current_node.key = new_key;                    
		parent_node = current_node.pnode;
		/* If the key value of the current node now points to greater
		 * than its parent, then perform cut and cascade cut operation
		 * to follow the properties of the fibonacci heap
		 */
		if((parent_node != null)&&(current_node.key > parent_node.key)) { 
			cut_operation(current_node, parent_node);
			cascadecut_operation(parent_node);
		}
		/* If the current node has key value greater than the max node
		 * set it as the new max node
		 */
		if(current_node.key > MaxNode.key) {          
			MaxNode = current_node;
		}
		return current_node;
	}


	/*
	 * The add_node operation inserts a new node into the 
	 * heap with respect to its key value.
	 */

	public void add_node(Node new_node, int key) {
		new_node.key = key;
		if(MaxNode != null) {                  
			new_node.lnode = MaxNode;        
			new_node.rnode = MaxNode.rnode;
			MaxNode.rnode = new_node;
			new_node.rnode.lnode = new_node;
			/* 
			 * After a new node is inserted, a 
			 * heap check is followed by, checking
			 * if the newly inserted node has the
			 * key value greater than that of max_node
			 * or not
			 */
			if(key > MaxNode.key) {             
				MaxNode = new_node;
			}
		}
		/* 
		 * If the heap is empty, set the new node as the 
		 * maxnode 
		 */
		else {                                  
			MaxNode = new_node;
		}
		/* Increase the heap size */
		ncount++;                           
	}
	

	/*
	 * The cascadecut operation() removes the children node 
	 * from parent node list recursively, until a parent node 
	 * with child cut value False is encountered.
	 */

	protected void cascadecut_operation(Node child_node) {
		Node parent_node = child_node.pnode;
		if(parent_node != null) {
			/* 
			 * The a parent is encountered with child cut
			 * value "false"; the cascade cut stops and it 
			 * sets the child cut value of the respective
			 * node to "true"
			 */
			if(!child_node.childcutval) {
				child_node.childcutval = true;
			}
			else {
				cut_operation(child_node, parent_node);
				cascadecut_operation(parent_node);
			}
		}
	}
	
	
	/*
	 * The cut_operation() takes as argument the child node which
	 * has to removed and it removes it from the children list
	 * of the parent. It sets the pointer of child node to null
	 * and adds it to the root list, associating it as root 
	 * respectively.
	 */
 
	protected void cut_operation(Node child_node, Node parent_node) {
		child_node.lnode.rnode = child_node.rnode;
		child_node.rnode.lnode = child_node.lnode;
		parent_node.Degree--;
		if(parent_node.cnode == child_node) {
			parent_node.cnode = child_node.rnode;
		}
		if(parent_node.Degree == 0) {
			parent_node.cnode = null;
		}
		/*Add the child node to the root list, next to max node*/
		child_node.lnode = MaxNode;
		child_node.rnode = MaxNode.rnode;
		MaxNode.rnode = child_node;
		child_node.rnode.lnode = child_node;
		child_node.pnode = null;
		child_node.childcutval = false;
	}
	

	/*
	 * The removeMAX() operation removes the maxnode from the
	 * heap and returns it as the result, while setting the 
	 * pointer of its children to null value.
	 */
	public Node removeMAX_operation() {
		Node removenode = MaxNode;
		if(removenode != null) {
			int  childCount = removenode.Degree;
			Node child_node = removenode.cnode;
			Node temp_child;
			/* The removeMAX operation resets the 
			 * pointer of children nodes. 
			 */
			while(childCount > 0) {
				temp_child = child_node.rnode;
				/*Now remove the child node from the child node node list.*/
				child_node.lnode.rnode = child_node.rnode;
				child_node.rnode.lnode = child_node.lnode;
				/* Add child node to root list and parent pointer
				 * to null 
				 */
				child_node.lnode = MaxNode;
				child_node.rnode = MaxNode.rnode;
				MaxNode.rnode = child_node;
				child_node.rnode.lnode = child_node;
				child_node.pnode = null;
				child_node = temp_child;
				childCount--;
			}
			/* Remove the max node from the root list */
			removenode.lnode.rnode = removenode.rnode;
			removenode.rnode.lnode = removenode.lnode;
			if(removenode == removenode.rnode) {
				MaxNode = null;
			}
			else {
				MaxNode = removenode.rnode;
				Pairwise_Combine_Operation();
			}
			ncount--;
		}
		/* Return the max node */
		return removenode;
	}
	

	/*
	 * PairWise Combination operation merges two trees with equivalent degrees
	 * into a single tree, until no tree has the same degree corresponding 
	 * to that of the other tree.
	 */
	protected void Pairwise_Combine_Operation() {
		int len_array = ncount + 1;
		Node[] rootpointer_list = new Node[len_array];
		for(int i = 0; i < len_array; i++) {
			rootpointer_list[i] = null;
		}
		/* FIND ROOT NODE COUNT */
		int count_root = 0;
		Node a = MaxNode;

		if(a != null) {
			count_root++;
			a = a.lnode;
			while(a != MaxNode) {
				count_root++;
				a = a.lnode;
			}
		}
		while(count_root > 0) {
			int index = a.Degree;
			Node next = a.lnode;
			while(rootpointer_list[index] != null) { 
				/* IF A NODE WITH SAME DEGREE IS ENCOUNTERED,
				 * the node with key value greater than the other
				 * becomes the parent node and is added to root
				 * list. The node with less key value is the 
				 * new child node to the parent node.
				 */
				Node b = rootpointer_list[index];
				if(a.key < b.key) {
					Node temp = b;
					b = a;
					a = temp;
				}
				b.lnode.rnode = b.rnode;
				b.rnode.lnode = b.lnode;
				b.pnode = a;
				if(a.cnode == null) {
					a.cnode = b;
					b.rnode = b;
					b.lnode = b;
				}
				else {
					b.lnode = a.cnode;
					b.rnode = a.cnode.rnode;
					a.cnode.rnode = b;
					b.rnode.lnode = b;
				}
				/* The degree of parent is incremented */
				a.Degree++;
				/* The child cut value of child is set to false*/
				b.childcutval = false;
				rootpointer_list[index] = null;
				index++;
			}
			rootpointer_list[index] = a;
			a = next;
			/*Count of number of roots is decreased.*/
			count_root--;
		}
		MaxNode = null;
		/*Re-initialize the root list, and set the values of newly obtained roots */
		for(int i = 0; i < len_array; i++) {
			if(rootpointer_list[i] != null) {           
				if(MaxNode != null) {
					rootpointer_list[i].lnode.rnode = rootpointer_list[i].rnode;
					rootpointer_list[i].rnode.lnode = rootpointer_list[i].lnode;
					rootpointer_list[i].lnode = MaxNode;
					rootpointer_list[i].rnode = MaxNode.rnode;
					MaxNode.rnode = rootpointer_list[i];
					rootpointer_list[i].rnode.lnode = rootpointer_list[i];
					/* Set the max node after the combination is completed */
					if(rootpointer_list[i].key > MaxNode.key) {           
						MaxNode = rootpointer_list[i];
					}
				}
				else {
					MaxNode = rootpointer_list[i];
				}
			}
		}	
	}
}

