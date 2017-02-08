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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

public class EventActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "MyLogs";

    int day, month, year, hour, minute, recurType = 0, recurDays = 0;
    Button saveBtn, readBtn, clearBtn;
    CheckBox allDay;
    LinearLayout timeLayout, recurLayout;
    TextView timeView;
    EditText eventName, eventText, tagView;
    Spinner recurSpinner;
    DBHelper dbHelper;
    SQLiteDatabase database;

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
        readBtn = (Button) findViewById(R.id.readBtn);
        clearBtn = (Button) findViewById(R.id.clearBtn);
        eventName = (EditText) findViewById(R.id.eventName);
        eventText = (EditText) findViewById(R.id.eventText);
        tagView = (EditText) findViewById(R.id.tagView);

        allDay.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        readBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        recurSpinner.setOnItemSelectedListener(recurSpinnerListener);
        //endregion

        //region Получение intent и настройка NumberPickers
        Intent intent = getIntent();

        day = intent.getIntExtra("Число", 1);
        dayPicker.setMaxValue(31);
        dayPicker.setMinValue(1);
        dayPicker.setValue(day);

        month = intent.getIntExtra("Месяц", 0);
        monthPicker.setMaxValue(12);
        monthPicker.setMinValue(1);
        monthPicker.setDisplayedValues( new String[] { "Январь", "Февраль", "Март", "Апрель",
                "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь" } );
        monthPicker.setValue(month + 1);

        year = intent.getIntExtra("Год", 2017);
        yearPicker.setMaxValue(3000);
        yearPicker.setMinValue(0);
        yearPicker.setValue(year);

        hour = intent.getIntExtra("Час", 0);
        hourPicker.setMaxValue(24);
        hourPicker.setMinValue(0);
        hourPicker.setValue(hour);

        minute = intent.getIntExtra("Минута", 0);
        minutePicker.setMaxValue(60);
        minutePicker.setMinValue(0);
        minutePicker.setValue(minute);

        recurDayPicker.setMaxValue(60);
        recurDayPicker.setMinValue(1);
        recurDayPicker.setValue(3);
        //endregion

        dbHelper = new DBHelper(this);

    }

    //region OnClickListener
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
                writeEvent();
                break;
            case R.id.readBtn:
                try {
                    database = dbHelper.getWritableDatabase();
                }
                catch (SQLiteException ex){
                    database = dbHelper.getReadableDatabase();
                } catch (Exception e) {
                    Log.d(TAG,"Ошибка чтения БД");
                }
                Cursor cursor = database.query(DBHelper.TABLE_EVENTS,
                        null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    int descriptionIndex = cursor.getColumnIndex(DBHelper.KEY_DESCRIPTION);
                    int dateIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
                    int monthIndex = cursor.getColumnIndex(DBHelper.KEY_MONTH);
                    int yearIndex = cursor.getColumnIndex(DBHelper.KEY_YEAR);
                    int recurTypeIndex = cursor.getColumnIndex(DBHelper.KEY_RECUR_TYPE);
                    int recurDaysIndex = cursor.getColumnIndex(DBHelper.KEY_RECUR_DAYS);
                    int tagIndex = cursor.getColumnIndex(DBHelper.KEY_TAG);
                    Log.d(TAG,"Вся база данных:");
                    do {
                        Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                                ", title = " + cursor.getString(titleIndex) +
                                ", description = " + cursor.getString(descriptionIndex) +
                                ", date = " + cursor.getString(dateIndex) +
                                ", month = " + cursor.getString(monthIndex) +
                                ", year = " + cursor.getString(yearIndex) +
                                ", recur_type = " + cursor.getString(recurTypeIndex) +
                                ", recur_days = " + cursor.getString(recurDaysIndex) +
                                ", tag = " + cursor.getString(tagIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d(TAG,"записей не найдено");

                cursor.close();
                break;
            case R.id.clearBtn:
                try {
                    database = dbHelper.getWritableDatabase();
                }
                catch (SQLiteException ex){
                    database = dbHelper.getReadableDatabase();
                } catch (Exception e) {
                    Log.d(TAG,"Ошибка чтения БД");
                }
                database.delete(DBHelper.TABLE_EVENTS, null, null);
                break;

        }
        dbHelper.close();
    }
    //endregion

    void writeEvent() {
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        }
        Log.d(TAG, "получаем копию базы данных");

        contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_TITLE, eventName.getText().toString());
        contentValues.put(DBHelper.KEY_DESCRIPTION, eventText.getText().toString());
        contentValues.put(DBHelper.KEY_DATE, dayPicker.getValue());
        contentValues.put(DBHelper.KEY_MONTH, monthPicker.getValue() - 1);
        contentValues.put(DBHelper.KEY_YEAR, yearPicker.getValue());
        contentValues.put(DBHelper.KEY_HOUR, hourPicker.getValue());
        contentValues.put(DBHelper.KEY_MINUTE, minutePicker.getValue());
        contentValues.put(DBHelper.KEY_RECUR_TYPE, recurType);
        contentValues.put(DBHelper.KEY_RECUR_DAYS, recurDayPicker.getValue());
        if (allDay.isChecked()) contentValues.put(DBHelper.KEY_ALL_DAY, 1);
        else contentValues.put(DBHelper.KEY_ALL_DAY, 0);
        contentValues.put(DBHelper.KEY_TAG, tagView.getText().toString());
//                    contentValues.put(DBHelper.KEY_COLOR, eventText.getText().toString());

        database.insert(DBHelper.TABLE_EVENTS, null, contentValues);
        dbHelper.close();
    }

    //region Listener для spinner
    AdapterView.OnItemSelectedListener recurSpinnerListener =
            new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (recurSpinner.getSelectedItemPosition()) {
                        case 1: //повторять каждый год
                            recurType = 1;
                            recurLayout.setVisibility(View.GONE);
                            break;
                        case 2: //повторять каждый месяц
                            recurType = 2;
                            recurLayout.setVisibility(View.GONE);
                            break;
                        case 3: //повторять каждую неделю
                            recurType = 3;
                            recurLayout.setVisibility(View.GONE);
                            recurDays = 7;
                            break;
                        case 4: //повторять каждые несколько дней
                            recurLayout.setVisibility(View.VISIBLE);
                            recurType = 4;
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
}
