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


public class FirstFragment extends Fragment {

    public static FirstFragment newInstance(String param1, String param2) {
        FirstFragment fragment = new FirstFragment();
        // Bundle args = new Bundle();
        return fragment;
    }

    public FirstFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "FirstFragment.onCreate()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first, container, false);

        Toast.makeText(getActivity(), "FirstFragment.onCreateView()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onCreateView");

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Toast.makeText(getActivity(), "FirstFragment.onAttach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onAttach");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "FirstFragment.onActivityCreated()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "FirstFragment.onStart()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "FirstFragment.onResume()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "FirstFragment.onPause()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "FirstFragment.onStop()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "FirstFragment.onDestroyView()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDestroyView");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(), "FirstFragment.onDestroy()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
        Toast.makeText(getActivity(), "FirstFragment.onDetach()",
                Toast.LENGTH_LONG).show();
        Log.d("Fragment 1", "onDetach");
    }
}

