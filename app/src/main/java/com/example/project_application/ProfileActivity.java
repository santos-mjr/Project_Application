package com.example.project_application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    private static final int READ_EXTERNAL_STORAGE_REQUEST = 101;
    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private Button logout,saveBtn;

    FirebaseAuth auth;
    private ImageView profileImg,backBtn;
    private TextView profileName,profileEmail;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageReference;
    private AlertDialog dialog;
    EditText fullNameTextView;
    private int checker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = (Button) findViewById(R.id.signOut);
        saveBtn = (Button) findViewById(R.id.save_data);
       backBtn =  findViewById(R.id.profile_back);
       profileName =  findViewById(R.id.profile_name);
       profileEmail =  findViewById(R.id.profile_email);
       backBtn.setOnClickListener(view -> {
           startActivity(new Intent(ProfileActivity.this, Home.class));
           finish();
       });
        saveBtn.setOnClickListener(view -> {
            if(checker == 1){
                saveData();
            }else{
                updateData();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, Login.class));
                finish();
            }
        });

        profileImg = findViewById(R.id.profile_img);
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
         storageReference = FirebaseStorage.getInstance().getReference().child("profileImage");
         fullNameTextView =  findViewById(R.id.fullName);
         fullNameTextView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 saveBtn.setVisibility(View.VISIBLE);
             }
         });
        profileImg.setOnClickListener(view -> {
        getPermissionsToShowphotos();

        });
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String fullName = userProfile.fullName;
                    fullNameTextView.setText(fullName);
                    profileName.setText(fullName);
                    profileEmail.setText(userProfile.email);
                    if(userProfile.image!=null){
                        Glide.with(ProfileActivity.this).load(userProfile.image).into(profileImg);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Something wrong happened!", Toast.LENGTH_SHORT).show();
            }
        });

        //Navigation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent_one = new Intent(ProfileActivity.this, Home.class);
                        intent_one.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_one);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:
                        Intent intent_two = new Intent(ProfileActivity.this, CommunityPlatform.class);
                        intent_two.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_two);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        Intent intent_three = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent_three.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent_three);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setView(R.layout.loading_dialog);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public void onBackPressed() {
    }
    private void getPermissionsToShowphotos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_REQUEST);
                return;
            } else {
                cropImage();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cropImage();
                } else {
                    Toast.makeText(this, "Permission is mandatory to get your photo", Toast.LENGTH_SHORT).show();
                }

        }

    }
    private void cropImage() {
        checker = 1;
        CropImage.activity(imageUri)
                .setAspectRatio(1, 1)
                .start(ProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImg.setImageURI(imageUri);
            saveBtn.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Error,Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            finish();
        }
    }
        private void uploadData(){
            dialog.show();
            if(imageUri!=null){
              final StorageReference photoRef = storageReference.child(userID+".jpg");
              final UploadTask uploadTask = photoRef.putFile(imageUri);
              uploadTask.addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(ProfileActivity.this, "the image is not uploaded", Toast.LENGTH_SHORT).show();
                      Log.d("TAG",e.getMessage());
                       dialog.dismiss();
                  }
              }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                  @Override
                  public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                      Toast.makeText(ProfileActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                      Task<Uri> uriTask =uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                          @Override
                          public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                              if (!task.isSuccessful()) {
                                  throw task.getException();
                              }
                              myUrl = photoRef.getDownloadUrl().toString();
                              return photoRef.getDownloadUrl();
                          }

                      }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                          @Override
                          public void onComplete(@NonNull Task<Uri> task) {
                              if (task.isSuccessful()) {
                                  Uri downloadUrl = task.getResult();
                                  myUrl = downloadUrl.toString();

                                  HashMap<String, Object> hashMap = new HashMap<>();
                                  hashMap.put("fullName", fullNameTextView.getText().toString());
                                  hashMap.put("image", myUrl);
                                  reference.child(userID).updateChildren(hashMap);
                                  dialog.dismiss();
                                  startActivity(new Intent(ProfileActivity.this, Home.class));
                                  finish();
                              }
                          }
                      });
                  }
              });
            }else {
                Toast.makeText(this, "Image did not select", Toast.LENGTH_SHORT).show();
            }
        }

    private void saveData(){
        if(TextUtils.isEmpty(fullNameTextView.getText().toString().trim())){
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
        }else{
            uploadData();
        }
    }
    private void updateData(){
        reference.child(userID).child("fullName").setValue(fullNameTextView.getText().toString().trim());
        startActivity(new Intent(ProfileActivity.this, Home.class));
        finish();
    }
}