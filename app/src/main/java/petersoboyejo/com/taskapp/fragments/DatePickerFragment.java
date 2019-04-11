package petersoboyejo.com.taskapp.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import petersoboyejo.com.taskapp.DetailActivity;
import petersoboyejo.com.taskapp.R;
import petersoboyejo.com.taskapp.models.Task;
import petersoboyejo.com.taskapp.utils.LocalDatabase;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), R.style.DialogTheme,this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        String dateFormatted = (view.getMonth() + 1) + "/" + view.getDayOfMonth() + "/" + view.getYear();
        DetailActivity activity = ((DetailActivity)getActivity());
        Task t = activity.getCurrentTask(); // Fetch Task ID from Activity Context
        LocalDatabase.updateTask(getActivity(), t.getId(), "dueDate", dateFormatted);
        activity.reload();

    }
}