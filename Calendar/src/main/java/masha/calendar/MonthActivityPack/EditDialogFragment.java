package masha.calendar.MonthActivityPack;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import masha.calendar.DBHelper;
import masha.calendar.EventActivity;
import masha.calendar.R;

/**
 * Created by Маша on 17.02.2017.
 */

public class EditDialogFragment extends DialogFragment {

    CursorAdapter adapter;
    long eventID = 0;
    ListView listView;
    Cursor cursor;

    public static EditDialogFragment newInstance(int num) {
        EditDialogFragment f = new EditDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //onCreateDialog() используется для формирования диалога с использованием AlertDialog.Builder
    //для создания полностью кастомного диалога, надо использовать метод onCreateView()
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Выберите событие");
        View v = inflater.inflate(R.layout.edit_dialog_layout, null);
        v.findViewById(R.id.btnEdit).setOnClickListener(buttonsListener);
        v.findViewById(R.id.btnCancel).setOnClickListener(buttonsListener);
        listView = (ListView) v.findViewById(R.id.listView);
        cursor = getAllEvents();
        CursorAdapter adapter = new EditDialogAdapter(getActivity(),
                cursor, 0);
        Log.d(MonthActivity.TAG, "stableId: " + adapter.hasStableIds());
        listView.setAdapter(adapter);
        //   listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //id совпадает с id курсора
                Log.d(MonthActivity.TAG, "id : " + id);
                Log.d(MonthActivity.TAG, "title : " + ((TextView) view.findViewById(R.id.title)).getText().toString());
                parent.setBackgroundResource(R.color.colorPrimaryLight);

                if (id == eventID) {
                    view.setBackgroundResource(R.color.colorPrimaryLight);
                } else {
                    view.setBackgroundResource(R.color.colorAccentLight);
                }
                eventID = id;
            }
        });

        return v;
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(MonthActivity.TAG, "EditDialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(MonthActivity.TAG, "EditDialog: onCancel");
    }

    Cursor getAllEvents() {
        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase database;

        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            return null;
        }
        Cursor cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                null,
                null,
                null,
                null,
                "title ASC");

        return cursor;
    }

    View.OnClickListener buttonsListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnEdit:
                    openEventActivity(eventID);
                    MonthActivity.setUpdateVariable("Обновить календарь");
                    break;
                case R.id.btnDelete:
                    MonthActivity.setUpdateVariable("Обновить календарь");
                    break;
                case R.id.btnCancel:

                    break;
        }
            dismiss();
    }};

    void openEventActivity(long id) {

        if (id != 0) {
            Toast.makeText(getActivity(), "Редактирование",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getActivity(), EventActivity.class);
            intent.putExtra("Строка из Events", id);
            startActivity(intent);
            Log.d(MonthActivity.TAG,"Запуск EventActivity");
            dismiss();
        } else {
            Toast.makeText(getActivity(), "Выберите событие",
                    Toast.LENGTH_SHORT).show();
        }

    }



}


