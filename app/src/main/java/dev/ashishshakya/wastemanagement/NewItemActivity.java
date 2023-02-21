package dev.ashishshakya.wastemanagement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
    private static final int IMAGE_REQUEST=12;
    EditText name;
    EditText material;
    EditText closestHub;
    EditText methodToRecycle_alternativeUse;
    EditText localResourcesAvailable;
    CheckBox recycleable;
    Button btnAddItem;
    Button addImage;
    Uri imageUri;

    String nameText;
    String materialText;
    String closestHubText;
    String methodToRecycle_alternativeUseText;
    String localResourcesAvailableText;
    String imageUrl;
    boolean isRecycleable;

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
            nameText=name.getText().toString().trim();
            materialText=material.getText().toString().trim();
            closestHubText=closestHub.getText().toString().trim();
            methodToRecycle_alternativeUseText=methodToRecycle_alternativeUse.getText().toString().trim();
            localResourcesAvailableText=localResourcesAvailable.getText().toString().trim();
            imageUrl="";
            isRecycleable=recycleable.isChecked();
            if(nameText.isEmpty()||materialText.isEmpty()||closestHubText.isEmpty()||
                    methodToRecycle_alternativeUseText.isEmpty()||localResourcesAvailableText.isEmpty()){
                Toast.makeText(NewItemActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
            }
            else{
                if (imageUri==null){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    uploadItem();
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Upload without image?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }else{
                    uploadItem();
                }
            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(NewItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(NewItemActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, IMAGE_REQUEST);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, IMAGE_REQUEST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case IMAGE_REQUEST:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, IMAGE_REQUEST);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
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
        }
    }

    void uploadImage(){
        if(imageUri!=null){
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("Uploading");
            progressDialog.show();
            StorageReference fileRef= FirebaseStorage.getInstance().getReference()
                    .child("Upload").child(name.getText().toString().trim()+"."+getFileExt(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            updateUrl(uri,progressDialog);
                        }
                    });
                }
            });
        }
        else {
            Toast.makeText(NewItemActivity.this, "Item submitted for evaluation", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(NewItemActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        }
    }

    void uploadItem(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        Item newItem=new Item(nameText,materialText,closestHubText,methodToRecycle_alternativeUseText,localResourcesAvailableText,isRecycleable,imageUrl);
        db.collection("waste-management-unchecked")
                .document(nameText).set(newItem.toMap()).addOnCompleteListener(task -> {
                    uploadImage();
                });
    }

    void updateUrl(Uri uri, ProgressDialog progressDialog){
        String url=uri.toString();
        progressDialog.dismiss();
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("waste-management-unchecked")
                .document(nameText).update("imageUrl",url).addOnCompleteListener(task -> {
                    Toast.makeText(NewItemActivity.this, "Item submitted for evaluation", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewItemActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                });
    }
}