package co.msingh.android.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {
    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if(viewType == VIEW_TYPE_TODAY){
            layoutId = R.layout.list_item_forecast_today;
        } else {
            layoutId = R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder  = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public int getItemViewType(int position){
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount(){
        return VIEW_TYPE_COUNT;
    }


    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());

        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                // Get weather icon
                viewHolder.weatherIcon.setImageResource(Utility.getArtResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                // Get weather icon
                viewHolder.weatherIcon.setImageResource(Utility.getIconResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }

        boolean isMetric = Utility.isMetric(context);

        int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);

        double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);

        Long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String condition = cursor.getString(ForecastFragment.COL_WEATHER_DESC);

        viewHolder.listItemForecastDay.setText(Utility.getFriendlyDayString(context, date));
        viewHolder.listItemForecastCondition.setText(condition);
        viewHolder.listItemForecastHigh.setText(Utility.formatTemperature(context, high, isMetric));
        viewHolder.listItemForecastLow.setText(Utility.formatTemperature(context, low, isMetric));
    }


    public static class ViewHolder {
        public final ImageView weatherIcon;
        public final TextView listItemForecastDay;
        public final TextView listItemForecastCondition;
        public final TextView listItemForecastHigh;
        public final TextView listItemForecastLow;

        public ViewHolder(View view){
            weatherIcon = (ImageView)view.findViewById(R.id.list_item_forecast_icon);
            listItemForecastDay = (TextView)view.findViewById(R.id.list_item_forecast_day);
            listItemForecastHigh = (TextView)view.findViewById(R.id.list_item_forecast_high);
            listItemForecastLow = (TextView)view.findViewById(R.id.list_item_forecast_low);
            listItemForecastCondition = (TextView)view.findViewById(R.id.list_item_forecast_condition);
        }
    }
}