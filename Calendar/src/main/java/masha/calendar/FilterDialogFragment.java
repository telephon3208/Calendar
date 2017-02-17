package masha.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * Created by Маша on 17.02.2017.
 */

public class FilterDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    final String LOG_TAG = "myLogs";

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setNegativeButton(R.string.cancel, this)
                .setPositiveButton(R.string.ok, this);

        //        .setMessage(R.string.message_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.select_dialog_singlechoice,
                new String[] {"filterDialog1", "filterDialog2", "filter3"});
        adb.setTitle("Фильтр тэгов");
        adb.setSingleChoiceItems(adapter, 2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(LOG_TAG, "нажат пункт: " + which);
            }
        });
        return adb.create();
    }

    public void onClick(DialogInterface dialog, int which) {
        int i = 0;
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                i = R.string.ok;
                break;
            case Dialog.BUTTON_NEGATIVE:
                i = R.string.cancel;
                break;
        }
        if (i > 0)
            Log.d(LOG_TAG, "Dialog 2: " + getResources().getString(i));
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 2: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 2: onCancel");
    }
}
