public class Node{

    protected int key;  // Frequency of the hashtag corresponding to the Node
    protected String tagName;   // HashTag corresponding to the Node
    protected int degree;   // Number of children of the Node
    protected Node parent;  // Reference to the Parent of the Node
    protected Node child;   // Reference to the Child of the Node
    protected Node prev;    // Reference to previous sibling Node
    protected Node next;    // Reference to next sibling Node
    protected boolean isMarked; // Flag for child cut

    public Node() {
        key = 0;
        tagName = null;
    }

    protected Node(int key, String tag) {
        this.key = key;
        this.tagName=tag;
        next = this;
        prev = this;
    }


    public int getKey() {
        return key;
    }

    public String getTag() {
        return tagName;
    }

    protected int compare(Node a){
        if(this.key > a.key)
            return 1;
        else
            return -1;
    }

}
