package com.example.afinal.Profile;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.afinal.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.example.afinal.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PICK_IMG_REQ_CODE = 1;
    Uri selectedImageUri;
    String uid;




    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        View v = binding.getRoot();

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
        uid = sp.getString(Constants.USER_UID_KEY,null);

        db.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                Picasso.get().load(user.getImg()).into(binding.profileUserImg);
                binding.profileUserName.setText(user.getName());
                binding.profileUserEmail.setText(user.getEmail());
                binding.profileUserPhone.setText(user.getPhone());
            }
        });


        return v;
    }

}