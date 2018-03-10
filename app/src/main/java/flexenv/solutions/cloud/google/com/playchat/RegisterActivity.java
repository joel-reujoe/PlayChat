package flexenv.solutions.cloud.google.com.playchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    public static final String CHAT_PREFS="chatpref";
    public static String USERNAME="username";
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText cpassword;
    private FirebaseAuth Auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email=findViewById(R.id.email);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);
        Auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance().getReference();

    }
    public void register(View view)
    {
        if(!isEmailValid(email.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Email is not valid",Toast.LENGTH_LONG).show();
        }
        if(!isPasswordValid(password.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),"Enter a valid password",Toast.LENGTH_LONG).show();
        }
        else {
            createUser();
        }
    }
    private   boolean isEmailValid(String email)
    {
        return email.contains("@");
    }
    private boolean isPasswordValid(String password)
    {
        String cpassword1=cpassword.getText().toString();
        return cpassword1.equals(password) && password.length()>4;
    }
    private void createUser(){
        String email1=email.getText().toString();
        String password1=password.getText().toString();
        Auth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(),"True",Toast.LENGTH_LONG).show();
                    displayName();
                    Intent i=new Intent(getApplicationContext(),MainActivity.class);
                    finish();
                    startActivity(i);
                }
                else
                {
                    showError("Registration was not successful");
                }
            }
        });
        User user=new User(username.getText().toString());
        database.child("user").push().setValue(user);

    }
    private void displayName(){
        String displayName=username.getText().toString();
        SharedPreferences preferences=getSharedPreferences(CHAT_PREFS,0);
        preferences.edit().putString(USERNAME,displayName).apply();
    }
    private void showError(String message)
    {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
