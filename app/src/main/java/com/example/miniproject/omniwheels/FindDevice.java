package com.example.miniproject.omniwheels;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.os.SystemClock.sleep;

public class FindDevice extends AppCompatActivity {

    private final String LOG_TAG = FindDevice.class.getSimpleName();

    public static String DEVICE_INFO_SERVICE = "00001800-0000-1000-8000-00805F9B34FB";

    public static String LEDSERVICE_SERVICE_UUID = "E95DD91D-251D-470A-A062-FA1922DFA9A8";
    public static String LEDMATRIXSTATE_CHARACTERISTIC_UUID = "E95D7B77-251D-470A-A062-FA1922DFA9A8";
    public static String LEDTEXT_CHARACTERISTIC_UUID = "E95D93EE-251D-470A-A062-FA1922DFA9A8";
    public static String SCROLLINGDELAY_CHARACTERISTIC_UUID = "E95D0D2D-251D-470A-A062-FA1922DFA9A8";

    public static String UARTSERVICE_SERVICE_UUID =    "6E400001-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String UART_RX_CHARACTERISTIC_UUID = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E";
    public static String UART_TX_CHARACTERISTIC_UUID = "6E400003-B5A3-F393-E0A9-E50E24DCCA9E";

    private UUID serviceUUID;
    private BluetoothDevice microbit;
    private final int REQUEST_ENABLE_BT = 100;
    private ArrayAdapter<String> mArrayAdapter;

    /**
     * Create a BroadcastReceiver for ACTION_FOUND ( bt device found so add to list )
     */

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

    /**
     * Used to check if enabling Bluetooth was successful
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == REQUEST_ENABLE_BT) {
            //BT turned on successfully
        }
    }

    /**
     * Setup array adapter and listview, query devices and populate list
     */
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
                Log.v(LOG_TAG, "Item clicked: " + mArrayAdapter.getItem(position));
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
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                this.microbit = device; //should only be one paired device
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Thread t = new Thread(){
            @Override
            public void run() {
                doBluetoothStuff();
            }
        };
        t.start();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    /**
     * Unregister broadcast receiver
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private  final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
             Log.d(LOG_TAG, "Connection state is: " + newState);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.d(LOG_TAG, "onCharacteristicWrite is: " + status);
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            if (status == BluetoothGatt.GATT_SUCCESS) {


                Log.d(LOG_TAG, "Display Gatt services *******************************************************************************************");

                // List Services
                List<BluetoothGattService> services = gatt.getServices();
                for (BluetoothGattService s: services) {
                    Log.d(LOG_TAG, "Gatt service is: " + s.getUuid());

                    if (s.getUuid().equals(serviceUUID)){
                        //service is UART
                        Log.d(LOG_TAG, "Service above is UART\n");
                    }

                    //for each service list characteristics
                    List<BluetoothGattCharacteristic> chars = s.getCharacteristics();
                    for (BluetoothGattCharacteristic c: chars) {
                        Log.d(LOG_TAG, "Gatt characteristic is: " + c.getUuid());
                    }
                    Log.d(LOG_TAG, "End of this service characteristics ***************************************************************************");
                }
                Log.d(LOG_TAG, "End of services ***********************************************************************************************");
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {

            Log.i(LOG_TAG, "Characteristic" + characteristic.getUuid() + " read");
        }
    };



    private void doBluetoothStuff() {

        Log.d(LOG_TAG, "Call doBluetoothStuff");

        // Device Discovery - Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

        // Make UUIDs
        UUID characteristicUUID = null;
        try {
            this.serviceUUID = UUID.fromString(UARTSERVICE_SERVICE_UUID);
            characteristicUUID = UUID.fromString(UART_RX_CHARACTERISTIC_UUID);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        // Make Value
        String s = "alligator";
        byte[] value = new byte[10];
        try {
            value = s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Connect to device
        BluetoothGatt gatt = microbit.connectGatt(this, false, gattCallback);
        sleep(1000);

        // Discover services before you can use them
        gatt.discoverServices();
    }
}





























