package com.example.wifidetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.exercise.wifidetails.R;

import java.io.IOException;
import java.net.UnknownHostException;
import java.net.InetAddress;

/**
 * Created by SYEDMUSADDIQ on 23-08-2015.
 */
public class ScanNetwork extends AppCompatActivity {

    private final static String TAG="Can't Reach any Host";

    TextView IPAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        IPAddress=(TextView)findViewById(R.id.IP1);
        IPAddress=(TextView)findViewById(R.id.IP2);
        IPAddress=(TextView)findViewById(R.id.IP3);

        WifiMonitor wm=new WifiMonitor();
        String subnet=wm.Subnet;
        checkHosts(subnet);
    }

    private void checkHosts(String subnet){
        WifiMonitor wm=new WifiMonitor();
        int timeout=1000,count=0;
        try {
            for (int i = 1; i < 255 && count<3; i++) {
                String host = subnet + "." + i;
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)  ) {
                        if(InetAddress.getByName(host).getHostAddress()!= wm.YourIPAddress) {
                            IPAddress.setText(host);
                            count = count + 1;
                        }

                    }
                } catch (UnknownHostException ex) {
                    Log.e(TAG, "Can't reach any Host");
                }

            }
        }catch (UnknownHostException e){
            Log.e(TAG,"Can't Reach any Host");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
