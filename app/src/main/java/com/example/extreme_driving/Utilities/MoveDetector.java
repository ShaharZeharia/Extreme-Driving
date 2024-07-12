package com.example.extreme_driving.Utilities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.extreme_driving.Interfaces.MoveCallback;

public class MoveDetector {
    private static final float MOVE_THRESHOLD = 2.0f;
    private static final float FORWARD_BACKWARD_THRESHOLD = 0.5f;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener sensorEventListener;

    private long timestamp = 0l;
    private final MoveCallback moveCallback;

    public MoveDetector(Context context, MoveCallback moveCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.moveCallback = moveCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                checkMovement(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // pass
            }
        };
    }

    private void checkMovement(float x, float y) {
        if (System.currentTimeMillis() - timestamp > 200) {
            timestamp = System.currentTimeMillis();

            if (x > MOVE_THRESHOLD && moveCallback != null) {
                moveCallback.onMoveLeft();
            } else if (x < -MOVE_THRESHOLD && moveCallback != null) {
                moveCallback.onMoveRight();
            }
            if (y > FORWARD_BACKWARD_THRESHOLD && moveCallback != null) {
                moveCallback.onMoveBackward();
            } else if (y < -FORWARD_BACKWARD_THRESHOLD && moveCallback != null) {
                moveCallback.onMoveForward();
            }
        }
    }

    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
