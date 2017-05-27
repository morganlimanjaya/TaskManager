package sg.edu.rp.c347.taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import static android.R.attr.id;

public class AddActivity extends AppCompatActivity {

    EditText etName, etContent, etReminder;
    Button btnAddTask, btnCancel;
    int reqCode = 12345;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        etName = (EditText)findViewById(R.id.editTextName);
        etContent = (EditText)findViewById(R.id.editTextDesc);
        etReminder = (EditText)findViewById(R.id.editTextReminder);

        btnAddTask = (Button)findViewById(R.id.btnAddTask);
        btnCancel = (Button)findViewById(R.id.btnCancel);

        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                String content = etContent.getText().toString();
                int seconds = Integer.parseInt(etReminder.getText().toString());

                DBHelper db = new DBHelper(AddActivity.this);
                //Insert a task

                db.insertTask(new Task(id,name,content));
                Toast.makeText(AddActivity.this, "Inserted", Toast.LENGTH_SHORT).show();
                db.close();

                //launch notification here through alarm
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, seconds);

                Intent intent = new Intent(AddActivity.this,
                        TaskBroadcastReceiver.class);
                intent.putExtra("name",name);
                intent.putExtra("content",content);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        AddActivity.this, reqCode,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager am = (AlarmManager)
                        getSystemService(Activity.ALARM_SERVICE);

                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                        pendingIntent);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });


    }
}
