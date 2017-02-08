package masha.calendar;

import masha.calendar.MonthActivity.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Маша on 06.02.2017.
 */

public class TagsFilter {

 //   private final static String MonthActivity.TAG = "MyLogs";
    SQLiteDatabase database;
    DBHelper dbHelper;
    Cursor cursor;

    ArrayList<String> tagsFilter() {
        Log.d(MonthActivity.TAG, "начало метода tagsFilter()");
        ArrayList<String> tags = new ArrayList<>();

        dbHelper = MonthActivity.dbHelper;
        try {
            database = dbHelper.getWritableDatabase();
            Log.d(MonthActivity.TAG, "получена копия базы данных getWritableDatabase()");
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
            Log.d(MonthActivity.TAG, "получена копия базы данных getReadableDatabase()");
        } catch (Exception e) {
            Log.d(MonthActivity.TAG, "Ошибка чтения базы данных");
        }

//        cursor = database.rawQuery("SELECT DISTINCT tag, checked FROM TABLE_EVENTS", null);
        cursor = database.rawQuery("SELECT DISTINCT ( " + DBHelper.KEY_TAG + " ), " + DBHelper.KEY_CHECKED + " FROM " + DBHelper.TABLE_EVENTS, null);

        Log.d(MonthActivity.TAG, "Создаем курсор");

        if (cursor.moveToFirst()) {     //проверка содержит ли cursor хоть одну запись
            do {
               Log.d(MonthActivity.TAG, "Запись тэга " + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TAG)));
               tags.add(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TAG)));
            } while (cursor.moveToNext()) ;
        } else {
            Log.d(MonthActivity.TAG, "курсор не содержит записей");
        }

        return tags;
    }

    ArrayList<Boolean> checkFilter() {
        Log.d(MonthActivity.TAG, "начало метода checkFilter()");
        ArrayList<Boolean> checkBoxes = new ArrayList<>();

        if (cursor.moveToFirst()) {     //проверка содержит ли cursor хоть одну запись
            do {
                Log.d(MonthActivity.TAG, "Запись тэга" + cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TAG)));
                if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_TAG)) == 1) {
                    checkBoxes.add(true);
                } else checkBoxes.add(false);
            } while (cursor.moveToNext()) ;
        } else {
            Log.d(MonthActivity.TAG, "курсор не содержит записей");
        }

        return checkBoxes;
    }
}
