package sg.edu.rp.c347.taskmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<Task> taskArrayList;
    ArrayAdapter aa;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView)findViewById(R.id.ListViewContent);
        btnAdd = (Button)findViewById(R.id.buttonTask);

//        Intent intentReceived = getIntent();
        DBHelper db = new DBHelper(MainActivity.this);
        taskArrayList = db.getAllTasks();

        aa = new TaskArrayAdaptor(MainActivity.this, R.layout.add_activity, taskArrayList) ;
        lv.setAdapter(aa);
        registerForContextMenu(lv);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),AddActivity.class);
                startActivityForResult(i,9);
            }
        });

    }
    @Override
    public void onCreateContextMenu (ContextMenu menu, View
            v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        //Context menu
        menu.add(Menu.NONE, 1, Menu.NONE, "Edit");
        menu.add(Menu.NONE, 2, Menu.NONE, "Delete");
    }
    @Override
    public boolean onContextItemSelected (MenuItem item){
        // TODO Auto-generated method stub
        AdapterView.AdapterContextMenuInfo menuinfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        long selectid = menuinfo.id; //_id from database in this case
        int selectpos = menuinfo.position; //position in the adapter
        switch (item.getItemId()) {
            case 1: {
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                int id = taskArrayList.get(selectpos).getId();
                String name = taskArrayList.get(selectpos).getName();
                String content = taskArrayList.get(selectpos).getDescription();
                Task target = new Task(id,name,content);
                i.putExtra("data", target);
                startActivityForResult(i,8);
            }
            break;
            case 2: {
                DBHelper dbh = new DBHelper(MainActivity.this);
                dbh.deleteTask(selectid);
                taskArrayList.remove(selectpos);
                dbh.close();
                aa.notifyDataSetChanged();
            }
            break;
        }

        return super.onContextItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if(data != null){
                if(requestCode == 9 || requestCode == 8) {
                    DBHelper dbh = new DBHelper(MainActivity.this);
                    taskArrayList.clear();
                    taskArrayList.addAll(dbh.getAllTasks());
                    dbh.close();
                    aa.notifyDataSetChanged();
                }
            }
        }
    }
}
