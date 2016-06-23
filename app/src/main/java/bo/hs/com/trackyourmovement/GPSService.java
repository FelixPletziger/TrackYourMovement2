package bo.hs.com.trackyourmovement;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Felix on 20.06.2016.
 */
public class GPSService extends Service {
    private DatabaseConnection datasource; //SQLDATABASE
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Integer id;
    private String username;
    DecimalFormat coorfor = new DecimalFormat("0.00000");
    DecimalFormat speedfor = new DecimalFormat("0");

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e("LOCATION:", "\nLat:" + location.getLatitude() + "  Lon:" + location.getLongitude());

                if(isForeground("bo.hs.com.trackyourmovement")){

                    Intent intent = new Intent("new-location");
                    intent.putExtra("lat",location.getLatitude());
                    intent.putExtra("lon",location.getLongitude());
                    intent.putExtra("speed",location.getSpeed());
                    intent.setAction("bo.hs.com.GPSLOCATION");
                    sendBroadcast(intent);
                }else{
                    Log.e("Forground","Activity is closed");
                }

                String dateFormatted = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(location.getTime());

                //DATABASE
                datasource = new DatabaseConnection(getApplicationContext());
                datasource.open();
                TrackPoint TrP = datasource.createTrackPoint(username,location.getLatitude(), location.getLongitude(), location.getSpeed(), dateFormatted);
                datasource.close();

                locationManager.removeUpdates(locationListener);
                if(id!=null){
                    Log.e("stopself","true");
                    stopSelf(id);
                    id=null;
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
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, final int startId)
    {
        super.onStart(intent, startId);
        Log.e("Service","hier");
        id=startId;
        username=intent.getStringExtra("username");
        Log.e("GPSSER",username);
        locationManager.requestLocationUpdates("gps", 0, 0, locationListener);

        /*mLocationManager.requestSingleUpdate(mCriteria, mLocationListener,
                getMainLooper());
        Log.i("TESTING LOCATION UPDATE: LOCATION:", "\nLat:"   + mLatitude +
                "  Lon:" + mLongitude);
        stopSelf(startId);
        Log.d("Testing", "Service Stopped!");
        */
    }

    @Override
    public void onDestroy()
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public boolean isForeground(String pack) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = manager.getRunningTasks(1);
        ComponentName componentInfo = task.get(0).topActivity;

        if (componentInfo.getPackageName().equals(pack)){
            Log.e("test","ja");
            return true;
        }else{
            Log.e("test","nein");
            return false;
        }

    }
}
