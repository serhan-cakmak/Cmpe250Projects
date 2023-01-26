import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;



public class FactoryImpl implements Factory{
    private Holder first;
    private Holder last;
    private Integer size = 0;

    public  void printTo(FileWriter fWriter){

        try {
            // First shouldn't be used due to risk of losing head info.
            Holder tmp = first;
            fWriter.write("{");
           while (tmp !=null){
               //if tmp equals to last then ',' shouldn't be added.
               if (tmp == last){
                   fWriter.write("("+tmp.getProduct().getId()+", "+tmp.getProduct().getValue()+")");
                    break;
               }
               fWriter.write("("+tmp.getProduct().getId()+", "+tmp.getProduct().getValue()+"),");
               tmp = tmp.getNextHolder();
           }

            fWriter.write("}\n");
        }

        // Catch block to handle if exception occurs
        catch (IOException e) {

            // Print the exception
            System.out.print(e.getMessage());
        }
    }

    @Override
    public void addFirst(Product product) {
        if (first != null){
            Holder lead = new Holder(null, product, first);   //new Holder object is added with dynamic fields. So when first's fields are changed then lead info is also updated.
            first.setPreviousHolder(lead);
            first = lead;
            size++;
        }else{
            first = new Holder(null, product, null);
            last = first;       //The first product added is also the last one.
            size++;
        }

    }

    @Override
    public void addLast(Product product) {
        //same thing with addFirst.
        if (last !=null){
            Holder back = new Holder(last, product, null);
            last.setNextHolder(back);
            last = back;
            size++;
        }else{
            first = new Holder(null, product, null);
            last = first;       //The first product added is also the last one.
            size++;
        }

    }

    @Override
    public Product removeFirst() throws NoSuchElementException {
        //When size = 1, we cannot change the nextholder's previous holder.
        if (size >= 2 ){
            first.getNextHolder().setPreviousHolder(null);
            Product tmp = first.getProduct();
            first = first.getNextHolder();
            size--;
            return tmp;
        } else if( size==1){
            Product tmp =  first.getProduct();
            first =null;
            last =null;
            size--;
            return tmp;
        }else {
            throw new NoSuchElementException();

        }


    }

    @Override
    public Product removeLast() throws NoSuchElementException {
        if (size >= 2 ){

            Product tmp = last.getProduct();
            last.getPreviousHolder().setNextHolder(null);
            last = last.getPreviousHolder();
            size--;
            return tmp;
        }else if( size==1){
            Product tmp =  first.getProduct();
            first =null;
            last =null;
            size--;
            return tmp;
        }else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public Product find(int id) throws NoSuchElementException {
        Holder head = first;
        while ( head !=null ){
            //Id check
            if (head.getProduct().getId() == id){
                return head.getProduct();
            }
            head = head.getNextHolder();

        }
        throw new NoSuchElementException();
    }

    @Override
    public Product update(int id, Integer value) throws NoSuchElementException {
        Holder head = first;

        while (head != null ){
            if (head.getProduct().getId() == id){
                //I used final because head.getProduct.getValue value is changed dynamically. So I returned a new object with old value instead.
                final int tmp =  head.getProduct().getValue();
                head.getProduct().setValue(value);
                return new Product(id, tmp);
            }
            head = head.getNextHolder();
        }
        throw new NoSuchElementException();
    }

    @Override
    public Product get(int index) throws IndexOutOfBoundsException {
        Holder front = first;
        int ind = 0;   //indicates the current index  in the iteration.
        while (front !=null){
            if (ind == index){
                return front.getProduct();
            }
            ind++;
            front = front.getNextHolder();
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void add(int index, Product product) throws IndexOutOfBoundsException {
        if (index == 0){                //first and last should be updated.
            addFirst(product);
            return;
        }else if (index == size){       // index ==size means that intention is to add an element to the end of the list.
            addLast(product);
            return;
        } else if ( index < 0 || index > size) {
            throw new IndexOutOfBoundsException();
        }
        // Iterated 1 time to not get errors about getpreviousholder etc.
        int ind = 1;
        Holder front =first.getNextHolder();

        while ( ind < size){
            if (ind == index){
                Holder added = new Holder(front.getPreviousHolder(),product, front);    //I added holder before front because the index I wanted to add is currently the front's index.
                front.getPreviousHolder().setNextHolder(added);
                front.setPreviousHolder(added);
                size++;
                return;
            }
            front = front.getNextHolder();
            ind++;
        }

    }

    @Override
    public Product removeIndex(int index) throws IndexOutOfBoundsException {

        if (index == 0){
            return removeFirst();
        }else if (index == size-1){         // this line is different from the add function's.
            return removeLast();
        } else if ( index < 0 || index >= size) {                                   //checking the edges
            throw new IndexOutOfBoundsException();
        }
        Holder front =first.getNextHolder();
        int ind = 1;
        while (ind <size-1){
            if (ind == index){
                front.getPreviousHolder().setNextHolder(front.getNextHolder());
                front.getNextHolder().setPreviousHolder(front.getPreviousHolder());
                size--;
                return  front.getProduct();
            }
            front = front.getNextHolder();
            ind++;
        }

        return null;            //every possible situtaion is handled above.
    }

    @Override
    public Product removeProduct(int value) throws NoSuchElementException {
        Holder front = first;
        while (front != null){
            if (front.getProduct().getValue()== value){
                  if (front == first){
                      return removeFirst();
                  } else if (front == last) {
                      return removeLast();
                  }else {
                      front.getPreviousHolder().setNextHolder(front.getNextHolder());
                      front.getNextHolder().setPreviousHolder(front.getPreviousHolder());
                      Product res = front.getProduct();
                      size--;
                      return res;
                  }
            }
            front = front.getNextHolder();
        }
        throw new NoSuchElementException();
    }

    @Override
    public int filterDuplicates() {
        HashSet<Integer> values = new HashSet<>();

/*
        List<Integer> values = new ArrayList<>();       //List to check if the value passed before or not.
*/
        Holder front = first;
        int res = 0;
        while (front != null){
            if (!values.contains(front.getProduct().getValue())){
                values.add(front.getProduct().getValue());
                front = front.getNextHolder();
            }else {
                if (front == last){             //there is no need to do the same for first because first's value cannot be in the list.
                removeLast();
                res++;
                break;
            }

                front.getPreviousHolder().setNextHolder(front.getNextHolder());
                front.getNextHolder().setPreviousHolder(front.getPreviousHolder());
                res++;
                size--;
                front = front.getNextHolder();
            }
        }

        return res;

    }

    @Override
    public void reverse() {
        Holder tmp;
        int a = 0;
        boolean initial = true;
         while (first != null){
             tmp = first.getPreviousHolder();                   //tmp is used in order not to lose connection between 2 holders.
             first.setPreviousHolder(first.getNextHolder());
             first.setNextHolder(tmp);

             if (initial){
                 last = first;          //since this process changes first and last I iterated using first.
                 initial = false;
             }
             a++;
             if (a ==size ){
                 break;
             }
             first = first.getPreviousHolder();

        }


    }
}