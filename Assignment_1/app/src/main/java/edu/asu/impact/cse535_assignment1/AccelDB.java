package edu.asu.impact.cse535_assignment1;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

public class AccelDB extends Service implements SensorEventListener{

    private SensorManager accelManage;
    private Sensor senseAccel;

    float x[]=new float[128];
    float y[]=new float[128];
    float z[]=new float[128];

    SQLiteDatabase db;
    String name;
    long lasttime;
    int entries=0;


    public void onSensorChanged(SensorEvent event){
        Sensor  sensor= event.sensor;
        if(sensor.getType()==Sensor.TYPE_ACCELEROMETER){
            long newtime=System.currentTimeMillis();
            if((newtime - lasttime)>=1000){
                //Toast.makeText(this, Long.toString(lasttime), Toast.LENGTH_LONG).show();
                db.execSQL("Insert into "+name+"(x,y,z) values ("+ event.values[0]+","+event.values[1]+","+event.values[2]+");");
                lasttime=newtime;
                entries++;
            }
            if(entries>10){
                db.execSQL("Delete from "+name+" WHERE Timestamp In (SELECT Min(Timestamp) FROM " + name+");");
                entries--;
            }
        }
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy){}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public int onStartCommand(Intent intent, int flags, int startid)
    {
        lasttime= System.currentTimeMillis();
        Bundle b=intent.getExtras();
        name= b.getString("TableName");
        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory() + "/myDB", null);
        //Toast.makeText(this, "Onstartcommand", Toast.LENGTH_LONG).show();
        accelManage=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        senseAccel=accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this,senseAccel,SensorManager.SENSOR_DELAY_NORMAL);
        return START_NOT_STICKY;
    }
}
