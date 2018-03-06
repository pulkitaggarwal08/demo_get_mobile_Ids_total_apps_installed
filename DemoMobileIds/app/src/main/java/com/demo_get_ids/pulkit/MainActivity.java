package com.demo_get_ids.pulkit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo_get_ids.pulkit.activities.TotalAppsInstalledInfoActivity;
import com.demo_get_ids.pulkit.utils.TelephonyInfo;
import com.demo_get_ids.pulkit.utils.Utils;

public class MainActivity extends AppCompatActivity {

    private TextView tv_device_id, tv_sim_serial, tv_ip_address, tv_wifi_name, tv_wifi_link_speed, tv_wifi_mac_address,
            tv_imei_number, tv_battery_status;

    private String android_id, wifi_name, imeiSerialNumber, wlan0_wifi_address, eth0_wifi_address;
    private int ip_address, wifi_link_speed;
    private String ip_address0, ip_address1;
    private Button btn_total_apps_installed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findIds();
        init();

    }

    private void findIds() {

        tv_device_id = (TextView) findViewById(R.id.tv_device_id);
        tv_sim_serial = (TextView) findViewById(R.id.tv_sim_serial);
        tv_ip_address = (TextView) findViewById(R.id.tv_ip_address);
        tv_wifi_name = (TextView) findViewById(R.id.tv_wifi_name);
        tv_wifi_link_speed = (TextView) findViewById(R.id.tv_wifi_link_speed);
        tv_wifi_mac_address = (TextView) findViewById(R.id.tv_wifi_mac_address);
        tv_imei_number = (TextView) findViewById(R.id.tv_imei_number);
        tv_battery_status = (TextView) findViewById(R.id.tv_battery_status);
        btn_total_apps_installed = (Button) findViewById(R.id.btn_total_apps_installed);
    }

    private void init() {

        device_id();
        simSerial();
        ipAddress();
        wifiName();
        wifiSpeed();
        wifiMacAddress();
        imeiNumber();
        batteryStatus();

        btn_total_apps_installed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TotalAppsInstalledInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void device_id() {

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        tv_device_id.setText(android_id);
        tv_device_id.setTextColor(Color.parseColor("#3F51B5"));
    }

    private void simSerial() {

        tv_sim_serial.setText(
                "SERIAL: " + Build.SERIAL + "\n" +
                        "MODEL: " + Build.MODEL + "\n" +
                        "Brand: " + Build.BRAND + "\n" +
                        "Baseband Version: " + Build.getRadioVersion() + "\n" +
                        "Build Number: " + Build.DISPLAY + "\n" +
                        "Version Code: " + Build.VERSION.RELEASE);

        tv_sim_serial.setTextColor(Color.parseColor("#3F51B5"));
    }

    private void ipAddress() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        ip_address = wifiInfo.getIpAddress();

        @SuppressLint("DefaultLocale")
        String ipAddress = String.format("%d.%d.%d.%d",
                (ip_address & 0xff),
                (ip_address >> 8 & 0xff),
                (ip_address >> 16 & 0xff),
                (ip_address >> 24 & 0xff));

        ip_address0 = Utils.getIPAddress(true);
        ip_address1 = "\n" + Utils.getIPAddress(false);

        tv_ip_address.setText(ip_address0 + ip_address1);
        tv_ip_address.setTextColor(Color.parseColor("#3F51B5"));
    }

    private void wifiName() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        wifi_name = wifiInfo.getSSID();

        tv_wifi_name.setText(wifi_name);
        tv_wifi_name.setTextColor(Color.parseColor("#3F51B5"));
    }

    private void wifiSpeed() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        wifi_link_speed = wifiInfo.getLinkSpeed();

        tv_wifi_link_speed.setText(String.valueOf(wifi_link_speed) + WifiInfo.LINK_SPEED_UNITS);
        tv_wifi_link_speed.setTextColor(Color.parseColor("#3F51B5"));
    }

    private void wifiMacAddress() {

        if (Utils.getMACAddress("wlan0") == "" || Utils.getMACAddress("wlan0") == null) {

            wlan0_wifi_address = " wlan0 : " + null;
            eth0_wifi_address = "\n eth0: " + Utils.getMACAddress("eth0");
        }
        if (Utils.getMACAddress("eth0") == "" || Utils.getMACAddress("eth0") == null) {

            wlan0_wifi_address = " wlan0 : " + Utils.getMACAddress("wlan0");
            eth0_wifi_address = "\n eth0: " + null;
        } else {

            wlan0_wifi_address = " wlan0 : " + Utils.getMACAddress("wlan0");
            eth0_wifi_address = "\n eth0: " + Utils.getMACAddress("eth0");
        }

        tv_wifi_mac_address.setText(wlan0_wifi_address + eth0_wifi_address);
        tv_wifi_mac_address.setTextColor(Color.parseColor("#3F51B5"));

    }

    @SuppressLint("HardwareIds")
    private void imeiNumber() {

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

//        imeiSerialNumber = telephonyManager.getDeviceId(0);

        TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(this);

        String imeiSIM1 = telephonyInfo.getImsiSIM1();
        String imeiSIM2 = telephonyInfo.getImsiSIM2();

        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

        boolean isDualSIM = telephonyInfo.isDualSIM();

        tv_imei_number.setText(" IME1 : " + imeiSIM1 + "\n" +
                " IME2 : " + imeiSIM2 + "\n" +
                " IS DUAL SIM : " + isDualSIM + "\n" +
                " IS SIM1 READY : " + isSIM1Ready + "\n" +
                " IS SIM2 READY : " + isSIM2Ready);

        tv_imei_number.setTextColor(Color.parseColor("#3F51B5"));
    }

    private void batteryStatus() {

        registerBatteryLevelReceiver();
    }

    private void registerBatteryLevelReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        registerReceiver(battery_receiver, filter);
    }

    BroadcastReceiver battery_receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isPresentBattery = intent.getBooleanExtra("present", false);
            String technology = intent.getStringExtra("technology");
            int plugged = intent.getIntExtra("plugged", -1);
            int scale = intent.getIntExtra("scale", -1);
            int health = intent.getIntExtra("health", 0);
            int status = intent.getIntExtra("status", 0);
            int rawlevel = intent.getIntExtra("level", -1);
            int voltage = intent.getIntExtra("voltage", 0);
            int temperature = intent.getIntExtra("temperature", 0);
            int level = 0;

            if (isPresentBattery) {

                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }

                String battery_level = "Battery Level: " + level + "%\n";
                String battery_technology = "Technology: " + technology + "\n";
                String battery_plugged = "Plugged: " + getPlugTypeString(plugged) + "\n";
                String battery_health = "Health: " + getHealthString(health) + "\n";
                String battery_status = "Status: " + getStatusString(status) + "\n";
                String battery_voltage = "Voltage: " + voltage + "\n";
                String battery_temperature = "Temperature: " + temperature + "\n";

                String bluetoothAddress = Settings.Secure.getString(getApplicationContext().getContentResolver(), "bluetooth_address");

                String bluetooth_address = "Bluetooth Address: " + bluetoothAddress;

                tv_battery_status.setText(battery_level + battery_technology + battery_plugged + battery_health + battery_status
                        + battery_voltage + battery_temperature + bluetooth_address);
                tv_battery_status.setTextColor(Color.parseColor("#3F51B5"));

            } else {
                tv_battery_status.setText("Battery not present!!!");
                tv_battery_status.setTextColor(Color.parseColor("#FF4081"));

            }
        }
    };

    private String getPlugTypeString(int plugged) {
        String plugType = "Unknown";

        switch (plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                plugType = "AC";
                break;
            case BatteryManager.BATTERY_PLUGGED_USB:
                plugType = "USB";
                break;
        }

        return plugType;
    }

    private String getHealthString(int health) {
        String healthString = "Unknown";

        switch (health) {
            case BatteryManager.BATTERY_HEALTH_DEAD:
                healthString = "Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD:
                healthString = "Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                healthString = "Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                healthString = "Over Heat";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                healthString = "Failure";
                break;
        }

        return healthString;
    }

    private String getStatusString(int status) {
        String statusString = "Unknown";

        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                statusString = "Charging";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                statusString = "Discharging";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                statusString = "Full";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                statusString = "Not Charging";
                break;
        }

        return statusString;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(battery_receiver);

        super.onDestroy();
    }


}
