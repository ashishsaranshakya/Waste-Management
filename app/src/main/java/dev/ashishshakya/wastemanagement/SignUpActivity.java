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
    private GoogleSignInClient googleSignInClient;

    private FirebaseAuth auth;
    private SignInButton btnGoogle;
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
        btnGoogle=findViewById(R.id.google);
        auth=FirebaseAuth.getInstance();
        bundle=getIntent().getExtras();
        if(bundle!=null) {
            boolean skipable = bundle.getBoolean("skipable", true);
            Log.d("asdfg",""+skipable);
            if (!skipable) {
                btnSkip.setVisibility(View.INVISIBLE);
            }
        }
        else{
            bundle=new Bundle();
            Log.d("asdfg","asdfghj");
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

//        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId("113996174738290498732")
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();
//
//        btnGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignInCredential googleCredential = oneTapClient.getSignInCredentialFromIntent(data);
//                String idToken = googleCredential.getGoogleIdToken();
//                if (idToken !=  null) {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with Firebase.
//                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//                    auth.signInWithCredential(firebaseCredential)
//                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
//                                        // Sign in success, update UI with the signed-in user's information
//                                        FirebaseUser user = auth.getCurrentUser();
//                                        //updateUI(user);
//                                    } else {
//                                        // If sign in fails, display a message to the user.
//                                        //Log.w("TAG", "signInWithCredential:failure", task.getException());
//                                        //updateUI(null);
//                                    }
//                                }
//                            });
//                }
//            }
//        });

//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("318253248681-dqkmfoss91d53o882loheo487fohdkek.apps.googleusercontent.com")
//                .requestEmail()
//                .build();
//
//        // Initialize sign in client
//        googleSignInClient = GoogleSignIn.getClient(SignUpActivity.this, googleSignInOptions);
//
//        btnGoogle.setOnClickListener((View.OnClickListener) view -> {
//            // Initialize sign in intent
//            Intent intent = googleSignInClient.getSignInIntent();
//            // Start activity for result
//            startActivityForResult(intent, 100);
//        });
//
//        FirebaseUser firebaseUser = auth.getCurrentUser();
//        // Check condition
//        if (firebaseUser != null) {
//            // When user already sign in redirect to profile activity
//            startActivity(new Intent(SignUpActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }
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