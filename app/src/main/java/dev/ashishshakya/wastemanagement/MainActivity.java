package dev.ashishshakya.wastemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter adapter;
    List<Item> itemList=new ArrayList<>();
    final List<String> itemNameList=new ArrayList<>();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listView);
        setListView();
        final ArrayAdapter adapter= new ArrayAdapter<>(this, R.layout.frame, itemNameList);
        this.adapter=adapter;

        listView.setAdapter(adapter);
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

        MenuItem search=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) search.getActionView();
        searchView.setQueryHint("Item name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s.trim());
                return false;
            }
        });

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

    void setListView(){
        CollectionReference colRef=FirebaseFirestore.getInstance().collection("waste-management");
        colRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Item item=new Item(document.getData());
                    item.setName(item.getName().substring(0,1).toUpperCase()+item.getName().substring(1));
                    itemList.add(item);
                    itemNameList.add(item.getName());
                }
                Collections.sort(itemList, (item, t1) -> item.getName().compareTo(t1.getName()));
                Collections.sort(itemNameList);
                adapter.notifyDataSetChanged();
            }
            else {
                Toast.makeText(MainActivity.this, "Connect to internet", Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle=new Bundle();
            Item item=itemList.get(i);
            bundle.putString("name",item.getName());
            bundle.putString("material",item.getMaterial());
            bundle.putString("closestHub",item.getClosestHub());
            bundle.putString("alternativeUse",item.getMethodToRecycle_alternativeUse());
            bundle.putString("localResources",item.getLocalResourcesAvailable());
            bundle.putBoolean("recycleable",item.isRecycleable());
            bundle.putString("imageUrl",item.getImageUrl());
            startActivity(new Intent(MainActivity.this,ItemActivity.class).putExtras(bundle));
        });
    }
}