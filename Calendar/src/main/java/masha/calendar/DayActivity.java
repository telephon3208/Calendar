package masha.calendar;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class DayActivity extends AppCompatActivity {

    TextView dateView;
    int day, month, year, hour, minute;
    FloatingActionButton fAB;

 //   private final static String TAG = "MyLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        dateView = (TextView) findViewById(R.id.dateView);
        fAB = (FloatingActionButton) findViewById(R.id.fAB);

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

        fAB.setOnClickListener(onClickListener);

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
            case R.id.CreateEvent:
                createEvent();
                return true;
            case R.id.Settings:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //endregion

    public void createEvent() {
        Intent intent = new Intent(DayActivity.this, EventActivity.class);
        intent.putExtra("Число", day);
        intent.putExtra("Месяц", month);
        intent.putExtra("Год", year);
        intent.putExtra("Час", hour);
        intent.putExtra("Минута", minute);
        startActivity(intent);
        Log.d(MonthActivity.TAG,"Запуск EventActivity");
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fAB:
                    createEvent();
                    break;
            }
        }
    };
}

