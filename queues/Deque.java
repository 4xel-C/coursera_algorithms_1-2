import java.util.Iterator;
import java.util.NoSuchElementException;

// Implementation of a deque using a linked list.
public class Deque<Item> implements Iterable<Item> {

    // Node for linked list
    private class Node {
        private Item data;
        private Node next;
        private Node previous; // Previous pointer to deleteLast in constant time

        Node(Item data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }
    }

    private Node head; // Keep track of the front Node
    private Node tail; // Keep track of the rear Node
    private int size; // size of the deque

    // Constructor method
    public Deque() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Check if the deque is empty
     *
     * @return a boolean
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Getter for size
     *
     * @return the size
     */
    public int size() {
        return size;
    }

    public void addFirst(Item item) {

        if (item == null) {
            throw new IllegalArgumentException("You tried to add a null value ot the deque!");
        }

        // Create the new node of the linked list
        Node node = new Node(item);

        // if first node:
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.previous = node;
            head = node;
        }
        size++;
    }

    public void addLast(Item item) {

        if (item == null) {
            throw new IllegalArgumentException("You tried to add a null value ot the deque!");
        }

        // Create the new node of the linked list
        Node node = new Node(item);

        // if first node:
        if (isEmpty()) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            node.previous = tail;
            tail = node;
        }
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty!");
        } else if (size == 1) {
            Item data = head.data;
            head = null;
            tail = null;
            size--;
            return data;
        } else {
            Item data = head.data;
            head = head.next;
            head.previous = null;
            size--;
            return data;
        }
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The deque is empty!");
        } else if (size == 1) {
            Item data = tail.data;
            head = null;
            tail = null;
            size--;
            return data;
        } else {
            Item data = tail.data;
            tail = tail.previous;
            tail.next = null;
            size--;
            return data;
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        // Start on the first node
        private Node current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elemnent to iterate on");
            }

            Item item = current.data;
            current = current.next; // Advance to the next node for the next iteration;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "No possibility to remove from the iterator. Use the Deque method.");
        }

    }

    /**
     * Unit testing
     *
     * @param args
     */
    public static void main(String[] args) {
        // Unit testing

        Deque<Integer> myDeque = new Deque<>();

        System.out.println("Empty? " + myDeque.isEmpty());

        myDeque.addFirst(1);
        myDeque.addFirst(2);
        myDeque.addLast(3);
        myDeque.addLast(4);

        System.out.println("Empty? : " + myDeque.isEmpty());

        for (int data : myDeque) {
            System.out.println(data);
        }

        myDeque.removeFirst();
        myDeque.removeLast();

        System.out.println();

        for (int data : myDeque) {
            System.out.println(data);
        }

        System.out.println("Size? : " + myDeque.size());
    }
}
