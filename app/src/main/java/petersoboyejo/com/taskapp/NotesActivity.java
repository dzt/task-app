package petersoboyejo.com.taskapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import petersoboyejo.com.taskapp.models.Task;
import petersoboyejo.com.taskapp.utils.LocalDatabase;

public class NotesActivity extends AppCompatActivity {

    String notes, taskID;
    EditText notes_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_notes);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Edit Notes");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            notes = extras.getString("TASK_NOTES");
            taskID = extras.getString("TASK_ID");
            this.init();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void init() {
        notes_editText = findViewById(R.id.notes_editText);
        notes_editText.setText(notes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu
                .notes_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_note:
                this.addNotes();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNotes() {
        LocalDatabase.updateTask(this, taskID, "notes", notes_editText.getText().toString());
        Intent intent = new Intent(this, DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("TASK_CHOICE", new Task(LocalDatabase.getTask(this, taskID)));
        startActivity(intent);
    }
}
