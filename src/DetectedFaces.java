import java.io.*;
import java.util.ArrayList;

public class DetectedFaces implements Serializable {
    ArrayList<String> names = new ArrayList<String>();

    public DetectedFaces(){

    }

    public void detectFace(String name){
        names.add(name);
    }

    public void write(){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("attendance.txt",true));
            oos.writeObject(this);

        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<String> read(int n){
        int start = 1;


        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("attendance.txt"));
            while (start != n){
                ois.readObject();
                start++;
            }
            DetectedFaces temp = (DetectedFaces) ois.readObject();
            return temp.names;

        } catch (FileNotFoundException e) {

            throw new RuntimeException(e);

        } catch (IOException e) {

            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
