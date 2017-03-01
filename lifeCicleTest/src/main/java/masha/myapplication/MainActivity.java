package masha.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    Button fragment1Button, fragment2Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Activity", "onCreate");
        setContentView(R.layout.activity_main);

        fragment1Button = (Button) findViewById(R.id.buttonFragment1);
        fragment2Button = (Button) findViewById(R.id.buttonFragment2);

        // get an instance of FragmentTransaction from your Activity
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        FirstFragment firstFragment = new FirstFragment();
        fragmentTransaction.add(R.id.container, firstFragment);
        fragmentTransaction.commit();

        fragment1Button.setOnClickListener(onButtonClickListener);
        fragment2Button.setOnClickListener(onButtonClickListener);
    }

    Button.OnClickListener onButtonClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Fragment newFragment = null;

            // Create new fragment
            if (v == fragment1Button) {
                newFragment = new FirstFragment();
            } else {
                newFragment = new SecondFragment();
            }

            // Create new transaction
            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Activity","onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Activity","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Activity","onPause");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("Activity","onRestoreInstanceState");
    }
}

