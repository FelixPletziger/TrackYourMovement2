package bo.hs.com.trackyourmovement;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

public class Position_Fragment extends Fragment {

    View position;
    private Switch gps;
    private boolean gpsboolean;
    /*private TextView lat;
    private TextView lon;
    private TextView speed;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = inflater.inflate(R.layout.position_fragment, container, false);

        gps = (Switch)   position.findViewById(R.id.gps_switch);
        gpsboolean = ((MainActivity)getActivity()).alarmUp;

        if(gpsboolean){
            gps.setChecked(true);
        }else{
            gps.setChecked(false);
        }

        ((MainActivity)getActivity()).setGps(gps);
        ((MainActivity)getActivity()).setLat((TextView) position.findViewById(R.id.lat));
        ((MainActivity)getActivity()).setLon((TextView) position.findViewById(R.id.lon));
        ((MainActivity)getActivity()).setSpeed((TextView) position.findViewById(R.id.speed));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10 /*Indikator*/);
            }
        }else{
            ((MainActivity)getActivity()).configureSwitch();
        }

        return position;
    }

}
