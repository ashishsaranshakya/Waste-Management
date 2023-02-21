package dev.ashishshakya.wastemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private EditText repassword;
    private Button btnSignUp;
    private Button btnSkip;
    private Button btnLogin;

    private FirebaseAuth auth;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        email= findViewById(R.id.email);
        password=findViewById(R.id.password);
        repassword=findViewById(R.id.repassword);
        btnSignUp=findViewById(R.id.sign_up);
        btnSkip=findViewById(R.id.skip);
        btnLogin=findViewById(R.id.login);
        auth=FirebaseAuth.getInstance();
        bundle=getIntent().getExtras();
        if(bundle!=null) {
            boolean skipable = bundle.getBoolean("skipable", true);
            if (!skipable) {
                btnSkip.setVisibility(View.INVISIBLE);
            }
        }
        else{
            bundle=new Bundle();
            bundle.putBoolean("new_data",false);
        }

        btnSignUp.setOnClickListener(v->{
            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || repassword.getText().toString().isEmpty()){
                Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
            } else if (!password.getText().toString().equals(repassword.getText().toString())) {
                Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
            } else if (password.getText().toString().length()<8) {
                Toast.makeText(this, "Password length should be greater than 8", Toast.LENGTH_SHORT).show();
            }
            else{
                registerUser(email.getText().toString(),password.getText().toString());
            }
        });
        btnSkip.setOnClickListener(v->{
            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
            finish();
        });
        btnLogin.setOnClickListener(v->{
            startActivity(new Intent(SignUpActivity.this,LoginActivity.class).putExtras(bundle));
            finish();
        });
    }

    private void registerUser(String email, String password) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "Signed up successfully", Toast.LENGTH_SHORT).show();
                    if (bundle.getBoolean("new_data",false)){
                        startActivity(new Intent(SignUpActivity.this , NewItemActivity.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        finish();
                    }
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Signup failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences=this.getSharedPreferences("dev.ashishshakya.wastemanagement", Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("firstTime",true) || bundle.getBoolean("login",false)){
            sharedPreferences.edit().putBoolean("firstTime",false).apply();
        }
        else{
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            finish();
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            if (bundle.getBoolean("new_data",false)){
                startActivity(new Intent(SignUpActivity.this , NewItemActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();
            }
        }
    }
}