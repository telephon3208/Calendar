package masha.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class SecondFragment extends Fragment {

    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        // Bundle args = new Bundle();
        return fragment;
    }

    public SecondFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "SecondFragment.onCreate()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        Toast.makeText(getActivity(), "SecondFragment.onCreateView()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onCreateView");

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Toast.makeText(getActivity(), "SecondFragment.onAttach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "SecondFragment.onActivityCreated()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "SecondFragment.onStart()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "SecondFragment.onResume()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "SecondFragment.onPause()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "SecondFragment.onStop()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "SecondFragment.onDestroyView()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(), "SecondFragment.onDestroy()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        Toast.makeText(getActivity(), "SecondFragment.onDetach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 2", "onDetach");
    }
}

