package flexenv.solutions.cloud.google.com.playchat;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    public String displayName;
    private ChatListAdapter adapter;
    private ListView chatList;
    EditText input;
    Button send;
    int a=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        final SharedPreferences preferences=getSharedPreferences(RegisterActivity.CHAT_PREFS,0);
        preferences.edit().putString("lastchat",Integer.toString(a)).apply();
        chatList=findViewById(R.id.chatList);
        SetupDisplayName();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        input=findViewById(R.id.chat);
        send=findViewById(R.id.send);
        send.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMessage();
                a=Integer.parseInt(preferences.getString("lastchat",null));
                if(a==0)
                {
                    chatList.setSelection(0);
                }
                else
                {
                    chatList.setSelection(a);
                    a++;
                    preferences.edit().putString("lastchat",Integer.toString(a)).apply();
                }
                return true;
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                a=Integer.parseInt(preferences.getString("lastchat",null));
                if(a==0)
                {
                    chatList.setSelection(0);
                }
                else
                {
                    chatList.setSelection(a);
                    a++;
                    preferences.edit().putString("lastchat",Integer.toString(a)).apply();
                }
            }
        });

    }
    @Override
    public void onStart()
    {
        super.onStart();
        adapter=new ChatListAdapter(this,databaseReference,displayName);
        chatList.setAdapter(adapter);

    }

    private void SetupDisplayName() {
        SharedPreferences preferences = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);
        displayName=preferences.getString(RegisterActivity.USERNAME,null);
        if(displayName==null)
            displayName="anonymous";
    }
    private void sendMessage()
    {
        String input1=input.getText().toString();
        if(!input1.equals(""))
        {
            InstantMessage chat=new InstantMessage(input1,displayName);
            databaseReference.child("messages").push().setValue(chat);
            input.setText("");
        }
    }
    @Override
    public void onStop()
    {
        super.onStop();
        adapter.cleanUp();
    }

}
