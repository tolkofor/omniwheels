package com.example.miniproject.omniwheels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import static android.bluetooth.BluetoothAdapter.STATE_CONNECTED;

public class FindDevice extends AppCompatActivity {

    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice microbit;
    private final int REQUEST_ENABLE_BT = 100;
    private ArrayAdapter<String> mArrayAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == REQUEST_ENABLE_BT) {
            //BT turned on successfully
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_device);

        // Make ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_devices, // The name of the layout ID.
                R.id.list_item_devices_textview, // The ID of the textview to populate.
                new ArrayList<String>());

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) findViewById(R.id.listview_devices);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String deviceInfo = mArrayAdapter.getItem(position);
            }
        });
        mArrayAdapter.add("Test list item");

        // Get BT adapter and turn on BT
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //Query devices that may already be paired before needing to do Device Discovery
        this.pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                this.microbit = device; //should only be one paired device
            }
        }

        // Device Discovery - Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        //Connect as client
        //BluetoothSocket socket = this.microbit.createRfcommSocketToServiceRecord(

        microbit.connectGatt(this, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

                Toast t = Toast.makeText(getApplicationContext(), "CONNECTION STATE CHANGE", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                t.show();

                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    t = Toast.makeText(getApplicationContext(), "CONNECTED", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                    t = Toast.makeText(getApplicationContext(), "DISCONNECTED", Toast.LENGTH_SHORT);
                    t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
                    t.show();
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();









    }
}





























