
public class Node<T> {

	private T element;
	private Node<T> next;
	private Node<T> previous;
	
	public Node(T element) {
		this.element = element;
		next = null;
		previous = null;
	}
	
	public T getElement() {
		return element;
	}
	
	public void setElement(T element) {
		this.element = element;
	}
	
	public Node<T> getPrevious() {
		return previous;
	}
	
	public void setPrevious(Node<T> previous) {
		this.previous = previous;
	}
	
	public Node<T> getNext() {
		return next;
	}
	
	public void setNext(Node<T> next) {
		this.next = next;
	}

}
