package com.example.sensors;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Set;

public class SensorActivity extends Activity implements SensorEventListener {
    // SENSORS
    private final SensorManager sensorManager;
    private final Sensor accelerometer;
    private final Sensor magnetometer;
    private final Sensor gravity;
    private String accelerometerData = "";
    private String magnetometerData = "";
    private String gravityData = "";

    // WIFI
    private final WifiManager wifiManager;

    // BLUETOOTH
    private final BluetoothManager bluetoothManager;

    @SuppressLint("ServiceCast")
    public SensorActivity(Context context) {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);

        gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);

//WIFI
        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);


//BLUETOOTH
        bluetoothManager = (BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE);
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, gravity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String x, y, z;
        x = Float.toString(event.values[0]);
        y = Float.toString(event.values[1]);
        z = Float.toString(event.values[2]);
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerData = "Accelerometer: x: " + x + ", y: " + y + ", z: " + z;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magnetometerData = "Magnetometer: x: " + x + ", y: " + y + ", z: " + z;
        } else if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            gravityData = "Gravity: x: " + x + ", y: " + y + ", z: " + z;
        }

    }

    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.S)
    public String getData() {
// WIFI
        String wifiData;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();


        int wfrequency;
        int wIpAddress;
        int wSpeed;
        int wifiStandard;

        if (wifiManager != null) {
            if (wifiManager.isWifiEnabled()) {
                wfrequency = wifiInfo.getFrequency();
                wIpAddress = wifiInfo.getIpAddress();
                wSpeed = wifiInfo.getLinkSpeed();
                wifiStandard = wifiInfo.getWifiStandard();

                wifiData = "Frequency [MHz]: " + wfrequency + "\nIP address: " + wIpAddress
                        + "\nSpeed [Mbps]: " + wSpeed + "\nWifi standard: " + wifiStandard;
            } else {
                wifiData = "Turn on wifi.";
            }
        } else {
            wifiData = "No wifi.";
        }


// BLUETOOTH
        String bluetoothData = "";
        int bState = 0;
        String bName = "";
        int bBondedSize;
        Set<BluetoothDevice> bBondedDevices;
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                bName = bluetoothAdapter.getName();
                bState = bluetoothAdapter.getState();
                bBondedDevices = bluetoothAdapter.getBondedDevices();
                bBondedSize = bBondedDevices.size();

                bluetoothData = "Name: " + bName + "\nState: " + bState + "\nnumber of bonded devices: " + bBondedSize;
            } else {
                bluetoothData = "Turn on bluetooth.";
            }
        } else {
            bluetoothData = "No bluetooth.";
        }
        return "SENSORS\n" + accelerometerData + "\n" + magnetometerData + "\n" + gravityData
                + "\n\nWIFI\n" + wifiData + "\n\nBLUETOOTH\n" + bluetoothData;

    }
}

