package co.msingh.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import co.msingh.android.sunshine.data.WeatherContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
    private String mForecast;
    static final String DETAIL_URI = "URI";
    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME+"."+ WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATETEXT,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID
    };
    private Uri mUri;
    private static final int COL_WEATHER_ID = 0;
    private static final int COL_WEATHER_DATE = 1;
    private static final int COL_WEATHER_DESC = 2;
    private static final int COL_WEATHER_MAX_TEMP = 3;
    private static final int COL_WEATHER_MIN_TEMP = 4;
    private static final int COL_WEATHER_HUMIDITY = 5;
    private static final int COL_WEATHER_WIND_SPEED = 6;
    private static final int COL_WEATHER_PRESSURE = 7;
    private static final int COL_WEATHER_DEGREES = 8;
    private static final int COL_WEATHER_TYPE_ID = 9;

    private ShareActionProvider mShareActionProvider;
    private TextView mDayTextView;
    private TextView mDateTextView;
    private TextView mHighTextView;
    private TextView mLowTextView;
    private TextView mHumidityTextView;
    private TextView mWindSpeedTextView;
    private TextView mPressureTextView;
    private TextView mWeatherDescriptionTextView;
    private ImageView mWeatherIcon;

    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceStat){
        //this - implements callbacks
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        super.onActivityCreated(savedInstanceStat);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailFragment.DETAIL_URI);
        }


        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);



        mDayTextView = (TextView)rootView.findViewById(R.id.fragment_detail_day_textview);
        mDateTextView = (TextView) rootView.findViewById(R.id.fragment_detail_date_textview);
        mHighTextView = (TextView) rootView.findViewById(R.id.fragment_detail_high_textview);
        mLowTextView  = (TextView) rootView.findViewById(R.id.fragment_detail_low_textview);
        mHumidityTextView = (TextView) rootView.findViewById(R.id.fragment_detail_humidity_textview);
        mWindSpeedTextView = (TextView) rootView.findViewById(R.id.fragment_detail_wind_textview);
        mPressureTextView = (TextView) rootView.findViewById(R.id.fragment_detail_pressure_textview);
        mWeatherDescriptionTextView = (TextView) rootView.findViewById(R.id.fragment_detail_weather_description_textview);

        mWeatherIcon = (ImageView) rootView.findViewById(R.id.fragment_detail_icon_imageview);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail_fragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(mShareActionProvider == null){
            Log.d(LOG_TAG, "SHARE ACTION IS NULL");
        }
        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mForecast != null ) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if ( null != mUri ) {
            // Now create and return a CursorLoader that will take care of
            // creating a Cursor for the data being displayed.
            return new CursorLoader(
                    getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null
            );
        }
            return null;

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data != null && data.moveToFirst()){
            int weatherId = data.getInt(COL_WEATHER_TYPE_ID);
            mWeatherIcon.setImageResource(Utility.getArtResourceForWeatherCondition(weatherId));

            boolean isMetric = Utility.isMetric(getActivity());
            String weatherDescription = data.getString(COL_WEATHER_DESC);
            long date = data.getLong(COL_WEATHER_DATE);

            String high = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MAX_TEMP), isMetric);
            String low = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP), isMetric);

            String dayText = Utility.getDayName(getActivity(), date);
            String dateText = Utility.getFormattedMonthDay(getActivity(), date);

            mDayTextView.setText(dayText);
            mDateTextView.setText(dateText);
            mHighTextView.setText(high);
            mLowTextView.setText(low);
            mWeatherDescriptionTextView.setText(data.getString(COL_WEATHER_DESC));
            mWindSpeedTextView.setText(Utility.getFormattedWind(getActivity(), data.getFloat(COL_WEATHER_WIND_SPEED), data.getFloat(COL_WEATHER_DEGREES)));
            mPressureTextView.setText(getActivity().getString(R.string.format_pressure, data.getFloat(COL_WEATHER_PRESSURE)));
            mHumidityTextView.setText(getActivity().getString(R.string.format_humidity, data.getFloat(COL_WEATHER_HUMIDITY)));

            mForecast = String.format("%s - %s - %s/%s", Utility.formatDate(date), weatherDescription, high, low);


            if(mShareActionProvider != null){
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }

            ((DetailActivity)getActivity()).updateTitle(dateText);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    void onLocationChanged( String newLocation ) {
        // replace the uri, since the location has changed
        Uri uri = mUri;
        if (null != uri) {
            long date = WeatherContract.WeatherEntry.getDateFromUri(uri);
            Uri updatedUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(newLocation, date);
            mUri = updatedUri;
            getLoaderManager().restartLoader(DETAIL_LOADER, null, this);
        }
    }

}