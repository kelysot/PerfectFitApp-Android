package com.example.perfectfitapp_android.post;

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

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


public class AddNewPostStep1Fragment extends Fragment {

    ImageButton cameraBtn, galleryBtn;
    ImageView image;
    Button okBtn;
//    Uri picUri;
//    private ArrayList<String> permissionsToRequest;
//    private ArrayList<String> permissionsRejected = new ArrayList<>();
//    private ArrayList<String> permissions = new ArrayList<>();
//    private final static int ALL_PERMISSIONS_RESULT = 107;
//    private final static int IMAGE_RESULT = 200;
////    FloatingActionButton fabCamera, fabUpload;
    Bitmap mBitmap;
private String mImageUrl = "";

    private static final int INTENT_REQUEST_CODE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_post_step1, container, false);

        cameraBtn = view.findViewById(R.id.add_new_post_step1_camera_btn);
        galleryBtn = view.findViewById(R.id.add_new_post_step1_gallery_btn);
        image = view.findViewById(R.id.add_new_post_step1_image_imv);
        okBtn = view.findViewById(R.id.add_new_post_step1_ok_btn);
        okBtn.setOnClickListener(v -> moveStep2());

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");

                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }
            }
        });

        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mImageUrl));
                startActivity(intent);
            }
        });

