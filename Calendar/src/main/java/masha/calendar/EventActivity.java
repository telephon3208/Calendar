package masha.calendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import masha.calendar.MonthActivityPack.MonthActivity;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MyLogs";

    int day, month, year, hour, minute, recurType = 0, recurDays = 0;
    Button saveBtn, cancel;
    CheckBox allDay;
    LinearLayout timeLayout, recurLayout;
    TextView timeView;
    AutoCompleteTextView autoCompleteTextView;
    EditText eventName, eventText;
    Spinner recurSpinner;
    DBHelper dbHelper;
    SQLiteDatabase database;
    boolean createEvent = true;
    Intent intent;
    long editEventID;

    NumberPicker dayPicker, monthPicker, yearPicker, hourPicker, minutePicker, recurDayPicker;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //region findViewById и setListener
        dayPicker = (NumberPicker) findViewById(R.id.dayPicker);
        monthPicker = (NumberPicker) findViewById(R.id.monthPicker);
        yearPicker = (NumberPicker) findViewById(R.id.yearPicker);
        hourPicker = (NumberPicker) findViewById(R.id.hourPicker);
        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        allDay = (CheckBox) findViewById(R.id.allDay);
        timeLayout = (LinearLayout) findViewById(R.id.timeLayout);
        timeView = (TextView) findViewById(R.id.timeView);
        recurSpinner = (Spinner) findViewById(R.id.spinner);
        recurDayPicker = (NumberPicker) findViewById(R.id.recurDayPicker);
        recurLayout = (LinearLayout) findViewById(R.id.recurLayout);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        cancel = (Button) findViewById(R.id.cancel);
        eventName = (EditText) findViewById(R.id.eventName);
        eventText = (EditText) findViewById(R.id.eventText);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.tagView);

        allDay.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        cancel.setOnClickListener(this);
        recurSpinner.setAdapter(new ArrayAdapter<String>(this,
                R.layout.event_activity_spinner_item,
                new String[] {"не повторять", "каждый год", "каждый месяц", "каждую неделю",
        "каждые несколько дней"}));
        recurSpinner.setOnItemSelectedListener(recurSpinnerListener);

        //endregion

        dbHelper = new DBHelper(this);

        //region настройка элементов интерфейса

        CursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                getAllTagsCursor(),
                new String[]{"tag"},
                new int[] {android.R.id.text1}, 0);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tag = ((TextView) view.findViewById(android.R.id.text1)).getText().toString();
                autoCompleteTextView.setText(tag);
            }
        });

        dayPicker.setMaxValue(31);
        dayPicker.setMinValue(1);
        dayPicker.setDividerPadding(5);

        monthPicker.setMaxValue(11);
        monthPicker.setMinValue(0);
        monthPicker.setDisplayedValues( new String[] { "Январь", "Февраль", "Март", "Апрель",
                "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" } );
        monthPicker.setDividerPadding(5);

        yearPicker.setMaxValue(3000);
        yearPicker.setMinValue(0);
        yearPicker.setDividerPadding(5);

        hourPicker.setMaxValue(23);
        hourPicker.setMinValue(0);
        hourPicker.setDividerPadding(5);

        minutePicker.setMaxValue(59);
        minutePicker.setMinValue(0);
        minutePicker.setDividerPadding(5);
        minutePicker.setDisplayedValues( new String[] { "00", "01", "02", "03",
                "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27",
                "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51",
                "52", "53", "54", "55", "56", "57", "58", "59"} );

        timeLayout.setVisibility(View.VISIBLE);
        timeView.setVisibility(View.VISIBLE);

        recurDayPicker.setMaxValue(60);
        recurDayPicker.setMinValue(1);
        recurDayPicker.setValue(3);

        recurLayout.setVisibility(View.VISIBLE);
        //endregion

        //region Получение intent и присвоение значений
        intent = getIntent();

        if (intent.hasExtra("Строка из MonthEvents")) {
            //режим редактирования события
            createEvent = false;
            editEventID = intent.getLongExtra("Строка из MonthEvents", 0);

            try {
                database = dbHelper.getWritableDatabase();
            }
            catch (SQLiteException ex){
                database = dbHelper.getReadableDatabase();
            } catch (Exception e) {
                Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            }

            Cursor cursor = database.query(DBHelper.TABLE_MONTH_EVENTS,
                    null,
                    "original_id = ?", //условие для выборки
                    new String [] {String.format("%s", editEventID)},
                    null,
                    null,
                    null);
            getCursorDate(cursor);

        } else if (intent.hasExtra("Строка из Events")) {
            //режим редактирования события
            createEvent = false;
            editEventID = intent.getLongExtra("Строка из Events", 0);

            try {
                database = dbHelper.getWritableDatabase();
            }
            catch (SQLiteException ex){
                database = dbHelper.getReadableDatabase();
            } catch (Exception e) {
                Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            }

            Cursor cursor = database.query(DBHelper.TABLE_EVENTS,
                    null,
                    "_id = ?", //условие для выборки
                    new String [] {String.format("%s", editEventID)},
                    null,
                    null,
                    null);
            if (cursor.moveToFirst()) {
                getCursorDate(cursor);
            } else {
                Log.d(MonthActivity.TAG,"Курсор пустой");
            }


        } else {
            //режим добавления события
            createEvent = true;
            day = intent.getIntExtra("Число", 1);
            month = intent.getIntExtra("Месяц", 0);
            year = intent.getIntExtra("Год", 2017);
            hour = intent.getIntExtra("Час", 0);
            minute = intent.getIntExtra("Минута", 0);
        }
        dayPicker.setValue(day);
        monthPicker.setValue(month);
        yearPicker.setValue(year);
        hourPicker.setValue(hour);
        minutePicker.setValue(minute);
        recurDayPicker.setValue(recurDays);

        //endregion

    }

    //region OnClickListener для всех кнопок
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.allDay:
                allDay = (CheckBox) v;
                if (allDay.isChecked()) {
                    timeLayout.setVisibility(View.GONE);
                    timeView.setVisibility(View.GONE);
                } else {
                    timeLayout.setVisibility(View.VISIBLE);
                    timeView.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.saveBtn:
                if (createEvent) {
                    createEvent();
                    onBackPressed();
                } else {
                    editEvent((int)editEventID);
                    onBackPressed();
                }
                break;
            case R.id.cancel:
                onBackPressed();
                break;
        }
        dbHelper.close();
    }
    //endregion

    void createEvent() {

        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
        }

        contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_TITLE, eventName.getText().toString());
        contentValues.put(DBHelper.KEY_DESCRIPTION, eventText.getText().toString());
        contentValues.put(DBHelper.KEY_DATE, dayPicker.getValue());
        contentValues.put(DBHelper.KEY_MONTH, monthPicker.getValue());
        contentValues.put(DBHelper.KEY_YEAR, yearPicker.getValue());
        contentValues.put(DBHelper.KEY_HOUR, hourPicker.getValue());
        contentValues.put(DBHelper.KEY_MINUTE, minutePicker.getValue());
        contentValues.put(DBHelper.KEY_RECUR_TYPE, recurType);
        if (recurType == 0) {
            contentValues.put(DBHelper.KEY_RECUR_DAYS, 0);
        } else {
            contentValues.put(DBHelper.KEY_RECUR_DAYS, recurDayPicker.getValue());
        }
        
        if (allDay.isChecked()) contentValues.put(DBHelper.KEY_ALL_DAY, 1);
        else contentValues.put(DBHelper.KEY_ALL_DAY, 0);
        contentValues.put(DBHelper.KEY_TAG, autoCompleteTextView.getText().toString());
