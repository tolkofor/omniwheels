package com.example.miniproject.omniwheels;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
        Button stop = (Button) findViewById(R.id.stop);
        Button spinLeft = (Button) findViewById(R.id.spinLeft);
        Button spinRight = (Button) findViewById(R.id.spinRight);

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast t = Toast.makeText(getApplicationContext(), "UP", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            }
        });

        topLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        topRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bottomLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bottomRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!connected){
                    Intent intent = new Intent(getApplicationContext(), FindDevice.class);
                    connected = true; //for now will have to restart app if it disconnects
                    startActivity(intent);
                }
                else{
                    //stop
                }

            }
        });

        spinLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        spinRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}



























