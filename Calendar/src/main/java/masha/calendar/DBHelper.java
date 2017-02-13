package masha.calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import masha.calendar.MonthActivity;

/**
 * Created by Маша on 25.01.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 24;
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
        Log.d(MonthActivity.TAG, "конструктор DBHelper");
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
        addBaseEvents(db);

        //тестовый блок событий
        ContentValues contentValues = new ContentValues();
        String title[] = {
                "Ежемесячное событие",
                "Каждая пятница",
                "1 марта неповторяющееся событие",
                "Сережина ночная смена"};
        int date[] = { 15, 12, 1, 23};
        int month[] = { 0, 7, 2, 0};
        int year[] = { 1988, 2016, 2017, 2017};
        int recur_type[] = { 2, 3, 0, 4};
        int recur_days[] = { 0, 7, 0, 8};
        int all_day[] = { 1, 1, 1, 1 };
        String tags[] = {
                "test event",
                "test event",
                "test event",
                "test event"};

        // заполним таблицу
        for (int i = 0; i < 4; i++) {
            contentValues.put("title", title[i]);
            contentValues.put("date", date[i]);
            contentValues.put("month", month[i]);
            contentValues.put("year", year[i]);
            contentValues.put("recur_type", recur_type[i]);
            contentValues.put("recur_days", recur_days[i]);
            contentValues.put("all_day", all_day[i]);
            contentValues.put("tag", tags[i]);
            db.insert(TABLE_EVENTS, null, contentValues);

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_EVENTS);
        onCreate(db);
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
                "Государственные праздники",
                "Государственные праздники",
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
