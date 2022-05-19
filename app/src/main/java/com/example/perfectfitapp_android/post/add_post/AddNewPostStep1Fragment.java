package com.example.perfectfitapp_android.post.add_post;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;
import static android.app.Activity.RESULT_OK;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class AddNewPostStep1Fragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;
    private String mImageUrl = "";

    ImageView image;
    Button okBtn;
    Bitmap mBitmap;
    ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_post_step1, container, false);

        image = view.findViewById(R.id.add_new_post_step1_image_imv);
        progressBar = view.findViewById(R.id.add_new_post_step1_progressBar);
        progressBar.setVisibility(View.GONE);

        okBtn = view.findViewById(R.id.add_new_post_step1_ok_btn);
        okBtn.setOnClickListener(v -> moveStep2());

        image.setOnClickListener(v -> showImagePickDialog());

        return view;
    }

    private void showImagePickDialog() {

        String[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Choose an Option");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                if (i == 0) {
                    openCam();
                }

                if (i == 1) {
                    openGallery();
                }
            }
        });

        builder.create().show();
    }

    public void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    public void openGallery() {
        Intent photoPicerIntent = new Intent(Intent.ACTION_PICK);
        photoPicerIntent.setType("image/*");
        startActivityForResult(photoPicerIntent, REQUEST_IMAGE_PIC);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                mBitmap = (Bitmap) extras.get("data");
                int width = getActivity().getResources().getDisplayMetrics().widthPixels;
                int height = (width*mBitmap.getHeight())/mBitmap.getWidth();
                mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
                image.setImageBitmap(mBitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_PIC) {
            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    mBitmap = BitmapFactory.decodeStream(imageStream);
                    int width = getActivity().getResources().getDisplayMetrics().widthPixels;
                    int height = (width*mBitmap.getHeight())/mBitmap.getWidth();
                    mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
                    image.setImageBitmap(mBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void moveStep2() {

        progressBar.setVisibility(View.VISIBLE);
        okBtn.setEnabled(false);

        if (mBitmap != null) {
            Model.instance.uploadImage(mBitmap, getActivity(), url -> {
                mImageUrl = url;
                Navigation.findNavController(okBtn)
                        .navigate(AddNewPostStep1FragmentDirections.actionAddNewPostStep1FragmentToAddNewPostFragment(mImageUrl));
            });
        } else {
            ArrayList<String> picturesArr = new ArrayList<>();
            Model.instance.getNewPost().setPicturesUrl(picturesArr);
            Navigation.findNavController(okBtn)
                    .navigate(AddNewPostStep1FragmentDirections.actionAddNewPostStep1FragmentToAddNewPostFragment(mImageUrl));
        }

    }

}