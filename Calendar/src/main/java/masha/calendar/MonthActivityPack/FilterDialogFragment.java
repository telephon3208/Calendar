package masha.calendar.MonthActivityPack;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import masha.calendar.DBHelper;
import masha.calendar.R;


/**
 * Created by Маша on 17.02.2017.
 */

public class FilterDialogFragment extends DialogFragment implements OnClickListener  {

    final String LOG_TAG = "myLogs";
    ListView listView;
    Cursor cursor;
    HashMap<Long, Boolean> collection;

    public static FilterDialogFragment newInstance(int num) {
        FilterDialogFragment f = new FilterDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        collection = new HashMap<>();
    }

    //onCreateDialog() используется для формирования диалога с использованием AlertDialog.Builder
    //для создания полностью кастомного диалога, надо использовать метод onCreateView()
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Фильтр событий");
        View v = inflater.inflate(R.layout.filter_dialog_layout, null);
        v.findViewById(R.id.btnYes).setOnClickListener(this);
        v.findViewById(R.id.btnCancel).setOnClickListener(this);

        listView = (ListView) v.findViewById(R.id.listView);
        cursor = getTags();
        CursorAdapter adapter = new FilterDialogAdapter(getActivity(),
                cursor, 0);
        Log.d(MonthActivity.TAG, "stableId: " + adapter.hasStableIds());
        listView.setAdapter(adapter);
     //   listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //id совпадает с id курсора
                Log.d(MonthActivity.TAG, "нажат пункт: " + position);
                Log.d(MonthActivity.TAG, "нажат пункт id: " + id);
                String tag = ((TextView) view.findViewById(R.id.tagTitle)).getText().toString();
                Log.d(MonthActivity.TAG, "название: " + tag);
                Log.d(MonthActivity.TAG, "id cursor: " + cursor.getInt(cursor.getColumnIndex("_id")));

                CheckBox chB = (CheckBox) view.findViewById(R.id.checkBox);
                chB.setChecked(!chB.isChecked());

                //добавляю нажатый пункт в коллекцию
                collection.put(id, chB.isChecked());
            }
        });
        Log.d(MonthActivity.TAG, "onItemClick");
        return v;
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(MonthActivity.TAG, "Dialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(MonthActivity.TAG, "Dialog: onCancel");
    }

    Cursor getTags() {
        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase database;

        try {
            database = dbHelper.getWritableDatabase();
            Log.d(MonthActivity.TAG,"получена БД");
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            return null;
        }

        Cursor cursor = database.rawQuery("SELECT _id, " +
                 DBHelper.KEY_TAG + ", " +
                 DBHelper.KEY_CHECKED + " FROM " +
                 DBHelper.TABLE_EVENTS + " GROUP BY " +
                 DBHelper.KEY_TAG + " ORDER BY " +
                 DBHelper.KEY_TAG + " ASC", null);

        cursor.moveToFirst();

        return cursor;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnYes:
                checkBoxWriter();
                break;
            case R.id.btnCancel:

                break;
        }
        dismiss();
    }

    void checkBoxWriter() {
        Log.d(MonthActivity.TAG, "начало метода checkBoxWriter()");

        ContentValues contentValues0 = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues0.put(DBHelper.KEY_CHECKED, 0);
        contentValues1.put(DBHelper.KEY_CHECKED, 1);

        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase db = null;

        try {
            db = dbHelper.getWritableDatabase();
            Log.d(MonthActivity.TAG,"получена БД");
        }
        catch (SQLiteException ex){
            db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
        }

        //записываем чекбоксы
        for (Map.Entry<Long, Boolean> entry : collection.entrySet()) {
            //если
            if (entry.getValue()) {
                db.update(DBHelper.TABLE_EVENTS, contentValues1,
                        DBHelper.KEY_ID + "= ?", new String[] {entry.getKey().toString()});
                db.update(DBHelper.TABLE_MONTH_EVENTS, contentValues1,
                        DBHelper.KEY_ORIGINAL_ID + "= ?", new String[] {entry.getKey().toString()});
            } else {
                db.update(DBHelper.TABLE_EVENTS, contentValues0,
                        DBHelper.KEY_ID + "= ?", new String[] {entry.getKey().toString()});
                db.update(DBHelper.TABLE_MONTH_EVENTS, contentValues0,
                        DBHelper.KEY_ORIGINAL_ID + "= ?", new String[] {entry.getKey().toString()});
            }
        }

    }
}
