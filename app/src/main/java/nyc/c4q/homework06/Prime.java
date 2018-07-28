package nyc.c4q.homework06;

import java.util.ArrayList;

/**
 * Created by nikunjan on 7/28/18.
 * this class will check if the user input number is prime or not
 */

public class Prime {
    boolean test_value=false;
    ArrayList<String> operators = new ArrayList<>(); //creating a arraylist for separating eqn



    public String primeNumber(String newString) {
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
        operators.add("f");

        String[] parts = newString.split(operators.toString());
         String[] newPrime= newString.split("p"); //getting the input and splitting the 'p'
         if(newPrime.length<1 || parts.length>1){  //error checker if user inputs no number
         return "0";
         }



        int num= Integer.parseInt(newPrime[0]);
        int prime_checker = (int) Math.sqrt(num);
        test_value = prime_finder(prime_checker, num);


        if (test_value) { //if true will return prime if not not prime

            return ( num + "  Is  prime");
        } else {
            return ( num + " Is not prime");
        }

    }

       public boolean prime_finder( int prime_checker, int num){ //function for checking prime number

            test_value=false;
            if(num==2 ||num ==3 || num==5 || num==7 ){
                test_value= true;

            }
            if (num%2!=0){
                for( int i=3;i<=prime_checker;i++){

                    if (num %i==0) {
                        test_value=  false;
                        break;
                    }
                    else {

                        test_value=  true;
                    }
                    i++;
                }
            }
            return test_value;

        }
    }

