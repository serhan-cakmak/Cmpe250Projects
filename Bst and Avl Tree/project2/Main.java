

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        AvlTree avl = new AvlTree();
        BsTree bs = new BsTree();
        File file = new File(args[0]);
        BufferedReader br = new BufferedReader(new FileReader(file));
        avl.fWriter =  new FileWriter(args[1]+"_AVL.txt");
        bs.fWriter = new FileWriter(args[1]+"_BST.txt");
        String st;

        String ro = br.readLine();
        avl.setRoot(ro);
        bs.setRoot(ro);


        while ((st = br.readLine()) != null) {

            String[] arr = st.split(" ");
            switch (arr[0]) {
                case "ADDNODE":
                    avl.addNode(arr[1]);
                    bs.addNode(arr[1]);
                     break;
                case "DELETE":
                    avl.remove(arr[1]);
                    bs.remove(arr[1]);
                    break;
                case  "SEND":
                    avl.sendMessage(arr[1], arr[2]);
                    bs.sendMessage(arr[1], arr[2]);
            }
        }
       /* AvlTree.printTree(avl.getRoot());
        BsTree.printTree(bs.getRoot());*/
        bs.fWriter.close();
        avl.fWriter.close();





    }


}
