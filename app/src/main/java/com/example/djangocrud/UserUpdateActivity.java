package com.example.djangocrud;

import android.util.Log;
import android.view.View;
import android.widget.*;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserUpdateActivity extends AppCompatActivity {
    // Declaration
    EditText edtName, edtGender, edtAge, edtHobby, edtProfession;
    ImageButton btnUpload, btnBack;
    Button btnSave;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_update);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialization
        edtName = findViewById(R.id.edtName);
        edtGender = findViewById(R.id.edtGender);
        edtAge = findViewById(R.id.edtAge);
        edtHobby = findViewById(R.id.edtHobby);
        edtProfession = findViewById(R.id.edtProfession);
        btnSave = findViewById(R.id.btnSave);
        btnUpload = findViewById(R.id.btnUpload);
        btnBack = findViewById(R.id.btnBack);

        // Setting Intent
        edtName.setText(getIntent().getStringExtra("name"));
        edtGender.setText(getIntent().getStringExtra("gender"));
        edtAge.setText(getIntent().getStringExtra("age"));
        edtHobby.setText(getIntent().getStringExtra("hobby"));
        edtProfession.setText(getIntent().getStringExtra("profession"));

        Glide.with(this)
                .load("http://192.168.100.218:8000" + getIntent().getStringExtra("profile_picture"))
                .placeholder(R.drawable.image_placeholder)
                .into(btnUpload);

        // Event Listener
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUser();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Create User Method
    private void updateUser() {
        // Getting User ID
        int userId = getIntent().getIntExtra("id", -1);

        // Getting User Input
        String name = edtName.getText().toString();
        String gender = edtGender.getText().toString();
        String age = edtAge.getText().toString();
        String hobby = edtHobby.getText().toString();
        String profession = edtProfession.getText().toString();

        // Check User Input
        if (name.isEmpty() || gender.isEmpty() || age.isEmpty() || hobby.isEmpty() || profession.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creating Image Part
        MultipartBody.Part imagePart = null;
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);
            inputStream.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageBytes);
            Log.d("Image:", imageUri.getLastPathSegment());
            imagePart = MultipartBody.Part.createFormData("profile_picture", imageUri.getLastPathSegment() + ".jpg", requestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Creating Request Body
        RequestBody nameRB = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody genderRB = RequestBody.create(MediaType.parse("text/plain"), gender);
        RequestBody ageRB = RequestBody.create(MediaType.parse("text/plain"), age);
        RequestBody hobbyRB = RequestBody.create(MediaType.parse("text/plain"), hobby);
        RequestBody professionRB = RequestBody.create(MediaType.parse("text/plain"), profession);

        // Calling API
        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<UserModel> call = apiService.updateUser(userId, imagePart, nameRB, genderRB, ageRB, hobbyRB, professionRB);

        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(UserUpdateActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UserUpdateActivity.this, "Failed to update user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable throwable) {
                Log.e("UserUpdateActivity", "Error: " + throwable.getMessage());
            }
        });
    }

    // Upload Image Method
    private void uploadImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageAndVideo.INSTANCE)
                .build());
    }
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                btnUpload.setImageURI(uri);
                if (uri != null) {
                    Log.d("Image:", uri.toString());
                    imageUri = uri;
                }
            });
}