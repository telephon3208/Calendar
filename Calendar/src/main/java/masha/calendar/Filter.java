package masha.calendar;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import masha.calendar.MonthActivityPack.MonthActivity;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;

import static masha.calendar.DBHelper.TABLE_MONTH_EVENTS;

/**
 * Created by Маша on 30.01.2017.
 */

public class Filter {

 //   private final static String TAG = "MyLogs";
    SQLiteDatabase database;
    DBHelper dbHelper;

    ContentValues contentValues;

    public void eventsFilter(Calendar c) {

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

        database.delete(DBHelper.TABLE_MONTH_EVENTS, null, null);

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
        String where = "(recur_type = 3 OR recur_type = 4) AND ((year = ? AND month <= ?) OR year < ?)";

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
    //    dbHelper.close();
   //     database.close();
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

    void computeRecurDays(Cursor cursor, Calendar C) {
        //создаем клон календаря, чтобы производить с ним манипуляции
        Calendar c = (Calendar) C.clone();
        int currentMonth = c.get(Calendar.MONTH);

        //создаю календарь соответствующий найденному событию
        Calendar firstEvent = (Calendar) C.clone();
        firstEvent.set(Calendar.YEAR, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_YEAR)));
        firstEvent.set(Calendar.MONTH, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MONTH)));
        firstEvent.set(Calendar.DATE, cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE)));
        firstEvent.set(Calendar.HOUR_OF_DAY, 0);
        firstEvent.set(Calendar.MINUTE, 0);
        firstEvent.set(Calendar.SECOND, 0);
        firstEvent.set(Calendar.MILLISECOND, 0);
        firstEvent.setLenient(false);

        int recurDays = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_RECUR_DAYS));
        if (recurDays == 0) {
            recurDays = 7; //чтобы не делить на ноль
        }
        long sutki = 1000 * 60 * 60 * 24; //сутки в миллисекундах
        long denominator = sutki * recurDays; //рассчитаем заранее знаменатель

        //если открыт месяц начала регулярных событий
        if ((firstEvent.get(Calendar.MONTH) == c.get(Calendar.MONTH) &&
                (firstEvent.get(Calendar.YEAR) == c.get(Calendar.YEAR)))) {

            while (firstEvent.get(Calendar.MONTH) == currentMonth) {
                addEntry(cursor, firstEvent);
                firstEvent.add(Calendar.DAY_OF_MONTH, recurDays);
            }

        } else { //если открыт последующий месяц
            //обнуляю календарь с
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            c.setLenient(false);

            long cMillis;
            long firstEventMillis;

            //повторяем пока не вышли за рамки текущего месяца
            while (c.get(Calendar.MONTH) == currentMonth) { //пока месяц не истек
                //это нужно чтобы компенсировать разницу часовых поясов (нашла в инете)
                cMillis = c.getTimeInMillis() + c.getTimeZone().getOffset(c.getTimeInMillis());
                firstEventMillis = firstEvent.getTimeInMillis() + firstEvent.getTimeZone().getOffset(firstEvent.getTimeInMillis());

                if ((cMillis - firstEventMillis) % denominator == 0) {
                    //если целое, то заносим день в базу данных и прибавляем цикл
                    addEntry(cursor, c);
                    c.add(Calendar.DAY_OF_MONTH, recurDays);
                } else {
                    //если нецелое, то прибавляем еще один день и проверяем снова
                    c.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }
    }
}
