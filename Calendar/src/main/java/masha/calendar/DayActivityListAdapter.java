package masha.calendar;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by Маша on 28.02.2017.
 */

public class DayActivityListAdapter extends CursorAdapter {


    public DayActivityListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = View.inflate(context, R.layout.day_activity_list_item, null);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        view.findViewById(R.id.column).setVisibility(View.VISIBLE);

        String title = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION));
        int hour = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_HOUR));
        int minute = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MINUTE));
        int allDay = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ALL_DAY));

        TextView hourTextView = (TextView) view.findViewById(R.id.hour);
        TextView minuteTextView = (TextView) view.findViewById(R.id.minute);

        ((TextView) view.findViewById(R.id.title)).setText(title);
        ((TextView) view.findViewById(R.id.description)).setText(description);

        if (allDay == 0 && hour != 0) {
            if (hour < 10) {
                hourTextView.setText("0" + hour);
            } else {
                hourTextView.setText(String.format("%s", hour));
            }
            if (minute < 10) {
                minuteTextView.setText("0" + minute);
            } else {
                minuteTextView.setText(String.format("%s", minute));
            }
        } else {
            hourTextView.setText("  ");
            view.findViewById(R.id.column).setVisibility(View.GONE);
            minuteTextView.setText("  ");
        }








    }
}
