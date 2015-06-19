package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class Evaluation extends ActionBarActivity {
    double[] values=new double[2];
    Stack Variables=new Stack();
    String[] Var=new String[2];
    String offset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        Intent intent1=getIntent();
        String expr=intent1.getStringExtra(DisplayMessageActivity.EXTRA_EXPRESSION);
        String value1=intent1.getStringExtra(DisplayMessageActivity.EXTRA_FIRST_VALUE);
        String value2=intent1.getStringExtra(DisplayMessageActivity.EXTRA_SECOND_VALUE);
        String message=intent1.getStringExtra(DisplayMessageActivity.EXTRA_MESSAGE);
        String value3=intent1.getStringExtra(DisplayMessageActivity.EXTRA_THIRD_VALUE);
        offset=intent1.getStringExtra(DisplayMessageActivity.EXTRA_DEPENDENT);
        Variables.push(offset);
        double final_value=Double.parseDouble(value3);
        Create_Var(message);
        if(Variables.search(offset)==1)
            Variables.push("x");
        values[0]=Double.parseDouble(value2);
        values[1]=Double.parseDouble(value1);
        Var[1]=(String)Variables.pop();
        Var[0]=(String)Variables.pop();
        double final_result=runge_kutta(expr,final_value);
        TextView textView4=(TextView) findViewById(R.id.second_button);
        textView4.setText(Double.toString(final_result));
    }
    public int dep_var()
    {
        if(Var[0].equals(offset))
            return 0;
        else
            return 1;
    }
    public int indep_var()
    {
        if(Var[0].equals(offset))
            return 1;
        else
            return 0;
    }
    private static final Map<String,int[]> OPERATORS=new HashMap<String,int[]>();
    static{
        OPERATORS.put("+",new int[]{0});
        OPERATORS.put("-",new int[]{0});
        OPERATORS.put("*",new int[]{5});
        OPERATORS.put("/",new int[]{5});
        OPERATORS.put("^",new int[]{10});
    }
    private static boolean isOperator(String token)
    {
        return OPERATORS.containsKey(token);
    }

    public void Create_Var(String expr) {

        int pos1 = 0, pos2 = 0;
        String temp;
        if (expr.charAt(expr.length() - 1) != ')')
            expr = expr.concat(")");
        while (pos2 < expr.length()) {
            if (isOperator(Character.toString(expr.charAt(pos2))) || expr.charAt(pos2) == ')' || expr.charAt(pos2) == '(') {
                if(pos1!=pos2) {
                    temp = expr.substring(pos1, pos2);
                    if (Variables.search(temp) == -1 && Character.isLetter(temp.charAt(0))&&function_seeker(expr.substring(pos1)).equals("nada"))
                        Variables.push(temp);
                }
                pos1 = pos2 + 1;
            }
            pos2++;
        }
    }
    private double eval(String expr,double[] values)
    {
        int pos=0;
        double result=0.0,a,b;
        Stack output=new Stack();
        String temp;
        while(pos<expr.length())
        {
            temp=Character.toString(expr.charAt(pos));
            if(!isOperator(temp))
            {
                if(Character.isDigit(expr.charAt(pos)))
                {
                    int pos1=pos;
                    double constant;
                    while(pos<expr.length()&&(Character.isDigit(expr.charAt(pos))||expr.charAt(pos)=='.'))
                        pos++;
                    if(pos==expr.length())
                    {   constant=Double.parseDouble(expr.substring(pos1));}
                    else{
                     constant=Double.parseDouble(expr.substring(pos1,pos));}
                    if(pos!=expr.length())
                    pos--;
                    output.push(constant);
                }
                else if(!function_seeker(expr.substring(pos)).equals("nada"))
                {
                    String temp1=function_seeker(expr.substring(pos));
                    char c=temp1.charAt(0);
                    switch(c){
                        case 's':a=(double)output.pop();
                                 result=Math.sin(a);
                                 output.push(result);
                                 break;
                        case 'c':a=(double)output.pop();
                                 result=Math.cos(a);
                                 output.push(result);
                                 break;
                        case 't':a=(double)output.pop();
                                 result=Math.tan(a);
                                 output.push(result);
                                 break;
                        case 'e':a=(double)output.pop();
                                 result=Math.exp(a);
                                 output.push(result);
                                 break;
                        case 'l':a=(double)output.pop();
                                 if(temp1.equals("ln"))
                                     a=Math.log(a);
                                 output.push(result);
                                 break;
                        case 'a':a=(double)output.pop();
                                 result=Math.abs(a);
                                 output.push(result);
                                 break;
                    }
                    pos=pos+temp1.length();
                }
                else
                output.push(values[argpos(temp)]);
            }
            else
            {
                switch(expr.charAt(pos))
                {
                    case '+':a=(double)output.pop();
                             b=(double)output.pop();
                             result=b+a;
                             output.push(result);
                             break;
                    case '-':a=(double)output.pop();
                             b=(double)output.pop();
                             result=b-a;
                             output.push(result);
                             break;
                    case '*':a=(double)output.pop();
                             b=(double)output.pop();
                             result=b*a;
                             output.push(result);
                             break;
                    case '/':a=(double)output.pop();
                             b=(double)output.pop();
                             result=b/a;
                             output.push(result);
                             break;
                    case '^':a=(double)output.pop();
                             b=(double)output.pop();
                             result=Math.pow(a,b);
                             output.push(result);
                             break;
                }
            }
            pos++;
        }
        result=(double)output.pop();
        return result;
    }
    private  int argpos(String token)
    {
        int i=0;
        if(token.equals(Var[0]))
            return 0;
        else if(token.equals(Var[1]))
            return 1;
        else
            return -1;

    }
    public double runge_kutta(String expr,double final_value)
    {
        int i=0,k,j;
        k=dep_var();
        j=indep_var();
        double a,b,c,d,temp1,temp2;
        double step_size=(final_value-values[j])/1000;
        while(i<1000)
        {
            temp1=values[j];
            temp2=values[k];
            a=eval(expr,values);
            values[j]=values[j]+step_size/2;
            values[k]=values[k]+(a*step_size/2);
            b=eval(expr,values);
            values[j]=temp1+step_size/2;
            values[k]=temp2+(b*step_size/2);
            c=eval(expr,values);
            values[j]=temp1+step_size;
            values[k]=temp2+(c*step_size);
            d=eval(expr,values);
            values[k]=temp2+step_size*(a+2*b+2*c+d)/6;
            i++;
        }
        return values[k];
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evaluation, menu);
        return true;
    }
    private static String function_seeker(String token)
    {
        char temp=token.charAt(0);
        switch(temp)
        {
            case 's':if(token.substring(0,3).equals("sin"))
                return "sin";
            case 'c':if(token.substring(0,3).equals("cos"))
                return "cos";
            case 't':if(token.substring(0,3).equals("tan"))
                return "tan";
            case 'e':if(token.substring(0,3).equals("exp"))
                return "exp";
            case 'l':if(token.substring(0,2).equals("ln"))
                return "ln";
            else if(token.substring(0,3).equals("log"))
                return "log";
            case 'a':if(token.substring(0,3).equals("abs"))
                return "abs";
        }
        return "nada";
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    protected void onPause()
    {
        super.onPause();
    }
    protected void onStop()
    {
        super.onStop();
    }
    protected void onResume()
    {
        super.onResume();
    }
    protected void onRestart()
    {
        super.onRestart();
    }
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
