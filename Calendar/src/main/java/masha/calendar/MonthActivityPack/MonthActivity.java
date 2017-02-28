package masha.calendar.MonthActivityPack;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import masha.calendar.DBHelper;
import masha.calendar.DayActivity;
import masha.calendar.Filter;
import masha.calendar.R;

public class MonthActivity extends AppCompatActivity {

    //region Объявление переменных
    Handler h;

    TextView monthView, timeView, textView;
    AlertDialog.Builder ad;
    Context context;
    ListView listView2;
    TableRow week1, week2, week3, week4, week5, week6;

    Button b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14;
    Button b15, b16, b17, b18, b19, b20, b21, b22, b23, b24, b25, b26;
    Button b27, b28, b29, b30, b31, b32, b33, b34, b35;
    Button b36, b37, b38, b39, b40, b41, b42, arrowLeft, arrowRight;
    Button[] w1 = {b1, b2, b3, b4, b5, b6, b7};
    Button[] w2 = {b8, b9, b10, b11, b12, b13, b14};
    Button[] w3 = {b15, b16, b17, b18, b19, b20, b21};
    Button[] w4 = {b22, b23, b24, b25, b26, b27, b28};
    Button[] w5 = {b29, b30, b31, b32, b33, b34, b35};
    Button[] w6 = {b36, b37, b38, b39, b40, b41, b42};
    Button today, button1, button2;

    public static Calendar rightNow, displayMonth;


    public final static String TAG = "MyLogs";
    SQLiteDatabase database;
    public static DBHelper dbHelper;
    Filter filter;
    DialogFragment filterDialog, editDialog;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        //region findViewById
        monthView = (TextView) findViewById(R.id.monthView);
        timeView = (TextView) findViewById(R.id.timeView);

        week1 = (TableRow) findViewById(R.id.week1);
        week2 = (TableRow) findViewById(R.id.week2);
        week3 = (TableRow) findViewById(R.id.week3);
        week4 = (TableRow) findViewById(R.id.week4);
        week5 = (TableRow) findViewById(R.id.week5);
        week6 = (TableRow) findViewById(R.id.week6);

        w1[0] = (Button) findViewById(R.id.b1);
        w1[1] = (Button) findViewById(R.id.b2);
        w1[2] = (Button) findViewById(R.id.b3);
        w1[3] = (Button) findViewById(R.id.b4);
        w1[4] = (Button) findViewById(R.id.b5);
        w1[5] = (Button) findViewById(R.id.b6);
        w1[6] = (Button) findViewById(R.id.b7);
        w2[0] = (Button) findViewById(R.id.b8);
        w2[1] = (Button) findViewById(R.id.b9);
        w2[2] = (Button) findViewById(R.id.b10);
        w2[3] = (Button) findViewById(R.id.b11);
        w2[4] = (Button) findViewById(R.id.b12);
        w2[5] = (Button) findViewById(R.id.b13);
        w2[6] = (Button) findViewById(R.id.b14);
        w3[0] = (Button) findViewById(R.id.b15);
        w3[1] = (Button) findViewById(R.id.b16);
        w3[2] = (Button) findViewById(R.id.b17);
        w3[3] = (Button) findViewById(R.id.b18);
        w3[4] = (Button) findViewById(R.id.b19);
        w3[5] = (Button) findViewById(R.id.b20);
        w3[6] = (Button) findViewById(R.id.b21);
        w4[0] = (Button) findViewById(R.id.b22);
        w4[1] = (Button) findViewById(R.id.b23);
        w4[2] = (Button) findViewById(R.id.b24);
        w4[3] = (Button) findViewById(R.id.b25);
        w4[4] = (Button) findViewById(R.id.b26);
        w4[5] = (Button) findViewById(R.id.b27);
        w4[6] = (Button) findViewById(R.id.b28);
        w5[0] = (Button) findViewById(R.id.b29);
        w5[1] = (Button) findViewById(R.id.b30);
        w5[2] = (Button) findViewById(R.id.b31);
        w5[3] = (Button) findViewById(R.id.b32);
        w5[4] = (Button) findViewById(R.id.b33);
        w5[5] = (Button) findViewById(R.id.b34);
        w5[6] = (Button) findViewById(R.id.b35);
        w6[0] = (Button) findViewById(R.id.b36);
        w6[1] = (Button) findViewById(R.id.b37);
        w6[2] = (Button) findViewById(R.id.b38);
        w6[3] = (Button) findViewById(R.id.b39);
        w6[4] = (Button) findViewById(R.id.b40);
        w6[5] = (Button) findViewById(R.id.b41);
        w6[6] = (Button) findViewById(R.id.b42);
        arrowLeft = (Button) findViewById(R.id.arrowLeft);
        arrowRight = (Button) findViewById(R.id.arrowRight);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        //endregion

