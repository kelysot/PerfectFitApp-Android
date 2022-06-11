package com.example.perfectfitapp_android.model.ModelServer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.model.Model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImagesModelServer {

    Server server = new Server();

    public void uploadImage(Bitmap imageBytes, Context context, Model.UploadImageListener listener) {
        File filesDir = context.getFilesDir();
        File file = new File(filesDir, "image" + ".png");

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageBytes.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");

        Call<ResponseBody> call = server.service.uploadImage(body, name);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response1) {
                if (response1.code() == 200) {
                    try {
                        listener.onComplete(response1.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onComplete(null);

            }
        });
    }

    public void getImages(String urlImage, Model.GetImagesListener listener) {
        Call<ResponseBody> call = server.service.getImage(urlImage);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        try {
                            Bitmap bitmap = convertCompressedByteArrayToBitmap(response.body().bytes());
                            listener.onComplete(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        listener.onComplete(null);
                    }
                } else {
                    listener.onComplete(null);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getLocalizedMessage());
                listener.onComplete(null);
            }
        });
    }

    public static Bitmap convertCompressedByteArrayToBitmap(byte[] src) {
        return BitmapFactory.decodeByteArray(src, 0, src.length);
    }
}
