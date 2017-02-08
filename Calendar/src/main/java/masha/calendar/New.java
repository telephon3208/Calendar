package masha.calendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

//import masha.myapplication.R;

public class New extends AppCompatActivity {

    ListView listEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listEvents = (ListView) findViewById(R.id.listEvents);
        DBHelper dbHelper = new DBHelper(this);

        ArrayList<HashMap<String, String>> myArrList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        // Досье на первого кота
        map = new HashMap<String, String>();
        map.put("Time", "1");
        map.put("Title", "Мурзик");
        map.put("Description", "495 501-3545dfesssssssssssssssssssssssssssssssssssssssssss");
        myArrList.add(map);

        // Досье на второго кота
        map = new HashMap<String, String>();
        map.put("Time", "2");
        map.put("Title", "Барсик");
        map.put("Description", "495 241-6845dgshgrshbtdghberhbdefberbdebfrvsvdsve");
        myArrList.add(map);

        // Досье на третьего кота
        map = new HashMap<String, String>();
        map.put("Time", "3");
        map.put("Title", "Васька");
        map.put("Description", "495 431-5468sdbfgmj,g,gyikgewgavwegvewg gserghert gtrhethget");
        myArrList.add(map);

        SimpleAdapter adapter = new SimpleAdapter(this, myArrList,
                R.layout.row, new String[] { "Time", "Title",
                "Description" }, new int[] { R.id.time, R.id.title,
                R.id.description });
        listEvents.setAdapter(adapter);
    }

}
