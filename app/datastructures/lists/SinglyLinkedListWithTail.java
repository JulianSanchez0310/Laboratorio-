package app.datastructures.lists;

import app.datastructures.nodes.Node;

/**
 * Singly linked list with tail pointer.
 * pushFront O(1) | pushBack O(1) | popFront O(1) | popBack O(n)
 * find O(n) | erase O(n) | addAfter O(n) | addBefore O(n)
 */
public class SinglyLinkedListWithTail implements IList {

    private Node head;
    private Node tail;

    @Override
    public void pushFront(double value) {
        Node newNode = new Node(value);
        if (head == null) { head = tail = newNode; return; }
        newNode.next = head;
        head = newNode;
    }

    @Override
    public void pushBack(double value) {
        Node newNode = new Node(value);
        if (tail == null) { head = tail = newNode; return; }
        tail.next = newNode;
        tail = newNode;
    }

    @Override
    public void popFront() {
        if (head == null) return;
        if (head == tail) { head = tail = null; return; }
        head = head.next;
    }

    @Override
    public void popBack() {
        if (head == null) return;
        if (head == tail) { head = tail = null; return; }
        Node current = head;
        while (current.next != tail) current = current.next;
        current.next = null;
        tail = current;
    }

    @Override
    public Node find(double value) {
        Node current = head;
        while (current != null) {
            if (current.value == value) return current;
            current = current.next;
        }
        return null;
    }

    @Override
    public void erase(double value) {
        if (head == null) return;
        if (head.value == value) {
            head = head.next;
            if (head == null) tail = null;
            return;
        }
        Node current = head;
        while (current.next != null && current.next.value != value) {
            current = current.next;
        }
        if (current.next != null) {
            if (current.next == tail) tail = current;
            current.next = current.next.next;
        }
    }

    @Override
    public void addAfter(double target, double value) {
        Node targetNode = find(target);
        if (targetNode == null) return;
        Node newNode = new Node(value);
        newNode.next = targetNode.next;
        targetNode.next = newNode;
        if (targetNode == tail) tail = newNode;
    }

    @Override
    public void addBefore(double target, double value) {
        if (head == null) return;
        if (head.value == target) { pushFront(value); return; }
        Node current = head;
        while (current.next != null && current.next.value != target) {
            current = current.next;
        }
        if (current.next != null) {
            Node newNode = new Node(value);
            newNode.next = current.next;
            current.next = newNode;
        }
    }
}
