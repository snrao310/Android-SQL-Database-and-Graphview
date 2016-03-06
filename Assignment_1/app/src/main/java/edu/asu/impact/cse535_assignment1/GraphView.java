package edu.asu.impact.cse535_assignment1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;

/**
 * GraphView creates a scaled line or bar graph with x and y axis labels.
 * @author Arno den Hond
 *
 */
public class GraphView extends View {

    public static boolean BAR = false;
    public static boolean LINE = true;

    private Paint paint;
    private float[] value1;
    private float[] value2;
    private float[] value3;
    private String[] horlabels;
    private String[] verlabels;
    private String title;
    private boolean type;

    public GraphView(Context context, float[] value1, float[] value2, float[] value3, String title, String[] horlabels, String[] verlabels, boolean type) {
        super(context);
        if (value1 == null)
            value1 = new float[0];

        else
            this.value1 = value1;

        if (value2 == null)
            value2= new float[0];

        else
            this.value2 = value2;

        if (value3 == null)
            value3 = new float[0];

        else
            this.value3 = value3;



        if (title == null)
            title = "";
        else
            this.title = title;
        if (horlabels == null)
            this.horlabels = new String[0];
        else
            this.horlabels = horlabels;
        if (verlabels == null)
            this.verlabels = new String[0];
        else
            this.verlabels = verlabels;
        this.type = type;
        paint = new Paint();
    }

    public void setValue1(float[] newValue1, float[] newValue2, float[] newValue3)
    {
        this.value1 = newValue1;
        this.value2=newValue2;
        this.value3=newValue3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float border = 20;
        float horstart = border * 2;
        float height = getHeight();
        float width = getWidth() - 1;
        float max = 10;//getMax();
        float min = -10;//getMin();
        float diff = max - min;
        float graphheight = height - (2 * border);
        float graphwidth = width - (2 * border);


        paint.setTextAlign(Align.LEFT);
        int vers = verlabels.length - 1;
        for (int i = 0; i < verlabels.length; i++) {
            paint.setColor(Color.DKGRAY);
            float y = ((graphheight / vers) * i) + border;
            canvas.drawLine(horstart, y, width, y, paint);
            paint.setColor(Color.WHITE);
            canvas.drawText(verlabels[i], 0, y, paint);
        }
        int hors = horlabels.length - 1;
        for (int i = 0; i < horlabels.length; i++) {
            paint.setColor(Color.DKGRAY);
            float x = ((graphwidth / hors) * i) + horstart;
            canvas.drawLine(x, height - border, x, border, paint);
            paint.setTextAlign(Align.CENTER);
            if (i==horlabels.length-1)
                paint.setTextAlign(Align.RIGHT);
            if (i==0)
                paint.setTextAlign(Align.LEFT);
            paint.setColor(Color.WHITE);
            canvas.drawText(horlabels[i], x, height - 4, paint);
        }

        paint.setTextAlign(Align.CENTER);
        canvas.drawText(title, (graphwidth / 2) + horstart, border - 4, paint);

        if (max != min) {
            paint.setColor(Color.LTGRAY);
            if (type == BAR) {
                float datalength = value1.length;
                float colwidth = (width - (2 * border)) / datalength;
                for (int i = 0; i < value1.length; i++) {
                    float val = value1[i] - min;
                    float rat = val / diff;
                    float h = graphheight * rat;
                    canvas.drawRect((i * colwidth) + horstart, (border - h) + graphheight, ((i * colwidth) + horstart) + (colwidth - 1), height - (border - 1), paint);
                }
            } else {
                float datalength = value1.length;
                float colwidth = (width - (2 * border)) / datalength;
                float halfcol = colwidth / 2;
                float lasth = 0;
                for (int i = 0; i < value1.length; i++) {
                    float val = value1[i] - min;
                    float rat = val / diff;
                    float h = graphheight * rat;
                    if (i >= 0)
                        paint.setColor(Color.GREEN);
                    paint.setStrokeWidth(2.0f);

                    canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
                    lasth = h;
                }


                 datalength = value2.length;
                 colwidth = (width - (2 * border)) / datalength;
                 halfcol = colwidth / 2;
                 lasth = 0;
                for (int i = 0; i < value2.length; i++) {
                    float val = value2[i] - min;
                    float rat = val / diff;
                    float h = graphheight * rat;
                    if (i >= 0)
                        paint.setColor(Color.RED);
                    paint.setStrokeWidth(2.0f);

                    canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
                    lasth = h;
                }



                datalength = value3.length;
                colwidth = (width - (2 * border)) / datalength;
                halfcol = colwidth / 2;
                lasth = 0;
                for (int i = 0; i < value3.length; i++) {
                    float val = value3[i] - min;
                    float rat = val / diff;
                    float h = graphheight * rat;
                    if (i >= 0)
                        paint.setColor(Color.YELLOW);
                    paint.setStrokeWidth(2.0f);

                    canvas.drawLine(((i - 1) * colwidth) + (horstart + 1) + halfcol, (border - lasth) + graphheight, (i * colwidth) + (horstart + 1) + halfcol, (border - h) + graphheight, paint);
                    lasth = h;
                }
            }
        }
    }

    private float getMax() {
        float largest = Integer.MIN_VALUE;
        for (int i = 0; i < value1.length; i++)
            if (value1[i] > largest)
                largest = value1[i];

        //largest = 3000;
        return largest;
    }

    private float getMin() {
        float smallest = Integer.MAX_VALUE;
        for (int i = 0; i < value1.length; i++)
            if (value1[i] < smallest)
                smallest = value1[i];

        //smallest = 0;
        return smallest;
    }

}