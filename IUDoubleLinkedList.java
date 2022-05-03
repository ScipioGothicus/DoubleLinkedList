import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * 
 * The custom implementation of a doubly-linked list complete with a ListIterator.
 * Implements IndexedUnsortedList and uses the Node class.
 * 
 * @author Preston Hardy
 *
 * @param <T>
 */

public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {

	private Node<T> head, tail;
	private int size;
	private int modCount;
	
	/**
	 * Constructs a doubly-linked list with a size of 0.
	 */
	public IUDoubleLinkedList() {
		head = tail = null;
		size = 0;
		modCount = 0;
	}
	

	@Override
	/**
	 * Adds a new Node to index 0.
	 * @param the element to be contained within the Node created.
	 */
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
	/**
	 * Adds a new Node to the end of the list (similar to a "push" for a stack).
	 * @param the element to be contained within the Node created.
	 */
	public void addToRear(T element) {
		Node<T> newNode = new Node<T>(element);
		
		if(isEmpty()) {
			head = newNode; 
			tail = newNode;
		}		
		else {
			newNode.setPrevious(tail);
			tail.setNext(newNode);
			tail = newNode;
		}
		
		size++;
		modCount++;

	}

	@Override
	/**
	 * Functions the same as addToRear ("pushes" elements like a stack).
	 * @param the element to be contained within the Node created.
	 */
	public void add(T element) {
		addToRear(element);
	}

	@Override
	/**
	 * Adds a new Node directly after a target Node.
	 * 
	 * @param the element to be contained with the Node 
	 * @param the target Node to add the new Node after.
	 * @throws NoSuchElementException if the target node could not be found.
	 */
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
	/**
	 * Adds a new Node at a specified index.
	 * 
	 * @param the index to add the new Node to
	 * @param the element to be contained with the Node
	 * @throws IndexOutOfBoundsException if the specified index is not within the list's constraints
	 */
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
	/**
	 * Removes the first Node (index 0) from the list.
	 * 
	 * @throws NoSuchElementException if the list is empty.
	 * @return the element removed.
	 */
	public T removeFirst() {
		if(isEmpty()) throw new NoSuchElementException();
		
		T returnVal = head.getElement();
		if(head.getNext() == null) head = null; 
		else {
			head = head.getNext();
			head.setPrevious(null);
		}
		
		size--;
		modCount++;
		
		return returnVal;
	}

	@Override
	/**
	 * Removes the last Node from the list
	 * 
	 * @throws NoSuchElementException if the list is empty.
	 * @return the element removed.
	 */
	public T removeLast() {
		if(isEmpty()) throw new NoSuchElementException();
		return remove(size-1);
	}

	@Override
	/**
	 * Searches through the list and removes the first matching element.
	 * 
	 * @param the Node to remove's element
	 * @throws NoSuchElementException if the element cannot be found.
	 * @return the element removed.
	 */
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
	/**
	 * Removes the Node at the specified index.
	 * 
	 * @param the index of the Node to be removed
	 * @throws IndexOutOfBoundsException if the specified index is not within the list's constraints
	 * @return the element removed.
	 */
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
	/**
	 * Sets a Node's element to a new specified element.
	 * 
	 * @param the index of the Node to be set
	 * @param the new element that the Node should be set to
	 * @throws IndexOutOfBoundsException if the specified index is not within the list's constraints
	 */
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
	/**
	 * Gets the element of a Node.
	 * 
	 * @param the index of the Node to be retrieved
	 * @throws IndexOutOfBoundsException if the specified index is not within the list's constraints
	 * @return the Node's element.
	 */
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
	/**
	 * Gets the index of the first element found using the specified element.
	 * 
	 * @param the element to find
	 * @return -1 if the element is not found, or returns the index of the first element found
	 */
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
	/**
	 * Gets the element of the first Node.
	 * 
	 * @throws NoSuchElementException if the list is empty.
	 * @return the element of the first Node.
	 */
	public T first() {
		if(isEmpty()) throw new NoSuchElementException();
		return head.getElement();
	}

	@Override
	/**
	 * Gets the element of the last Node.
	 * 
	 * @throws NoSuchElementException if the list is empty.
	 * @return the element of the last Node.
	 */
	public T last() {
		if(isEmpty()) throw new NoSuchElementException();
		return tail.getElement();
	}

	@Override
	/**
	 * Searches through a list to find if it contains a Node with the target element.
	 * 
	 * @param the target element.
	 * @return true if the list contains the target element, false otherwise.
	 */
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
	/**
	 * Checks if the list is empty.
	 * 
	 * @return true if the list is empty, false otherwise.
	 */
	public boolean isEmpty() { 
		return size == 0;
	}

	@Override
	/**
	 * Returns the size of the list.
	 * 
	 * @return the size of the list.
	 */
	public int size() {
		return size;
	}

	@Override
	/**
	 * Returns the list as a readable string, showing all Nodes within a list.
	 * 
	 * @return the list as a String
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		Iterator<T> it = iterator();
		while(it.hasNext()) {
			str.append(it.next().toString());
			str.append(", ");
		}
		if (size > 0) {
			str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}
	
	@Override
	/**
	 * Constructs a new iterator.
	 * 
	 * @return an Iterator.
	 */
	public Iterator<T> iterator() {
		return new DLLListIterator();
	}

	@Override
	/**
	 * Constructs a new ListIterator.
	 * 
	 * @return a ListIterator
	 */
	public ListIterator<T> listIterator() {
		return new DLLListIterator();
	}

	@Override
	/**
	 * Constructs a new ListIterator with the cursor index set by the parameter.
	 * 
	 * @param the index to start the cursor at
	 * @return a ListIterator with cursor set by the parameter
	 */
	public ListIterator<T> listIterator(int startingIndex) {
		return new DLLListIterator(startingIndex);
	}
	
	/**
	 *
	 * A custom doubly-linked ListIterator implementing the Java ListIterator. Uses the Node class.
	 * 
	 * @author Preston Hardy
	 *
	 */
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
		/**
		 * Creates a new ListIterator at the starting index.
		 * 
		 * @param startingIndex
		 */
		public DLLListIterator(int startingIndex) {
			if(startingIndex < 0 || startingIndex > size) throw new IndexOutOfBoundsException();
			
			nextNode = head;
			
			for(int i = 0; i < startingIndex; i++) {
				previousNode = nextNode;
				nextNode = nextNode.getNext();
				
			}
			nextCalled = false;
			previousCalled = false;
			iterModCount = modCount;
			index = startingIndex;

		}
		
		@Override
		public boolean hasNext() {
			if(iterModCount != modCount) throw new ConcurrentModificationException();
			return nextNode != null;
		}

		@Override
		public T next() {
			if(!hasNext()) throw new NoSuchElementException();
			
			T returnVal = nextNode.getElement();
			
			previousNode = nextNode;
			nextNode = nextNode.getNext();
			index++;
			nextCalled = true;
			previousCalled = false;
			return returnVal;
		}

		@Override
		public boolean hasPrevious() {
			if(iterModCount != modCount) throw new ConcurrentModificationException();
			return previousNode != null;
		}

		@Override
		public T previous() {
			if(!hasPrevious()) throw new NoSuchElementException();
			
			T returnVal = previousNode.getElement();
			
			nextNode = previousNode;
			previousNode = previousNode.getPrevious();
			previousCalled = true;
			nextCalled = false;
			index--;
			return returnVal;
		}

		@Override
		public int nextIndex() {
			if(iterModCount != modCount) throw new ConcurrentModificationException();
			return index;
		}

		@Override
		public int previousIndex() {
			if(iterModCount != modCount) throw new ConcurrentModificationException();
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
			if(nextCalled) {
				previousNode = previousNode.getPrevious();
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
			else {
				nextNode = nextNode.getNext();
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
			if(iterModCount != modCount) throw new ConcurrentModificationException();
			
			
			if(nextCalled) {
				previousNode.setElement(e);
			}
			else if(previousCalled) {
				nextNode.setElement(e);
			}
			else {
				throw new IllegalStateException();
			}
			nextCalled = false;
			previousCalled = false;
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
			nextCalled = false;
			previousCalled = false;
			size++;
			iterModCount++;
			modCount++;
		}
		
	}

}