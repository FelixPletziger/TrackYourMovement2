package bo.hs.com.trackyourmovement;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Felix on 19.06.2016.
 */
public class Map_Fragment extends Fragment{

    View mapfrag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapfrag = inflater.inflate(R.layout.map_fragment, container, false);
        return mapfrag;
    }
}
