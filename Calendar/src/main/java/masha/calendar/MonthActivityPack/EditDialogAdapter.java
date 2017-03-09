package masha.calendar.MonthActivityPack;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import masha.calendar.DBHelper;
import masha.calendar.R;

/**
 * Created by Маша on 17.02.2017.
 */

public class EditDialogAdapter extends CursorAdapter {


    public EditDialogAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    //Makes a new view to hold the data pointed to by cursor
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = View.inflate(context, R.layout.edit_item_2, null);
        return v;
    }

    //Bind an existing view to the data pointed to by cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE));
        ((TextView) view.findViewById(R.id.title)).setText(title);
        String description = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION));
        ((TextView) view.findViewById(R.id.description)).setText(description);
        if (((TextView) view.findViewById(R.id.description)).getText().equals("")) {
            view.findViewById(R.id.description).setVisibility(View.GONE);
        }

        String dateText = Integer.toString(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE))) + "  ";

        //  int date = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE));

        //  ((TextView) view.findViewById(R.id.date)).setText(Integer.toString(date));
        //  int year = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_YEAR));
        //   ((TextView) view.findViewById(R.id.year)).setText(Integer.toString(year));

        //    dateText += (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MONTH)) + 1) + ".";

        switch (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MONTH))) {
            case 0:
                dateText += "января";
                //   ((TextView) view.findViewById(R.id.month)).setText("января");

                break;
            case 1:
                //  ((TextView) view.findViewById(R.id.month)).setText("февраля");
                dateText += "февраля";
                break;
            case 2:
                //   ((TextView) view.findViewById(R.id.month)).setText("марта");
                dateText += "марта";
                break;
            case 3:
                //  ((TextView) view.findViewById(R.id.month)).setText("апреля");
                dateText += "апреля";
                break;
            case 4:
                //  ((TextView) view.findViewById(R.id.month)).setText("мая");
                dateText += "мая";
                break;
            case 5:
                //  ((TextView) view.findViewById(R.id.month)).setText("июня");
                dateText += "июня";
                break;
            case 6:
                //  ((TextView) view.findViewById(R.id.month)).setText("июля");
                dateText += "июля";
                break;
            case 7:
                //  ((TextView) view.findViewById(R.id.month)).setText("августа");
                dateText += "августа";
                break;
            case 8:
                //  ((TextView) view.findViewById(R.id.month)).setText("сентября");
                dateText += "сентября";
                break;
            case 9:
                // ((TextView) view.findViewById(R.id.month)).setText("октября");
                dateText += "октября";
                break;
            case 10:
                //  ((TextView) view.findViewById(R.id.month)).setText("ноября");
                dateText += "ноября";
                break;
            case 11:
                // ((TextView) view.findViewById(R.id.month)).setText("декабря");
                dateText += "декабря";
                break;

        }
        dateText += "  " + Integer.toString(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_YEAR)));
        ((TextView) view.findViewById(R.id.date)).setText(dateText);
    }

}
