package edu.sharif.ce.mir.dal.data.impl;

/**
 * Adopted from: http://www.brilliantsheep.com/array-based-min-heap-java-implementation/
 * with changes to also save ids
 */
public class MinHeap {

    private double[] array;
    private Object[] ids;
    private int n = 0;

    public MinHeap(final int size) {
        array = new double[size];
        ids = new Object[size];
    }

    public double min() {
        if (isEmpty()) {
            return -1;
        }

        return array[0];
    }
    
    public Object minId() {
        if (isEmpty()) {
            return null;
        }

        return ids[0];
    }

    public double removeMin() {
        if (isEmpty()) {
            throw new RuntimeException("Heap is empty!");
        }

        final double min = array[0];
        array[0] = array[n - 1];
        ids[0] = ids[n-1];
        if (--n > 0) siftDown(0);
        return min;
    }

    /**
     * Checks if the heap is empty.
     */
    public boolean isEmpty() {
        return n == 0;
    }
    
    public int getLen(){
        return n;
    }

    /**
     * Adds a new element to the heap and sifts up/down accordingly.
     */
    public void add(final double value, Object song) {
        if (n == array.length) {
            throw new RuntimeException("Heap is full!");
        }

        array[n] = value;
        ids[n] = song;
        siftUp(n);
        n++;
    }

    /**
     * Sift up to make sure the heap property is not broken.
     * This method is used when a new element is added to the heap
     * and we need to make sure that it is at the right spot.
     */
    private void siftUp(final int index) {
        if (index > 0) {
            final int parent = (index - 1) / 2;
            if (array[parent] > array[index]) {
                swap(parent, index);
                siftUp(parent);
            }
        }
    }

    /**
     * Sift down to make sure that the heap property is not broken
     * This method is used when removing the min element, and we need
     * to make sure that the replacing element is at the right spot.
     */
    private void siftDown(int index) {

        final int leftChild = 2 * index + 1;
        final int rightChild = 2 * index + 2;

        // Check if the children are outside the array bounds.
        if (rightChild >= n && leftChild >= n) return;

        // Determine the smallest child out of the left and right children.
        final int smallestChild =
                array[rightChild] > array[leftChild] ? leftChild : rightChild;

        if (array[index] > array[smallestChild]) {
            swap(smallestChild, index);
            siftDown(smallestChild);
        }
    }

    /**
     * Helper method for swapping array elements
     */
    private void swap(int a, int b) {
        double temp = array[a];
        array[a] = array[b];
        array[b] = temp;
        Object temp2 = ids[a];
        ids[a] = ids[b];
        ids[b] = temp2;
    }
}
