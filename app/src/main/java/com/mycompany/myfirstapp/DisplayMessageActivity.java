package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class DisplayMessageActivity extends ActionBarActivity {
    public final static String EXTRA_FIRST_VALUE="com.mycompany.myfirstapp.FIRST_VALUE";
    public final static String EXTRA_SECOND_VALUE="com.mycompany.myfirstapp.SECOND_VALUE";
    public final static String EXTRA_EXPRESSION="com.mycompany.myfirstapp.EXPRESSION";
    public final static String EXTRA_MESSAGE="com.mycompany.myfirstapp.MESSAGE";
    public final static String EXTRA_THIRD_VALUE="com.mycompany.myfirstapp.THIRD_VALUE";
    public final static String EXTRA_DEPENDENT="com.mycompany.myfirstapp.DEPENDENT";
    public String RPN;
    public String message;
    public String dep_var;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        message=intent.getStringExtra(MyActivity.EXTRA_EQUATION);
        setContentView(R.layout.activity_display_message);
        RPN=infixtoRPN(message);
        dep_var=intent.getStringExtra(MyActivity.EXTRA_VARIABLE);
        Variables.push(dep_var);
        Create_Var(message);
        if(Variables.search(dep_var)==1)
            Variables.push("x");
        String First_variable=(String)Variables.pop();
        String Second_variable=(String)Variables.pop();
        String dep=an(First_variable,Second_variable);
        TextView textView1=(TextView) findViewById(R.id.first_variable);
        textView1.setTextSize(20);
        textView1.setText(First_variable);
        TextView textView2=(TextView) findViewById(R.id.second_variable);
        textView2.setTextSize(20);
        TextView textView3=(TextView) findViewById(R.id.third_variable);
        textView3.setTextSize(20);
        textView2.setText(Second_variable);
        textView3.setText(dep);

//        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public String an(String First_variable,String Second_variable)
    {
        if(First_variable.equals(dep_var))
            return Second_variable;
        else
            return First_variable;

    }
    private static int LEFT_ASSOC=0;
    private static int RIGHT_ASSOC=1;
    private static final Map<String,int[]> OPERATORS=new HashMap<String,int[]>();
    static{
        OPERATORS.put("+",new int[]{0,LEFT_ASSOC});
        OPERATORS.put("-",new int[]{0,LEFT_ASSOC});
        OPERATORS.put("*",new int[]{5,LEFT_ASSOC});
        OPERATORS.put("/",new int[]{5,LEFT_ASSOC});
        OPERATORS.put("^",new int[]{10,RIGHT_ASSOC});
    }
    private static boolean isOperator(String token)
    {
        return OPERATORS.containsKey(token);
    }
    private static boolean isAssociative(String token,int type)
    {
        if(!isOperator(token))
            throw new IllegalArgumentException("Invalid token:"+token);
        if(OPERATORS.get(token)[1]==type)
            return true;
        else
            return false;
    }
    private static final int cmpPrecedence(String token1,String token2) {
        if (!isOperator(token1) || !isOperator(token2))
            throw new IllegalArgumentException("Invalied tokens: " + token1
                    + " " + token2);
        return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
    }
    public static String infixtoRPN(String expr)
    {
        int pos=0;
        String c="a";
        Stack S=new Stack();
        StringBuilder token=new StringBuilder();
        while(pos<expr.length())
        {
            if(isOperator(Character.toString(expr.charAt(pos))))
            {
                c=Character.toString(expr.charAt(pos));
                pos++;
                while(!S.empty()&&isOperator((String)S.peek()))
                {
                    if((isAssociative(c,LEFT_ASSOC)&&(cmpPrecedence(c,(String)S.peek())<=0))||(isAssociative(c, RIGHT_ASSOC)&&(cmpPrecedence(c,(String)S.peek())<0)))
                    {
                        token.append(S.pop());
                        continue;
                    }
                    break;
                }
                S.push(c);
            }
            else {
                if (!function_seeker(expr.substring(pos)).equals("nada")) {
                    String temp = function_seeker(expr.substring(pos));
                    pos = pos + temp.length();
                    S.push(temp);
                } else {
                    c = Character.toString(expr.charAt(pos));
                    pos++;
                    if (c.equals("(")) {
                        S.push(c);
                    } else if (c.equals(")")) {
                        while (!S.empty() && !S.peek().equals("("))
                            token.append(S.pop());
                        S.pop();
                    } else {
                        token.append(c);
                    }
                }
            }
        }
        while(!S.empty())
            token.append(S.pop());
        return token.toString();
    }
    public static Stack Variables=new Stack();
    public static void Create_Var(String expr) {

        int pos1 = 0, pos2 = 0;
        String temp;
        if (expr.charAt(expr.length() - 1) != ')')
            expr = expr.concat(")");
        while (pos2 < expr.length()) {
            if(Character.isDigit(expr.charAt(pos2)))
            {
                pos2++;
                continue;
            }
            else if (isOperator(Character.toString(expr.charAt(pos2))) || expr.charAt(pos2) == ')' || expr.charAt(pos2) == '(') {
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

/*        switch(id)
        {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                //this.finish();
                return true;
            case R.id.action_settings
                return true
            default:
                return super.onOptionsItemSelected(item);
        }
*/
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    public void sendMessage1(View view)
    {
        Intent intent1=new Intent(this,Evaluation.class);
        EditText editText1=(EditText) findViewById(R.id.edit_message1);
        EditText editText2=(EditText) findViewById(R.id.edit_message2);
        EditText editText3=(EditText) findViewById(R.id.edit_message3);
        String first_value=editText1.getText().toString();
        String second_value=editText2.getText().toString();
        String third_value=editText3.getText().toString();
        intent1.putExtra(EXTRA_FIRST_VALUE,first_value);
        intent1.putExtra(EXTRA_SECOND_VALUE,second_value);
        intent1.putExtra(EXTRA_EXPRESSION,RPN);
        intent1.putExtra(EXTRA_MESSAGE,message);
        intent1.putExtra(EXTRA_THIRD_VALUE,third_value);
        intent1.putExtra(EXTRA_DEPENDENT,dep_var);
        startActivity(intent1);
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
