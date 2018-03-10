package flexenv.solutions.cloud.google.com.playchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserList extends AppCompatActivity {
    private ListView userList;
    private DatabaseReference databaseReference;
    private ArrayList<DataSnapshot> snapshots;
    private String displayName;
    private UserListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("UserList");
        userList=findViewById(R.id.userlist);
        databaseReference=FirebaseDatabase.getInstance().getReference();
        SetupDisplayName();

    }
    @Override
    public void onStart()
    {
        super.onStart();
        adapter=new UserListAdapter(this,databaseReference,displayName);
        userList.setAdapter(adapter);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                startActivity(intent);
            }
        });

    }
    private void SetupDisplayName() {
        SharedPreferences preferences = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        displayName=preferences.getString(RegisterActivity.USERNAME,null);
        if(displayName==null)
            displayName="anonymous";
    }
    @Override
    public void onStop()
    {
        super.onStop();
        adapter.cleanUp();
    }


}
