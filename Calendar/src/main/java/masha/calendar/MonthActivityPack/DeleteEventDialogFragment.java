package masha.calendar.MonthActivityPack;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.CursorAdapter;

import masha.calendar.R;

/**
 * Created by Маша on 21.02.2017.
 */

public class DeleteEventDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    int eventId;

    void DeleteEventDialogFragment(int id) {
        eventId = id;
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setPositiveButton(R.string.ok, this)
                .setNegativeButton(R.string.cancel, this);

        //        .setMessage(R.string.message_text);

        adb.setTitle("Удалить событие?");
        //    adb.setAdapter(adapter);
        return adb.create();
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
  //      Log.d(LOG_TAG, "EditDialog: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
  //      Log.d(LOG_TAG, "EditDialog: onCancel");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //удаленее события из базы данных
                break;
            case Dialog.BUTTON_NEGATIVE:
                onDismiss(dialog);
                break;
        }
    }
}
