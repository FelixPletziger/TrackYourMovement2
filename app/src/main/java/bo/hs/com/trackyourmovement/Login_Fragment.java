package bo.hs.com.trackyourmovement;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Felix on 21.06.2016.
 */
public class Login_Fragment extends Fragment implements AsyncResponse{

    View login;
    private String username;
    private String userpassword;
    private AsyncResponse ctx;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = this;
        login = inflater.inflate(R.layout.login_fragment, container, false);
        final Button button = (Button) login.findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                username = ((EditText) login.findViewById(R.id.username)).getText().toString();
                userpassword = ((EditText) login.findViewById(R.id.userpassword)).getText().toString();

                PgsqlcheckuserTask asyncTask = new PgsqlcheckuserTask();
                asyncTask.delegate = ctx;
                asyncTask.execute(username,userpassword);
            }
        });

        final TextView txt = (TextView) login.findViewById(R.id.link_to_register);
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, ((MainActivity)getActivity()).regfragment);
                fragmentTransaction.commit();
            }
        });


        return login;
    }

    public void processFinish(String output){
        Log.e("output",output.toString());
        if( output.toString() == userpassword){
            Toast.makeText(login.getContext(), "Wrong Login", Toast.LENGTH_LONG).show();
        }else{
            Log.e("testtest",Boolean.toString(((MainActivity)getActivity()).alarmUp));
            ((MainActivity)getActivity()).setUsername(username);
            ((MainActivity)getActivity()).gpscal = Calendar.getInstance();
            ((MainActivity)getActivity()).gpsintent = new Intent(getActivity(), GPSService.class);
            ((MainActivity)getActivity()).gpsintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ((MainActivity)getActivity()).gpsintent.putExtra("username",((MainActivity)getActivity()).username);
            ((MainActivity)getActivity()).gpspintent = PendingIntent.getService(getActivity(), 0, ((MainActivity)getActivity()).gpsintent, 0);
            ((MainActivity)getActivity()).gpsalarm = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            Toast.makeText(login.getContext(), "Correct Login", Toast.LENGTH_LONG).show();
            fragmentManager = getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, ((MainActivity)getActivity()).positionfragment);
            fragmentTransaction.commit();
        }
    }

}
