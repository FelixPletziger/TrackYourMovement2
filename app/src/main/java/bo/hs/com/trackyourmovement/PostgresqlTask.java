package bo.hs.com.trackyourmovement;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by Felix on 18.06.2016.
 */
public class PostgresqlTask extends AsyncTask<Void, Void, Void> {
    private static final String pgurl ="jdbc:postgresql://136.243.22.183:57741/dbm1";
    private static final String user ="HSBoFBGdb";
    private static final String pwd ="GxSod:4T$X";
    private static final String table ="trackyourmovement";
    private List<TrackPoint> data;

    public PostgresqlTask(List<TrackPoint> data) {
        super();
        this.data=data;
    }

    @Override
    protected Void doInBackground(Void... params) {

        Connection con = null;
        Statement st = null;

        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(pgurl, user, pwd);
            Log.e("PostgresqlTask","Verbunden zur PGSQL DB");
            st = con.createStatement();

            //String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            //Log.e("Zeit",timeStamp.toString());

            for (TrackPoint poi : data) {
                String sql = "INSERT INTO "+table+" (users, latitude, longitude, speed, time) VALUES" +
                        "('"+poi.getUser()+"', "+poi.getLatitude()+", "+poi.getLongitude()+", "+poi.getSpeed()+", '"+poi.getDate()+"');";

                st.executeUpdate(sql);
            }
        }
        catch(Exception e){
            e.printStackTrace();
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

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}
