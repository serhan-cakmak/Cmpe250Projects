import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AvlTree {
    private AvlNode root;
    public FileWriter fWriter;

    public AvlNode getRoot() {
        return root;
    }

    public void setRoot(String key) {
        this.root = new AvlNode(key);
    }

    public AvlTree( )
    {
        root = null;
    }

    public void addNode(String x ) throws IOException {
        root = addNode( x, root, null );
    }

    public void remove( String x ) throws IOException {
        root = remove( x, root ,false);
    }



    private AvlNode remove(String key, AvlNode node, boolean logged ) throws IOException {
        if( node == null || key.equals(root.key))
            return node;

        int compareResult = key.compareTo( node.key );

        if( compareResult < 0 ){
            node.left = remove( key, node.left, logged );
        } else if( compareResult > 0 ){
            node.right = remove( key, node.right, logged);
        }else{
            node = balance(node);
            if( node.left != null && node.right != null )
            {
                final String tmp = node.key;
                node.key = findMin( node.right ).key;
                logDel2(node, tmp);
                node.right = remove( node.key, node.right,true );
            }else if (node.left !=null){
                if (!logged )
                    logDel(node);
                node.left.parent = node.parent;
                node = node.left;
            }else if (node.right != null){
                if (!logged )
                    logDel(node);
                node.right.parent = node.parent;
                node = node.right;
            }else {
                if (!logged )
                    logDelLeaf(node);
                node =null;
            }
        }


        return balance( node );
    }




    public boolean isEmpty( )
    {
        return root == null;
    }




    private AvlNode balance(AvlNode node ) throws IOException
    {
        if( node == null )
            return node;

        if( height( node.left ) - height( node.right ) > 1 ){
            if( height( node.left.left ) >= height( node.left.right ) )
                node = rightRotate( node, true );
            else
                node = leftRightRotate( node );
        } else if( height( node.right ) - height( node.left ) > 1 ){
            if( height( node.right.right ) >= height( node.right.left ) )
                node = leftRotate( node, true );
            else
                node = rightLeftRotate( node );
        }

        node.height = Math.max( height( node.left ), height( node.right ) ) + 1;
        return node;
    }




    private AvlNode addNode(String key, AvlNode node, AvlNode parent ) throws IOException {
        if( node == null ){
            logAddNode(key);
            return new AvlNode( key,null,null, parent );
        }


        int compareResult = key.compareTo( node.key );

        if( compareResult < 0 )
            node.left = addNode( key, node.left , node);
        else if( compareResult > 0 )
            node.right = addNode( key, node.right, node) ;
        else
            ;  // Duplicate; do nothing
        return balance( node );
    }


    private AvlNode findMin(AvlNode node )
    {
        if( node == null )
            return node;

        while( node.left != null )
            node = node.left;
        return node;
    }



    public static void printTree( AvlNode t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.key );
            printTree( t.right );
        }
    }


    private int height( AvlNode node )
    {
        if (node==null){
            return -1;
        }
        return  node.height;
    }


    private AvlNode rightRotate(AvlNode k1, boolean flag) throws IOException {
        AvlNode tmp = k1.parent;
        AvlNode k2 = k1.left;
        k1.left = k2.right;
        k2.right = k1;

        k2.parent = tmp;
        k1.parent = k2;
        if (k1.left !=null)
            k1.left.parent = k1;

        k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
        k2.height = Math.max( height( k2.left ), k1.height ) + 1;
        if (flag)
            logBalance("right rotation");
        return k2;
    }


    private AvlNode leftRotate(AvlNode k1, boolean flag) throws IOException {
        AvlNode tmp = k1.parent;
        AvlNode k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;

        k2.parent = tmp;
        k1.parent = k2;
        if (k1.right != null)
            k1.right.parent = k1;

        k1.height = Math.max( height( k1.left ), height( k1.right ) ) + 1;
        k2.height = Math.max( height( k2.right ), k1.height ) + 1;
        if (flag)
            logBalance("left rotation");
        return k2;
    }


    private AvlNode leftRightRotate(AvlNode k3 ) throws IOException {
        k3.left = leftRotate( k3.left, false);
        logBalance("left-right rotation");
        return rightRotate( k3 ,false);
    }


    private AvlNode rightLeftRotate(AvlNode k1 ) throws IOException {
        k1.right = rightRotate( k1.right,false );
        logBalance("right-left rotation");
        return leftRotate( k1, false );
    }


    public  void logAddNode(String key) throws IOException {
        logAddNode(key, root);
    }
    public void logAddNode(String key, AvlNode node) throws IOException {
        if (node == null){
            return;
        }
        int compareResult = key.compareTo( node.key );
        fWriter.write(node.key + ": New node being added with IP:" + key+"\n");
        if( compareResult < 0 ){
            logAddNode( key, node.left );
        } else if( compareResult > 0 ){
            logAddNode( key, node.right );
        }
    }
    public List<String> search(String key){
        return search(key , root, new ArrayList<>());
    }
    public List<String> search(String key, AvlNode node, ArrayList<String> arr){
        arr.add(node.key);
        if (node.key.equals(key)){
            return arr;
        }

        int com = key.compareTo(node.key);
        if (com >0 ){

            return search(key, node.right,arr);
        } else {

            return search(key, node.left,arr);
        }

    }
    public void sendMessage(String sender, String receiver) throws IOException {
        List<String> senderPath = search(sender);
        List<String> receiverPath = search(receiver);
        int a = 0;
        String common ="";
        for (int i = 0; i < Math.min(senderPath.size(), receiverPath.size()); i++){
            if (senderPath.get(i).equals(receiverPath.get(i))){
                a++;
                common = senderPath.get(i);
                continue;
            }
            break;
        }
        fWriter.write(sender+ ": Sending message to: "+receiver+"\n");


        senderPath = senderPath.subList(a, senderPath.size());
        receiverPath = receiverPath.subList(a, receiverPath.size());
        if (senderPath.size() ==0){

            logSendInter(receiverPath.get(0), sender, receiver,sender);
        } else if (receiverPath.size() ==0) {
            Collections.reverse(senderPath);

        } else{
            senderPath.add(0,common);
            Collections.reverse(senderPath);
            receiverPath.add(0,common);

        }



        for (int i =0; i < senderPath.size()-1; i++){
            logSendInter(senderPath.get(i+1),senderPath.get(i), receiver,sender);
        }
        for (int i =0; i < receiverPath.size()-2; i++){
            logSendInter(receiverPath.get(i+1),receiverPath.get(i), receiver,sender);
        }
        fWriter.write(receiver +": Received message from: " +sender+"\n");





    }
    public void  logSendInter(String next, String back, String receiver, String sender) throws IOException {

        if (!next.equals(receiver)){
            fWriter.write(next + ": Transmission from: " + back + " receiver: " + receiver + " sender:" + sender+"\n");
        }
    }
    public void logDelLeaf(AvlNode node) throws IOException {
        fWriter.write(node.parent.key+ ": Leaf Node Deleted: " +node.key+"\n");
    }

    public void logDel2(AvlNode node, String deleted ) throws IOException {
        fWriter.write(node.parent.key+": Non Leaf Node Deleted; removed: "+ deleted + " replaced: "+node.key+"\n");
    }

    public void logDel (AvlNode node) throws IOException {
        fWriter.write(node.parent.key+ ": Node with single child Deleted: " +node.key+"\n");

    }

    public void  logBalance(String s) throws IOException {
        fWriter.write("Rebalancing: "+s+"\n");
    }
}
