package nyc.c4q.homework06;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikunjan on 7/26/18.
 */

public class FactorialAndPercentage {


    public String simplify(String newString){

   // this is a test example String newstring = "(4!+6%+5!+2!-(25+8!-45%))";

    ArrayList<String> operators = new ArrayList<>(); //creating a arraylist for separating eqn

        operators.add("-");
        operators.add("(");
        operators.add(")");
        operators.add("+");
        operators.add("/");
        operators.add("*");
    //  operators.add("!");
//        operators.add("pi");
//        operators.add("e");
//        operators.add("%");
//        operators.add("Rnd");
//        operators.add("Exp");

       // System.out.println(operators.toString());
    ArrayList<String> stringSplit = new ArrayList<String>();

    String[] parts = newString.split(operators.toString());  //this will split the string to array,when it finds the operators


    List<Integer> factorialList = new ArrayList<Integer>();
    List<Integer> percentList=new ArrayList<Integer>();

        for(int i = 0; i < newString.length(); i++) {   //this will store the position of the factorial and  percentage number in eqn

            if (newString.charAt(i) == '!') {

            factorialList.add(i);
        }
        if (newString.charAt(i) == '%') {

            percentList.add(i);

        }
    }
    //   System.out.println("parts" + parts.length);

    StringBuilder finalString = new StringBuilder();
        finalString.append(newString); //appending the eqn to finalstring
    ArrayList<Character> str= new ArrayList<>();

        for(int z=0;z<finalString.length();z++) {  //initializing the eqn to char array
        str.add(finalString.charAt(z));

    }



    int j = 0,k=0;
    String newStringLength ="";
        for (int i = 0; i < parts.length; i++) {
        int pos = 0;
        String ok = "";
        stringSplit.add(parts[i]);  //this will catch all
        if (parts[i].contains("!")) { //if the parts array contains "!" will execute the statement

            pos = factorialList.get(0); //this wil get the factorial position
            int a = parts[i].length() - 1; //this will store how many digit factorial is the number
            String[] b = parts[i].split("!"); //this will store the number only in string format
            int result = Integer.parseInt(b[0]); //convert to number


            int factoria = factorial(result);  //this will call the factorial function for evaluation

            ok = Integer.toString(factoria); //this will convert the factorial value to string
            for (int l = 0; l < parts[i].length(); l++) { //this will remove the factorial sign and the number ie (3!+47)=>(+47) 0='(',1=3,2=!,'3'=+,4='6',5='7',6=')'=>0='(',1='+',2='6',3='7',4=')'
                str.remove(pos - l);
            }
            for (int l = 0; l < ok.length(); l++) { //this will add the new evaluated value (+47)=>(6+47)

                char z = ok.charAt(l);
                str.add(pos + l - (a), z);


            }



            parts = newString.split(operators.toString());
            factorialList.clear(); //this needs to be cleared as every time we need to evaluated new factorial
            for (int l = 0; l < str.size(); l++) {

                if (str.get(l) == '!') {
                    factorialList.add(l);
                    break; //this will let one factorial to be evaluated at a time
                }
            }
            j++;
        }
    }

        for (int i = 0; i < parts.length; i++) {
        int pos = 0;
        String ok = "";
        stringSplit.add(parts[i]);
        if (parts[i].contains("%")) {
            //   System.out.println(parts[i]);
            pos = percentList.get(0);
            //  System.out.println("pos" + pos);
            int a = parts[i].length() - 1;
            String[] b = parts[i].split("%");
            Double result = Double.valueOf(Integer.parseInt(b[0]));

            //  System.out.println(" percentList!" + result + "len" + a);
            double percentage = percentage(result);
            //   System.out.println("this is percent%" + percentage);
            ok = Double.toString( percentage);
            for (int l = 0; l < parts[i].length(); l++) {
                str.remove(pos - l);
            }
            for (int l = 0; l < ok.length(); l++) {

                char z = ok.charAt(l);
                str.add(pos + l - (a), z);
            }



            parts = newString.split(operators.toString());
            percentList.clear();

            for (int l = 0; l < str.size(); l++) {

                if (str.get(l) == '%') {
                    percentList.add(l);

                    break;
                }
            }

        }

    }
    String finalS= new String(String.valueOf(str));

    finalS = finalS.replace("]", ""); //this will convert char aray yo string
    finalS=finalS.replace("[","");
    finalS=finalS.replace(",","");
    finalS=finalS.replace(" ","");
        System.out.println(finalS);

        return finalS;
}

    public static int factorial(int n) {
        if (n == 0)
            return 1;
        else {
            return (n * factorial(n - 1));
        }

    }
    public static double percentage(Double n) {
        return n*.01;

    }

}


