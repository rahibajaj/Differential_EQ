package com.mycompany.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
//import android.os.Bundle;
import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
//import android.widget.PopupMenu;
import android.widget.PopupWindow;
//import android.widget.Toast;
//import android.support.v7.internal.widget.ActionBarOverlayLayout;


public class MyActivity extends ActionBarActivity {

    public final static String EXTRA_EQUATION = "com.mycompany.myfirstapp.EQUATION";
    public final static String EXTRA_VARIABLE = "com.mycompany.myfirstapp.VARIABLE";


    Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        Button  instructions_popup;
        instructions_popup = (Button) findViewById(R.id.instructions_popup);

        instructions_popup.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0){

                if(p!=null)
                {
                    showPopup(MyActivity.this, p);
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        Button button = (Button) findViewById(R.id.instructions_popup);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    private void showPopup(final Activity context, Point p) {
        int popupWidth = 800;
        int popupHeight = 1000;


        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.instructions);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.instruct_popup, viewGroup);

        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        popup.setBackgroundDrawable(new BitmapDrawable());

        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });
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
