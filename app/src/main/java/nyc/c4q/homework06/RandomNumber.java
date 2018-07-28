package nyc.c4q.homework06;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikunjan on 7/27/18.
 * if the user press random number this class will generate a random number bet(1-1000)
 */

public class RandomNumber {

       private int minValue =1;
       private int maxValue=1000;



       public String randomNumber(String newString) {
              int j = 0,k=0;
              String newStringLength ="";
              ArrayList<String> operators = new ArrayList<>(); //creating a arraylist for separating eqn
              operators.add("-");
              operators.add("(");
              operators.add(")");
              operators.add("+");
              operators.add("/");
              operators.add("*");

              ArrayList<String> stringSplit = new ArrayList<String>();

              String[] parts = newString.split(operators.toString());


              List<Integer> randomList = new ArrayList<Integer>();


              for(int i = 0; i < newString.length(); i++) {  //this will store the position of the random number in eqn


                     if (newString.charAt(i) == 'r') {

                            randomList.add(i);
                     }

              }

              StringBuilder finalString = new StringBuilder();
              finalString.append(newString);
              ArrayList<Character> str= new ArrayList<>();

              for(int z=0;z<finalString.length();z++) {
                     str.add(finalString.charAt(z));  //breaking the regular string to char array

              }




              for (int i = 0; i < parts.length; i++) {
                     int pos = 0;
                     String ok = "";
                     stringSplit.add(parts[i]);
                     if (parts[i].contains("r")) {
                            //   System.out.println(parts[i]); // to check if the parts has been divided
                            pos = randomList.get(0);
                            //  System.out.println("pos" + pos); //finds the position of the random
                            int a = parts[i].length() - 1;
                            String[] b = parts[i].split("r");
                          //  int result = Integer.parseInt(b[0]);


                            double factoria = randomFinder();
                            ok = Double.toString(factoria);
                            for (int l = 0; l < parts[i].length(); l++) {
                                   str.remove(pos - l);
                            }
                            for (int l = 0; l < ok.length(); l++) {

                                   char z = ok.charAt(l);
                                   str.add(pos + l - (a), z);


                            }



                            parts = newString.split(operators.toString());
                            randomList.clear(); //this list needs to be cleared as we dont want to korn in same random number

                            for (int l = 0; l < str.size(); l++) {


                                   if (str.get(l) == 'r') {
                                          randomList.add(l);

                                          break;
                                   }
                            }


                            j++;
                     }
              }


              String finalS= new String(String.valueOf(str));  //taking back the char array to regular string
              finalS = finalS.replace("]", "");
              finalS=finalS.replace("[","");
              finalS=finalS.replace(",","");
              finalS=finalS.replace(" ","");
              System.out.println(finalS);


              return finalS;


       }

       private double randomFinder() {

              double a = (Math.random() * ((maxValue - minValue) + 1)) + minValue;
              return a;
       }


}
