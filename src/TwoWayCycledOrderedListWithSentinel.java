
import java.util.*;

public class TwoWayCycledOrderedListWithSentinel<E extends Comparable<? super E>> implements IList<E> {

	private class Element {
		E object;
		Element next = null;
		Element prev = null;

		public Element(E e) {
			this.object = e;
		}

		public Element(E e, Element next, Element prev) {
			this.object = e;
			this.next = next;
			this.prev = prev;
		}

		// add element e after this
		public void addAfter(Element elem) {
			elem.setNext(this.next);
			elem.setPrev(this);
			this.next.prev = elem;
			this.next = elem;
		}

		public void addBefore(Element elem) {
			elem.prev = this.prev;
			elem.next = this;
			this.prev.next = elem;
			this.prev = elem;
		}

		// assert it is NOT a sentinel
		public void remove() {
			if (!this.equals(sentinel)) {
				this.next.prev = this.prev;
				this.prev.next = this.next;
			}
		}

		public E getObject() {
			return object;
		}

		public void setObject(E object) {
			this.object = object;
		}

		public Element getNext() {
			return next;
		}

		public void setNext(Element next) {
			this.next = next;
		}

		public Element getPrev() {
			return prev;
		}

		public void setPrev(Element prev) {
			this.prev = prev;
		}

		@Override
		public String toString() {
			return "Element{" +
					"object=" + object +
					'}';
		}
	}

	Element sentinel;
	int size;

	public TwoWayCycledOrderedListWithSentinel() {
		this.sentinel = new Element(null);
		this.size = 0;
	}

	private class InnerIterator implements Iterator<E> {
		Element actElem;
		int position = 0;

		public InnerIterator() {
			this.actElem = sentinel.getNext();
		}

		@Override
		public boolean hasNext() {
			return actElem != null && !actElem.equals(sentinel);
		}

		@Override
		public E next() {
			E actElemValue = actElem.getObject();
			actElem = actElem.getNext();
			position++;

			return actElemValue;
		}
	}

	private class InnerListIterator implements ListIterator<E> {
		Element pos;
		Element posPrev;
		int counter;

		public InnerListIterator() {
			this.pos = sentinel.next;
			this.posPrev = sentinel.prev;
			this.counter = 0;
		}

		@Override
		public boolean hasNext() {
			return pos != null && counter < size;
		}

		@Override
		public E next() {
			E next = pos.object;
			pos = pos.next;
			counter++;
			return next;
		}

		@Override
		public void add(E arg0) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean hasPrevious() {
			return posPrev != null && counter < size;
		}

		@Override
		public int nextIndex() {
			throw new UnsupportedOperationException();
		}

		@Override
		public E previous() {
			E previous = posPrev.object;
			posPrev = posPrev.prev;
			counter++;
			return previous;
		}

		@Override
		public int previousIndex() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void set(E arg0) {
			throw new UnsupportedOperationException();
		}
	}

	//    	@SuppressWarnings("unchecked")
	@Override
	public boolean add(E e) {
		Element newElement = new Element(e);

		if (size == 0) {
			sentinel.setNext(newElement);
			sentinel.setPrev(newElement);
			newElement.setPrev(sentinel);
			newElement.setNext(sentinel);

			size++;

			return true;
		}

		Element actElement = sentinel.getNext();
		while (actElement.getObject() != null && !actElement.equals(sentinel)) {
			if (e.compareTo(actElement.getObject()) < 0) {
				actElement.addBefore(newElement);
				size++;

				return true;
				// > or >= !!!
			} else if (e.compareTo(actElement.getObject()) >= 0 && actElement.getNext().getObject() != null) {
				if (e.compareTo(actElement.getNext().getObject()) < 0) {
					actElement.addAfter(newElement);
					size++;

					return true;
				}

			} else if (e.compareTo(actElement.getObject()) >= 0 && actElement.getNext().equals(sentinel)) {
				actElement.addAfter(newElement);
				size++;

				return true;
			}

			actElement = actElement.getNext();
		}

		return true;
	}

