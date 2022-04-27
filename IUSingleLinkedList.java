import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Single-linked node implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported.
 * 
 * @author Preston Hardy
 * 
 * @param <T> type to store
 */
public class IUSingleLinkedList<T> implements IndexedUnsortedList<T> {
	private Node<T> head, tail;
	private int size;
	private int modCount;
	private Node<T> previous; // used to fix iterator tests
	
	/** Creates an empty list */
	public IUSingleLinkedList() {
		head = tail = null;
		previous = null;
		size = 0;
		modCount = 0;
	}

	@Override
	public void addToFront(T element) {
		Node<T> newNode = new Node<T>(element);
		
		newNode.setNext(head);
		
		head = newNode;
		
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		add(size, element); 
	}

	@Override
	public void add(T element) {
		addToRear(element);
	}

	@Override
	public void addAfter(T element, T target) {
		Node<T> currentNode = head;
		Node<T> previousNode = null;
		boolean found = false;
		
		if(isEmpty()) throw new NoSuchElementException();
		
		while(currentNode != null && !found) {
			if (currentNode.getElement().equals(target)) {
				found = true;
			} else {
				previousNode = currentNode;
				currentNode = currentNode.getNext();
			}
		}
		
		if(!found) throw new NoSuchElementException();
		
		Node<T> newNode = new Node<T>(element);
		currentNode.setNext(newNode);
		newNode.setNext(currentNode.getNext());
		
		size++;
		modCount++;
		
	}

	@Override
	public void add(int index, T element) {
		
		Node<T> currentNode = head;
		Node<T> previousNode = null;
		int currentIndex = 0;
		
		boolean found = false;
		
		if(index < 0 || index > size) throw new IndexOutOfBoundsException();
		
		while(currentNode != null && !found) {
			if(currentIndex >= index) {
				found = true;
			}
			else {
				previousNode = currentNode;
				currentNode = currentNode.getNext();
				currentIndex++;
			}
		}
		

		Node<T> newNode = new Node<T>(element);
		
		if(isEmpty()) head = newNode;
		
		else {
			
			if(previousNode != null) previousNode.setNext(newNode);
			newNode.setNext(currentNode);
			if(newNode.getNext() == null) tail = newNode;
			
		}
		
		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if(isEmpty()) throw new NoSuchElementException();
		return remove(0);
	}

	@Override
	public T removeLast() {
		if(isEmpty()) throw new NoSuchElementException();
		return remove(size-1);
	}

	@Override
	public T remove(T element) {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		boolean found = false;
		Node<T> previousNode = null;
		Node<T> currentNode = head;
		
		while (currentNode != null && !found) {
			if (element.equals(currentNode.getElement())) {
				found = true;
			} else {
				previousNode = currentNode;
				currentNode = currentNode.getNext();
			}
		}
		
		if (!found) {
			throw new NoSuchElementException();
		}
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (currentNode == head) { //first node
			head = currentNode.getNext();
		} else if (currentNode == tail) { //last node
			tail = previousNode;
			tail.setNext(null);
		} else { //somewhere in the middle
			previousNode.setNext(currentNode.getNext());
		}
		
		size--;
		modCount++;
		
		return currentNode.getElement();
	}

	@Override
	public T remove(int index) {
		T returnVal;
		
		Node<T> currentNode = head;
		Node<T> previousNode = null;
		int currentIndex = 0;
		
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		while(currentNode != null && currentIndex < index) {
			previousNode = currentNode;
			currentNode = currentNode.getNext();
			currentIndex++;
		}
		
		if(currentNode == null) throw new NoSuchElementException();
		
		returnVal = currentNode.getElement();
		
		if (size() == 1) { //only node
			head = tail = null;
		} else if (currentNode == head) { //first node
			head = currentNode.getNext();
		} else if (currentNode == tail) { //last node
			tail = previousNode;
			tail.setNext(null);
		} else { //somewhere in the middle
			previousNode.setNext(currentNode.getNext());
		}
		
		size--;
		modCount++;
		return returnVal;
	}

	@Override
	public void set(int index, T element) {
		Node<T> currentNode = head;
		int currentIndex = 0;
		
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		while(currentNode != null && currentIndex < index) {
			currentNode = currentNode.getNext();
			currentIndex++;
		}
		
		currentNode.setElement(element);
		modCount++;
	}

	@Override
	public T get(int index) {
		Node<T> currentNode = head;
		int currentIndex = 0;
		
		if(index < 0 || index >= size) throw new IndexOutOfBoundsException();
		
		while(currentNode != null && currentIndex < index) {
			currentNode = currentNode.getNext();
			currentIndex++;
		}
		
		return currentNode.getElement();
	}

	@Override
	public int indexOf(T element) {
		int index = 0;
		
		if(isEmpty()) return -1;
		
		Node<T> currentNode = head;
		while (currentNode != null && !currentNode.getElement().equals(element)) {
			currentNode = currentNode.getNext();
			index++;
		}
		
		if (currentNode == null) return -1;
		
		return index;
	}

	@Override
	public T first() {
		if(isEmpty()) throw new NoSuchElementException();
		return get(0);
	}

	@Override
	public T last() {
		if(isEmpty()) throw new NoSuchElementException();
		return get(size-1);
	}

	@Override
	public boolean contains(T target) {
		Node<T> currentNode = head;
		
		while(currentNode != null) {
			if(currentNode.getElement().equals(target)) return true;
			else {
				currentNode = currentNode.getNext();
			}
		}
		
		return false;
	}

	@Override
	public boolean isEmpty() { 
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public String toString() {
		Node<T> currentNode = head;
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (int i = 0; i < size; i++) {
			str.append(currentNode.getElement());
			str.append(", ");
			currentNode = currentNode.getNext();
		}
		if (size > 0) {
			str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}
	
	@Override
	public Iterator<T> iterator() {
		return new SLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUSingleLinkedList */
	private class SLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		private boolean nextCalled;
		private Node<T> currentNode;
		private Node<T> previousNode;
		
		/** Creates a new iterator for the list */
		public SLLIterator() {
			nextNode = head;
			currentNode = null;
			previousNode = null;
			nextCalled = false;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			return nextNode != null;
		}

		@Override
		public T next() {
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(!hasNext()) throw new NoSuchElementException();
			
			nextCalled = true;
			
			previousNode = currentNode;
			currentNode = nextNode;
			nextNode = nextNode.getNext();

			return currentNode.getElement();
		}
		
		@Override
		public void remove() {
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(!nextCalled) throw new IllegalStateException();
			
			currentNode = previousNode;
			previousNode = null;
			currentNode.setNext(nextNode);
			
			nextCalled = false;
			iterModCount++;
			modCount++;
			
		}
	}
}
