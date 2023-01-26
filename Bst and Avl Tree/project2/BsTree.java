import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BsTree {
    private Node root;
    public FileWriter fWriter;

    /** set filewriter in the main*/


    public Node getRoot() {
        return root;
    }

    public void setRoot(String key) {
        this.root = new Node(key);
    }

    public BsTree() {
        root = null;
    }
    public BsTree(String key) {
        root = new Node(key);
    }



    public void addNode( String x ) throws IOException {
        root = addNode( x, root, null);
    }
    private Node addNode( String key, Node node, Node parent ) throws IOException {
        if( node == null ){
            logAddNode(key);
            return new Node(key, null,null, parent) ;
        }

        int compareResult = key.compareTo( node.key );

        if( compareResult < 0 ){
            node.left = addNode( key, node.left, node );
        } else if( compareResult > 0 ){
            node.right = addNode( key, node.right,node );
        }
        return node;
    }
    public static void printTree( Node t )
    {
        if( t != null )
        {
            printTree( t.left );
            System.out.println( t.key );
            printTree( t.right );
        }
    }

    public void remove( String x ) throws IOException {
        root = remove( x, root,false);
    }

    private Node remove( String key,Node node ,boolean logged ) throws IOException {
        if( node == null || key.equals(root.key))
            return node;   // Item not found; do nothing
                                                                            /** root cannot be removed*/
        int compareResult = key.compareTo( node.key );

        if( compareResult < 0 ){
            node.left = remove( key, node.left ,logged);
        } else if( compareResult > 0 ){
            node.right = remove( key, node.right, logged );
        }else{
            if( node.left != null && node.right != null ) // Two children
            {
                final String tmp = node.key;
                node.key = findMin( node.right ).key;
                logDel2(node, tmp);
                node.right = remove( node.key, node.right, true);
            }else if (node.left !=null){
                if (!logged)
                    logDel(node);
                node.left.parent = node.parent;
                node = node.left;
            }else if (node.right != null){
                if (!logged)
                    logDel(node);
                node.right.parent = node.parent;
                node = node.right;
            }else {
                if (!logged)
                    logDelLeaf(node);
                node =null;
            }
        }

        return node;
    }
    private Node findMin( Node node )
    {
        if( node == null )
            return null;
        else if( node.left == null )
            return node;
        return findMin( node.left );
    }

    public List<String> search(String key){
        return search(key , root, new ArrayList<>());
    }
    public List<String> search(String key, Node node, ArrayList<String> arr){
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

        if (!next.equals(receiver) ){
            fWriter.write(next + ": Transmission from: " + back + " receiver: " + receiver + " sender:" + sender+"\n");
        }
    }

    public  void logAddNode(String key) throws IOException {
        logAddNode(key, root);
    }
    public void logAddNode(String key, Node node) throws IOException {
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

    public void logDelLeaf(Node node) throws IOException {
        fWriter.write(node.parent.key+ ": Leaf Node Deleted: " +node.key+"\n");
    }

    public void logDel2(Node node, String deleted ) throws IOException {
        if (node != root)
            fWriter.write(node.parent.key+": Non Leaf Node Deleted; removed: "+ deleted + " replaced: "+node.key+"\n");
    }

    public void logDel (Node node) throws IOException {
        fWriter.write(node.parent.key+ ": Node with single child Deleted: " +node.key+"\n");

    }

}
