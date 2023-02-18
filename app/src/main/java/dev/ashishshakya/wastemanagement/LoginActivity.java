package dev.ashishshakya.wastemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private FirebaseAuth auth;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= findViewById(R.id.email);
        password=findViewById(R.id.password);
        btnLogin =findViewById(R.id.login);
        auth=FirebaseAuth.getInstance();
        bundle=getIntent().getExtras();
        btnLogin.setOnClickListener(v->{
            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else{
                loginUser(email.getText().toString(),password.getText().toString());
            }
        });
    }

    private void loginUser(String email, String password) {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                if (bundle.getBoolean("new_data",false)){
                    startActivity(new Intent(LoginActivity.this , NewItemActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
            else{
                Toast.makeText(LoginActivity.this, "Log-in failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}