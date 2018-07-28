package nyc.c4q.homework06;


import java.util.ArrayList;

/**
 * Created by nikunjan on 7/28/18.
 */

public class Fibonnaci {
    int num1=1,num2=1,num3=0;

    ArrayList<String> operators = new ArrayList<>(); //creating a arraylist for separating eqn

    public String fibo(String newString ){
        operators.add("-");
        operators.add("(");
        operators.add(")");
        operators.add("+");
        operators.add("/");
        operators.add("*");
        operators.add("!");
        operators.add("pi");
        operators.add("e");
        operators.add("%");
        operators.add("r");
        operators.add("p");

        String[] parts = newString.split(operators.toString());

     String[] str=newString.split("f"); //will split f
     if(str.length<1 || parts.length>1){ //if the operator is caught more than one then the default output will be zero
         return "0";
     }
        int fib= Integer.parseInt(str[0]);
        String finalString="";


        for (int i=0;i<fib;i++){ //calculating the fibonacci number
            if(i==0 || i==1){
               finalString+="1";
            }
            else{
                num3=num1+num2;
                finalString+=num3;
                num1=num2;
                num2=num3;
            }

        }


        return finalString;
    }



}