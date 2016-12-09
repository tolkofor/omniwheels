package com.example.miniproject.omniwheels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class FindDevice extends AppCompatActivity {

    private Set<BluetoothDevice> pairedDevices;
    private BluetoothDevice microbit;
    private final int REQUEST_ENABLE_BT = 100;
    private ArrayAdapter<String> mArrayAdapter;

    // Create a BroadcastReceiver for ACTION_FOUND (bt device found so add to list)
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

    // Used to check if enabling Bluetooth was successful
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

        //Connect to device
        BluetoothGatt gatt = microbit.connectGatt(this, false, gattCallback);

        //Instantiate private class
        //ConnectThread ct = new ConnectThread(microbit, mBluetoothAdapter);

    }  //end onCreate

    private  final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            Toast t = Toast.makeText(getApplicationContext(),
                    "Connection state changed", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 0, 0);
            t.show();
        }
    };

    /**
     * Connecting as a client - Inner Class
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private final BluetoothAdapter mBluetoothAdapter;

        public ConnectThread(BluetoothDevice device, BluetoothAdapter bluetoothAdapter) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            this.mmDevice = device;
            this.mBluetoothAdapter = bluetoothAdapter;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(
                        (UUID.fromString("what do I put here?")));
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            //manageConnectedSocket(mmSocket);
        }


        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }
}





























