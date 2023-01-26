public class Node {
    public String key;
    public  Node left;
    public  Node right;
    public  Node parent;


    Node( String theElement ) {
        this( theElement, null, null, null);
    }

    Node( String  theElement, Node lt, Node rt, Node parent ) {
        key = theElement;
        left     = lt;
        right    = rt;
        this.parent = parent;
    }
}