        //region SetListeners
        arrowLeft.setOnClickListener(arrowsListener);
        arrowRight.setOnClickListener(arrowsListener);

        w1[0].setOnClickListener(buttonsListener);
        w1[1].setOnClickListener(buttonsListener);
        w1[2].setOnClickListener(buttonsListener);
        w1[3].setOnClickListener(buttonsListener);
        w1[4].setOnClickListener(buttonsListener);
        w1[5].setOnClickListener(buttonsListener);
        w1[6].setOnClickListener(buttonsListener);
        w2[0].setOnClickListener(buttonsListener);
        w2[1].setOnClickListener(buttonsListener);
        w2[2].setOnClickListener(buttonsListener);
        w2[3].setOnClickListener(buttonsListener);
        w2[4].setOnClickListener(buttonsListener);
        w2[5].setOnClickListener(buttonsListener);
        w2[6].setOnClickListener(buttonsListener);
        w3[0].setOnClickListener(buttonsListener);
        w3[1].setOnClickListener(buttonsListener);
        w3[2].setOnClickListener(buttonsListener);
        w3[3].setOnClickListener(buttonsListener);
        w3[4].setOnClickListener(buttonsListener);
        w3[5].setOnClickListener(buttonsListener);
        w3[6].setOnClickListener(buttonsListener);
        w4[0].setOnClickListener(buttonsListener);
        w4[1].setOnClickListener(buttonsListener);
        w4[2].setOnClickListener(buttonsListener);
        w4[3].setOnClickListener(buttonsListener);
        w4[4].setOnClickListener(buttonsListener);
        w4[5].setOnClickListener(buttonsListener);
        w4[6].setOnClickListener(buttonsListener);
        w5[0].setOnClickListener(buttonsListener);
        w5[1].setOnClickListener(buttonsListener);
        w5[2].setOnClickListener(buttonsListener);
        w5[3].setOnClickListener(buttonsListener);
        w5[4].setOnClickListener(buttonsListener);
        w5[5].setOnClickListener(buttonsListener);
        w5[6].setOnClickListener(buttonsListener);
        w6[0].setOnClickListener(buttonsListener);
        w6[1].setOnClickListener(buttonsListener);
        w6[2].setOnClickListener(buttonsListener);
        w6[3].setOnClickListener(buttonsListener);
        w6[4].setOnClickListener(buttonsListener);
        w6[5].setOnClickListener(buttonsListener);
        w6[6].setOnClickListener(buttonsListener);
        button1.setOnClickListener(testButtonsListener);
        button2.setOnClickListener(testButtonsListener);

        //endregion

        //region Инициализация переменных
        rightNow = Calendar.getInstance();              //берем системное время и дату
        displayMonth = (Calendar) rightNow.clone();
        dbHelper = new DBHelper(this);
        filter = new Filter();
   //     filterDialog = new FilterDialogFragment();
        editDialog = new EditDialogFragment();

        //endregion

        createCalendar(rightNow);              //создаем календарь



