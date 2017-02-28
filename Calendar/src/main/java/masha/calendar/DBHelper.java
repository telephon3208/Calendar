package masha.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import masha.calendar.MonthActivityPack.MonthActivity;

/**
 * Created by Маша on 25.01.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 35;
    public static final String DATABASE_NAME = "eventsDB";
    public static final String TABLE_EVENTS = "events";
    public static final String TABLE_MONTH_EVENTS = "monthevents";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DATE = "date";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_HOUR = "hour";
    public static final String KEY_MINUTE = "minute";
    public static final String KEY_RECUR_TYPE = "recur_type";
    public static final String KEY_RECUR_DAYS = "recur_days";
    public static final String KEY_ALL_DAY = "all_day";
    public static final String KEY_TAG = "tag";
    public static final String KEY_CHECKED = "checked";
    public static final String KEY_COLOR = "color";
    public static final String KEY_ORIGINAL_ID = "original_id";

 //   private static final String TAG = "MyLogs";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
   //     Log.d(MonthActivity.TAG, "конструктор DBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(MonthActivity.TAG, "создание новой БД");
        db.execSQL("create table " + TABLE_EVENTS + "(" +
                KEY_ID + " integer primary key," +
                KEY_TITLE + " text not null," +
                KEY_DESCRIPTION + " text," +
                KEY_DATE + " integer not null," +
                KEY_MONTH + " integer not null default 0," +
                KEY_YEAR + " integer," +
                KEY_HOUR + " integer," +
                KEY_MINUTE + " integer," +
                KEY_RECUR_TYPE + " integer not null default 0," +
                KEY_RECUR_DAYS + " integer," +
                KEY_ALL_DAY + " numeric not null default 1," +
                KEY_TAG + " text," +
                KEY_CHECKED + " numeric not null default 0," +
                KEY_COLOR + " blob" + ")");

        Log.d(MonthActivity.TAG, "создана таблица EVENTS");

        db.execSQL("create table " + TABLE_MONTH_EVENTS + "(" +
                KEY_ID + " integer primary key," +
                KEY_TITLE + " text not null," +
                KEY_DESCRIPTION + " text," +
                KEY_DATE + " integer not null," +
                KEY_MONTH + " integer not null default 0," +
                KEY_YEAR + " integer," +
                KEY_HOUR + " integer," +
                KEY_MINUTE + " integer," +
                KEY_RECUR_TYPE + " integer not null default 0," +
                KEY_RECUR_DAYS + " integer," +
                KEY_ALL_DAY + " numeric not null default 1," +
                KEY_TAG + " text," +
                KEY_CHECKED + " numeric not null default 0," +
                KEY_COLOR + " blob," +
                KEY_ORIGINAL_ID + " integer not null" +
                ")");
        Log.d(MonthActivity.TAG, "создана таблица MONTH_EVENTS");

   //     MonthActivity.tags.add("Государственные праздники");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > 31) {
            //сохраним в курсоре данные из таблицы
            Cursor cursorEventsTable = db.query(TABLE_EVENTS, null, null, null, null, null, null);
            //    Cursor cursorMonthEventsTable = db.query(TABLE_MONTH_EVENTS, null, null, null, null, null, null);
            Log.d(MonthActivity.TAG,"курсор : " + cursorEventsTable.getCount());

            //удалим старую таблицу и создадим новую
            db.execSQL("drop table if exists " + TABLE_EVENTS);
            db.execSQL("drop table if exists " + TABLE_MONTH_EVENTS);
            onCreate(db);
            Log.d(MonthActivity.TAG,"курсор : " + cursorEventsTable.getCount());
            //заполним её данными из старой таблицы
            ContentValues cv = new ContentValues();

            int titleIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_TITLE);
            int descriptionIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_DESCRIPTION);
            int dateIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_DATE);
            int monthIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_MONTH);
            int yearIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_YEAR);
            int hourIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_HOUR);
            int minuteIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_MINUTE);
            int recurTypeIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_RECUR_TYPE);
            int recurDaysIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_RECUR_DAYS);
            int allDayIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_ALL_DAY);
            int tagIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_TAG);
            int checkedIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_CHECKED);
            //   int colorIndex = cursorEventsTable.getColumnIndex(DBHelper.KEY_CHECKED);

            // заполним таблицу
            if (cursorEventsTable.moveToFirst()) {
                do {
                    cv.put(DBHelper.KEY_TITLE, cursorEventsTable.getString(titleIndex));
                    cv.put(DBHelper.KEY_DESCRIPTION, cursorEventsTable.getString(descriptionIndex));
                    cv.put(DBHelper.KEY_DATE, cursorEventsTable.getInt(dateIndex));
                    cv.put(DBHelper.KEY_MONTH, cursorEventsTable.getInt(monthIndex));
                    cv.put(DBHelper.KEY_YEAR, cursorEventsTable.getInt(yearIndex));
                    cv.put(DBHelper.KEY_HOUR, cursorEventsTable.getInt(hourIndex));
                    cv.put(DBHelper.KEY_MINUTE, cursorEventsTable.getInt(minuteIndex));
                    cv.put(DBHelper.KEY_RECUR_TYPE, cursorEventsTable.getInt(recurTypeIndex));
                    cv.put(DBHelper.KEY_RECUR_DAYS, cursorEventsTable.getInt(recurDaysIndex));
                    cv.put(DBHelper.KEY_ALL_DAY, cursorEventsTable.getInt(allDayIndex));
                    cv.put(DBHelper.KEY_TAG, cursorEventsTable.getString(tagIndex));
                    cv.put(DBHelper.KEY_CHECKED, cursorEventsTable.getInt(checkedIndex));
                    db.insert(TABLE_EVENTS, null, cv);
                } while (cursorEventsTable.moveToNext());
            } else {
                Log.d(MonthActivity.TAG,"записей в events не найдено");
            }

        } else {
            db.execSQL("drop table if exists " + TABLE_EVENTS);
            db.execSQL("drop table if exists " + TABLE_MONTH_EVENTS);
            onCreate(db);
            addBaseEvents(db);
        }

    }

    void addBaseEvents(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        String title[] = {
                "День защитника Отечества",
                "Международный женский день",
                "Праздник весны и труда",
                "День победы",
                "День России",
                "День народного единства",
                "Новый год"};
        int date[] = { 23, 8, 1, 9, 12, 4, 1 };
        int month[] = { 1, 2, 4, 4, 5, 10, 0 };
        int year[] = { 1922, 1965, 1917, 1945, 1990, 2005, 1919 };
        int recur_type[] = { 1, 1, 1, 1, 1, 1, 1 };
        int all_day[] = { 1, 1, 1, 1, 1, 1, 1 };
        String tags[] = {
                "Мужской праздник",
                "Женский праздник",
                "Государственные праздники",
                "Государственные праздники",
                "Государственные праздники",
                "Государственные праздники",
                "Государственные праздники"};

        // заполним таблицу
        for (int i = 0; i < 7; i++) {
            contentValues.put("title", title[i]);
            contentValues.put("date", date[i]);
            contentValues.put("month", month[i]);
            contentValues.put("year", year[i]);
            contentValues.put("recur_type", recur_type[i]);
            contentValues.put("all_day", all_day[i]);
            contentValues.put("tag", tags[i]);
            db.insert(TABLE_EVENTS, null, contentValues);

        }
    }

    void checkBoxWriter(SQLiteDatabase db, String[] s, boolean[] b) {
        Log.d(MonthActivity.TAG, "начало метода checkBoxWriter()");

        ContentValues contentValues0 = new ContentValues();
        ContentValues contentValues1 = new ContentValues();
        contentValues0.put(KEY_CHECKED, 0);
        contentValues1.put(KEY_CHECKED, 1);

        //прогоняем каждый тэг s[i] через цикл for
        for (int i = 0; i < s.length; i++) {
            Log.d(MonthActivity.TAG, "String[] имеет " + s.length + " членов");
            Log.d(MonthActivity.TAG, "s[0] = " + s[0]);
            Log.d(MonthActivity.TAG, "s[1] = " + s[1]);
            Log.d(MonthActivity.TAG, "boolean[] имеет " + b.length + " членов");
            Log.d(MonthActivity.TAG, "b[0] = " + b[0]);
            Log.d(MonthActivity.TAG, "b[1] = " + b[1]);

            if (b[i]) {
                db.update(TABLE_EVENTS, contentValues1, KEY_TAG + "= ?", new String[] {s[i]});
                db.update(TABLE_MONTH_EVENTS, contentValues1, KEY_TAG + "= ?", new String[] {s[i]});
            } else {
                db.update(TABLE_EVENTS, contentValues0, KEY_TAG + "= ?", new String[] {s[i]});
                db.update(TABLE_MONTH_EVENTS, contentValues0, KEY_TAG + "= ?", new String[] {s[i]});
            }
        }

    }



}
