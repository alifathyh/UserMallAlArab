package com.example.afinal.item;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Toast;

import com.example.afinal.ActivityBase;
import com.example.afinal.ListOrder.OrderItem;
import com.example.afinal.MainActivity;

import com.example.afinal.R;
import com.example.afinal.databinding.ActivityItemDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ItemDetails extends ActivityBase {
    ActivityItemDetailsBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String docId;
    Item item;
    double value;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        docId = intent.getStringExtra(ItemFragment.ITEM_KEY);



        db.collection("Items").document(docId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                item = documentSnapshot.toObject(Item.class);
                Picasso.get().load(item.getImage()).into(binding.itemImg);
                binding.itemDescription.setText( item.getDescription());
                binding.itemPrice.setText( item.getPrice() + " NIS");
                binding.itemName.setText( item.getName());
//                binding.itemCategory.setText("Category: \n " + item.getCategoryName());
            }
        });

        binding.itemImgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = Double.parseDouble(binding.itemEdCount.getText().toString());
                if(item.getIndexTheCondition()==1){
                    Toast.makeText(ItemDetails.this,  getString(R.string.products_not_available), Toast.LENGTH_SHORT).show();
                }else{
                    if(item.getIndexQuantity()==0){
                        value += 1.0;
                    }else{
                        value += 0.25;
                    }
                }
                binding.itemEdCount.setText(String.valueOf(value));
            }
        });

        binding.itemImgSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = Double.parseDouble(binding.itemEdCount.getText().toString());
                if(item.getIndexTheCondition()==1){
                    Toast.makeText(ItemDetails.this,  getString(R.string.products_not_available), Toast.LENGTH_SHORT).show();
                }else{
                    if(item.getIndexQuantity()==0){
                        if (value > 1.0)
                            value -= 1.0;
                    }else{
                        if (value > 1)
                            value -= 0.25;
                    }
                }
                binding.itemEdCount.setText(String.valueOf(value));
            }
        });


        binding.ItemBtnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.getIndexTheCondition() == 0){
                    value = Double.parseDouble(binding.itemEdCount.getText().toString());
                    OrderItem orderItem = new OrderItem(item, value, "");
                    orderItems.add(orderItem);
                    Toast.makeText(getBaseContext(),getString(R.string.add_to_cart), Toast.LENGTH_SHORT).show();
                }else{
                Toast.makeText(getBaseContext(),  getString(R.string.products_not_available), Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.itemDescription.setMovementMethod(new ScrollingMovementMethod());
    }
}