package com.example.afinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.Profile.User;
import com.example.afinal.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final int PICK_IMG_REQ_CODE = 1;
    Uri selectedImageUri;

    private Button register;
    private TextView have_account;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        register = binding.activityRegisterBtnRegister;
        have_account = binding.activityRegisterTvHaveAccount;
        img = binding.profileUserImg;

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, PICK_IMG_REQ_CODE);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                String name = binding.activityRegisterEdName.getText().toString();
                String phone = binding.activityRegisterEdPhone.getText().toString().trim();
                String email = binding.activityRegisterEdEmail.getText().toString().trim();
                String pass = binding.activityRegisterEdPassword.getText().toString().trim();

                if(TextUtils.isEmpty(name))
                    binding.activityRegisterEdName.setError(getString(R.string.name_error));
                else if(TextUtils.isEmpty(phone))
                    binding.activityRegisterEdPhone.setError(getString(R.string.phone_error));
                else if(TextUtils.isEmpty(email))
                    binding.activityRegisterEdEmail.setError(getString(R.string.email_error));
                else if(TextUtils.isEmpty(pass))
                    binding.activityRegisterEdPassword.setError(getString(R.string.password_error));
                else if(selectedImageUri == null)
                    Toast.makeText(RegisterActivity.this, getString(R.string.img_error), Toast.LENGTH_SHORT).show();
                else{

                    binding.RpbLoading.setVisibility(View.VISIBLE);
                    binding.activityRegisterBtnRegister.setEnabled(false);
                    binding.activityRegisterTvHaveAccount.setEnabled(false);

                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {

                                    User user = new User(name, phone, email, "", mAuth.getUid());

                                    addImageToStoreg(user);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            binding.RpbLoading.setVisibility(View.INVISIBLE);
                            binding.RpbLoading.setVisibility(View.GONE);
                            binding.activityRegisterBtnRegister.setEnabled(true);
                            binding.activityRegisterTvHaveAccount.setEnabled(true);

                            Toast.makeText(RegisterActivity.this, getString(R.string.reg_error), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }

        });

        have_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addImageToStoreg(User user) {
        Calendar calendar = Calendar.getInstance();
        storage.getReference().child("profile/"+calendar.getTimeInMillis()).putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // another request to get image url
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        user.setImg(uri.toString());
                        addUserToFirestore(user);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.RpbLoading.setVisibility(View.INVISIBLE);
                binding.RpbLoading.setVisibility(View.GONE);
                binding.activityRegisterBtnRegister.setEnabled(true);
                binding.activityRegisterTvHaveAccount.setEnabled(true);

                Toast.makeText(RegisterActivity.this, getString(R.string.upload_img_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addUserToFirestore(User user) {
        db.collection("Users").document(user.getUserId()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        binding.RpbLoading.setVisibility(View.INVISIBLE);
                        binding.RpbLoading.setVisibility(View.GONE);
                        binding.activityRegisterBtnRegister.setEnabled(true);
                        binding.activityRegisterTvHaveAccount.setEnabled(true);

                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString(Constants.USER_UID_KEY,user.getUserId());
                        edit.putString(Constants.USER_EMAIL_KEY,user.getEmail());
                        edit.putString(Constants.USER_NAME_KEY,user.getName());
                        edit.putString(Constants.USER_IMAGE_KEY,user.getImg());
                        edit.putString(Constants.USER_PHONE_KEY,user.getPhone());
                        edit.commit();

                        Toast.makeText(RegisterActivity.this, getString(R.string.add_user), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.RpbLoading.setVisibility(View.INVISIBLE);
                binding.RpbLoading.setVisibility(View.GONE);
                binding.activityRegisterBtnRegister.setEnabled(true);
                binding.activityRegisterTvHaveAccount.setEnabled(true);

                Toast.makeText(RegisterActivity.this, getString(R.string.reg_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMG_REQ_CODE && resultCode == RESULT_OK && data != null) {
            Uri u = data.getData();
            int flag = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getContentResolver().takePersistableUriPermission(u,flag);

            img.setImageURI(u);
            selectedImageUri = u;
        }
    }
}