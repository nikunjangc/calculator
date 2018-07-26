package nyc.c4q.homework06;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nikunjan on 7/26/18.
 */

public class FactorialAndPercentage {


    public String simplify(String newString){

   // String newstring = "(4!+6%+5!+2!-(25+8!-45%))";
    //    System.out.println(newString);
    ArrayList<String> operators = new ArrayList<>();

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

    String[] parts = newString.split(operators.toString());


    List<Integer> factorialList = new ArrayList<Integer>();
    List<Integer> percentList=new ArrayList<Integer>();

        for(int i = 0; i < newString.length(); i++) {



        if (newString.charAt(i) == '!') {

            factorialList.add(i);
        }
        if (newString.charAt(i) == '%') {

            percentList.add(i);

        }
    }
    //   System.out.println("parts" + parts.length);

    StringBuilder finalString = new StringBuilder();
        finalString.append(newString);
    ArrayList<Character> str= new ArrayList<>();

        for(int z=0;z<finalString.length();z++) {
        str.add(finalString.charAt(z));
        //     System.out.print(str.get(z));
    }


    //  System.out.println(factorialList);
    //  System.out.println(perc);
    int j = 0,k=0;
    String newStringLength ="";
        for (int i = 0; i < parts.length; i++) {
        int pos = 0;
        String ok = "";
        stringSplit.add(parts[i]);
        if (parts[i].contains("!")) {
            //   System.out.println(parts[i]);
            pos = factorialList.get(0);
            //  System.out.println("pos" + pos);
            int a = parts[i].length() - 1;
            String[] b = parts[i].split("!");
            int result = Integer.parseInt(b[0]);

            //  System.out.println(" fact!" + result + "len" + a);
            int factoria = factorial(result);
            //  System.out.println("this is fact!" + factoria);
            ok = Integer.toString(factoria);
            for (int l = 0; l < parts[i].length(); l++) {
                str.remove(pos - l);
            }
            for (int l = 0; l < ok.length(); l++) {

                char z = ok.charAt(l);
                str.add(pos + l - (a), z);

                //     System.out.println(str);
            }

            //  System.out.println("after" + str);

            parts = newString.split(operators.toString());
            factorialList.clear();
            //  System.out.println("factorialList is" + factorialList);
            for (int l = 0; l < str.size(); l++) {


                if (str.get(l) == '!') {
                    factorialList.add(l);

                    break;
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

                //   System.out.println(str);
            }

            //   System.out.println("after" + str);

            parts = newString.split(operators.toString());
            percentList.clear();
            //  System.out.println("percent is" + percentList);
            for (int l = 0; l < str.size(); l++) {


                if (str.get(l) == '%') {
                    percentList.add(l);

                    break;
                }
            }

        }

        //  System.out.println(parts[i]);
    }
    String finalS= new String(String.valueOf(str));

    finalS = finalS.replace("]", "");
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


