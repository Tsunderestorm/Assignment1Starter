package edu.uw.ubicomplab.accelapp;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class StepCounter {
    private int windowSize = 70;
    private DescriptiveStatistics accelWindow = new DescriptiveStatistics(windowSize);
    public int stepCount = 0;

    public boolean addDataPoint(float time, float ax, float ay, float az) {
        // Calculate magnitude and add to window
        double amag = Math.sqrt(ax*ax + ay*ay + az*az);
        accelWindow.addValue(amag);

        return false;
    }

    public void reset() {
        stepCount = 0;
    }
}
