import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array;
    private int size;
    private int max = 8;

    public RandomizedQueue() {
        array = (Item[]) new Object[max]; // Using cast to create an array of a generic type.
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null element.");
        }

        // If array too small, double the size
        if (size == max) {
            Item[] newArray = (Item[]) new Object[max * 2];

            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
            max = max * 2;
        }

        array[size] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("The RandomizedQueue is empty!");
        }

        // If size < 25% of max, divide the size of the array by 2
        if (size < max * 0.25) {

            Item[] newArray = (Item[]) new Object[max / 2];

            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
            max = max / 2;
        }

        // Randomized an index
        int index = StdRandom.uniformInt(size);

        // Replace by the last element in the array
        Item item = array[index];
        array[index] = array[size - 1];
        array[size - 1] = null;

        size--;

        return item;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("The RandomizedQueue is empty!");
        }

        // Randomized an index
        int index = StdRandom.uniformInt(size);

        return array[index];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    // Inner class for the iterator
    private class RandomizedQueueIterator implements Iterator<Item> {

        private Item[] randomizedArray;
        private int currentIndex = 0;

        private RandomizedQueueIterator() {
            randomizedArray = (Item[]) new Object[size];

            // copy the array
            System.arraycopy(array, 0, randomizedArray, 0, size);

            // Shuffle the array using the Fisher-Yates method
            for (int i = size - 1; i >= 0; i--) {

                int randomIndex = StdRandom.uniformInt(i + 1);

                // Permute the elements in a random position
                Item temp = randomizedArray[i];
                randomizedArray[i] = randomizedArray[randomIndex];
                randomizedArray[randomIndex] = temp;
            }
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elemnent to iterate on");
            }

            Item item = randomizedArray[currentIndex];
            currentIndex++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException(
                    "No possibility to remove from the iterator. Use the RandomizedQueue's dequeue() method.");
        }

    }

    public static void main(String[] args) {

        RandomizedQueue<Integer> bag = new RandomizedQueue<>();

        bag.enqueue(1);
        bag.enqueue(2);
        bag.enqueue(3);
        bag.enqueue(4);

        for (int item : bag) {
            System.out.println(item);
        }

        System.out.println();

        for (int item : bag) {
            System.out.println(item);
        }
        System.out.println();

        bag.dequeue();

        for (int item : bag) {
            System.out.println(item);
        }
        System.out.println();

        System.out.println(bag.sample());

    }

}
