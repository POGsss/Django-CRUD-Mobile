package com.example.djangocrud;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.DELETE;
import retrofit2.http.Query;

public interface ApiService {
    // Create User
    @Multipart
    @POST("user/create/")
    Call<UserModel> createUser(
            @Part MultipartBody.Part profile_picture,
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("age") RequestBody age,
            @Part("hobby") RequestBody hobby,
            @Part("profession") RequestBody profession
    );
    
    // Read User
    @GET("user/")
    Call<List<UserModel>> getUsers();

    @GET("user/")
    Call<List<UserModel>> getSearchUser(@Query("search") String query);

    // Update User
    @Multipart
    @PUT("user/{id}/")
    Call<UserModel> updateUser(
            @Path("id") int id,
            @Part MultipartBody.Part profile_picture,
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("age") RequestBody age,
            @Part("hobby") RequestBody hobby,
            @Part("profession") RequestBody profession
    );

    // Delete User
    @DELETE("user/{id}/")
    Call<Void> deleteUser(@Path("id") int id);
}
