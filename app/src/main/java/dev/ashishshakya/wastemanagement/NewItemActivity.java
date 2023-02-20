package dev.ashishshakya.wastemanagement;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class NewItemActivity extends AppCompatActivity {
    EditText name;
    EditText material;
    EditText closestHub;
    EditText methodToRecycle_alternativeUse;
    EditText localResourcesAvailable;
    CheckBox recycleable;
    Button btnAddItem;
    Button addImage;
    Uri imageUri;
    private static final int IMAGE_REQUEST=2;

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
        addImage=findViewById(R.id.add_image);

        btnAddItem.setOnClickListener(view -> {
            String nameText=name.getText().toString().trim();
            String materialText=material.getText().toString().trim();
            String closestHubText=closestHub.getText().toString().trim();
            String methodToRecycle_alternativeUseText=methodToRecycle_alternativeUse.getText().toString().trim();
            String localResourcesAvailableText=localResourcesAvailable.getText().toString().trim();
            String imageUrl="https://en.wikipedia.org/wiki/File:Wysypisko.jpg";
            boolean isRecycleable=recycleable.isChecked();
            if(nameText.isEmpty()||materialText.isEmpty()||closestHubText.isEmpty()||
                    methodToRecycle_alternativeUseText.isEmpty()||localResourcesAvailableText.isEmpty()){
                Toast.makeText(NewItemActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else{
                FirebaseFirestore db=FirebaseFirestore.getInstance();
                Item newItem=new Item(nameText,materialText,closestHubText,methodToRecycle_alternativeUseText,localResourcesAvailableText,isRecycleable,imageUrl);
                db.collection("waste-management-unchecked")
                        .document(nameText).set(newItem.toMap()).addOnCompleteListener(task -> {
                            Toast.makeText(NewItemActivity.this, "Item submitted for evaluation", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewItemActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        });
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                12);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant

                        return;
                    }
                    else{
                        Intent intent=new Intent();
                        intent.setType("image/");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent,IMAGE_REQUEST);
                    }
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 12: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent=new Intent();
                    intent.setType("image/");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent,IMAGE_REQUEST);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }

    String getFileExt(Uri uri){
        ContentResolver resolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(resolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK){
            imageUri=data.getData();
            uploadImage();
        }
    }

    void uploadImage(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        if(imageUri!=null){
            StorageReference fileRef= FirebaseStorage.getInstance().getReference()
                    .child("Upload").child(name.getText().toString().trim()+"."+getFileExt(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url=uri.toString();
                            progressDialog.dismiss();
                            Toast.makeText(NewItemActivity.this, "Upload successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }
}