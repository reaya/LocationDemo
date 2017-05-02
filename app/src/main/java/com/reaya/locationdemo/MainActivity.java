package com.reaya.locationdemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    TextView textView;
    Button btnPress;
    LocationManager locationManager;
    ProgressDialog progressDialog;

    public void init() {

        textView = (TextView) findViewById(R.id.textView);
        btnPress = (Button) findViewById(R.id.button);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        btnPress.setOnClickListener(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("peocessing,finding locn");
        progressDialog.setCancelable(false);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.button) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"please enable your permissions from settings",Toast.LENGTH_LONG).show();
            }
            else{
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5, 10, this);
                progressDialog.show();
        }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

         double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        textView.setText("lat :"+latitude+" long :"+longitude);
        locationManager.removeUpdates(this);
        progressDialog.dismiss();

       Geocoder geocoder=new Geocoder(this);
        try {
            List<Address> addressList=geocoder.getFromLocation(latitude,longitude,2);

            if (addressList!= null && addressList.size()>0){

              Address address =  addressList.get(0);

                StringBuffer buffer=new StringBuffer();
                for (int i =0; i<address.getMaxAddressLineIndex();i++){
                         buffer.append(address.getAddressLine(i)+"\n");

                }
                textView.setText(buffer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
