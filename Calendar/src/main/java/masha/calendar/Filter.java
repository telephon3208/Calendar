package masha.calendar;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import masha.calendar.DBHelper;
import masha.calendar.MonthActivity;
import java.util.Calendar;
import java.util.ArrayList;

import static masha.calendar.DBHelper.TABLE_MONTH_EVENTS;

/**
 * Created by Маша on 30.01.2017.
 */

public class Filter {

    private final static String TAG = "Filter";
    SQLiteDatabase database;
    DBHelper dbHelper;

    ContentValues contentValues;

    void eventsFilter(Calendar c) {

        Log.d(TAG, "начало EventsFilter()");
        dbHelper = MonthActivity.dbHelper;
        try {
            database = dbHelper.getWritableDatabase();
            Log.d(TAG, "получена копия базы данных getWritableDatabase()");
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
            Log.d(TAG, "получена копия базы данных getReadableDatabase()");
        } catch (Exception e) {
            Log.d(TAG, "Ошибка чтения базы данных");
        }

        //фильтруем одноразовые события
        Log.d(TAG, "фильтруем одноразовые события");
        Cursor cursor = database.query(
                DBHelper.TABLE_EVENTS,
                null,
                "recur_type = ? AND year = ? AND month = ?", //условие для выборки
                new String [] {"0",
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.MONTH))},
                null,
                null,
                null);
        processEntries(cursor, c);

        //фильтруем ежегодные события
        Log.d(TAG, "фильтруем ежегодные события");
        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                "(recur_type = ? AND month = ?) AND (year < ? OR year = ?)", //условие для выборки
                new String [] {"1",
                        String.format("%s", c.get(Calendar.MONTH)),
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.YEAR))},
                null,
                null,
                null);
        processEntries(cursor, c);

        //фильтруем ежемесячные события
        Log.d(TAG, "фильтруем ежемесячные события");
        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                "(recur_type = ? AND year <= ?) AND month <= ?", //условие для выборки
                new String [] {"2",
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.MONTH))},
                null,
                null,
                null);
        processEntries(cursor, c);

        //Фильтруем еженедельные события
        Log.d(TAG, "фильтруем еженедельные события");
        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                "recur_type = ? AND ((year = ? AND month = ? AND date <= ?) OR " +
                        "(year = ? AND month < ?) OR (year < ?))", //условие для выборки
                new String [] {"3",
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.MONTH)),
                        String.format("%s", c.get(Calendar.DAY_OF_MONTH)),
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.MONTH)),
                        String.format("%s", c.get(Calendar.YEAR))},
                null,
                null,
                null);
        processEntries(cursor, c);

        //каждые несколько дней
        Log.d(TAG, "фильтруем события раз в несколько дней");
        cursor = database.query(
                DBHelper.TABLE_EVENTS,
                null,
                "recur_type = ? AND ((year = ? AND month = ? AND date <= ?) OR " +
                        "(year = ? AND month < ?) OR (year < ?))", //условие для выборки
                new String [] {"4",
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.MONTH)),
                        String.format("%s", c.get(Calendar.DAY_OF_MONTH)),
                        String.format("%s", c.get(Calendar.YEAR)),
                        String.format("%s", c.get(Calendar.MONTH)),
                        String.format("%s", c.get(Calendar.YEAR))},
                null,
                null,
                null);
        processEntries(cursor, c);

        cursor.close();
        dbHelper.close();
        database.close();
        Log.d(TAG, "конец фильтра");
    }

    void processEntries(Cursor cursor, Calendar c) {
        Log.d(TAG, "в объекте cursor " + cursor.getCount() + " записей");

        if (cursor.moveToFirst()) {     //проверка содержит ли cursor хоть одну запись
            int recurType = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_RECUR_TYPE));
            if (recurType < 3) {
                do {       //в случае когда recurType < 3
                    addEntry(cursor, c); //запись одной строчки курсора
                } while (cursor.moveToNext()) ;
            } else {
                //в случае когда recurType = 3 или 4
                do {
                    computeRecurDays(cursor, c);
                } while (cursor.moveToNext()) ;
            }
        } else {
            Log.d(TAG, "курсор не содержит записей");
        }
    }

    void addEntry(Cursor cursor, Calendar c) {  //запись в таблицу одной строчки курсора
        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
        int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
        int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
        int monthIndex = cursor.getColumnIndex(DBHelper.KEY_MONTH);
        int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
        int hourIndex = cursor.getColumnIndex(DBHelper.KEY_HOUR);
        int minuteIndex = cursor.getColumnIndex(DBHelper.KEY_MINUTE);
        int recurTypeIndex = cursor.getColumnIndex(DBHelper.KEY_RECUR_TYPE);
        int recurDaysIndex = cursor.getColumnIndex(DBHelper.KEY_RECUR_DAYS);
        int all_dayIndex = cursor.getColumnIndex(DBHelper.KEY_ALL_DAY);
        int tagIndex = cursor.getColumnIndex(DBHelper.KEY_TAG);

        contentValues = new ContentValues();

        if (cursor.getInt(recurTypeIndex) < 3) {
            contentValues.put("date", cursor.getInt(dateIndex));
        } else {
            contentValues.put("date", c.get(Calendar.DAY_OF_MONTH));
        }
        contentValues.put("month", c.get(Calendar.MONTH));
        contentValues.put("year", c.get(Calendar.YEAR));
        contentValues.put("title", cursor.getString(titleIndex));
        contentValues.put("description", cursor.getString(descriptionIndex));contentValues.put("hour", cursor.getInt(hourIndex));
        contentValues.put("minute", cursor.getInt(minuteIndex));
        contentValues.put("recur_type", cursor.getInt(recurTypeIndex));
        contentValues.put("recur_days", cursor.getInt(recurDaysIndex));
        contentValues.put("all_day", cursor.getInt(all_dayIndex));
        contentValues.put("tag", cursor.getString(tagIndex));
        contentValues.put("original_id", cursor.getInt(idIndex));

        database.insert(TABLE_MONTH_EVENTS, null, contentValues);

    }

    void computeRecurDays(Cursor cursor, Calendar c) {
        Log.d(TAG, "Начало метода computeRecurDays");
        //создаю календарь соответствующий найденному событию
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_YEAR)));
        cal.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MONTH)));
        cal.set(Calendar.DAY_OF_MONTH, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE)));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        //создаю копию отборажаемого календаря
        Calendar event = (Calendar) c.clone();
        event.set(Calendar.DAY_OF_MONTH, 1);
        event.set(Calendar.HOUR_OF_DAY, 0);
        event.set(Calendar.MINUTE, 0);
        event.set(Calendar.SECOND, 0);
        event.set(Calendar.MILLISECOND, 0);

        if (event.after(cal)) {   //если событие было в прошлом
            int displayMonth = event.get(Calendar.MONTH);
            int displayYear = event.get(Calendar.YEAR);
            int recurDays = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_RECUR_DAYS));
            if (recurDays == 0) recurDays = 7; //чтобы не делить на ноль

            if ((displayMonth == cal.get(Calendar.MONTH)
                    && (displayYear == cal.get(Calendar.YEAR)))) {

                event.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));

                while (displayYear == cal.get(Calendar.YEAR)) {
                    addEntry(cursor, event);
                    event.add(Calendar.DAY_OF_MONTH, recurDays);
                }
            } else {
                if (((displayMonth > cal.get(Calendar.MONTH)
                        && (displayYear == cal.get(Calendar.YEAR))))
                        || (displayYear > cal.get(Calendar.YEAR))) {

                    //если нецелое количество циклов то прибавляем по одному дню
                    while ((event.getTimeInMillis() - cal.getTimeInMillis())
                            % (1000 * 60 * 60 * 24 * recurDays) != 0) {
                        event.add(Calendar.DAY_OF_MONTH, 1);
                    }

                    //прибавляем по одному циклу и заносим в таблицу пока не кончится месяц
                    while (event.get(Calendar.MONTH) == displayMonth) {
                        addEntry(cursor, event);
                        event.add(Calendar.DAY_OF_MONTH, recurDays);
                    }
                }
            }
        }
    }
}
