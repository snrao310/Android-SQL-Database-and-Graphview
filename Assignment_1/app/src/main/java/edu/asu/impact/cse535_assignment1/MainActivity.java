package edu.asu.impact.cse535_assignment1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    GraphView graphView;
    SQLiteDatabase db;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=getIntent();
        name = intent.getStringExtra("TableName");


        String parts[]=name.split("_");
        EditText pname_field= (EditText) findViewById(R.id.p_name);
        EditText pid_field= (EditText) findViewById(R.id.p_id);
        EditText page_field= (EditText) findViewById(R.id.p_age);
        RadioButton male=(RadioButton)  findViewById(R.id.male);
        RadioButton female=(RadioButton)  findViewById(R.id.female);
        RadioGroup psex= (RadioGroup)  findViewById(R.id.p_sex);
        pname_field.setText(parts[0]);
        pid_field.setText(parts[1]);
        page_field.setText(parts[2]);
        //Toast.makeText(this,parts[3],Toast.LENGTH_LONG).show();

        if(parts[3].equals("Male")) {
            male.setChecked(true);
        }
        else
            female.setChecked(true);

        float[] values = new float[15];
        Arrays.fill(values,-10);
        String[] verlabels = new String[] { "2000", "1500", "1000", "500", "0" };
        String[] horlabels = new String[] { "2000", "2200", "2400", "2600", "2800", "3000", "3200", "3400", "3600", "3800", "4000",
                "4200", "4400", "4600", "4800"};

        graphView = new GraphView(MainActivity.this, values , values,values,"GraphViewDemo",horlabels, verlabels, GraphView.LINE);
        LinearLayout ll= (LinearLayout) findViewById(R.id.theGraph);
        ll.addView(graphView);


        //calling service which updates database with accelerometer value.
        Intent accel=new Intent(this, AccelDB.class);
        Bundle b=new Bundle();
        b.putString("TableName",name);
        accel.putExtras(b);
        startService(accel);
    }

    public void onRun(View v){
        EditText pid_field = (EditText) findViewById(R.id.p_id);
        EditText pname_field=(EditText) findViewById(R.id.p_name);
        EditText page_field=(EditText) findViewById(R.id.p_age);
        RadioButton male=(RadioButton) findViewById(R.id.male);
        RadioButton female=(RadioButton) findViewById(R.id.female);
        RadioGroup psex=(RadioGroup) findViewById(R.id.p_sex);

        String pid_str = pid_field.getText().toString();
        String pname_str = pname_field.getText().toString();
        String page_str = page_field.getText().toString();
        if(pid_str.matches("") || pname_str.matches("") || page_str.matches("")|| (psex.getCheckedRadioButtonId()==-1)) {
            Toast.makeText(MainActivity.this, "Enter details before running", Toast.LENGTH_LONG).show();
        }
        else {

            db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/myDB", null);

            int pid = Integer.parseInt(pid_str);
            int page = Integer.parseInt(page_str);


            try {
                Cursor resultx = db.rawQuery("Select x from " + name +" order by Timestamp desc limit 10;",null);
                resultx.moveToFirst();

                Cursor resulty = db.rawQuery("Select y from " + name +" order by Timestamp desc limit 10;",null);
                resulty.moveToFirst();

                Cursor resultz = db.rawQuery("Select z from " + name +" order by Timestamp desc limit 10;",null);
                resultz.moveToFirst();


                float[] resultsx = new float[10];
                float[] resultsy = new float[10];
                float[] resultsz = new float[10];
                for(int i=0;i<10;i++)
                {
                    resultsx[i]=Float.parseFloat(resultx.getString(0));
                    resultsy[i]=Float.parseFloat(resulty.getString(0));
                    resultsz[i]=Float.parseFloat(resultz.getString(0));
                    resultx.moveToNext();
                    resulty.moveToNext();
                    resultz.moveToNext();
                }



                /*String res1 = result.getString(0);
                String[] res2 = res1.split(",");
                float[] results = new float[res2.length];
                for (int i = 0; i < res2.length; i++) {
                    results[i] = Float.parseFloat(res2[i]);
                }*/

                //System.out.print(results[0]);
                graphView.setValue1(resultsx,resultsy,resultsz);
                LinearLayout ll = (LinearLayout) findViewById(R.id.theGraph);
                ll.removeAllViews();
                ll.addView(graphView);
            } catch(Exception ex) {
                Toast.makeText(MainActivity.this, "Details not found! Please try again!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onStop(View v){
        float[] values = new float[15];
        Arrays.fill(values,-10);
        graphView.setValue1(values,values,values);
        LinearLayout ll= (LinearLayout) findViewById(R.id.theGraph);
        ll.removeAllViews();
        ll.addView(graphView);
    }



}
