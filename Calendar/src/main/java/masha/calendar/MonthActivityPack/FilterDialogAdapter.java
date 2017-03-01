package masha.calendar.MonthActivityPack;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import masha.calendar.DBHelper;
import masha.calendar.R;

/**
 * Created by Маша on 17.02.2017.
 */

public class FilterDialogAdapter extends CursorAdapter {

    View v = null;
    Cursor cursor;
    Context context;
    ListView listView;

    public FilterDialogAdapter(Context context, Cursor c, int flags) {
        super(context, c, 0);
        this.context = context;
        cursor = c;
    }


    //Makes a new view to hold the data pointed to by cursor
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        v = View.inflate(context, R.layout.filter_list_item, null);
        listView = (ListView) parent;

        return v;
    }

    //Bind an existing view to the data pointed to by cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String tag = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TAG));
        if (tag.isEmpty()) {
            ((TextView) view.findViewById(R.id.tagTitle)).setText("Без тэга");
        } else {
            ((TextView) view.findViewById(R.id.tagTitle)).setText(tag);
        }

        if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_CHECKED)) == 1) {
            ((CheckBox) view.findViewById(R.id.checkBox)).setChecked(true);
        } else {
            ((CheckBox) view.findViewById(R.id.checkBox)).setChecked(false);
        }
    }


    @Override
    public int getCount() {
        return cursor.getCount();
    }

}
