package flexenv.solutions.cloud.google.com.playchat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth Auth;
    private EditText email;
    private EditText password;
    private Button register;
    private Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        signIn=findViewById(R.id.signIn);
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i==R.id.signIn || i==EditorInfo.IME_NULL)
                {
                    attemptSignIn();
                    return true;
                }
                return false;
            }
        });
        Auth=FirebaseAuth.getInstance();
    }

    public void signInUser(View view)
    {
        attemptSignIn();
    }
    public void register(View view){
        Intent i=new Intent(getApplicationContext(),RegisterActivity.class);
        startActivity(i);
    }
    public void attemptSignIn(){
        String email1=email.getText().toString();
        String password1=password.getText().toString();
        if(email1.equals("")|| password1.equals(""))
        {
            return;
        }
        Toast.makeText(getApplicationContext(),"Login In Progress",Toast.LENGTH_LONG).show();
        Auth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), UserList.class);
                    finish();
                    startActivity(i);
                } else {
                    showError("Login Failed");
                }
            }
        });
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
