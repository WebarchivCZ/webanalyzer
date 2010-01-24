
package cz.webarchiv.webanalyzer.multithread.testobj;

import java.io.*;

public class TemtTest {
  public static void main(String[] args) throws IOException{
    BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter a char:");
    String str = buff.readLine();

    String[] bytesArray = str.split(" ");
    for (int i = 0; i< bytesArray.length; i++) {

        Byte b = new Byte(bytesArray[i]);
//        System.out.print(b.toString() + " ");
        char c = (char)b.byteValue();
        System.out.print(c);
        System.out.print("");

    }

     System.out.println();


//    public static void main(String[] args) throws IOException{
//    BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
//    System.out.println("Enter a char:");
//    String str = buff.readLine();
//
//    for (int i = 0; i<str.toCharArray().length; i++) {
//        System.out.print((byte)str.toCharArray()[i]);
//        System.out.print(" ");
//    }
////    char c = str.charAt(4);
////    byte bValue = (byte)c;
//    System.out.println();
//  }

}
}