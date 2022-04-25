import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IUArrayList<T> implements IndexedUnsortedList<T> {

	private T[] array;
	private int rear; // reer lol
	private int modCount;
	
	private static final int NOT_FOUND = -1;
	private static final int DEFAULT_CAPACITY = 10;
	

	/**
	 * Default constructor that creates a generic-type array with the default capacity of 10. 
	 */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/**
	 * Constructor that takes the desired capacity as the single argument and sets the array size to the capacity.
	 * @param initialCapacity - the desired capacity of the array
	 */
	
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		this.array = (T[]) new Object[initialCapacity];
		this.rear = 0;
		modCount = 0;
	}

	private void expandArrayIfNecessary() {
		if (rear >= array.length) {
			array = Arrays.copyOf(array, array.length * 2);
		}
	}
	
	@Override
	public void addToFront(T element) {
		array = Arrays.copyOf(array, array.length + 1);
		for(int i = 0; i < rear; i++) {
			array[i + 1] = array[i];
		}
		array[0] = element;
		rear++;
	}

	@Override
	public void addToRear(T element) {
		add(element);
	}

	@Override
	public void add(T element) {
		array[rear] = element;
		modCount++;
		rear++;
	}

	@Override
	public void addAfter(T element, T target) {
		int targetIndex = indexOf(target);
		
		if(targetIndex < 0) {
			throw new NoSuchElementException();
		}

		expandArrayIfNecessary();
		
		for(int i = targetIndex; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}
		
		array[targetIndex + 1] = element;
		
		modCount++;
		
		rear++;
		
	}

	@Override
	public void add(int index, T element) {
		if(index < 0 || index > rear) {
			throw new IndexOutOfBoundsException();
		}
	
		for(int i = index; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}
		
		array[index + 1] = element;
		
		modCount++;
		
		rear++;
	}

	@Override
	public T removeFirst() {
		
		if(rear == 0) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[0];

		array[0] = null;
		for(int i = 0; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}
		
		modCount++;
		rear--;
		
		return retVal;
	}

	@Override
	public T removeLast() {
		
		if(rear == 0) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[rear - 1];

		array[rear - 1] = null;
		modCount++;
		rear--;
		
		return retVal;
	}

	@Override
	public T remove(T element) {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[index];
		
		rear--;
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}
		
		T returnVal = array[index];

		for(int i = index; i < rear - 1; i++) {
			array[i] = array[i + 1];
		}
		
		array[rear - 1] = null;
		modCount++;
		rear--;
		

		return returnVal;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= rear) {
			throw new IndexOutOfBoundsException();
		}
		
		array[index] = element;
		modCount++;
	}

	@Override
	public T get(int index) {
		if(index < 0 || index >= rear) throw new IndexOutOfBoundsException();
		return array[index];
	}

	@Override
	public int indexOf(T element) {

		int index = -1;
		
		for(int i = 0; i < rear && index == -1; i++) {
			if(array[i].equals(element)) {
				index = i;
			}
		}
		
		return index;
	}

	@Override
	public T first() {
		if(rear == 0) throw new NoSuchElementException();
		return array[0];
	}

	@Override
	public T last() {
		if(rear == 0) throw new NoSuchElementException();
		return array[rear - 1];
	}

	@Override
	public boolean contains(T target) {
		return (indexOf(target) != NOT_FOUND); 
	}

	@Override
	public boolean isEmpty() {
		return rear == 0;
	}

	@Override
	public int size() {
		return rear;
	}
	
	
	@Override
	public String toString() {
		// shamelessly copied from SimpleSet
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (int i = 0; i < rear; i++) {
			str.append(array[i]);
			str.append(", ");
		}
		if (rear > 0) {
			str.delete(str.length()-2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	private class ArrayListIterator implements Iterator<T> {

		private int nextIndex;
		private int iterModCount;
		private boolean nextCalled;
		
		
		public ArrayListIterator() {
			nextIndex = 0;
			nextCalled = false;
			iterModCount = modCount;
		}
		
		@Override
		public boolean hasNext() {
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			
			if(array[nextIndex] == null) {
				return false;
			}
			return true;
		}

		@Override
		public T next() {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(nextIndex >= rear) throw new NoSuchElementException();
			
			nextCalled = true;
			nextIndex++;
			return array[nextIndex - 1];
		}

		@Override
		public void remove() {
			
			if(modCount != iterModCount) throw new ConcurrentModificationException();
			if(nextCalled == false) throw new IllegalStateException();
			
			IUArrayList.this.remove(array[nextIndex-1]);
			iterModCount++;
			nextCalled = false;
		}
	}
	
	@Override
	public Iterator<T> iterator() {
		return new ArrayListIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}


	
}