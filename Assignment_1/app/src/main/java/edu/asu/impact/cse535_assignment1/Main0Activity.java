package edu.asu.impact.cse535_assignment1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import edu.asu.impact.cse535_assignment1.R;

public class Main0Activity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);


    }


    public void onEnter(View v)
    {
        String name;
        String id;
        String age;
        String sex;

        EditText pname_field= (EditText) findViewById(R.id.p_name);
        EditText pid_field= (EditText) findViewById(R.id.p_id);
        EditText page_field= (EditText) findViewById(R.id.p_age);
        RadioButton male=(RadioButton)  findViewById(R.id.male);
        RadioButton female=(RadioButton)  findViewById(R.id.female);
        RadioGroup psex= (RadioGroup)  findViewById(R.id.p_sex);

        name=pname_field.getText().toString();
        name+="_"+pid_field.getText().toString();
        name+="_"+page_field.getText().toString();
        name+="_";
        RadioButton temp=(RadioButton)psex.findViewById(psex.getCheckedRadioButtonId());
        name+=temp.getText().toString();


        try {
            db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/myDB", null);
            db.execSQL("Drop Table if exists " + name);
            db.execSQL("Create Table " + name + "(Timestamp DateTime Default Current_Timestamp, x integer, y integer, z integer);");
            Intent nextac=new Intent(this, MainActivity.class);
            nextac.putExtra("TableName", name);
            startActivity(nextac);
        }
        catch (Exception e){
            Toast.makeText(Main0Activity.this, "Error Creating Database",
                    Toast.LENGTH_SHORT).show();
        }
    }

}
