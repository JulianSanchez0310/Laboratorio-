package app.datastructures.lists;

import app.datastructures.nodes.Node;

/**
 * Doubly linked list without tail pointer.
 * pushFront O(1) | pushBack O(n) | popFront O(1) | popBack O(n)
 * find O(n) | erase O(n) | addAfter O(n) | addBefore O(n)
 */
public class DoublyLinkedList implements IList {

    private Node head;

    @Override
    public void pushFront(double value) {
        Node newNode = new Node(value);
        if (head != null) { newNode.next = head; head.prev = newNode; }
        head = newNode;
    }

    @Override
    public void pushBack(double value) {
        Node newNode = new Node(value);
        if (head == null) { head = newNode; return; }
        Node current = head;
        while (current.next != null) current = current.next;
        current.next = newNode;
        newNode.prev = current;
    }

    @Override
    public void popFront() {
        if (head == null) return;
        if (head.next == null) { head = null; return; }
        head = head.next;
        head.prev = null;
    }

    @Override
    public void popBack() {
        if (head == null) return;
        if (head.next == null) { head = null; return; }
        Node current = head;
        while (current.next != null) current = current.next;
        current.prev.next = null;
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
        Node node = find(value);
        if (node == null) return;
        if (node == head) {
            head = head.next;
            if (head != null) head.prev = null;
        } else {
            node.prev.next = node.next;
            if (node.next != null) node.next.prev = node.prev;
        }
    }

    @Override
    public void addAfter(double target, double value) {
        Node targetNode = find(target);
        if (targetNode == null) return;
        Node newNode = new Node(value);
        newNode.next = targetNode.next;
        newNode.prev = targetNode;
        if (targetNode.next != null) targetNode.next.prev = newNode;
        targetNode.next = newNode;
    }

    @Override
    public void addBefore(double target, double value) {
        Node targetNode = find(target);
        if (targetNode == null) return;
        if (targetNode == head) { pushFront(value); return; }
        Node newNode = new Node(value);
        newNode.next = targetNode;
        newNode.prev = targetNode.prev;
        targetNode.prev.next = newNode;
        targetNode.prev = newNode;
    }
}
