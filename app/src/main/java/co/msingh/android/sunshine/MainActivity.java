package co.msingh.android.sunshine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private String LOG_TAG = "Sunshine";
    private String mLocation;

    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLocation = Utility.getPreferredLocation(this);
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }


    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(LOG_TAG, "onStart");
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.i(LOG_TAG, "onRestart");
    }

    //this is also called when settings is closed
    @Override
    protected void onResume(){
        super.onResume();
        Log.i(LOG_TAG, "onResume");


        String location = Utility.getPreferredLocation(this);

        //location cna be null if its the first time the app is loaded
        if(location != null && !location.equals(mLocation)){
            ForecastFragment ff = (ForecastFragment)getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if(ff != null){
                ff.onLocationChanged();
            }
            mLocation = location;
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.i(LOG_TAG, "onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        Log.i(LOG_TAG, "onStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class );
            startActivity(intent);
            return true;
        } else if(id == R.id.action_show_location){
            openPreferredLocationInMap();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openPreferredLocationInMap(){
//        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String loc = Utility.getPreferredLocation(this);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:0,0?q=" + loc));
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        } else {
            Toast.makeText(this, "No relevant app found", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

}
