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

 //   private final static String TAG = "MyLogs";
    SQLiteDatabase database;
    DBHelper dbHelper;

    ContentValues contentValues;

    void eventsFilter(Calendar c) {

        Log.d(MonthActivity.TAG, "начало EventsFilter()");
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

   //     String date = String.format("%s", c.get(Calendar.DAY_OF_MONTH));
        String month = String.format("%s", c.get(Calendar.MONTH));
        String year = String.format("%s", c.get(Calendar.YEAR));

        //фильтруем одноразовые события
        Cursor cursor = database.query(
                DBHelper.TABLE_EVENTS,
                null,
                "recur_type = ? AND year = ? AND month = ?", //условие для выборки
                new String [] {"0", year, month},
                null,
                null,
                null);
        processEntries(cursor, c);

        //фильтруем ежегодные события
        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                "recur_type = ? AND month = ? AND year <= ?", //условие для выборки
                new String [] {"1", month, year},
                null,
                null,
                null);
        processEntries(cursor, c);

        //фильтруем ежемесячные события
        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                "recur_type = ? AND year <= ? AND month <= ?", //условие для выборки
                new String [] {"2", year, month},
                null,
                null,
                null);
        processEntries(cursor, c);

        //фильтруем события раз в несколько дней
        String where = "recur_type IN (3, 4) AND ((year = ? AND month <= ?) OR year < ?)";

        cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_EVENTS,
                null,
                where,
                new String [] {year, month, year},
                null,
                null,
                null);

        processEntries(cursor, c);

        cursor.close();
        dbHelper.close();
        database.close();
    }

    void processEntries(Cursor cursor, Calendar c) {

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
            Log.d(MonthActivity.TAG, "курсор не содержит записей");
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
        int checkedIndex = cursor.getColumnIndex(DBHelper.KEY_CHECKED);

        contentValues = new ContentValues();

        if (cursor.getInt(recurTypeIndex) < 3) {
            contentValues.put("date", cursor.getInt(dateIndex));
        } else {
            contentValues.put("date", c.get(Calendar.DAY_OF_MONTH));
        }
        contentValues.put("month", c.get(Calendar.MONTH));
        contentValues.put("year", c.get(Calendar.YEAR));
        contentValues.put("title", cursor.getString(titleIndex));
        contentValues.put("description", cursor.getString(descriptionIndex));
        contentValues.put("hour", cursor.getInt(hourIndex));
        contentValues.put("minute", cursor.getInt(minuteIndex));
        contentValues.put("recur_type", cursor.getInt(recurTypeIndex));
        contentValues.put("recur_days", cursor.getInt(recurDaysIndex));
        contentValues.put("all_day", cursor.getInt(all_dayIndex));
        contentValues.put("tag", cursor.getString(tagIndex));
        contentValues.put("checked", cursor.getInt(checkedIndex));
        contentValues.put("original_id", cursor.getInt(idIndex));

        database.insert(TABLE_MONTH_EVENTS, null, contentValues);

    }

    void computeRecurDays(Cursor cursor, Calendar c) {
        Log.d(MonthActivity.TAG, "новое событие");
        //создаю календарь соответствующий найденному событию
        Calendar firstEvent = Calendar.getInstance();
        firstEvent.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_YEAR)));
        firstEvent.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MONTH)));
        firstEvent.set(Calendar.DAY_OF_MONTH, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE)));
        //выставляем нулевое время
        firstEvent.set(Calendar.HOUR_OF_DAY, 0);
        firstEvent.set(Calendar.MINUTE, 0);
        firstEvent.set(Calendar.SECOND, 0);
        firstEvent.set(Calendar.MILLISECOND, 0);
      //  firstEvent.setLenient(true);

        //создаю копию отборажаемого календаря
        Calendar nextEvent = (Calendar) c.clone();
        nextEvent.set(Calendar.DAY_OF_MONTH, 1);
        //выставляем нулевое время
        nextEvent.set(Calendar.HOUR_OF_DAY, 0);
        nextEvent.set(Calendar.MINUTE, 0);
        nextEvent.set(Calendar.SECOND, 0);
        nextEvent.set(Calendar.MILLISECOND, 0);
      //  nextEvent.setLenient(true);

        int recurDays = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_RECUR_DAYS));
        if (recurDays == 0) recurDays = 7; //чтобы не делить на ноль
        int sutki = 1000 * 60 * 60 * 24; //сутки в миллисекундах
        long denominator = sutki * recurDays; //рассчитаем заранее знаменатель

        //если открыт месяц начала регулярных событий
        if ((firstEvent.get(Calendar.MONTH) == c.get(Calendar.MONTH) &&
                (firstEvent.get(Calendar.YEAR) == c.get(Calendar.YEAR)))) {
            while (c.get(Calendar.MONTH) == firstEvent.get(Calendar.YEAR)) {
                addEntry(cursor, c);
                c.add(Calendar.DAY_OF_MONTH, recurDays);
            }
        } else { //если открыт последующий месяц

            //повторяем пока не вышли за рамки текущего месяца
            while (c.get(Calendar.MONTH) == nextEvent.get(Calendar.MONTH)) { //пока месяц не истек
        //        Log.d(MonthActivity.TAG, "тело цикла");
                //проверяем целое ли количество циклов прошло
                if ((nextEvent.getTimeInMillis() - firstEvent.getTimeInMillis())
                        % denominator != 0) {
                    Log.d(MonthActivity.TAG, "нецелое количество циклов");
/*                    Log.d(MonthActivity.TAG, "миллисекунд в nextEvent : " + nextEvent.getTimeInMillis());
                    Log.d(MonthActivity.TAG, "миллисекунд в firstEvent : " + firstEvent.getTimeInMillis());
                    Log.d(MonthActivity.TAG, "делитель : " + denominator);
                    double d = ((nextEvent.getTimeInMillis() - firstEvent.getTimeInMillis())
                            / denominator);
                    Log.d(MonthActivity.TAG, "количество недель разделяющих события : " + d);*/
                    Log.d(MonthActivity.TAG, "остаток от деления : " + (nextEvent.getTimeInMillis() - firstEvent.getTimeInMillis())
                            % denominator);
                    //если нецелое, то прибавляем еще один день и проверяем снова
                    nextEvent.add(Calendar.MILLISECOND, sutki);
                } else {
                    Log.d(MonthActivity.TAG, "целое количество циклов");
                    //если целое, то заносим день в базу данных и прибавляем цикл
                    addEntry(cursor, nextEvent);
                    nextEvent.add(Calendar.DAY_OF_MONTH, recurDays);
                  /*  nextEvent.set(Calendar.HOUR_OF_DAY, 0);
                    nextEvent.set(Calendar.MINUTE, 0);
                    nextEvent.set(Calendar.SECOND, 0);
                    nextEvent.set(Calendar.MILLISECOND, 0);*/
                }
            }
        }
    }
}
