package dev.ashishshakya.wastemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        Bundle bundle=getIntent().getExtras();
        ((TextView)findViewById(R.id.item_name)).setText(bundle.getString("name"));
        ((TextView)findViewById(R.id.material_type)).setText(String.format("Material: %s", bundle.getString("material")));
        ((TextView)findViewById(R.id.closest_hub)).setText(String.format("Closest hub for disposal:\n%s", bundle.getString("closestHub")));
        ((TextView)findViewById(R.id.alternative_use)).setText(String.format("Alternative use for item:\n%s", bundle.getString("alternativeUse")));
        ((TextView)findViewById(R.id.local_resources)).setText(String.format("Locally available resources:\n%s", bundle.getString("localResources")));
        ((TextView)findViewById(R.id.recycleable)).setText(String.format("Is the item recyclable: %s", bundle.getBoolean("recycleable")));
    }
}