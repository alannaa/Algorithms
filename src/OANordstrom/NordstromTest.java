package OANordstrom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NordstromTest {

  public static void main(String[] args) {

    String line = "3,7,4";

    List<String> inputSeparated = Arrays.asList(line.split(","));

    int a = Integer.parseInt(inputSeparated.get(0));
    int b = Integer.parseInt(inputSeparated.get(1));
    int c = Integer.parseInt(inputSeparated.get(2));

    System.out.println(a+b==c || a+c==b || b+c==a);

    ArrayList<Integer> intList = new ArrayList<>();

    for(int i=0; i<inputSeparated.size(); i++) {
      intList.add(Integer.parseInt(inputSeparated.get(i)));
    }

  }

}
