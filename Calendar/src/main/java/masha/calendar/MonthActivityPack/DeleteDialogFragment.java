package masha.calendar.MonthActivityPack;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import masha.calendar.DBHelper;
import masha.calendar.R;

/**
 * Created by Маша on 02.03.2017.
 */

public class DeleteDialogFragment extends DialogFragment {

    long ID = 0;
    ListView listView;
    Cursor cursor;
    static ArrayList<Long> iDArray;

    public static DeleteDialogFragment newInstance(int num, ArrayList<Long> arrayList) {
        DeleteDialogFragment f = new DeleteDialogFragment();
        Log.d(MonthActivity.TAG,"DeleteDialogFragment конструктор");
        iDArray = arrayList;

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(MonthActivity.TAG,"DeleteDialogFragment метод onCreateView");
        getDialog().setTitle("Удалить события?");
        View v = inflater.inflate(R.layout.filter_dialog_layout, null);
        v.findViewById(R.id.btnYes).setOnClickListener(buttonsListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(buttonsListener);
        listView = (ListView) v.findViewById(R.id.listView);
        cursor = getEventsFromID();
        CursorAdapter adapter = new EditDialogAdapter(getActivity(),
                cursor, 0);
        listView.setAdapter(adapter);
        //   listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //id совпадает с id курсора
                ID = id;
            }
        });
        MonthActivity.setUpdateVariable("");
        return v;
    }

    Cursor getEventsFromID() {
        SQLiteDatabase database;

        try {
            database = MonthActivity.dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = MonthActivity.dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            return null;
        }

        String whereClause = "";
        for (Long item : iDArray) {
            whereClause += "_id = " + item;
        }
        Log.d(MonthActivity.TAG,"whereClause = " + whereClause);
        Cursor cur = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                whereClause,
                null,
                null,
                null,
                "title ASC");

        return cur;
    }

    View.OnClickListener buttonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnYes:
                    MonthActivity.dbHelper.deleteEvent(ID);
                    MonthActivity.setUpdateVariable("Обновить календарь");
                    break;
                case R.id.btnCancel:

                    break;
            }
            dismiss();
        }};



}
