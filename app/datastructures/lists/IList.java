package app.datastructures.lists;

import app.datastructures.nodes.Node;

public interface IList {
    void pushFront(double value);
    void pushBack(double value);
    void popFront();
    void popBack();
    Node find(double value);
    void erase(double value);
    void addAfter(double target, double value);
    void addBefore(double target, double value);
}
