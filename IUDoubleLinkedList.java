import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	public IUDoubleLinkedList() {
		head = tail = null;
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
		boolean found = false;
		
		if(isEmpty()) throw new NoSuchElementException();
		
		while(currentNode != null && !found) {
			if (currentNode.getElement().equals(target)) {
				found = true;
			} else {
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
			if(currentIndex == index) {
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
			if(currentIndex == 0) head = newNode;
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
		// TODO Auto-generated method stub
		return new DLLIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		return new DLLListIterator();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLListIterator(startingIndex);
	}
	
	/** Iterator for IUSingleLinkedList */
	private class DLLIterator implements Iterator<T> {
		private Node<T> nextNode;
		private int iterModCount;
		private boolean nextCalled;
		private Node<T> currentNode;
		
		/** Creates a new iterator for the list */
		public DLLIterator() {
			nextNode = head;
			currentNode = null;
			nextCalled = false;
			iterModCount = modCount;
		}

		@Override
		public boolean hasNext() {
			if(modCount != iterModCount) throw new ConcurrentModificationException();

			if(nextNode == null) {
				return false;
			}
			
			return true;
		}

		@Override
		public T next() {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(nextNode == null) throw new NoSuchElementException();
			
			nextCalled = true;
			currentNode = nextNode;
			nextNode = nextNode.getNext();

			return currentNode.getElement();
		}
		
		@Override
		public void remove() {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(nextCalled == false) throw new IllegalStateException();

			IUDoubleLinkedList.this.remove(currentNode.getElement());

			iterModCount++;
			nextCalled = false;
			
		}
	}
	
	private class DLLListIterator implements ListIterator<T> {
		private Node<T> nextNode;
		private Node<T> previousNode;
		private int index;
		private int iterModCount;
		private boolean nextCalled;
		
		/** Creates a new iterator for the list */
		public DLLListIterator() {
			nextNode = head;
			previousNode = null;
			nextCalled = false;
			iterModCount = modCount;
			index = 0;
		}

		public DLLListIterator(int startingIndex) {
			this();
			index = startingIndex;
		}
		
		@Override
		public boolean hasNext() {
			if(isEmpty()) return false;
			T nextElement = get(index + 1);
			if(nextElement != null) return true;
			return false;
		}

		@Override
		public T next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean hasPrevious() {
			if(isEmpty()) return false;
			T previousElement = get(index);
			if(previousElement != null) return true;
			return false;
		}

		@Override
		public T previous() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int nextIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int previousIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void set(T e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void add(T e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
