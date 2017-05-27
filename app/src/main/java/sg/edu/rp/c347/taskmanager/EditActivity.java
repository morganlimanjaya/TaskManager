package sg.edu.rp.c347.taskmanager;

/**
 * Created by 15017171 on 27/5/2017.
 */

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    EditText etName, etDesc, etTime;
    Button btnUpdate, btnCancel;
    Task data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etName = (EditText)findViewById(R.id.etUpdatedName);
        etDesc = (EditText)findViewById(R.id.etUpdatedDesc);
        etTime = (EditText)findViewById(R.id.etUpdatedReminder);

        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnCancel = (Button)findViewById(R.id.btnCancelUpdate);

        Intent i = getIntent();
        data = (Task) i.getSerializableExtra("data");

        etName.setText(data.getName());
        etDesc.setText(data.getDescription());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditActivity.this);
                data.setName(etName.getText().toString());
                data.setDescription(etDesc.getText().toString());
                dbh.updateTask(data);
                dbh.close();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.SECOND, Integer.parseInt(etTime.getText().toString()));

                Intent intent = new Intent(EditActivity.this,
                        TaskBroadcastReceiver.class);
                intent.putExtra("name",data.getName());
                intent.putExtra("content",data.getDescription());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        EditActivity.this, 123,
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
