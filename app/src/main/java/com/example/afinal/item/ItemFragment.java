package com.example.afinal.item;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.afinal.OnViewItemClickListener;
import com.example.afinal.databinding.FragmentItemBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemFragment extends Fragment {

    private static final String CAT_NAME = "cat_name";
    private String catName;
    public static final String ITEM_KEY = "item_key";
    FragmentItemBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private OnFragmentInteractionListener listener;

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            listener = (OnFragmentInteractionListener) context;
//        }
//    }




    public ItemFragment() {
    }

    public static ItemFragment newInstance(String catName) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putString(CAT_NAME, catName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catName = getArguments().getString(CAT_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentItemBinding.inflate(inflater);
//        db = FirebaseFirestore.getInstance();

        db.collection("Items").whereEqualTo("categoryName", catName).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()) {
                    ArrayList<Item> items = (ArrayList<Item>) value.toObjects(Item.class);

                    ItemAdapter adapter = new ItemAdapter(items, new OnViewItemClickListener() {
                        @Override
                        public void onItemClick(String id) {
                            Intent i = new Intent(getContext(), ItemDetails.class);
                            i.putExtra(ITEM_KEY, id);
                            startActivity(i);
                        }
                    },requireContext());
//                    adapter.setCartClickListener(new OnCartClickListener() {
//                        @Override
//                        public void onCartClickListener(Item items, int value) {
//
//                                listener.onItemSend(items, value);
//                        }
//                    });

                    binding.rv.setAdapter(adapter);
                    binding.rv.setHasFixedSize(true);
                    binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));

//                    populateDataIntoRV(items);
                }
            }
        });



        return binding.getRoot();
    }

//    private void populateDataIntoRV(ArrayList<Item> items) {
//        ItemAdapter adapter = new ItemAdapter(items, new OnViewItemClickListener() {
//            @Override
//            public void onItemClick(String id) {
//                Intent i = new Intent(getContext(), ItemDetails.class);
//                i.putExtra(ITEM_KEY, id);
//                startActivity(i);
//            }
//        });
//        adapter.setCartClickListener(new OnCartClickListener() {
//            @Override
//            public void onCartClickListener(Item item, int value) {
////                listener.onItemSend(item, value);
//                onCartClickListener(item,value);
//            }
//        });
//        binding.rv.setAdapter(adapter);
//        binding.rv.setHasFixedSize(true);
//        binding.rv.setLayoutManager(new LinearLayoutManager(requireContext()));
//    }

}