        //region Поток, отвечающий за отображение времени
        //создаем handler для изменения времени
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                displayTime();
            }
        };

        //создаем второй поток для изменения времени
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Thread.sleep(1000);                 //ждем секунду
                        rightNow.add(Calendar.SECOND, 1); //добавляем секунду
                        h.sendEmptyMessage(0);          //отправляем сообщение чтобы поменять текст
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
//                    } catch (Exception e) {
//
                    }
                }
            }
        });
        t.start();
        //endregion

    }


    //region Создание календаря
    public void createCalendar(Calendar c) {
        Log.d(TAG,"начало createCalendar()");
        cleanAll();                     //очищаем все

        int weekDay;                    //текущий день недели
        int maxDay = lastDayOfMonth(c); //вычисляем количество дней в месяце

        int dayNumber = c.get(Calendar.DAY_OF_MONTH);

        if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            weekDay = c.get(Calendar.DAY_OF_WEEK) - 1;
        } else weekDay = 7;

        while (dayNumber != 1) {  //Высчитываем на какой день недели приходится
            weekDay--;            //1е число месяца
            dayNumber--;
            if (weekDay == 0) weekDay = 7;
        }
      //  Log.d(TAG,"день недели первого числа месяца: " + getMinimalDaysInFirstWeek(c));

        int daysIn1W = 7 - weekDay + 1;             //количество дней месяца в 1 неделе(строке)

        //добавляем нужные линии
        if (!(weekDay == 1 && maxDay == 28)) {
            findViewById(R.id.line29).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line30).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line31).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line32).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line33).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line34).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line35).setBackgroundResource(R.color.colorPrimaryShade);
        }
        if ((weekDay == 6 && maxDay == 31) || (weekDay == 7 && maxDay >= 30)) {
            findViewById(R.id.line36).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line37).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line38).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line39).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line40).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line41).setBackgroundResource(R.color.colorPrimaryShade);
            findViewById(R.id.line42).setBackgroundResource(R.color.colorPrimaryShade);
        }

        for (int i = 0, j = daysIn1W + 1; i < 7; i++, j++) { //переименовываем кнопки чтобы
            //получился календарь
            if (j - 7 > 0) {
                w1[i].setText(String.format("%s", j - 7));
            }
            w2[i].setText(String.format("%s", j));
            w3[i].setText(String.format("%s", j + 7));
            w4[i].setText(String.format("%s", j + 14));
            if (j + 21 <= maxDay) {
                w5[i].setText(String.format("%s", j + 21));
            }
            if (j + 28 <= maxDay) {
                w6[i].setText(String.format("%s", j + 28));
            }
        }

        setMonth(c);                                 //устанавливаем название месяца
        monthView.append(" " + String.format("%s", c.get(Calendar.YEAR)));  //прибавляем год к месяцу

        displayTime();
        filter.eventsFilter(c);
        displayEvents();

        Log.d(TAG,"календарь создан");
    }
    public Button btnSearch(int day) {
        Button b = b1;
        for (Button i : w1) {
            if (i.getText().equals(Integer.toString(day))) b = i;
        }
        for (Button i : w2) {
            if (i.getText().equals(Integer.toString(day))) b = i;
        }
        for (Button i : w3) {
            if (i.getText().equals(Integer.toString(day))) b = i;
        }
        for (Button i : w4) {
            if (i.getText().equals(Integer.toString(day))) b = i;
        }
        for (Button i : w5) {
            if (i.getText().equals(Integer.toString(day))) b = i;
        }
        for (Button i : w6) {
            if (i.getText().equals(Integer.toString(day))) b = i;
        }
        return b;
    }

    void setMonth(Calendar c) {
        switch (c.get(Calendar.MONTH)) {
            case 1:
                monthView.setText("Февраль");
                break;
            case 2:
                monthView.setText("Март");
                break;
            case 3:
                monthView.setText("Апрель");
                break;
            case 4:
                monthView.setText("Май");
                break;
            case 5:
                monthView.setText("Июнь");
                break;
            case 6:
                monthView.setText("Июль");
                break;
            case 7:
                monthView.setText("Август");
                break;
            case 8:
                monthView.setText("Сентябрь");
                break;
            case 9:
                monthView.setText("Октябрь");
                break;
            case 10:
                monthView.setText("Ноябрь");
                break;
            case 11:
                monthView.setText("Декабрь");
                break;
            default:
                monthView.setText("Январь");
        }
    }

    void cleanAll() {
        for (Button j : w1) {
            j.setText("");
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w2) {
            j.setText("");
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w3) {
            j.setText("");
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w4) {
            j.setText("");
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w5) {
            j.setText("");
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w6) {
            j.setText("");
            j.setBackgroundResource(android.R.color.transparent);
        }

        findViewById(R.id.line29).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line30).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line31).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line32).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line33).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line34).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line35).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line36).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line37).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line38).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line39).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line40).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line41).setBackgroundResource(android.R.color.transparent);
        findViewById(R.id.line42).setBackgroundResource(android.R.color.transparent);

        try {
            database = dbHelper.getWritableDatabase();
        }
        catch (SQLiteException ex){
            database = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            Log.d(TAG,"Ошибка чтения БД");
        }
        //удаляем все события из таблицы текущего месяца
        database.delete(DBHelper.TABLE_MONTH_EVENTS, null, null);

 //       database.close();
    }

    int lastDayOfMonth(Calendar c) {
        Calendar cal = Calendar.getInstance();
        cal.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
//        textView.setText(Integer.toString(rightNow.get(Calendar.DAY_OF_MONTH)));
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    //endregion

    //region Все что касается меню
    //формирование меню при нажатии кнопки Меню
    Menu globalMenu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        globalMenu = menu;
        return true;
    }

    //действия при нажатии на пункты меню
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.SetDate:
                setDate(rightNow);
                return true;
            case R.id.SetTime:
                setTime(findViewById(R.id.SetTime));
                return true;
            case R.id.Settings:

                return true;
            case R.id.filter:
                showDialog("filterDialog");
                Log.d(MonthActivity.TAG,"перед displayEvents()");
                displayEvents();
                return true;
            case R.id.editMenuItem:
                showDialog("editDialog");
                displayEvents();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showDialog(String tag) {

        //списано с Android Guideline
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = null;
        switch (tag) {
            case "filterDialog" :
                newFragment = FilterDialogFragment.newInstance(0);
                break;
            case "editDialog" :
                newFragment = EditDialogFragment.newInstance(0);
                break;
        }

        newFragment.show(ft, tag);
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(Calendar c) {
        DatePickerDialog dialog = new DatePickerDialog(this, d,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
   //     ((DatePicker) dialog.getDatePicker()).setId(R.id.datePicker1);
       dialog.show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(this, t,
                rightNow.get(Calendar.HOUR_OF_DAY),
                rightNow.get(Calendar.MINUTE), true)
                .show();
    }
    //endregion

    // отображение времени на экране
    private void displayTime() {
        timeView.setText(DateUtils.formatDateTime(this,
                rightNow.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_TIME));
    }

    //region ArrowListener
    View.OnClickListener arrowsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.arrowLeft:
                    if (displayMonth.get(Calendar.MONTH) >= 1)
                        displayMonth.set(displayMonth.get(Calendar.YEAR),
                                displayMonth.get(Calendar.MONTH) - 1, 1);
                    else displayMonth.set(displayMonth.get(Calendar.YEAR) - 1, 11, 1);
                    break;
                case R.id.arrowRight:
                    if (displayMonth.get(Calendar.MONTH) != 11)
                        displayMonth.set(displayMonth.get(Calendar.YEAR),
                                displayMonth.get(Calendar.MONTH) + 1, 1);
                    else displayMonth.set(displayMonth.get(Calendar.YEAR) + 1, 0, 1);
                    break;
            }
            //если совпадают год и месяц, то создаем календарь текущего месяца
            if (rightNow.get(Calendar.MONTH) == displayMonth.get(Calendar.MONTH) &&
                    rightNow.get(Calendar.YEAR) == displayMonth.get(Calendar.YEAR)) {
                createCalendar(rightNow);
            }
            createCalendar(displayMonth);
        }
    };
    //endregion

    //region Listener DatePickerDialog и TimePickerDialog
    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            rightNow.set(Calendar.YEAR, year);
            rightNow.set(Calendar.MONTH, monthOfYear);
            rightNow.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            createCalendar(rightNow);            //создаем календарь
            displayMonth.set(rightNow.get(Calendar.YEAR), rightNow.get(Calendar.MONTH),
                    rightNow.get(Calendar.DAY_OF_MONTH));
        }
    };

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            rightNow.set(Calendar.HOUR_OF_DAY, hourOfDay);
            rightNow.set(Calendar.MINUTE, minute);
            displayTime();
        }
    };
    //endregion

    //region ButtonsListener
    View.OnClickListener buttonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Button b = (Button) v;
            if (!b.getText().equals("")) {
                Intent intent = new Intent(MonthActivity.this, DayActivity.class);
                intent.putExtra("Число", b.getText());
                switch (v.getId()) {
                    case R.id.monthView:
          //              setDate(findViewById(R.id.SetDate));
                        new DatePickerDialog(MonthActivity.this, d,
                                displayMonth.get(Calendar.YEAR),
                                displayMonth.get(Calendar.MONTH),
                                displayMonth.get(Calendar.DAY_OF_MONTH))
                                .show();
                        break;
                    case R.id.b1:
                        intent.putExtra("День недели", "Пн");
                        break;
                    case R.id.b2:
                        intent.putExtra("День недели", "Вт");
                        break;
                    case R.id.b3:
                        intent.putExtra("День недели", "Ср");
                        break;
                    case R.id.b4:
                        intent.putExtra("День недели", "Чт");
                        break;
                    case R.id.b5:
                        intent.putExtra("День недели", "Пт");
                        break;
                    case R.id.b6:
                        intent.putExtra("День недели", "Сб");
                        break;
                    case R.id.b7:
                        intent.putExtra("День недели", "Вс");
                        break;
                    case R.id.b8:
                        intent.putExtra("День недели", "Пн");
                        break;
                    case R.id.b9:
                        intent.putExtra("День недели", "Вт");
                        break;
                    case R.id.b10:
                        intent.putExtra("День недели", "Ср");
                        break;
                    case R.id.b11:
                        intent.putExtra("День недели", "Чт");
                        break;
                    case R.id.b12:
                        intent.putExtra("День недели", "Пт");
                        break;
                    case R.id.b13:
                        intent.putExtra("День недели", "Сб");
                        break;
                    case R.id.b14:
                        intent.putExtra("День недели", "Вс");
                        break;
                    case R.id.b15:
                        intent.putExtra("День недели", "Пн");
                        break;
                    case R.id.b16:
                        intent.putExtra("День недели", "Вт");
                        break;
                    case R.id.b17:
                        intent.putExtra("День недели", "Ср");
                        break;
                    case R.id.b18:
                        intent.putExtra("День недели", "Чт");
                        break;
                    case R.id.b19:
                        intent.putExtra("День недели", "Пт");
                        break;
                    case R.id.b20:
                        intent.putExtra("День недели", "Сб");
                        break;
                    case R.id.b21:
                        intent.putExtra("День недели", "Вс");
                        break;
                    case R.id.b22:
                        intent.putExtra("День недели", "Пн");
                        break;
                    case R.id.b23:
                        intent.putExtra("День недели", "Вт");
                        break;
                    case R.id.b24:
                        intent.putExtra("День недели", "Ср");
                        break;
                    case R.id.b25:
                        intent.putExtra("День недели", "Чт");
                        break;
                    case R.id.b26:
                        intent.putExtra("День недели", "Пт");
                        break;
                    case R.id.b27:
                        intent.putExtra("День недели", "Сб");
                        break;
                    case R.id.b28:
                        intent.putExtra("День недели", "Вс");
                        break;
                    case R.id.b29:
                        intent.putExtra("День недели", "Пн");
                        break;
                    case R.id.b30:
                        intent.putExtra("День недели", "Вт");
                        break;
                    case R.id.b31:
                        intent.putExtra("День недели", "Ср");
                        break;
                    case R.id.b32:
                        intent.putExtra("День недели", "Чт");
                        break;
                    case R.id.b33:
                        intent.putExtra("День недели", "Пт");
                        break;
                    case R.id.b34:
                        intent.putExtra("День недели", "Сб");
                        break;
                    case R.id.b35:
                        intent.putExtra("День недели", "Вс");
                        break;
                    case R.id.b36:
                        intent.putExtra("День недели", "Пн");
                        break;
                    case R.id.b37:
                        intent.putExtra("День недели", "Вт");
                        break;
                    case R.id.b38:
                        intent.putExtra("День недели", "Ср");
                        break;
                    case R.id.b39:
                        intent.putExtra("День недели", "Чт");
                        break;
                    case R.id.b40:
                        intent.putExtra("День недели", "Пт");
                        break;
                    case R.id.b41:
                        intent.putExtra("День недели", "Сб");
                        break;
                    case R.id.b42:
                        intent.putExtra("День недели", "Вс");
                        break;
                }
                intent.putExtra("Месяц", displayMonth.get(Calendar.MONTH));
                intent.putExtra("Год", displayMonth.get(Calendar.YEAR));
                intent.putExtra("Час", displayMonth.get(Calendar.HOUR_OF_DAY));
                intent.putExtra("Минута", displayMonth.get(Calendar.MINUTE));
                startActivity(intent);
                Log.d(TAG,"Запустили DayActivity");
            }

        }
    };

    View.OnClickListener testButtonsListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                database = dbHelper.getWritableDatabase();
            }
            catch (SQLiteException ex){
                database = dbHelper.getReadableDatabase();
            } catch (Exception e) {
                Log.d(TAG,"Ошибка чтения БД");
            }
            switch (v.getId()) {
                case R.id.button1:

                    Cursor cursor = database.query(DBHelper.TABLE_EVENTS,
                            null, null, null, null, null, null);

                    if (cursor.moveToFirst()) {
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
                        int tagIndex = cursor.getColumnIndex(DBHelper.KEY_TAG);
                        int checkedIndex = cursor.getColumnIndex(DBHelper.KEY_CHECKED);
                        Log.d(TAG,"Таблица events:");
                        do {
                            Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                                    ", title = " + cursor.getString(titleIndex) +
                       //             ", description = " + cursor.getString(descriptionIndex) +
                                    ", date = " + cursor.getString(dateIndex) +
                                    ", month = " + cursor.getString(monthIndex) +
                                    ", year = " + cursor.getString(yearIndex) +
                                    ", hour = " + cursor.getString(hourIndex) +
                                    ", minute = " + cursor.getString(minuteIndex) +
                                    ", recur_type = " + cursor.getString(recurTypeIndex) +
                                    ", recur_days = " + cursor.getString(recurDaysIndex) +
                                    ", tag = " + cursor.getString(tagIndex) +
                                    ", checked = " + cursor.getInt(checkedIndex));
                        } while (cursor.moveToNext());
                    } else
                        Log.d(TAG,"записей в events не найдено");

                    cursor.close();

                    break;
                case R.id.button2:

                    cursor = database.query(DBHelper.TABLE_MONTH_EVENTS,
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
                        int checkedIdIndex = cursor.getColumnIndex(DBHelper.KEY_CHECKED);
                        Log.d(TAG,"Таблица month_events:");
                        do {
                            Log.d(TAG, "ID = " + cursor.getInt(idIndex) +
                                    ", title = " + cursor.getString(titleIndex) +
     //                               ", description = " + cursor.getString(descriptionIndex) +
                                    ", date = " + cursor.getString(dateIndex) +
                                    ", month = " + cursor.getString(monthIndex) +
                                    ", year = " + cursor.getString(yearIndex) +
                                    ", recur_type = " + cursor.getString(recurTypeIndex) +
                                    ", recur_days = " + cursor.getString(recurDaysIndex) +
                                    ", tag = " + cursor.getString(tagIndex) +
                                    ", checked = " + cursor.getInt(checkedIdIndex));
                        } while (cursor.moveToNext());
                    } else
                        Log.d(TAG,"записей в month_events не найдено");

                    cursor.close();

                    break;
            }}};
    //endregion

    //region onCreateDialog()
   /* protected Dialog onCreateDialog(int id) {

        TagsFilter tagsFilter = new TagsFilter();
        ArrayList<String> tags = tagsFilter.tagsFilter();
        final String [] items = tags.toArray(new String [tags.size()]); //получили массив тэгов

        //преобразование ArrayList<Boolean> в boolean[]
        ArrayList<Boolean> b = tagsFilter.checkFilter();
        Log.d(TAG, "вызвали метод checkFilter()");
        final Boolean[] b1 = b.toArray(new Boolean [b.size()]);
        final boolean checkedItems [] = new boolean[b1.length];

        for (int i = 0; i < checkedItems.length; i++) {
                checkedItems[i] = b1[i];
        }
        Log.d(TAG, "преобразовали ArrayList в массив boolean");
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
                            //    database.close();
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

    public void displayEvents() {
        cleanColor();
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

        Cursor cursor = database.query(
                DBHelper.TABLE_MONTH_EVENTS,
                new String[] {DBHelper.KEY_DATE, DBHelper.KEY_TAG},
                DBHelper.KEY_CHECKED + " = ?",
                new String[] {"1"},
                null,
                null,
                null);

        Button b;
        if (cursor.moveToFirst()) {     //проверка содержит ли cursor хоть одну запись
            //запись одной строчки курсора
            do {
                //находим кнопку, которую надо выделить
                b = btnSearch(cursor.getInt(cursor.getColumnIndex(DBHelper.KEY_DATE)));
                b.setBackgroundResource(R.drawable.event_background);
            } while (cursor.moveToNext());
        } else {
            Log.d(TAG, "событий для отображения не найдено");
        }
        cursor.close();
  //      database.close();
        highlightTodayButton();
    }

    void cleanColor() {
        for (Button j : w1) {
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w2) {
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w3) {
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w4) {
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w5) {
            j.setBackgroundResource(android.R.color.transparent);
        }
        for (Button j : w6) {
            j.setBackgroundResource(android.R.color.transparent);
        }
    }

    void highlightTodayButton() {
        if (rightNow.get(Calendar.MONTH) == displayMonth.get(Calendar.MONTH) &&
                rightNow.get(Calendar.YEAR) == displayMonth.get(Calendar.YEAR)) {
            today = btnSearch(rightNow.get(Calendar.DAY_OF_MONTH));  //находим кнопку today
       //выделяем сегодняшний день
            today.setBackgroundResource(R.drawable.today_background);
            today.setText(Integer.toString(rightNow.get(Calendar.DAY_OF_MONTH)));
        }


    }




















}
