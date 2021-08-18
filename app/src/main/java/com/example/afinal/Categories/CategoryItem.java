package com.example.afinal.Categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.afinal.OnViewItemClickListener;
import com.example.afinal.R;
import com.example.afinal.databinding.ActivityCategoryItemBinding;
import com.example.afinal.item.Item;
import com.example.afinal.item.ItemAdapter;
import com.example.afinal.item.ItemDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CategoryItem extends AppCompatActivity {
    ActivityCategoryItemBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final String ITEM_KEY = "item_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        db = FirebaseFirestore.getInstance();


        Intent intent = getIntent();

        String name = intent.getStringExtra(CategoriesFragment.ITEM_KEY);

        db.collection("Items").whereEqualTo("categoryName",name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Item> items = (ArrayList<Item>) queryDocumentSnapshots.toObjects(Item.class);
                ItemAdapter adapter = new ItemAdapter(items, new OnViewItemClickListener() {
                    @Override
                    public void onItemClick(String id) {
                        Intent i = new Intent(getBaseContext(), ItemDetails.class);
                        i.putExtra(ITEM_KEY, id);
                        startActivity(i);
                    }
                },getBaseContext());
                binding.rv.setAdapter(adapter);
                binding.rv.setHasFixedSize(true);
                binding.rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CategoryItem.this,  R.string.category_item_failed, Toast.LENGTH_SHORT).show();
            }
        });


    }
}