//                    contentValues.put(DBHelper.KEY_COLOR, eventText.getText().toString());

        database.insert(DBHelper.TABLE_EVENTS, null, contentValues);

    }

    void editEvent(int id) {
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
        }

        contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_TITLE, eventName.getText().toString());
        contentValues.put(DBHelper.KEY_DESCRIPTION, eventText.getText().toString());
        contentValues.put(DBHelper.KEY_DATE, dayPicker.getValue());
        contentValues.put(DBHelper.KEY_MONTH, monthPicker.getValue());
        contentValues.put(DBHelper.KEY_YEAR, yearPicker.getValue());
        contentValues.put(DBHelper.KEY_RECUR_TYPE, recurType);
        switch (recurType) {
            case 0:
                contentValues.put(DBHelper.KEY_RECUR_DAYS, 0);
                break;
            default:
                contentValues.put(DBHelper.KEY_RECUR_DAYS, recurDayPicker.getValue());
        }

        if (allDay.isChecked()) {
            contentValues.put(DBHelper.KEY_ALL_DAY, 1);
        }
        else {
            contentValues.put(DBHelper.KEY_HOUR, hourPicker.getValue());
            contentValues.put(DBHelper.KEY_MINUTE, minutePicker.getValue());
            contentValues.put(DBHelper.KEY_ALL_DAY, 0);
        }

        contentValues.put(DBHelper.KEY_TAG, autoCompleteTextView.getText().toString());
//                    contentValues.put(DBHelper.KEY_COLOR, eventText.getText().toString());

        //строка из TableMonthEvents
        Cursor cursorTME = database.query(DBHelper.TABLE_EVENTS,
                null,
                "_id = ?", //условие для выборки
                new String [] {String.format("%s", id)},
                null,
                null,
                null);
        cursorTME.moveToFirst();

        database.update(DBHelper.TABLE_EVENTS, contentValues, "_id = ?",
                new String[] {String.format("%s", id)});
    }

    //region Listener для spinner
    AdapterView.OnItemSelectedListener recurSpinnerListener =
    new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            recurType = position;
            switch (position) {
                case 1: //повторять каждый год
                    recurLayout.setVisibility(View.GONE);
                    break;
                case 2: //повторять каждый месяц
                    recurLayout.setVisibility(View.GONE);
                    break;
                case 3: //повторять каждую неделю
                    recurLayout.setVisibility(View.GONE);
                    recurDays = 7;
                    break;
                case 4: //повторять каждые несколько дней
                    recurLayout.setVisibility(View.VISIBLE);
                    break;
                default: //не повторять
                    recurLayout.setVisibility(View.GONE);
                    recurDays = 0;
                    recurType = 0;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    //endregion

    //получение данных из курсора
    void getCursorDate(Cursor cursor) {
        cursor.moveToFirst();

        eventName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TITLE)));
        eventText.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION)));
        autoCompleteTextView.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TAG)));
        day = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE));
        month = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MONTH));
        year = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_YEAR));
        hour = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_HOUR));
        minute = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_MINUTE));
        if (cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ALL_DAY)) == 1) {
            allDay.setChecked(true);
            timeLayout.setVisibility(View.GONE);
            timeView.setVisibility(View.GONE);
        }
        recurType = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_RECUR_TYPE));
        if (recurType <= 3) {
            recurLayout.setVisibility(View.GONE);
        }
        recurSpinner.setSelection(recurType);
        recurDays = cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_RECUR_DAYS));
    }

    Cursor getAllTagsCursor() {
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
        }

        Cursor cursor = database.query(DBHelper.TABLE_EVENTS, new String[] {"_id", "tag"},
                null, null, "tag", null, null);

        return cursor;
    }
}
