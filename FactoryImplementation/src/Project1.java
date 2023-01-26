import java.io.*;

public class Project1 {
    public static void main(String[] args) throws IOException {
        File file = new File(
                args[0]);

        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        String outputPath = args[1];
        FileWriter fWriter = new FileWriter(outputPath);
        FactoryImpl obj = new FactoryImpl();
        while ((st = br.readLine()) != null) {

            String[] arr = st.split(" ");
            switch (arr[0]) {
                case "P":
                    obj.printTo(fWriter);
                    break;
                case "AF":
                    obj.addFirst(new Product(Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
                    break;
                case "AL":
                    obj.addLast(new Product(Integer.parseInt(arr[1]), Integer.parseInt(arr[2])));
                    break;
                case "A":
                    try {
                        obj.add(Integer.parseInt(arr[1]), new Product(Integer.parseInt(arr[2]), Integer.parseInt(arr[3])));

                    }catch (Exception e){
                        fWriter.write("Index out of bounds."+"\n");
                    }
                    break;
                case "RF":
                    try {
                        Product ret = obj.removeFirst();
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Factory is empty."+"\n");
                    }
                    break;
                case "RL":
                    try {
                        Product ret = obj.removeLast();
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Factory is empty."+"\n");
                    }
                    break;
                case "RI":
                    try {
                        Product ret = obj.removeIndex(Integer.parseInt(arr[1]));
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Index out of bounds."+"\n");
                    }
                    break;
                case "RP":
                    try {
                        Product ret = obj.removeProduct(Integer.parseInt(arr[1]));
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Product not found."+"\n");
                    }
                    break;
                case "F":
                    try {
                        Product ret = obj.find(Integer.parseInt(arr[1]));
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Product not found."+"\n");
                    }
                    break;
                case "G":
                    try {
                        Product ret = obj.get(Integer.parseInt(arr[1]));
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Index out of bounds."+"\n");
                    }
                    break;
                case "U":
                    try {
                        Product ret = obj.update(Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
                        fWriter.write("("+ret.getId() + ", " + ret.getValue()+")"+"\n");
                    }catch (Exception e){
                        fWriter.write("Product not found."+"\n");
                    }
                    break;
                case "FD":
                    int total = obj.filterDuplicates();
                    fWriter.write(total + "\n");
                    break;
                case "R":
                    obj.reverse();
                    obj.printTo(fWriter);
                    break;

            }
        }
        fWriter.close();

    }
}