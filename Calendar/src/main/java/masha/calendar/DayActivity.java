package masha.calendar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DayActivity extends AppCompatActivity {

    TextView dateView;
    int day, month, year, hour, minute;
    FloatingActionButton fAB;
    ListView listEvents;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ArrayList<HashMap<String, String>> myArrList;
    HashMap<String, String> map;

 //   private final static String TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        dateView = (TextView) findViewById(R.id.dateView);
        fAB = (FloatingActionButton) findViewById(R.id.fAB);
        listEvents = (ListView) findViewById(R.id.listEvents);

        fAB.setOnClickListener(fABonClickListener);
        listEvents.setOnItemClickListener(listEventsOnClickListener);
        dbHelper = new DBHelper(this);

        //region Получение данных и установка даты
        Intent intent = getIntent();
        dateView.setText(intent.getStringExtra("День недели") + ", ");

        day = Integer.parseInt(intent.getStringExtra("Число"));
        dateView.append(day + " ");

        month = intent.getIntExtra("Месяц", 0);
        switch (month) {
            case 0: dateView.append("Января");
                break;
            case 1: dateView.append("Февраля");
                break;
            case 2: dateView.append("Марта");
                break;
            case 3: dateView.append("Апреля");
                break;
            case 4: dateView.append("Мая");
                break;
            case 5: dateView.append("Июня");
                break;
            case 6: dateView.append("Июля");
                break;
            case 7: dateView.append("Августа");
                break;
            case 8: dateView.append("Сентября");
                break;
            case 9: dateView.append("Октября");
                break;
            case 10: dateView.append("Ноября");
                break;
            case 11: dateView.append("Декабря");
                break;
        }

        year = intent.getIntExtra("Год", 2017);
        hour = intent.getIntExtra("Час", 0);
        minute = intent.getIntExtra("Минута", 0);
        //endregion

        CursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.event_list_item, getDayEvents(),
                new String[] { "hour", "minute", "title", "description" },
                new int[] { R.id.hour, R.id.minute, R.id.title, R.id.description }, 0);
        listEvents.setAdapter(adapter);

    }

    //region Все что касается меню
    //формирование меню при нажатии кнопки Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.daymenu, menu);
        return true;
    }

    //действия при нажатии на пункты меню
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(MonthActivity.TAG,"Начало метода onOptionsItemSelected");
        switch (item.getItemId()){
            case R.id.menuItemCreateEventDayActivity:
                createEventActivity();
                return true;
            case R.id.menuItemEditEventDayActivity:
                showDialog(0); //вместо этого надо использовать DialogFragment
                return true;
            case R.id.Settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    public void createEventActivity() {
        Intent intent = new Intent(DayActivity.this, EventActivity.class);
        intent.putExtra("Число", day);
        intent.putExtra("Месяц", month);
        intent.putExtra("Год", year);
        intent.putExtra("Час", hour);
        intent.putExtra("Минута", minute);
        startActivity(intent);
        Log.d(MonthActivity.TAG,"Запуск EventActivity");
    }

    View.OnClickListener fABonClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //snackbar, тут можно добавить несколько пунктов
      //      Snackbar.make(v, "Добавить событие", Snackbar.LENGTH_LONG)
      //              .setAction("Action", null).show();

            switch (v.getId()) {
                case R.id.fAB:
                    createEventActivity();
                    break;
            }
        }
    };

    //region listEventsListener
    AdapterView.OnItemClickListener listEventsOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            try {
                database = dbHelper.getWritableDatabase();
            }
            catch (SQLiteException ex){
                database = dbHelper.getReadableDatabase();
            } catch (Exception e) {
                Log.d(MonthActivity.TAG,"Ошибка чтения БД");
            }

            Toast.makeText(getApplicationContext(), "Редактирование",
                    Toast.LENGTH_SHORT).show();

            Cursor cursor = database.query(        //критерии выборки из БД
                            DBHelper.TABLE_MONTH_EVENTS,
                            null,
                            "date = ? AND title = ?", //условие для выборки
                            new String [] {String.format("%s", day),
                                    ((TextView) view.findViewById(R.id.title)).getText().toString()},
                            null,
                            null,
                            null);
            cursor.moveToFirst();

            Intent intent = new Intent(DayActivity.this, EventActivity.class);
            intent.putExtra("Строка из MonthEvent", cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_ID)));
            startActivity(intent);
            Log.d(MonthActivity.TAG,"Запуск EventActivity");
        }
    };
    //endregion

    Cursor getDayEvents() {
        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(MonthActivity.TAG,"Ошибка чтения БД");
        }
        Cursor cursor = database.query(        //критерии выборки из БД
                DBHelper.TABLE_MONTH_EVENTS,
                null,
                "date = ?", //условие для выборки
                new String [] {String.format("%s", day)},
                null,
                null,
                "hour ASC");

        return cursor;
    }

    //region onCreateDialog()
   /* protected Dialog onCreateDialog(int id) {

        //получили массив чекбоксов
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Фильтр событий")
                .setCancelable(false)
                .setMultiChoiceItems(items, checkedItems,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                checkedItems[which] = isChecked;
                            }
                        })

                // Добавляем кнопки
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
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
                                dbHelper.checkBoxWriter(database, items, checkedItems);
                                database.close();
                                StringBuilder state = new StringBuilder();
                                for (int i = 0; i < items.length; i++) {
                                    state.append("" + items[i]);
                                    if (checkedItems[i])
                                        state.append(" выбран\n");
                                    else
                                        state.append(" не выбран\n");
                                }
                                Toast.makeText(getApplicationContext(),
                                        state.toString(), Toast.LENGTH_LONG)
                                        .show();
                                cleanColor();
                                highlightTodayButton();
                                displayEvents();
                            }
                        })

                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        });
        return builder.create();

    }*/
    //endregion
}

