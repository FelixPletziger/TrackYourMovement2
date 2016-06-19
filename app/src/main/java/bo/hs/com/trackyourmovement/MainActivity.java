package bo.hs.com.trackyourmovement;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseConnection datasource; //SQLDATABASE

    public Switch gps;
    public TextView lat;
    public TextView lon;
    public TextView speed;
    //private ListView coordinates;

    private LocationManager locationManager;
    private LocationListener locationListener;

    DecimalFormat coorfor = new DecimalFormat("0.00000");
    DecimalFormat speedfor = new DecimalFormat("0");

    //ArrayList<String> list = new ArrayList<String>();
    //ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //DATABASE
        datasource = new DatabaseConnection(this);

        /*gps   = (Switch) findViewById(R.id.gps_switch);
        lat   = (TextView) findViewById(R.id.lat);
        lon   = (TextView) findViewById(R.id.lon);
        speed = (TextView) findViewById(R.id.speed);*/

        //coordinates = (ListView) findViewById(R.id.coorlist);

        //list.add("Latitude:\t\tLongitude:\t\tGeschwindigkeit:");
        /** Defining the ArrayAdapter to set items to ListView */
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        //coordinates.setAdapter(adapter);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                /*if(lat.getText()!="" && lon.getText()!="") {
                    if (location.hasSpeed()){
                        list.add(coorfor.format(location.getLatitude()) + "\t\t\t" + coorfor.format(location.getLongitude()) + "\t\t\t\t\t" + speedfor.format(location.getSpeed()));
                    }else{
                        list.add(coorfor.format(location.getLatitude()) + "\t\t\t" + coorfor.format(location.getLongitude()));
                    }
                    adapter.notifyDataSetChanged();
                }*/

                lat.setText(coorfor.format(location.getLatitude()));
                lon.setText(coorfor.format(location.getLongitude()));
                speed.setText(speedfor.format(location.getSpeed()));
                String dateFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(location.getTime());

                datasource.open();
                TrackPoint TrP = datasource.createTrackPoint("test",location.getLatitude(), location.getLongitude(), location.getSpeed(), dateFormatted);
                datasource.close();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    configureSwitch();
                }
                return;
        }
    }

    public void configureSwitch() {
        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    /* requestLocationUpdates( provider | minTime in milsec | minDistance in meter | locationListener ) */
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                }else{
                    locationManager.removeUpdates(locationListener);
                    /*datasource.open();
                    List<TrackPoint> values = datasource.getAllTrackPoints();
                    datasource.close();
                    Log.e("Database DATA", values.toString());*/
                    //new PostgresqlTask().execute();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_position_fragment) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,
                            new Position_Fragment())
                    .commit();
        } else if (id == R.id.nav_transfer_to_pgsql) {
            transfertopgsql();
        } else if (id == R.id.nav_akt_map) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,
                            new Map_Fragment())
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (datasource != null) {
            datasource.close();
        }
        super.onPause();
    }

    public void transfertopgsql(){
        //Read data from local Database
        datasource.open();
        List<TrackPoint> values = datasource.getAllTrackPoints();
        Log.e("Vorher:",values.toString());
        datasource.close();

        new PostgresqlTask(values).execute();
        datasource.open();
        datasource.deleteCompleTable();
        List<TrackPoint> valuesa = datasource.getAllTrackPoints();
        Log.e("Nachher:",valuesa.toString());
        datasource.close();

    }

    public Switch getGps() {
        return gps;
    }

    public void setGps(Switch gps) {
        this.gps = gps;
    }

    public TextView getLat() {
        return lat;
    }

    public void setLat(TextView lat) {
        this.lat = lat;
    }

    public TextView getLon() {
        return lon;
    }

    public void setLon(TextView lon) {
        this.lon = lon;
    }

    public TextView getSpeed() {
        return speed;
    }

    public void setSpeed(TextView speed) {
        this.speed = speed;
    }
}
