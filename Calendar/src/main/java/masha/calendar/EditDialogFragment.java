package masha.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;

/**
 * Created by Маша on 17.02.2017.
 */

public class EditDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "myLogs";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.edit, this)
                .setNeutralButton(R.string.delete, this)
                .setNegativeButton(R.string.cancel, this);

        //        .setMessage(R.string.message_text);

        CursorAdapter adapter = new CustomCursorAdapter(getActivity(),
                getAllEvents(), 0);
        adb.setTitle("Выберите событие");
        adb.setSingleChoiceItems(adapter, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, "нажат пункт: " + which);
            }
        });
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                i = R.string.edit;
                break;
            case Dialog.BUTTON_NEGATIVE:
                i = R.string.cancel;
                break;
            case Dialog.BUTTON_NEUTRAL:
                i = R.string.delete;
                break;
        }
        if (i > 0)
            Log.d(LOG_TAG, "Dialog 2: " + getResources().getString(i));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
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
}

