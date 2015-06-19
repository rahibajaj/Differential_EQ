package com.mycompany.myfirstapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_EQUATION = "com.mycompany.myfirstapp.EQUATION";
    public final static String EXTRA_VARIABLE = "com.mycompany.myfirstapp.VARIABLE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }
    public String equation_generator(String equation)
    {
        int pos=0;
        while(equation.charAt(pos)!='=')
            pos++;
        return equation.substring(pos+1);
    }
    public String dependent_var(String equation)
    {
        int pos=0;
        while(equation.charAt(pos)!='\'')
        pos++;
        return equation.substring(0,pos);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
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
    public void sendMessage(View view){
        Intent intent=new Intent(this,DisplayMessageActivity.class);
        EditText editText=(EditText) findViewById(R.id.edit_message);
        String message=editText.getText().toString();
        String equation=equation_generator(message);
        String dep_var=dependent_var(message);
        intent.putExtra(EXTRA_EQUATION,equation);
        intent.putExtra(EXTRA_VARIABLE,dep_var);
        startActivity(intent);
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
