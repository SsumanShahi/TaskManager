package com.suman.taskmanager.api;

import com.suman.taskmanager.model.User;
import com.suman.taskmanager.serverrespone.ImageRespone;
import com.suman.taskmanager.serverrespone.SignupResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Userapi {


    @POST("users/signup")
    Call<SignupResponse> resgisterUser(@Body User user);
//
//    @Multipart
//    @POST("upload")
//    Call<ImageRespone> uploadImage(@Part MultipartBody.Part img);

}
