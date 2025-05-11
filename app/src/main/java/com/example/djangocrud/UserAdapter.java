package com.example.djangocrud;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<UserModel> userList;

    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        UserModel userModel = userList.get(position);

        holder.textTitle.setText(userModel.getName());
        holder.textAgeGender.setText(userModel.getAge() + ", " + userModel.getGender());
        holder.textHobbyProfession.setText(userModel.getHobby() + ", " + userModel.getProfession());

        Glide.with(holder.imgProfile.getContext())
                .load("http://192.168.100.218:8000" + userModel.getProfile_picture())
                .placeholder(R.drawable.image_placeholder)
                .into(holder.imgProfile);

        holder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), UserUpdateActivity.class);

                intent.putExtra("profile_picture", userModel.getProfile_picture());
                intent.putExtra("id", userModel.getId());
                intent.putExtra("name", userModel.getName());
                intent.putExtra("gender", userModel.getGender());
                intent.putExtra("age", userModel.getAge());
                intent.putExtra("hobby", userModel.getHobby());
                intent.putExtra("profession", userModel.getProfession());

                holder.itemView.getContext().startActivity(intent);
            }
        });

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiService apiService = RetrofitClient.getInstance().create(ApiService.class);
                Call<Void> call = apiService.deleteUser(userModel.getId());

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            userList.remove(userModel);
                            notifyDataSetChanged();
                            Toast.makeText(holder.itemView.getContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable throwable) {
                        Toast.makeText(holder.itemView.getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        // Declaration
        TextView textTitle, textAgeGender, textHobbyProfession;
        ImageView imgProfile;
        ImageButton editIcon, deleteIcon;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialization
            textTitle = itemView.findViewById(R.id.textTitle);
            textAgeGender = itemView.findViewById(R.id.textAgeGender);
            textHobbyProfession = itemView.findViewById(R.id.textHobbyProfession);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            editIcon = itemView.findViewById(R.id.editIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
