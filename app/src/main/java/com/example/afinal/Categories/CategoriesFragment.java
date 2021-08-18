package com.example.afinal.Categories;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.afinal.OnCategoryClickListener;
import com.example.afinal.R;
import com.example.afinal.databinding.FragmentCategoriesBinding;
import com.example.afinal.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class CategoriesFragment extends Fragment {
    FragmentCategoriesBinding binding;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    public static final String ITEM_KEY = "item_key";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoriesBinding.inflate(inflater);
//        db = FirebaseFirestore.getInstance();

        db.collection("Categories").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    ArrayList<Category> categories = (ArrayList<Category>) queryDocumentSnapshots.toObjects(Category.class);

                    CategoryAdapter adapter = new CategoryAdapter(categories, new OnCategoryClickListener() {
                        @Override
                        public void onItemClick(String name) {
                            Intent i = new Intent(getContext(),CategoryItem.class);
                            i.putExtra(ITEM_KEY,name);
                            startActivity(i);
                        }
                    });
                    binding.rv.setAdapter(adapter);
                    binding.rv.setHasFixedSize(true);
                    binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), R.string.category_failed, Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();

    }
}