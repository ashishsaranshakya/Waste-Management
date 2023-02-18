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
        ((TextView)findViewById(R.id.material_type)).setText(bundle.getString("material"));
        ((TextView)findViewById(R.id.closest_hub)).setText(bundle.getString("closestHub"));
        ((TextView)findViewById(R.id.alternative_use)).setText(bundle.getString("alternativeUse"));
        ((TextView)findViewById(R.id.local_resources)).setText(bundle.getString("localResources"));
        ((TextView)findViewById(R.id.recycleable)).setText(String.valueOf(bundle.getBoolean("recycleable")));
    }
}