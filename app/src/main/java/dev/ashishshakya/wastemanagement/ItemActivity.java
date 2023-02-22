package dev.ashishshakya.wastemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

public class ItemActivity extends AppCompatActivity {

    @SuppressLint("UseCompatLoadingForDrawables")
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
        if (bundle.getBoolean("recycleable")) ((TextView)findViewById(R.id.recycleable)).setText(new String("Is the item recyclable: Yes"));
        else ((TextView)findViewById(R.id.recycleable)).setText(new String("Is the item recyclable: No"));
        if(Objects.equals(bundle.getString("imageUrl"), "")){
            ((ImageView) findViewById(R.id.item_image)).setImageDrawable(getDrawable(R.drawable.image_not_found));
        }else{
            Glide.with(this).load(bundle.getString("imageUrl")).into((ImageView) findViewById(R.id.item_image));
        }
    }
}