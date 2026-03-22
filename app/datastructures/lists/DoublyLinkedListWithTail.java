package app.datastructures.lists;

import app.datastructures.nodes.Node;

/**
 * Doubly linked list with tail pointer.
 * pushFront O(1) | pushBack O(1) | popFront O(1) | popBack O(1)
 * find O(n) | erase O(n) | addAfter O(n) | addBefore O(n)
 */
public class DoublyLinkedListWithTail implements IList {

    private Node head;
    private Node tail;

    @Override
    public void pushFront(double value) {
        Node newNode = new Node(value);
        if (head == null) { head = tail = newNode; return; }
        newNode.next = head;
        head.prev = newNode;
        head = newNode;
    }

    @Override
    public void pushBack(double value) {
        Node newNode = new Node(value);
        if (tail == null) { head = tail = newNode; return; }
        newNode.prev = tail;
        tail.next = newNode;
        tail = newNode;
    }

    @Override
    public void popFront() {
        if (head == null) return;
        if (head == tail) { head = tail = null; return; }
        head = head.next;
        head.prev = null;
    }

    @Override
    public void popBack() {
        if (tail == null) return;
        if (head == tail) { head = tail = null; return; }
        tail = tail.prev;
        tail.next = null;
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
        if (node == head) { popFront(); return; }
        if (node == tail) { popBack();  return; }
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    @Override
    public void addAfter(double target, double value) {
        Node targetNode = find(target);
        if (targetNode == null) return;
        if (targetNode == tail) { pushBack(value); return; }
        Node newNode = new Node(value);
        newNode.next = targetNode.next;
        newNode.prev = targetNode;
        targetNode.next.prev = newNode;
        targetNode.next = newNode;
    }

    @Override
    public void addBefore(double target, double value) {
        Node targetNode = find(target);
        if (targetNode == null) return;
        if (targetNode == head) { pushFront(value); return; }
        Node newNode = new Node(value);
        newNode.prev = targetNode.prev;
        newNode.next = targetNode;
        targetNode.prev.next = newNode;
        targetNode.prev = newNode;
    }
}
