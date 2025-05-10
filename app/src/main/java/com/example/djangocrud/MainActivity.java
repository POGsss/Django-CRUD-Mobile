package com.example.djangocrud;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    // Declaration
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private List<UserModel> userList = new ArrayList<>();
    FloatingActionButton fabAdd;
    ImageButton btnFilter;
    SearchView searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialization
        fabAdd = findViewById(R.id.fabAdd);
        btnFilter = findViewById(R.id.btnFilter);
        searchItem = findViewById(R.id.searchItem);
        userRecyclerView = findViewById(R.id.userRecyclerView);

        // RecyclerView
        userRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        userAdapter = new UserAdapter(userList);
        userRecyclerView.setAdapter(userAdapter);

        // Event Listener
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserCreateActivity.class);
                startActivity(intent);
            }
        });

        // Search Functionality
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                // Call Search Method
                fetchSearchUser(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Call Search Method
                fetchSearchUser(s);
                return false;
            }
        });

        // Call Fetch Methods
        fetchUser();
    }

    // Fetch Method
    public void fetchUser() {
        userList.clear();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<UserModel>> call = apiService.getUsers();

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> users = response.body();
                    if (users != null && !users.isEmpty()) {
                        userList.addAll(users);
                        userAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: " + throwable.getMessage());
            }
        });
    }

    // Search Method
    public void fetchSearchUser(String query) {
        userList.clear();

        ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
        Call<List<UserModel>> call = apiService.getSearchUser(query);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                if (response.isSuccessful()) {
                    List<UserModel> users = response.body();
                    if (users != null && !users.isEmpty()) {
                        userList.addAll(users);
                        userAdapter.notifyDataSetChanged();
                    } else {
                        userList.clear();
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "No users found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<UserModel>> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: " + throwable.getMessage());
            }
        });
    }
}