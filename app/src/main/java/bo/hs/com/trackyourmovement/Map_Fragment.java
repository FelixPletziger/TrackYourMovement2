package bo.hs.com.trackyourmovement;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by Felix on 19.06.2016.
 */
public class Map_Fragment extends Fragment implements OnMapReadyCallback {
    View view;
    private MapView mapView;
    public GoogleMap googleMap;
    private DatabaseConnection datas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("map","mpa");
        view = inflater.inflate(R.layout.map_fragment, container, false);
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        datas = ((MainActivity)getActivity()).datasource;
        datas.open();
        List<TrackPoint> values = datas.getAllTrackPoints();
        datas.close();

        googleMap = map;

        for(TrackPoint poi:values) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(poi.getLatitude(), poi.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_point)));
        }

        ((MainActivity)getActivity()).setmMap(googleMap);

    }

}