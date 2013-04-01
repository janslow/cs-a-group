package grouppractical.utils;

/**
 * Class representing a node in a doubly-linked list. Allows O(1) movement to next, previous and head nodes
 * @author janslow
 *
 * @param <T> Type of the data to store in the node
 */
class Node<T> {
	private final T data;
	private Node<T> next, prev;
	private Node<T> head;
	
	/**
	 * Constructs a new header node
	 */
	public Node() {
		this.data = null;
		this.head = this.next = this.prev = this;
	}
	/**
	 * Constructs a new data node
	 * @param data Data to contain
	 * @param prev Previous node in list
	 * @param next Next node in list
	 */
	private Node(T data, Node<T> prev, Node<T> next) {
		this.data = data;
		this.next = next; this.prev = prev;
	}
	
	/**
	 * Data held by node
	 * @return Data
	 */
	public T getData() { return data; }
	/**
	 * Gets the next node in the list
	 * @return Next Node
	 */
	public Node<T> next() { return next; }
	/**
	 * Gets the previous node in the list
	 * @return Previous node
	 */
	public Node<T> prev() { return prev; }
	/**
	 * Gets the head node in the list
	 * @return Header Node
	 */
	public Node<T> head() { return head; }
	/**
	 * Gets whether this node is the head
	 * @return True if this is the head, otherwise false
	 */
	public boolean isHead() { return head() == this; }
	
	public boolean isEmpty() { return head() == head().next(); }
	/**
	 * Inserts a new node after the current node
	 * @param newData Data for the new node
	 * @return Returns the next mode (i.e., the new node)
	 */
	public Node<T> insertAfter(T newData) {
		Node<T> n = new Node<T>(newData, this, next);
		this.next.prev = n;
		this.next = n;
		return n;
	}
	/**
	 * Removes this node from the list
	 * @return Returns the next node in the list, or the head node, if this node is the head
	 */
	public Node<T> delete() {
		if (!isHead())
			return this;
		next.prev = prev;
		prev.next = next;
		return next;
	}
}