	private Element getElement(int index) {
		if (index < 0) throw new NoSuchElementException();

		Element actElem = sentinel.getNext();

		while (index > 0 && actElem != null) {
			index--;
			actElem = actElem.getNext();
		}

		if (actElem == null)
			throw new IndexOutOfBoundsException();

		return actElem;
	}

	public Element getElement(E obj) {
		Element actElem = sentinel.getNext();
		int counter = 0;

		while (actElem.getObject() != null) {
			if (actElem.getObject().equals(obj)) {
				return actElem;
			}

			counter++;
			actElem = actElem.getNext();
		}

		return null;
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		this.size = 0;
	}

	@Override
	public boolean contains(E element) {
		return getElement(element) != null;
	}

	@Override
	public E get(int index) {
		if (index < 0 || index >= size) throw new NoSuchElementException();

		return getElement(index).getObject();
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(E element) {
		Element actElement = sentinel.getNext();
		int actIndex = 0;

		while (actElement != null) {
			if (element == null ? actElement.getObject() == null : element.equals(actElement.getObject())) {
				return actIndex;
			}

			actIndex++;
			actElement = actElement.getNext();
		}

		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<E> iterator() {
		return new InnerIterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return new InnerListIterator();
	}

	@Override
	public E remove(int index) {
		if (index < 0 || index >= size) throw new NoSuchElementException();

		Element removedElement = getElement(index);

		if (size == 1) {
			this.clear();

			return removedElement.getObject();
		}

		removedElement.remove();
		size--;

		return removedElement.getObject();
	}

	@Override
	public boolean remove(E e) {
		if (indexOf(e) < 0) {
			return false;
		}

		return remove(indexOf(e)) != null;
	}

	@Override
	public int size() {
		return size;
	}

	//@SuppressWarnings("unchecked")
	public void add(TwoWayCycledOrderedListWithSentinel<E> other) {
		if (this.equals(other) || other.isEmpty()) {
			return;
		}

		if (this.isEmpty()) {
			this.sentinel = other.sentinel;
			this.size = other.size;
			other.clear();

			return;
		}

		Element actElement = sentinel.getNext();
		Element actElementOther = other.sentinel.getNext();
		while (actElement != sentinel && actElementOther != sentinel) {
			if (actElement.getObject().compareTo(actElementOther.getObject()) > 0) {
				actElementOther = actElementOther.getNext();
				actElement.addBefore(actElementOther.prev);

				size++;
			} else {
				actElement = actElement.getNext();
			}
		}

		if (actElementOther != other.sentinel) {
			while (actElementOther.getObject() != null) {
				actElementOther = actElementOther.getNext();
				actElement.addBefore(actElementOther.prev);

				size++;
			}
		}

		other.clear();
	}

	//@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeAll(E e) {
		Element actElement = getElement(e);


		while (actElement.getObject().equals(e)) {
			remove(e);

			actElement = getElement(e);

			if (actElement == null) {
				return;
			}
		}
	}

	@Override
	public String toString() {
		if (isEmpty()) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		result.append("\n");

		int counter = 1;

		for (E e : this) {
			if (counter % 10 != 0) {
				if (counter == size) {
					result.append(e);
					break;
				}

				result.append(e).append(" ");

			} else {
				result.append(e).append("\n");
			}

			counter++;
		}

		return result.toString();
	}

	public String toStringReverse() {
		if (isEmpty()) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		int counter = 1;
		Element lastElement = sentinel.getPrev();

		result.append("\n");

		while (lastElement.getObject() != null) {
			if (counter % 10 != 0) {
				if (counter == size) {
					result.append(lastElement.getObject());
					break;
				}

				result.append(lastElement.getObject()).append(" ");

			} else {
				result.append(lastElement.getObject()).append("\n");

			}

			lastElement = lastElement.getPrev();
			counter++;
		}

		return result.toString();
	}

	public void remDup() {
		if (this.size == 0 || this.size == 1) {
			return;
		}

		Element actElement = sentinel.getNext();

		while (actElement.getObject() != null) {
			if (actElement.getObject().equals(actElement.getNext().getObject())) {
				actElement.setNext(actElement.getNext().getNext());
				actElement.getNext().setPrev(actElement);

				size--;
			} else {
				actElement = actElement.getNext();
			}
		}
	}
}