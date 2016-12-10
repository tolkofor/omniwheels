package com.example.miniproject.omniwheels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.os.SystemClock.sleep;

public class FindDevice extends AppCompatActivity {

    private final String LOG_TAG = FindDevice.class.getSimpleName();

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
        sleep(1000);

        String uuid = "E95D7B77-251D-470A-A062-FA1922DFA9A8";
        UUID service = null;
        try {
            service = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        boolean begin = gatt.beginReliableWrite();
        Log.d(LOG_TAG, "Begin write is: " + begin);

        BluetoothGattCharacteristic characteristic =
                new BluetoothGattCharacteristic(service, 0b1111100000111110000011111, //LED matrix
                        BluetoothGattCharacteristic.PERMISSION_WRITE);

        sleep(1000);
        boolean write = gatt.writeCharacteristic(characteristic);
        Log.d(LOG_TAG, "Do write is: " + write);

        sleep(1000);
        boolean execute =  gatt.executeReliableWrite();
        Log.d(LOG_TAG, "Execute write is: " + execute);

    }  //end onCreate



    private  final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
             Log.d(LOG_TAG, "Connection state is: " + newState);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(LOG_TAG, "onCharacteristicWrite is: " + status);
        }
    };
}





