//        askPermissions();

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                try {

                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    mBitmap = BitmapFactory.decodeStream(imageStream);
                    image.setImageBitmap(mBitmap);

//                    Bundle extras = data.getExtras();
//                    mBitmap = (Bitmap) extras.get("data");
//                    profilepic.setImageBitmap(imageBitmap);

                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());

                    Model.instance.uploadImage(mBitmap, getActivity(), new Model.UploadImageListener() {
                        @Override
                        public void onComplete(String url) {
                            mImageUrl = url;
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void moveStep2() {
        ArrayList<String> picturesArr = new ArrayList<>();
        Model.instance.getNewPost().setPicturesUrl(picturesArr);
        Navigation.findNavController(okBtn)
                .navigate(AddNewPostStep1FragmentDirections.actionAddNewPostStep1FragmentToAddNewPostFragment(mImageUrl));
    }


//    private void askPermissions() {
//        permissions.add(CAMERA);
//        permissions.add(WRITE_EXTERNAL_STORAGE);
//        permissions.add(READ_EXTERNAL_STORAGE);
//        permissionsToRequest = findUnAskedPermissions(permissions);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//
//            if (permissionsToRequest.size() > 0)
//                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
//        }
//    }


//    public Intent getPickImageChooserIntent() {
//
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        List<Intent> allIntents = new ArrayList<>();
//        PackageManager packageManager = getActivity().getPackageManager();
//
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
//        for (ResolveInfo res : listCam) {
//            Intent intent = new Intent(captureIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            if (outputFileUri != null) {
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//            }
//            allIntents.add(intent);
//        }
//
//        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        galleryIntent.setType("image/*");
//        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
//        for (ResolveInfo res : listGallery) {
//            Intent intent = new Intent(galleryIntent);
//            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
//            intent.setPackage(res.activityInfo.packageName);
//            allIntents.add(intent);
//        }
//
//        Intent mainIntent = allIntents.get(allIntents.size() - 1);
//        for (Intent intent : allIntents) {
//            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
//                mainIntent = intent;
//                break;
//            }
//        }
//        allIntents.remove(mainIntent);
//
//        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));
//
//        return chooserIntent;
//    }
//
//
//    private Uri getCaptureImageOutputUri() {
//        Uri outputFileUri = null;
//        File getImage = getActivity().getExternalFilesDir("");
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
//        }
//        return outputFileUri;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//
//        if (resultCode == Activity.RESULT_OK) {
//
//            if (requestCode == IMAGE_RESULT) {
//
//                String filePath = getImageFilePath(data);
//                if (filePath != null) {
//                    mBitmap = BitmapFactory.decodeFile(filePath);
//                    image.setImageBitmap(mBitmap);
//                }
//            }
//
//        }
//
//    }
//
//
//    private String getImageFromFilePath(Intent data) {
//        boolean isCamera = data == null || data.getData() == null;
//
//        if (isCamera) return getCaptureImageOutputUri().getPath();
//        else return getPathFromURI(data.getData());
//
//    }
//
//    public String getImageFilePath(Intent data) {
//        return getImageFromFilePath(data);
//    }
//
//    private String getPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Audio.Media.DATA};
//        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        outState.putParcelable("pic_uri", picUri);
//    }
//
////    @Override
////    public void onSaveInstanceState(Bundle outState) {
////        super.onSaveInstanceState(outState);
////        outState.putInt("curChoice", mCurCheckPosition);
////    }
////    @Override
////    public void onActivityCreated(Bundle savedInstanceState) {
////        super.onActivityCreated(savedInstanceState);
//////        if (savedInstanceState != null) {
//////            // Restore last state for checked position.
//////            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
//////        }
////        savedInstanceState.putParcelable("pic_uri", picUri);
////
////    }
//
//
//    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
//        ArrayList<String> result = new ArrayList<String>();
//
//        for (String perm : wanted) {
//            if (!hasPermission(perm)) {
//                result.add(perm);
//            }
//        }
//
//        return result;
//    }
//
//    private boolean hasPermission(String permission) {
//        if (canMakeSmores()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
//            }
//        }
//        return true;
//    }
//
//    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
//        new AlertDialog.Builder(MyApplication.getContext())
//                .setMessage(message)
//                .setPositiveButton("OK", okListener)
//                .setNegativeButton("Cancel", null)
//                .create()
//                .show();
//    }
//
//    private boolean canMakeSmores() {
//        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//
//        switch (requestCode) {
//
//            case ALL_PERMISSIONS_RESULT:
//                for (String perms : permissionsToRequest) {
//                    if (!hasPermission(perms)) {
//                        permissionsRejected.add(perms);
//                    }
//                }
//
//                if (permissionsRejected.size() > 0) {
//
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
//                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
//                                        }
//                                    });
//                            return;
//                        }
//                    }
//
//                }
//
//                break;
//        }
//
//    }
//
////    private void multipartImageUpload() {
////        try {
////            File filesDir = MyApplication.getContext().getFilesDir();
////            File file = new File(filesDir, "image" + ".png");
////
////
////            ByteArrayOutputStream bos = new ByteArrayOutputStream();
////            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
////            byte[] bitmapdata = bos.toByteArray();
////
////
////            FileOutputStream fos = new FileOutputStream(file);
////            fos.write(bitmapdata);
////            fos.flush();
////            fos.close();
////
////
////            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
////            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
////            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
////
////            Call<ResponseBody> req = apiService.postImage(body, name);
////            req.enqueue(new Callback<ResponseBody>() {
////                @Override
////                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
////
////                    if (response.code() == 200) {
////                        textView.setText("Uploaded Successfully!");
////                        textView.setTextColor(Color.BLUE);
////                    }
////
////                    Toast.makeText(getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
////                }
////
////                @Override
////                public void onFailure(Call<ResponseBody> call, Throwable t) {
////                    textView.setText("Uploaded Failed!");
////                    textView.setTextColor(Color.RED);
////                    Toast.makeText(getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
////                    t.printStackTrace();
////                }
////            });
////
////
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
////    @Override
////    public void onClick(View view) throws IOException {
////        switch (view.getId()) {
////            case R.id.add_new_post_step1_camera_btn:
////                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
////                break;
////
////            case R.id.add_new_post_step1_gallery_btn:
////                if (mBitmap != null){
////                    Model.instance.uploadImage(mBitmap, new Model.UploadImageListener() {
////                        @Override
////                        public void onComplete(Boolean isSuccess) {
////                            if(isSuccess){
////                                moveStep2();
////                                Toast.makeText(MyApplication.getContext(), "Bitmap succeed", Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                    });
////                }
//////                    multipartImageUpload();
////                else {
////                    Toast.makeText(MyApplication.getContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
////                }
////                break;
////        }
////    }
//
//


//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.add_new_post_step1_camera_btn:
//                startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);
//                break;
//
//            case R.id.add_new_post_step1_gallery_btn:
//                if (mBitmap != null) {
//                    try {
//                        Model.instance.uploadImage(mBitmap, new Model.UploadImageListener() {
//                            @Override
//                            public void onComplete(Boolean isSuccess) {
//                                if(isSuccess){
//                                    moveStep2();
//                                    Toast.makeText(MyApplication.getContext(), "Bitmap succeed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                else {
//                    Toast.makeText(MyApplication.getContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
}