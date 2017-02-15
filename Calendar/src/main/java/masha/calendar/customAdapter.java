package masha.calendar;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Маша on 14.02.2017.
 */

public class CustomAdapter extends SimpleCursorAdapter {

    Context context;
    int layout;
    Cursor cursor;
    String[] from;
    int[] to;
    int flags;
    private final LayoutInflater inflater;

    public CustomAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context = context;
        this.layout = layout;
        this.inflater=LayoutInflater.from(context);
        this.cursor = c;
        this.from = from;
        this.to = to;
        this.flags = flags;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

    }
}
