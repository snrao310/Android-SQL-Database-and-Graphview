package com.mcassignments.a2.cse535_assignment2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Map<Integer, float[]> id_map=new HashMap<Integer, float[]>();

    private String[] names = {  "Tom Hanks", "Leonardo DiCaprio", "Brad Pitt",
                                "George Clooney", "Ben Affleck", "Henry Cavill",
                                "Matt Dammon", "Hugh Jackman", "Jennifer Lawrence",
                                "Amanda Seyfried", "Amber Heard", "Julia Roberts",
                                "Sandra Bullock", "Angelina Jolie", "Scarlett Johanson"};
    private int[] age = { 57, 45, 42, 56, 44,
                          32, 34, 44, 27, 28,
                          26, 45, 50, 39, 31 };
    GraphView graphView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        float[] values = new float[15];

        String[] verlabels = new String[] { "2000", "1500", "1000", "500", "0" };

        String[] horlabels = new String[] { "2000", "2200", "2400", "2600", "2800",
                                            "3000", "3200", "3400", "3600", "3800",
                                            "4000", "4200", "4400", "4600", "4800" };

        graphView = new GraphView(MainActivity.this, values , "GraphViewDemo",horlabels, verlabels, GraphView.LINE);
        LinearLayout ll= (LinearLayout) findViewById(R.id.theGraph);
        ll.addView(graphView);

        try {
            db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/myDB", null);
            db.execSQL(" DROP TABLE IF EXISTS PATIENT ");
            db.execSQL(" CREATE TABLE IF NOT EXISTS PATIENT " +
                       " (Patient_Name VARCHAR, Patient_ID INTEGER PRIMARY KEY, " +
                       " Patient_Age INTEGER, Patient_Sex INTEGER, Data TEXT);");
            db.execSQL(" CREATE TABLE IF NOT EXISTS Name_ID_Age_Sex (" +
                       " TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP," +
                       " X_VALUES INTEGER, Y_VALUES INTEGER, Z_VALUES INTEGER )");

            for (int i = 0; i < 15; i++) {
                String value = new String();
                Random rgen = new Random();
                for (int j = 0; j < 15; j++) {
                    value += "," + (rgen.nextFloat() * 2000);
                }
                value = value.substring(1, value.length());

                db.execSQL("INSERT INTO PATIENT VALUES( \"" + names[i] + "\"," +
                        i + "," + age[i] + "," + (i > 7 ? 2 : 1) + ",\"" + value + "\");");
                
            }
        } catch(Exception ex) {
            Toast.makeText(getApplicationContext(),"Some error occurred! Please try again",
                    Toast.LENGTH_SHORT).show();
        }
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
            int pid = Integer.parseInt(pid_str);
            int page = Integer.parseInt(page_str);

            String temp="Select Data from PATIENT where "+
                    "Patientname=\'"+pname_str+"\' AND "+"PatientID=" +pid + " AND "+
                    "Patientage="+page+" AND "+"Patientsex="+(pid>7?2:1)+";";

            try {
                Cursor result = db.rawQuery("Select Data from PATIENT where " +
                        "Patientname=\"" + pname_str + "\" AND " + "PatientID=" + pid + " AND " +
                        "Patientage=" + page + " AND " + "Patientsex=" + (pid > 7 ? 2 : 1) + ";", null);
                result.moveToFirst();


                String res1 = result.getString(0);
                String[] res2 = res1.split(",");
                float[] results = new float[res2.length];
                for (int i = 0; i < res2.length; i++) {
                    results[i] = Float.parseFloat(res2[i]);
                }

                System.out.print(results[0]);
                graphView.setValues(results);
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
        graphView.setValues(values);
        LinearLayout ll= (LinearLayout) findViewById(R.id.theGraph);
        ll.removeAllViews();
        ll.addView(graphView);
    }
}
