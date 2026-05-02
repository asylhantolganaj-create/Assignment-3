import java.util.*;

public class BST<K extends Comparable<K>, V> {

    private class Node {
        private K key;
        private V val;
        private Node left, right;

        public Node(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    // Helper class for iteration (both key & value)
    public class Pair {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() { return key; }
        public V getValue() { return value; }
    }

    private Node root;
    private int size = 0;

    public void put(K key, V val) {
        if (root == null) {
            root = new Node(key, val);
            size++;
            return;
        }

        Node current = root;
        Node parent = null;

        while (current != null) {
            parent = current;
            int cmp = key.compareTo(current.key);

            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                current.val = val;
                return;
            }
        }

        if (key.compareTo(parent.key) < 0) {
            parent.left = new Node(key, val);
        } else {
            parent.right = new Node(key, val);
        }
        size++;
    }

    public V get(K key) {
        Node current = root;

        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return current.val;
            }
        }
        return null;
    }

    public void delete(K key) {
        Node current = root;
        Node parent = null;

        // Find node
        while (current != null) {
            int cmp = key.compareTo(current.key);
            if (cmp < 0) {
                parent = current;
                current = current.left;
            } else if (cmp > 0) {
                parent = current;
                current = current.right;
            } else {
                break;
            }
        }

        if (current == null) return; // not found

        // Case 1: no children
        if (current.left == null && current.right == null) {
            if (parent == null) {
                root = null;
            } else if (parent.left == current) {
                parent.left = null;
            } else {
                parent.right = null;
            }
            size--;
            return;
        }

        // Case 2: one child
        if (current.left == null) {
            if (parent == null) {
                root = current.right;
            } else if (parent.left == current) {
                parent.left = current.right;
            } else {
                parent.right = current.right;
            }
            size--;
            return;
        }

        if (current.right == null) {
            if (parent == null) {
                root = current.left;
            } else if (parent.left == current) {
                parent.left = current.left;
            } else {
                parent.right = current.left;
            }
            size--;
            return;
        }

        // Case 3: two children - find inorder successor
        Node successorParent = current;
        Node successor = current.right;
        while (successor.left != null) {
            successorParent = successor;
            successor = successor.left;
        }

        current.key = successor.key;
        current.val = successor.val;

        if (successorParent.left == successor) {
            successorParent.left = successor.right;
        } else {
            successorParent.right = successor.right;
        }
        size--;
    }

    public int size() {
        return size;
    }

    public Iterable<Pair> iterator() {
        return new Iterable<Pair>() {
            public Iterator<Pair> iterator() {
                return new BSTIterator();
            }
        };
    }

    // In-order traversal without recursion (using stack)
    private class BSTIterator implements Iterator<Pair> {
        private Stack<Node> stack = new Stack<>();

        public BSTIterator() {
            pushAllLeft(root);
        }

        private void pushAllLeft(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public Pair next() {
            if (!hasNext()) throw new NoSuchElementException();

            Node node = stack.pop();
            Pair pair = new Pair(node.key, node.val);

            if (node.right != null) {
                pushAllLeft(node.right);
            }

            return pair;
        }
    }
}
