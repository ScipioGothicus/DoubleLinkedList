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
		if(!isEmpty()) head.setPrevious(newNode);
		head = newNode;
		
		if(isEmpty()) tail = newNode;
		
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
		newNode.setNext(currentNode.getNext());
		newNode.setPrevious(currentNode);
		currentNode.setNext(newNode);
		
		size++;
		modCount++;
		
	}

	@Override
	public void add(int index, T element) {
		
		Node<T> currentNode = head;
		int currentIndex = 0;
		
		boolean found = false;
		
		if(index < 0 || index > size) throw new IndexOutOfBoundsException();
		
		while(currentNode != null && !found) {
			if(currentIndex == index) {
				found = true;
			}
			else {
				currentNode = currentNode.getNext();
				currentIndex++;
			}
		}
		

		Node<T> newNode = new Node<T>(element);
		
		if(isEmpty()) {
			head = newNode; 
			tail = newNode;
		}
		
		else if(index == 0) {
			newNode.setNext(head);
			head.setPrevious(newNode);
			head = newNode;
		}
		
		else if(index == size) {
			newNode.setPrevious(tail);
			tail.setNext(newNode);
			tail = newNode;
		}
		
		else {
			newNode.setNext(currentNode.getNext());
			currentNode.setNext(newNode);
			newNode.setPrevious(currentNode);
			if(newNode.getNext() != null) {
				newNode.getNext().setPrevious(newNode); // cheatning??
			}
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
			tail.setPrevious(previousNode.getPrevious());
			tail = previousNode;
			tail.setNext(null);
		} else { //somewhere in the middle
			previousNode.setNext(currentNode.getNext());
			currentNode.getNext().setPrevious(previousNode);
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
			tail.setPrevious(previousNode.getPrevious());
			tail = previousNode;
			tail.setNext(null);
		} else { //somewhere in the middle
			previousNode.setNext(currentNode.getNext());
			currentNode.getNext().setPrevious(previousNode);
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
		return head.getElement();
	}

	@Override
	public T last() {
		if(isEmpty()) throw new NoSuchElementException();
		return tail.getElement();
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
		return new DLLListIterator();
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
		private Node<T> previousNode;
		
		/** Creates a new iterator for the list */
		public DLLIterator() {
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
			currentNode.setNext(nextNode);
			previousNode = null;
			
			size--;
			
			nextCalled = false;
			iterModCount++;
			modCount++;
			
		}
	}
	
	private class DLLListIterator implements ListIterator<T> {
		private Node<T> nextNode;
		private Node<T> previousNode;
		private int index;
		private int iterModCount;
		private boolean nextCalled;
		private boolean previousCalled;
		
		/** Creates a new iterator for the list */
		public DLLListIterator() {
			nextNode = head;
			previousNode = null;
			nextCalled = false;
			previousCalled = false;
			iterModCount = modCount;
			index = 0;
		}

		public DLLListIterator(int startingIndex) {
			this();
			index = startingIndex;
		}
		
		@Override
		public boolean hasNext() {
			return nextNode != null;
		}

		@Override
		public T next() {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(isEmpty()) throw new NoSuchElementException();
			
			if(size() == 1) {
				index++;
				nextNode = head;
			}
			else if(nextNode.getNext() != null) {
				index++;
				previousNode = nextNode;
				nextNode = nextNode.getNext();
			}
			else {
				throw new NoSuchElementException();
			}
			
			nextCalled = true;
			
			return nextNode.getElement();
		}

		@Override
		public boolean hasPrevious() {
			if(previousNode != null) return true;
			return false;
		}

		@Override
		public T previous() {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			
			if(size() == 1) {
				index--;
				previousNode = head;
			}
			else if(hasPrevious()) {
				index--;
				nextNode = previousNode;
				if(previousNode.getPrevious() != null) previousNode = previousNode.getPrevious();
				else previousNode = null;
			}
			else {
				throw new NoSuchElementException();
			}
			
			previousCalled = true;
			return previousNode.getElement();
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Override
		public void remove() {
			if (modCount != iterModCount) {
				throw new ConcurrentModificationException();
			}
			if (!nextCalled && !previousCalled) {
				throw new IllegalStateException();
			}
			if (nextCalled) {
				if(previousNode != null) previousNode = previousNode.getPrevious();
				if (nextNode != null) {
					nextNode.setPrevious(previousNode);
				}
				else { // tail is getting removed
					tail = tail.getPrevious();
				}
				if (previousNode != null) {
					previousNode.setNext(nextNode);
				}
				else { // head is getting removed
					head = head.getNext();
				}
			}
			else { // previousCalled
				if(nextNode != null) nextNode = nextNode.getPrevious();
				if (nextNode != null) {
					nextNode.setPrevious(previousNode);
				}
				else { // tail is getting removed
					tail = tail.getPrevious();
				}
				if (previousNode != null) {
					previousNode.setNext(nextNode);
				}
				else { // head is getting removed
					head = head.getNext();
				}
				
			}
			nextCalled = false;
			previousCalled = false;
			iterModCount++;
			modCount++;
			size--;
		}

		@Override
		public void set(T e) {
			
			if(nextCalled || previousCalled) {
				nextNode.setElement(e);
			}
			else {
				throw new IllegalStateException();
			}
			
			iterModCount++;
			modCount++;
		}

		@Override
		public void add(T e) {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			
			Node<T> newNode = new Node<T>(e);
			
			if(isEmpty()) {
				head = newNode; 
				tail = newNode;
			}
			
			else if(index == 0) {
				newNode.setNext(head);
				head.setPrevious(newNode);
				head = newNode;
			}
			
			else if(index == size) {
				tail.setNext(newNode);
				newNode.setPrevious(tail);
				tail = newNode;
			}
			
			else {
				newNode.setNext(nextNode);
				if(previousNode != null) {
					previousNode.setNext(newNode);
					newNode.setPrevious(previousNode);
				}
				if(newNode.getNext() != null) {
					newNode.getNext().setPrevious(newNode); // cheatning??
				}
			}
			
			iterModCount++;
			modCount++;
		}
		
	}

}