package edu.uw.ubicomplab.accelapp;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // GLOBALS
    // Accelerometer
    private LineGraphSeries<DataPoint> timeAccelX = new LineGraphSeries<>();
    // Graph
    private GraphView graph1;
    private int graphXBounds = 100;
    private int graphYBounds = 50;
    private int graphColor[] = {Color.argb(255,244,170,50),
            Color.argb(255, 60, 175, 240),
            Color.argb(225, 50, 220, 100)};
    private static final int MAX_DATA_POINTS_UI_IMU = 100; // Adjust to show more points on graph
    public int accelGraphXTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the graphs
        initializeGraph();

        // Get the sensors
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelGraphXTime += 1;

            // Add the original data to the graph
            DataPoint dataPointAccX = new DataPoint(accelGraphXTime, event.values[0]);
            timeAccelX.appendData(dataPointAccX, true, MAX_DATA_POINTS_UI_IMU);


            // Advance the graph
            graph1.getViewport().setMinX(accelGraphXTime-graphXBounds);
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
        graph1.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph1.getViewport().setXAxisBoundsManual(true);
        graph1.getViewport().setYAxisBoundsManual(true);
        graph1.getViewport().setMinX(0);
        graph1.getViewport().setMaxX(graphXBounds);
        graph1.getViewport().setMinY(-graphYBounds);
        graph1.getViewport().setMaxY(graphYBounds);
        timeAccelX.setColor(graphColor[0]);
        timeAccelX.setThickness(10);
        graph1.addSeries(timeAccelX);
    }
}
