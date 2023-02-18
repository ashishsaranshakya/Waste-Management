package dev.ashishshakya.wastemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((Button)findViewById(R.id.btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef=FirebaseFirestore.getInstance().collection("waste-management-unchecked")
                        .document("tshirt");
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot=task.getResult();
                            Item item=new Item((HashMap<String, Object>) snapshot.get("cotton"));
                            ((TextView)findViewById(R.id.txt)).append("\n"+item.toString());
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Item does not exist in database", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        MenuItem menuItem=menu.findItem(R.id.icon_login_logout);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            menuItem.setTitle("Log out");
        }
        else{
            menuItem.setTitle("Log in");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();
        if (itemId==R.id.icon_login_logout){
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
                finish();
            }
            else{
                Bundle bundle=new Bundle();
                bundle.putBoolean("skipable",false);
                bundle.putBoolean("login",true);
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
            return true;
        }
        else if (itemId==R.id.icon_add_item){
            Bundle bundle=new Bundle();
            bundle.putBoolean("skipable",false);
            bundle.putBoolean("new_data",true);
            bundle.putBoolean("login",true);
            Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
}