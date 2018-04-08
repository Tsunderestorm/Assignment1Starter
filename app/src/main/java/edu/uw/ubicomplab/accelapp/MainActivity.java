package edu.uw.ubicomplab.accelapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // GLOBALS
    // Accelerometer
    private LineGraphSeries<DataPoint> timeAccelX = new LineGraphSeries<>();

    // Step counter
    private StepCounter stepCounter = new StepCounter();
    
    // Graph
    private GraphView graph1;
    private static final int GRAPH_X_BOUNDS = 100; // Adjust to show more points on graph
    private static final int GRAPH_Y_BOUNDS = 50;
    private int graphColor[] = {Color.argb(255,244,170,50),
            Color.argb(255, 60, 175, 240),
            Color.argb(225, 50, 220, 100)};
    public int accelGraphXTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the graphs
        initializeGraph();

        // Get the sensors
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        // Alternative way to initialize button
//        Button specialButton = findViewById(R.id.button1);
//        specialButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("ALEX", "YAY");
//            }
//        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelGraphXTime += 1;

            // Add the original data to the graph
            DataPoint dataPointAccX = new DataPoint(accelGraphXTime, event.values[0]);
            timeAccelX.appendData(dataPointAccX, true, GRAPH_X_BOUNDS);

            // Add the original data to the StepCounter object
            stepCounter.addDataPoint(event.values[0],
                    event.values[1], event.values[1], event.values[2]);

            // Advance the graph
            graph1.getViewport().setMinX(accelGraphXTime - GRAPH_X_BOUNDS);
            graph1.getViewport().setMaxX(accelGraphXTime);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Initializes the graph that will show unfiltered data
     */
    public void initializeGraph() {
        graph1 = findViewById(R.id.graph1);
        graph1.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph1.setBackgroundColor(Color.TRANSPARENT);
        graph1.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph1.getGridLabelRenderer().setVerticalLabelsVisible(true);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setYAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(GRAPH_X_BOUNDS);
        graph1.getViewport().setMinY(-GRAPH_Y_BOUNDS);
        graph1.getViewport().setMaxY(GRAPH_Y_BOUNDS);
        timeAccelX.setColor(graphColor[0]);
        timeAccelX.setThickness(10);
        graph1.addSeries(timeAccelX);
    }

    public void button1Action(View v) {
        // Log statement
        Log.d("ALEX", "This is a log message");

        // Toast statement
        Toast.makeText(this,
                "This is a toast message", Toast.LENGTH_SHORT).show();

        // Example TimerTask
        Timer startTimer = new Timer();
        TimerTask startTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "This is a delayed toast message", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        // One-time task after a delay
        startTimer.schedule(startTask, 1000);
    }
}
