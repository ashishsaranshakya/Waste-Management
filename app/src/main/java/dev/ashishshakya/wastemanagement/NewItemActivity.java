package dev.ashishshakya.wastemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NewItemActivity extends AppCompatActivity {
    EditText name;
    EditText material;
    EditText closestHub;
    EditText methodToRecycle_alternativeUse;
    EditText localResourcesAvailable;
    CheckBox recycleable;
    Button btnAddItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        name=findViewById(R.id.item_name);
        material=findViewById(R.id.item_material);
        closestHub=findViewById(R.id.item_closestHub);
        methodToRecycle_alternativeUse=findViewById(R.id.item_methodToRecycle_alternativeUse);
        localResourcesAvailable=findViewById(R.id.item_localResourcesAvailable);
        recycleable=findViewById(R.id.recycleable);
        btnAddItem=findViewById(R.id.item_add);

        btnAddItem.setOnClickListener(view -> {
            String nameText=name.getText().toString().trim();
            String materialText=material.getText().toString().trim();
            String closestHubText=closestHub.getText().toString().trim();
            String methodToRecycle_alternativeUseText=methodToRecycle_alternativeUse.getText().toString().trim();
            String localResourcesAvailableText=localResourcesAvailable.getText().toString().trim();
            boolean isRecycleable=recycleable.isChecked();
            if(nameText.isEmpty()||materialText.isEmpty()||closestHubText.isEmpty()||
                    methodToRecycle_alternativeUseText.isEmpty()||localResourcesAvailableText.isEmpty()){
                Toast.makeText(NewItemActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else{
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                Item newItem=new Item(nameText,materialText,closestHubText,methodToRecycle_alternativeUseText,localResourcesAvailableText,isRecycleable);
                db.collection("waste-management-unchecked")
                        .document(nameText).set(newItem.toMap()).addOnCompleteListener(task -> {
                            Toast.makeText(NewItemActivity.this, "Item submitted for evaluation", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewItemActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        });
            }
        });

    }
}