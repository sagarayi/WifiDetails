package com.example.wifidetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.R;
//import android.view.Menu;
//import android.view.MenuItem;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Enumeration;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.net.NetworkInterface;

import com.exercise.wifidetails.R;


/**
 * Created by SYED MUSADDIQ FARAZ on 20/8/15.
 */

public class WifiMonitor extends AppCompatActivity implements View.OnClickListener {
    public String Subnet="";
    public String YourIPAddress="";
    TextView textConnected, textIp, textSsid, textBssid, textMac, textSpeed, textRssi;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.Scan).setOnClickListener(this);


        textConnected = (TextView)findViewById(R.id.Connected);
        textIp = (TextView)findViewById(R.id.Ip);

        textSsid = (TextView)findViewById(R.id.Ssid);
        textBssid = (TextView)findViewById(R.id.Bssid);
        textMac = (TextView)findViewById(R.id.Mac);
        textSpeed = (TextView)findViewById(R.id.Speed);
        textRssi = (TextView)findViewById(R.id.Rssi);

        DisplayWifiState();
        DisplayLocalIP();

        this.registerReceiver(this.myWifiReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

    }

    private BroadcastReceiver myWifiReceiver
            = new BroadcastReceiver(){

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            NetworkInfo networkInfo = (NetworkInfo) arg1.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                DisplayWifiState();
            }
        }};

    private void DisplayWifiState(){

        ConnectivityManager myConnManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo myNetworkInfo = myConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager myWifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();

        textMac.setText(myWifiInfo.getMacAddress());
        int myIp = myWifiInfo.getIpAddress();

        int intMyIp3 = myIp/0x1000000;
        int intMyIp3mod = myIp%0x1000000;

        int intMyIp2 = intMyIp3mod/0x10000;
        int intMyIp2mod = intMyIp3mod%0x10000;

        int intMyIp1 = intMyIp2mod/0x100;
        int intMyIp0 = intMyIp2mod%0x100;

        textIp.setText(String.valueOf(intMyIp0)
                        + "." + String.valueOf(intMyIp1)
                        + "." + String.valueOf(intMyIp2)
                        + "." + String.valueOf(intMyIp3)
        );
        Subnet=String.valueOf(intMyIp0)
                + "." + String.valueOf(intMyIp1)
                + "." + String.valueOf(intMyIp2);
        YourIPAddress=String.valueOf(intMyIp0)
                + "." + String.valueOf(intMyIp1)
                + "." + String.valueOf(intMyIp2)
                + "." + String.valueOf(intMyIp3);
        if (myNetworkInfo.isConnected()){


            textConnected.setText("    CONNECTED ");



            textSsid.setText(myWifiInfo.getSSID());
            textBssid.setText(myWifiInfo.getBSSID());

            textSpeed.setText(String.valueOf(myWifiInfo.getLinkSpeed()) + " " + WifiInfo.LINK_SPEED_UNITS);
            textRssi.setText(String.valueOf(myWifiInfo.getRssi()));
        }
        else{
            textConnected.setText("DISCONNECTED");
        }

    }
    private  void DisplayLocalIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() ) {
                        textIp.setText(inetAddress.getHostAddress());
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
            textIp.setText(null);
        }

    }
    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(this,ScanNetwork.class);
        startActivity(i);
    }

}
