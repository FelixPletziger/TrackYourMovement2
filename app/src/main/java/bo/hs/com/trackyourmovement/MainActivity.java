package bo.hs.com.trackyourmovement;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DatabaseConnection datasource; //SQLDATABASE

    /*public static final int MINUTE_INTERVALL = 60000;
    public static final int FIVE_INTERVALL = 300000;*/

    public Switch gps;
    public TextView lat;
    public TextView lon;
    public TextView speed;

    public GoogleMap mMap;

    public LocationManager locationManager;
    public LocationListener locationListener;

    DecimalFormat coorfor = new DecimalFormat("0.00000");
    DecimalFormat speedfor = new DecimalFormat("0");

    //ArrayList<String> list = new ArrayList<String>();
    //ArrayAdapter<String> adapter;

    //ALARM MANAGER
    private Calendar gpscal;
    private Intent gpsintent;
    private PendingIntent gpspintent;
    private AlarmManager gpsalarm;
    //ALARM MANAGER ENDE

    public boolean alarmUp;

    //Fragmente
    public Position_Fragment positionfragment;
    public Map_Fragment mapfragment;
    public Login_Fragment loginfragment;
    public Reg_Fragment regfragment;


    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    private IntentFilter intentFilter = new IntentFilter("bo.hs.com.GPSLOCATION");

    private BroadcastReceiver LocationBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Double late = intent.getDoubleExtra("lat", 0);
            Double longe = intent.getDoubleExtra("lon", 0);
            Double speeds = intent.getDoubleExtra("speed", 0);
            Log.e("testung", String.valueOf(late));
            if (late != 0 && longe != 0) {
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if (fr instanceof Position_Fragment) {
                    if (fr != null && fr.isVisible()) {

                        lat.setText(coorfor.format(late));
                        lon.setText(coorfor.format(longe));
                        speed.setText(speedfor.format(speeds));
                    }
                }

                if (fr instanceof Map_Fragment) {
                    if (fr != null && fr.isVisible()) {
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(late, longe))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_point)));
                    }
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inizialisierung der Fragmente
        positionfragment = (Position_Fragment) Fragment.instantiate(this, Position_Fragment.class.getName());
        mapfragment = (Map_Fragment) Fragment.instantiate(this, Map_Fragment.class.getName());
        loginfragment = (Login_Fragment) Fragment.instantiate(this, Login_Fragment.class.getName());
        regfragment = (Reg_Fragment) Fragment.instantiate(this, Reg_Fragment.class.getName());

        //ALARM MANAGER
        gpscal = Calendar.getInstance();
        gpsintent = new Intent(this, GPSService.class);
        gpsintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        gpspintent = PendingIntent.getService(this, 0, gpsintent, 0);
        gpsalarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //ALARM MANAGER ENDE

        alarmUp = (PendingIntent.getBroadcast(this, 0, new Intent(this, GPSService.class), PendingIntent.FLAG_NO_CREATE) != null);

        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.content_frame, loginfragment);
            fragmentTransaction.commit();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //DATABASE
        datasource = new DatabaseConnection(this);

        //coordinates = (ListView) findViewById(R.id.coorlist);

        //list.add("Latitude:\t\tLongitude:\t\tGeschwindigkeit:");
        /** Defining the ArrayAdapter to set items to ListView */
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        //coordinates.setAdapter(adapter);

        /*locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.content_frame);
                if(fr instanceof Position_Fragment){
                    if(fr!=null && fr.isVisible()){
                        lat.setText(coorfor.format(location.getLatitude()));
                        lon.setText(coorfor.format(location.getLongitude()));
                        speed.setText(speedfor.format(location.getSpeed()));
                    }
                }

                if(fr instanceof Map_Fragment){
                    if(fr!=null && fr.isVisible()){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_point)));
                    }
                }
                String dateFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(location.getTime());

                datasource.open();
                TrackPoint TrP = datasource.createTrackPoint("test",location.getLatitude(), location.getLongitude(), location.getSpeed(), dateFormatted);
                datasource.close();*/

                /*if(mMap != null) {
                    LatLng point = new LatLng(TrP.getLatitude(), TrP.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(point));
                }*/

            /*}

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
        };*/
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
                    //locationManager.requestLocationUpdates("gps", FIVE_INTERVALL, 0, locationListener);
                    gpsalarm.setRepeating(AlarmManager.RTC, gpscal.getTimeInMillis(), 60*1000, gpspintent);

                }else{
                    //locationManager.removeUpdates(locationListener);
                    if(gpsalarm!=null){
                        gpsalarm.cancel(gpspintent);
                    }
                    if (gpspintent != null) {
                        gpspintent.cancel();
                    }
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

        int id = item.getItemId();

        if (id == R.id.nav_position_fragment) {
            Log.e("1","1");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, positionfragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_transfer_to_pgsql) {
            Log.e("2","2");
            transfertopgsql();
        } else if (id == R.id.nav_akt_map) {
            Log.e("3","3");
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, mapfragment);
            fragmentTransaction.commit();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(LocationBroadcast,intentFilter);
    }

    @Override
    protected void onPause() {
        if (datasource != null) {
            datasource.close();
        }
        unregisterReceiver(LocationBroadcast);
        super.onPause();
    }

    public void transfertopgsql(){
        //Read data from local Database
        datasource.open();
        List<TrackPoint> values = datasource.getAllTrackPoints();
        Log.e("Vorher:",values.toString());
        datasource.close();

        new PgsqlInsertTPTask(values).execute();
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

    public GoogleMap getmMap() {
        return mMap;
    }

    public void setmMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    public AlarmManager getGpsalarm() {
        return gpsalarm;
    }

    public void setGpsalarm(AlarmManager gpsalarm) {
        this.gpsalarm = gpsalarm;
    }

    public DatabaseConnection getDatasource() {
        return datasource;
    }

    public void setDatasource(DatabaseConnection datasource) {
        this.datasource = datasource;
    }

    public boolean isAlarmUp() {
        return alarmUp;
    }

    public void setAlarmUp(boolean alarmUp) {
        this.alarmUp = alarmUp;
    }

}
