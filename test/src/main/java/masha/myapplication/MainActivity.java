package masha.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //region Объявление переменных
    Handler h;

    TextView monthView, timeView, textView;
    AlertDialog.Builder ad;
    Context context;
    ListView listView1;
    TableRow week1, week2, week3, week4, week5, week6;

    ImageButton b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11, b12, b13, b14;
    ImageButton b15, b16, b17, b18, b19, b20, b21, b22, b23, b24, b25, b26;
    ImageButton b27, b28, b29, b30, b31, b32, b33, b34, b35;
    ImageButton b36, b37, b38, b39, b40, b41, b42, arrowLeft, arrowRight;
    ImageButton[] w1 = {b1, b2, b3, b4, b5, b6, b7};
    ImageButton[] w2 = {b8, b9, b10, b11, b12, b13, b14};
    ImageButton[] w3 = {b15, b16, b17, b18, b19, b20, b21};
    ImageButton[] w4 = {b22, b23, b24, b25, b26, b27, b28};
    ImageButton[] w5 = {b29, b30, b31, b32, b33, b34, b35};
    ImageButton[] w6 = {b36, b37, b38, b39, b40, b41, b42};
    ImageButton today, button1, button2;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        w1[0] = (ImageButton) findViewById(R.id.b1);
        w1[1] = (ImageButton) findViewById(R.id.b2);
        w1[2] = (ImageButton) findViewById(R.id.b3);
        w1[3] = (ImageButton) findViewById(R.id.b4);
        w1[4] = (ImageButton) findViewById(R.id.b5);
        w1[5] = (ImageButton) findViewById(R.id.b6);
        w1[6] = (ImageButton) findViewById(R.id.b7);
        w2[0] = (ImageButton) findViewById(R.id.b8);
        w2[1] = (ImageButton) findViewById(R.id.b9);
        w2[2] = (ImageButton) findViewById(R.id.b10);
        w2[3] = (ImageButton) findViewById(R.id.b11);
        w2[4] = (ImageButton) findViewById(R.id.b12);
        w2[5] = (ImageButton) findViewById(R.id.b13);
        w2[6] = (ImageButton) findViewById(R.id.b14);
        w3[0] = (ImageButton) findViewById(R.id.b15);
        w3[1] = (ImageButton) findViewById(R.id.b16);
        w3[2] = (ImageButton) findViewById(R.id.b17);
        w3[3] = (ImageButton) findViewById(R.id.b18);
        w3[4] = (ImageButton) findViewById(R.id.b19);
        w3[5] = (ImageButton) findViewById(R.id.b20);
        w3[6] = (ImageButton) findViewById(R.id.b21);
        w4[0] = (ImageButton) findViewById(R.id.b22);
        w4[1] = (ImageButton) findViewById(R.id.b23);
        w4[2] = (ImageButton) findViewById(R.id.b24);
        w4[3] = (ImageButton) findViewById(R.id.b25);
        w4[4] = (ImageButton) findViewById(R.id.b26);
        w4[5] = (ImageButton) findViewById(R.id.b27);
        w4[6] = (ImageButton) findViewById(R.id.b28);
        w5[0] = (ImageButton) findViewById(R.id.b29);
        w5[1] = (ImageButton) findViewById(R.id.b30);
        w5[2] = (ImageButton) findViewById(R.id.b31);
        w5[3] = (ImageButton) findViewById(R.id.b32);
        w5[4] = (ImageButton) findViewById(R.id.b33);
        w5[5] = (ImageButton) findViewById(R.id.b34);
        w5[6] = (ImageButton) findViewById(R.id.b35);
        w6[0] = (ImageButton) findViewById(R.id.b36);
        w6[1] = (ImageButton) findViewById(R.id.b37);
        w6[2] = (ImageButton) findViewById(R.id.b38);
        w6[3] = (ImageButton) findViewById(R.id.b39);
        w6[4] = (ImageButton) findViewById(R.id.b40);
        w6[5] = (ImageButton) findViewById(R.id.b41);
        w6[6] = (ImageButton) findViewById(R.id.b42);

        w3[3].setBackgroundResource(R.drawable.ic_panorama_fish_eye_black_48dp);
    }
}
