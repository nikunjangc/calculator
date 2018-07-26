package nyc.c4q.homework06;

import android.widget.TextView;

import com.fathzer.soft.javaluator.Constant;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.Function;
import com.fathzer.soft.javaluator.Operator;
import com.fathzer.soft.javaluator.Parameters;


/**
 * Created by nikunjan on 7/10/18.
 */

public class CalculatorEvaluation {
    private String equation;
    ExtendedDoubleEvaluator evaluator = new ExtendedDoubleEvaluator();
    Double result;
    public void evaluateEquation(String s, TextView textView){



       try {
           result = evaluator.evaluate(s);
           textView.setText(String.valueOf(result));
       }catch (IllegalArgumentException i){
           textView.setText(String.valueOf(s));
       }

    }


    public String getEquation() {
        return equation;
    }

    public void setEquation(String equation) {
        this.equation = equation;
    }



}






















