package edu.sjsu.cohort3.cmpe295b.grp1.personalbigdata;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

    private final Runnable trackUser = new Runnable() {
        @Override
        public void run() {
            try {
                GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
                if (gpsTracker.canGetLocation()) {
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    String msg = "lat: " + latitude + ", longitude: " + longitude;
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://ec2-54-200-58-132.us-west-2.compute.amazonaws.com:3000/users");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                    nameValuePairs.add(new BasicNameValuePair("user_id", "123456789"));
                    nameValuePairs.add(new BasicNameValuePair("device_id", "987654321"));
                    nameValuePairs.add(new BasicNameValuePair("latitude", String.valueOf(latitude)));
                    nameValuePairs.add(new BasicNameValuePair("longitude", String.valueOf(longitude)));
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    Log.e("ANAND", "before HTTP POST");
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    Log.e("ANAND", "after HTTP POST");
                }
            } catch (Exception e) {
                Log.e("ANAND", e.getMessage());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler r = new Handler();
        r.postDelayed(trackUser, 5000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
