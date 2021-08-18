package com.example.afinal.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.afinal.Categories.Category;
import com.example.afinal.R;
import com.example.afinal.item.PagerAdapter;
import com.example.afinal.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
//        db = FirebaseFirestore.getInstance();

        db.collection("Categories").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Category> categories = (ArrayList<Category>) queryDocumentSnapshots.toObjects(Category.class);
                setupViewPagerWithTabs(categories);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(requireContext(), R.string.category_failed, Toast.LENGTH_SHORT).show();

            }
        });
        return binding.getRoot();
    }

    private void setupViewPagerWithTabs(ArrayList<Category> categories) {

        PagerAdapter adapter = new PagerAdapter(requireActivity(), categories);
        binding.displayItemPager.setAdapter(adapter);
        new TabLayoutMediator(binding.displayItemTable, binding.displayItemPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(categories.get(position).getName());
            }
        }).attach();
    }


}