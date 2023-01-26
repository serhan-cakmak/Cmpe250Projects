public class AvlNode  {
    public String  key;      // The data in the node
    public AvlNode left;         // Left child
    public AvlNode right;        // Right child
    public AvlNode parent;
    int     height;       // Height
    AvlNode( String theElement )
    {
        this( theElement, null, null ,null);
    }

    AvlNode(String theElement, AvlNode left, AvlNode right, AvlNode parent )
    {
        key  = theElement;
        this.left     = left;
        this.right    = right;
        this.parent = parent;
        height   = 0;
    }



}
