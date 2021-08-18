package com.example.afinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.example.afinal.Profile.User;
import com.example.afinal.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Button login;
    private TextView  register, skip, forget;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sp;
    SharedPreferences.Editor edit;


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth != null && mAuth.getCurrentUser()!=null && sp.getString(Constants.USER_UID_KEY,null) != null){
            moveToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        mAuth = FirebaseAuth.getInstance();
//        db = FirebaseFirestore.getInstance();
        sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        edit = sp.edit();

        login = binding.activityLoginBtnLogin;
        register = binding.activityLoginTvRegister;
        skip = binding.activityLoginTvSkip;
        forget = binding.goToForgetPassword;



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = binding.activityLoginEdEmail.getText().toString().trim();
                String pass = binding.activityLoginEdPassword.getText().toString().trim();

                if(email.isEmpty())
                    binding.activityLoginEdEmail.setError(getString(R.string.email_error));
                else if(pass.isEmpty())
                    binding.activityLoginEdPassword.setError(getString(R.string.password_error));
                else{

                    binding.LpbLoading.setVisibility(View.VISIBLE);
                    binding.activityLoginBtnLogin.setEnabled(false);
                    binding.activityLoginTvSkip.setEnabled(false);
                    binding.activityLoginTvRegister.setEnabled(false);
                    binding.goToForgetPassword.setEnabled(false);

                    mAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            if (authResult.getUser() != null)
                                getUserDataFromFirestore(authResult.getUser().getUid());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.LpbLoading.setVisibility(View.INVISIBLE);
                            binding.LpbLoading.setVisibility(View.GONE);
                            binding.activityLoginBtnLogin.setEnabled(true);
                            binding.activityLoginTvSkip.setEnabled(true);
                            binding.activityLoginTvRegister.setEnabled(true);
                            binding.goToForgetPassword.setEnabled(true);

                            Toast.makeText(LoginActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getUserDataFromFirestore(String uid) {
        db.collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                binding.LpbLoading.setVisibility(View.INVISIBLE);
                binding.LpbLoading.setVisibility(View.GONE);
                binding.activityLoginBtnLogin.setEnabled(true);
                binding.activityLoginTvSkip.setEnabled(true);
                binding.activityLoginTvRegister.setEnabled(true);
                binding.goToForgetPassword.setEnabled(true);

                if(documentSnapshot != null){
                    User user = documentSnapshot.toObject(User.class);


                    edit.putString(Constants.USER_UID_KEY,user.getUserId());
                    edit.putString(Constants.USER_EMAIL_KEY,user.getEmail());
                    edit.putString(Constants.USER_NAME_KEY,user.getName());
                    edit.putString(Constants.USER_IMAGE_KEY,user.getImg());
                    edit.putString(Constants.USER_PHONE_KEY,user.getPhone());
                    edit.commit();

                    moveToMainActivity();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.LpbLoading.setVisibility(View.INVISIBLE);
                binding.LpbLoading.setVisibility(View.GONE);
                binding.activityLoginBtnLogin.setEnabled(true);
                binding.activityLoginTvSkip.setEnabled(true);
                binding.activityLoginTvRegister.setEnabled(true);
                binding.goToForgetPassword.setEnabled(true);
                Toast.makeText(LoginActivity.this, getString(R.string.get_field_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}