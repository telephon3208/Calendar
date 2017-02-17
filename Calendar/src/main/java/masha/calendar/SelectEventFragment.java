package masha.calendar;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Маша on 16.02.2017.
 */

public class SelectEventFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private View form=null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        form= getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_fragment_events, null);

        ListView listView = (ListView) form.findViewById(R.id.eventsListMonthActivity);

        CursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.event_list_item2, getCursor(),
                new String[] { "title", "description", "date", "year" },
                new int[] { R.id.title, R.id.description, R.id.date, R.id.year }, 0);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(listItemOnClickListener);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return(builder.setTitle("Выберите событие для редактирования").setView(form)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, null).create());
    }


    //происходит при нажатии на кнопку ОК
    @Override
    public void onClick(DialogInterface dialog, int which) {

/*        EditText loginBox=(EditText)form.findViewById(R.id.login);
        EditText passwordBox=(EditText)form.findViewById(R.id.password);
        String login = loginBox.getText().toString();
        String password = passwordBox.getText().toString();

        TextView loginText = (TextView)getActivity().findViewById(R.id.loginText);
        TextView passwordText = (TextView)getActivity().findViewById(R.id.passwordText);
        loginText.setText(login);
        passwordText.setText(password);*/
    }

    @Override
    public void onDismiss(DialogInterface unused) {
        super.onDismiss(unused);
    }

    //происходит при нажатии на кнопку CANCEL
    @Override
    public void onCancel(DialogInterface unused) {
        super.onCancel(unused);
    }


    Cursor getCursor() {
        DBHelper dbHelper = new DBHelper(getActivity());
        SQLiteDatabase database;
        Cursor cursor;
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            return null;
        }

        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                null, //условие для выборки
                null,
                null,
                null,
                "title ASC");

        return cursor;
    }

    //region listEventsListener
    AdapterView.OnItemClickListener listItemOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            DBHelper dbHelper = new DBHelper(getActivity());
            SQLiteDatabase database = null;
            Cursor cursor;
            try {
                database = dbHelper.getWritableDatabase();
            } catch (SQLiteException ex){
                database = dbHelper.getReadableDatabase();
            } catch (Exception e) {
                Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            }


            Toast.makeText(getActivity(), "Редактирование",
                    Toast.LENGTH_SHORT).show();

            cursor = database.query(        //критерии выборки из БД
                    DBHelper.TABLE_EVENTS,
                    null,
                    "date = ? AND title = ?", //условие для выборки
                    new String [] {((TextView) view.findViewById(R.id.date)).getText().toString(),
                            ((TextView) view.findViewById(R.id.title)).getText().toString()},
                    null,
                    null,
                    null);
            cursor.moveToFirst();

            Intent intent = new Intent(getActivity(), EventActivity.class);
            intent.putExtra("Строка из Event", cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
            startActivity(intent);
            Log.d(MonthActivity.TAG,"Запуск EventActivity");
        }
    };
    //endregion
}
