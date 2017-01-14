
import java.util.*;

public class FibonacciHeap{

    private Node maxNode;
    private int size;

    public FibonacciHeap() {
        maxNode = null;
        size = 0;
    }
	
      /**
       * returns the size of the max fibonacci heap
       * */
     public int getSize(){
        return size;
    }

    /**
     * The following procedure creates a node and inserts node x into Fibonacci heap H
     * @param key
     * @param tag
     * @return Node
     */
    public Node insert(int key, String tag) {
        Node x = new Node(key,tag);

        // adds x to the root list of heap
        maxNode = merge(maxNode, x);
        size++;
        return x;
    }

    /**^
     * The following procedure increases node's key to newKey
     * @param node
     * @param newKey
     */
    public void increaseKey(Node node, int newKey) {

        // newKey is no greater than the current key of node
        if (newKey < node.key) {
            throw new IllegalArgumentException("Invalid new key");
        }

        node.key = newKey;
        Node parent = node.parent;

        // if node has a parent and node's key is greater than the parent, then perform CUT and CASCADINGCUT
        if (parent != null && node.compare(parent) > 0) {
            cut(node, parent);
            cascadingCut(parent);
        }
        // update max node if necessary
        if (node.compare(maxNode) > 0) {
            maxNode = node;
        }
    }

    /**
     * The CUT procedure cuts the link between node and its parent, adding node to the rootlist.
     * @param node
     * @param parent
     */
    private void cut(Node node, Node parent) {

        if(node.next != node)
            removeNodeFromSiblings(node);

        if (parent.child == node) {
            /* If there are any other children, pick one of them arbitrarily. */
            if (node.next != node) {
                parent.child = node.next;
            }
            /* Otherwise, there aren't any children left and we should clear the
             * pointer and drop the node's degree.
             */
            else {
                parent.child = null;
            }
        }
        parent.degree--;
        merge(maxNode, node);
        node.parent = null;
        node.isMarked = false;
    }

    /**^
     * If node is marked, it has just lost its second child; node is cut,
       and CASCADING-CUT calls itself recursively node's parent. The
       CASCADING-CUT procedure recurses its way up the tree until either a root or an
       unmarked node is found
     * @param node
     */
    private void cascadingCut(Node node) {
        Node parent = node.parent;
        if (parent != null) {
            if (node.isMarked) {
                cut(node, parent);
                cascadingCut(parent);
            } else {
                node.isMarked = true;
            }
        }
    }

    /**^
     * extractMax works by first making a root out of each of the minimum node’s children and
       removing the minimum node from the root list. It then consolidates the root list by linking
       roots of equal degree until at most one root remains of each degree.
     * @return Node
     */
    public Node extractMax() {
        Node extractedNode = maxNode;
        if (extractedNode != null) {
            // Set parent to null for the minimum's children
            if (extractedNode.child != null) {
                Node child = extractedNode.child;
                do {
                    child.parent = null;
                    child = child.next;
                } while (child != extractedNode.child);
            }

            Node nextInRootList = maxNode.next == maxNode ? null : maxNode.next;

            // Remove max from root list
            removeNodeFromSiblings(extractedNode);
            size--;

            // Merge the children of the max node with the root list
            maxNode = merge(nextInRootList, extractedNode.child);

            if (nextInRootList != null) {
                maxNode = nextInRootList;
                consolidate();
            }
        }
        return extractedNode;
    }

    /**^
     * Consolidating the root list consists of repeatedly executing the following steps until
       every root in the root list has a distinct degree value
     */
    private void consolidate() {


        HashMap<Integer,Node> degreeMap = new HashMap<>();
        LinkedList<Node> rootList = new LinkedList<>();
        Node tempNode = maxNode;

        do {
            rootList.add(tempNode);
            tempNode = tempNode.next;
        } while (maxNode != tempNode);


        while (rootList.peek() != null) {
            Node current = rootList.removeFirst();

            while (degreeMap.containsKey(current.degree)) {

                if (current.compare(degreeMap.get(current.degree)) < 0) {
                    Node temp = current;
                    current = degreeMap.get(current.degree);
                    degreeMap.replace(current.degree, temp);
                }

                linkHeaps(degreeMap.get(current.degree), current);
                degreeMap.remove(current.degree);
                current.degree++;
            }

            degreeMap.put(current.degree, current);
        }

        Iterator<Integer> iter= degreeMap.keySet().iterator();

        maxNode = null;
        while(iter.hasNext()) {
            Integer tempInt=iter.next();
            if (degreeMap.containsKey(tempInt)) {
                // Remove siblings before merging
                degreeMap.get(tempInt).next = degreeMap.get(tempInt);
                degreeMap.get(tempInt).prev = degreeMap.get(tempInt);
                maxNode = merge(maxNode, degreeMap.get(tempInt));
            }
        }
    }

    /**^
     * remove the given node from the linkedList of siblings
     * @param nodeA
     */
    private void removeNodeFromSiblings(Node nodeA) {
        Node prev = nodeA.prev;
        Node next = nodeA.next;
        prev.next = next;
        next.prev = prev;

        nodeA.next = nodeA;
        nodeA.prev = nodeA;
    }

    private void linkHeaps(Node nodeA, Node nodeB) {
        removeNodeFromSiblings(nodeA);
        nodeB.child = merge(nodeA,nodeB.child);

        nodeA.parent = nodeB;
        nodeA.isMarked = false;
    }

    /**^
     * Merges two lists and returns the max node
     * @param a
     * @param b
     * @return Node
     */
    public Node merge(
            Node a, Node b) {

        if (a == null && b == null) {
            return null;
        }
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }

        Node temp = a.next;
        a.next = b.next;
        a.next.prev = a;
        b.next = temp;
        b.next.prev = b;

        return a.compare(b) > 0 ? a : b;
    }
}
