package bo.hs.com.trackyourmovement;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Felix on 18.06.2016.
 */
public class PgsqlinsertuserTask extends AsyncTask<String, Void, Boolean>{
    private static final String pgurl ="jdbc:postgresql://136.243.22.183:57741/dbm1";
    private static final String user ="HSBoFBGdb";
    private static final String pwd ="GxSod:4T$X";
    private static final String table ="userdata";
    private String username;
    private String userpassword;

    public AsyncResponses delegate = null;


    public PgsqlinsertuserTask() {
        super();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        this.username=params[0];
        this.userpassword=params[1];

        Connection con = null;
        Statement st = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(pgurl, user, pwd);
            Log.e("PgsqlcheckTPTask","Verbunden zur PGSQL DB");
            st = con.createStatement();

            String sql = "INSERT INTO "+table+" (username, userpassword) VALUES"+
                    "('"+this.username+"', '"+this.userpassword+"');";
            Log.e("String",sql);
            st.executeUpdate(sql);
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            //finally block used to close resources
            try{
                if(st!=null)
                    con.close();
            }catch(SQLException se){
            }// do nothing
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }


        }
    }

    protected void onPostExecute(Boolean result) {
        delegate.processFinish(result);
    }
}
