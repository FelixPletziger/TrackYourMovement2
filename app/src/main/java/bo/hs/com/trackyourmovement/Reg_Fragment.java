package bo.hs.com.trackyourmovement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Felix on 21.06.2016.
 */
public class Reg_Fragment extends Fragment implements AsyncResponses{

    View reg;
    private String username;
    private String userpassword;
    private AsyncResponses ctx;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = this;
        reg = inflater.inflate(R.layout.reg_fragment, container, false);
        final Button button = (Button) reg.findViewById(R.id.btnRegister);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                username = ((EditText) reg.findViewById(R.id.reg_fullname)).getText().toString();
                userpassword = ((EditText) reg.findViewById(R.id.reg_password)).getText().toString();

                PgsqlinsertuserTask asyncTask = new PgsqlinsertuserTask();
                asyncTask.delegate = ctx;
                asyncTask.execute(username,userpassword);
            }
        });
        final TextView txt = (TextView) reg.findViewById(R.id.link_to_login);
        txt.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, ((MainActivity)getActivity()).loginfragment);
                fragmentTransaction.commit();
            }
        });

        return reg;
    }

    public void processFinish(Boolean output){
        if(output) {
            Toast.makeText(reg.getContext(), "Regist OK", Toast.LENGTH_LONG).show();
        }
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, ((MainActivity)getActivity()).loginfragment);
        fragmentTransaction.commit();
    }

}
