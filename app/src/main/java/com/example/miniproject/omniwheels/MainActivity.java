package com.example.miniproject.omniwheels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private OutputStream outputStream;
    private InputStream inStream;
    private final static int REQUEST_ENABLE_BT = 100;

    private BluetoothSocket socket = null;

    private final String mac = "67:08:2D:9E:66:0F";
    private boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button up = (Button) findViewById(R.id.up);
        Button down = (Button) findViewById(R.id.down);
        Button left = (Button) findViewById(R.id.left);
        Button right = (Button) findViewById(R.id.right);
        Button topLeft = (Button) findViewById(R.id.topLeft);
        Button topRight = (Button) findViewById(R.id.topRight);
        Button bottomLeft = (Button) findViewById(R.id.bottomLeft);
        Button bottomRight = (Button) findViewById(R.id.bottomRight);

        up.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast t = Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT);
                        t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                        t.show();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        Toast u = Toast.makeText(getApplicationContext(), "STOP UP", Toast.LENGTH_SHORT);
                        u.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                        u.show();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });


        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String s = String.valueOf(connected);
                Toast t = Toast.makeText(getApplicationContext(), "Connected: " + connected, Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FindDevice.class);
                startActivity(intent);
            }
        });

        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext(), "TOP LEFT", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext(), "TOP RIGHT", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        bottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext(), "BOTTOM LEFT", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        bottomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getApplicationContext(), "BOTTOM RIGHT", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connect();
    }

    private void connect() {

        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();














    }
}



























