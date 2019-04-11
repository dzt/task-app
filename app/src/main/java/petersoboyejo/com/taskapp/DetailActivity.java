package petersoboyejo.com.taskapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import petersoboyejo.com.taskapp.fragments.DatePickerFragment;
import petersoboyejo.com.taskapp.utils.Format;
import petersoboyejo.com.taskapp.models.Task;
import petersoboyejo.com.taskapp.utils.LocalDatabase;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Task currentTask;

    private EditText name_details;
    private Button delete_details;
    private TextView list_details, dueDate_details, notes_details;
    private CardView card_list, card_dueDate, card_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentTask = (Task) getIntent().getSerializableExtra("TASK_CHOICE");
            this.init();
        }

    }

    private void init() {

        name_details = findViewById(R.id.name_details);
        delete_details = findViewById(R.id.delete_details);

        list_details = findViewById(R.id.list_details);
        dueDate_details = findViewById(R.id.dueDate_details);
        notes_details = findViewById(R.id.notes_details);

        card_list = findViewById(R.id.card_list);
        card_dueDate = findViewById(R.id.card_dueDate);
        card_notes = findViewById(R.id.card_notes);

        this.setUIValues();

        name_details.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence c, int start, int before, int count) {
                /* Update Task Title */
                LocalDatabase.updateTask(DetailActivity.this, currentTask.getId(), "title", name_details.getText().toString());
            }

            public void beforeTextChanged(CharSequence c, int start, int count, int after) {}

            public void afterTextChanged(Editable c) {}

        });

        /* Card View */
        card_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setTitle("Choose List");

                final String[] listNames = Format.narrow(LocalDatabase.fetchLists(DetailActivity.this), "title");
                builder.setItems(listNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int index) {
                        LocalDatabase.updateTask(DetailActivity.this, DetailActivity.this.currentTask.getId(), "list", listNames[index]);
                        DetailActivity.this.reload();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        card_dueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
        card_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailActivity.this, NotesActivity.class);
                intent.putExtra("TASK_NOTES", currentTask.getNotes());
                intent.putExtra("TASK_ID", currentTask.getId());
                startActivity(intent);
            }
        });

        delete_details.setOnClickListener(this);

    }

    public void reload () {
        currentTask = new Task(LocalDatabase.getTask(this, currentTask.getId()));
        this.setUIValues();
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setUIValues () {
        name_details.setText(currentTask.getTitle().toString(), TextView.BufferType.EDITABLE);
        list_details.setText(currentTask.getList().toString());
        if (currentTask.getDueDate().equals("")) {
            dueDate_details.setText("None");
        } else {
            dueDate_details.setText(currentTask.getDueDate().toString());
        }
        notes_details.setText(currentTask.getNotes().toString());
    }

    private void deleteTask() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to permanently remove this task?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        LocalDatabase.deleteTask(DetailActivity.this, getCurrentTask().getId());
                        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    public void onClick(View v) {

        Button _b = (Button) findViewById(v.getId());
        switch (v.getId()) {
            case R.id.delete_details:
                deleteTask();
                break;
        }
    }

}
