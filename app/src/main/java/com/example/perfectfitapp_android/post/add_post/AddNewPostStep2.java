package com.example.perfectfitapp_android.post.add_post;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.generalModel;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddNewPostStep2 extends Fragment {

    EditText productNameEt, skuEt, descriptionEt, linkEt, priceEt;
    Button postBtn;
    RatingBar ratingBar, sizeAdjSk;

    TextInputLayout sizeTxtIL, categoryTxtIL, subcategoryTxtIL, companyTxtIl, colorTxtIl;
    AutoCompleteTextView sizeAutoTv, categoryAuto, subCategoryAuto, companyAuto, colorAuto;
    String[] categoryArr;
    ArrayList<String> subcategoryArr;
    ArrayList<String> pics = new ArrayList<>();
    ArrayAdapter<String> sizeAdapter, categoryAdapter, subcategoryAdapter, companyAdapter, colorAdapter;
    String chosenCategory;
    List<String> sizesList, companiesList, colorsList;
    LottieAnimationView progressBar;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;
    private String mImageUrl = "";
    ImageView image;
    Bitmap mBitmap;

    int check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_post_step2, container, false);

        image = view.findViewById(R.id.addnewpost_step2_image_imv);
        image.setOnClickListener(v -> showImagePickDialog());

        Model.instance.getGeneral(map -> {
            generalModel.instance.map = map;
        });

        productNameEt = view.findViewById(R.id.addnewpost_productname_et);
        skuEt = view.findViewById(R.id.addnewpost_sku_et);
        descriptionEt = view.findViewById(R.id.addnewpost_description_et);
        linkEt = view.findViewById(R.id.addnewpost_link_et);
        priceEt = view.findViewById(R.id.addnewpost_price_et);
        progressBar = view.findViewById(R.id.addnewpost_progress_bar);
        progressBar.setVisibility(View.GONE);

        sizeAdjSk = view.findViewById(R.id.addnewpost_sizeadjustment_ratingbar);
        ratingBar = view.findViewById(R.id.addnewpost_rating_ratingbar);

        postBtn = view.findViewById(R.id.addnewpost_post_btn);
        postBtn.setOnClickListener(v -> post(view));

        setAllDropDownMenus(view);

        return view;
    }

    String productName, sku, size, company, color, category, subCategory, description;
    String link, price;
    String sizeAdj, rating;

    //TODO: Make all fields require.
    private void post(View view) {

        progressBar.setVisibility(View.VISIBLE);
        postBtn.setEnabled(false);



        productName = productNameEt.getText().toString();
        sku = skuEt.getText().toString();
        description = descriptionEt.getText().toString();
        link = linkEt.getText().toString();

        size = sizeAutoTv.getText().toString();
        company = companyAuto.getText().toString();
        color = colorAuto.getText().toString();
        category = categoryAuto.getText().toString();
        subCategory = subCategoryAuto.getText().toString();
        rating = String.valueOf(ratingBar.getRating());
        sizeAdj = String.valueOf(sizeAdjSk.getRating());
        price = priceEt.getText().toString();

        //TODO: add validations

        boolean flag = true;

        if(category.isEmpty()){
            categoryAuto.setError("Please enter category");
            //TODO: remove the error after the client fill the field
            flag = false;
        }
        if(subCategory.isEmpty()){
            subCategoryAuto.setError("Please enter sub-category.");
            flag = false;
        }
        if(company.isEmpty()){
            companyAuto.setError("Please enter company.");
            flag = false;
        }
        if(color.isEmpty()){
            colorAuto.setError("Please enter color.");
            flag = false;
        }
        if(size.isEmpty()){
            sizeAutoTv.setError("Please enter size.");
            flag = false;
        }
        if(price.isEmpty()){
            priceEt.setError("Please enter price.");
            flag = false;
        }
        if(productName.isEmpty()){
            productNameEt.setError("Please enter product name.");
            flag = false;
        }


        //TODO: Require or not:

        if(description.isEmpty()){
            description = "-";
        }
        if (sku.isEmpty()){
            sku = "-";
        }

        if(link.isEmpty()){
            link = "-";
        }
        else if(!Patterns.WEB_URL.matcher(link).matches() && !URLUtil.isHttpUrl(link)){
            linkEt.setError("Please enter a valid link or leave the field blank.");
            flag = false;
        }
        else{
            if(!link.contains("http")){
                String newLink = "http://" + link;
                link = newLink;
            }
        }

        if(flag){

            String date = "";

            StringBuilder count = new StringBuilder();
            count.append(Model.instance.getCount());
            Model.instance.setCount(Model.instance.getCount() + 1);

            /*********************************/

            if (mBitmap != null) {
                Model.instance.uploadImage(mBitmap, getActivity(), url -> {
                    pics.add(url);
                    sendPostToServer();
                });
            } else {
                Model.instance.getNewPost().setPicturesUrl(pics);
                sendPostToServer();
            }

            /*********************************/


//        newPost.setPicturesUrl(Model.instance.getNewPost().getPicturesUrl());

        }
        else{
            progressBar.setVisibility(View.GONE);
            postBtn.setEnabled(true);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                categoryAuto.setError(null);
                subCategoryAuto.setError(null);
                companyAuto.setError(null);
                colorAuto.setError(null);
                sizeAutoTv.setError(null);
                priceEt.setError(null);
                productNameEt.setError(null);
            }, 5000);
        }
    }

    public void sendPostToServer(){

        Post newPost = new Post("", Model.instance.getProfile().getUserName(), productName, sku, size, company, color, category,
                subCategory, description, "", link, sizeAdj, rating, pics, price, null, null, "false");


        Model.instance.addNewPost(newPost, post -> {
            if (post != null) {
                Model.instance.addPost(post);
                Model.instance.setNewPost(new Post());
                Model.instance.getProfile().getMyPostsListId().add(post.getPostId());
                Navigation.findNavController(postBtn)
                        .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());
            } else {
                //TODO: dialog
                postBtn.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyApplication.getContext(), "Post didn't saved",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    public void delay(){
        categoryTxtIL.setError(null);
    }

    public void setAllDropDownMenus(View view){

        /****** size ******/
        sizeTxtIL = view.findViewById(R.id.addnewpost_size_txtil);
        sizeAutoTv = view.findViewById(R.id.addnewpost_size_auto);
//        sizeArr = getResources().getStringArray(R.array.sizes);
        sizesList = generalModel.instance.map.get("Sizes");

        sizeAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, sizesList);
        sizeAutoTv.setAdapter(sizeAdapter);
        sizeAutoTv.setThreshold(1);

        /****** categories ******/

        categoryTxtIL = view.findViewById(R.id.addnewpost_categories_txtil);
        categoryAuto = view.findViewById(R.id.addnewpost_category_auto);

        int size = Model.instance.getCategories().size();
        categoryArr = new String[size];
        int i = 0;
        for(String key : Model.instance.getCategoriesAndSubCategories().keySet()){
            categoryArr[i]= key;
            i++;
        }

        Arrays.sort(categoryArr);

        categoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, categoryArr);
        categoryAuto.setAdapter(categoryAdapter);
        categoryAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenCategory = categoryAuto.getText().toString();

                subCategoryAuto.setEnabled(true);
                subcategoryTxtIL.setEnabled(true);
                subcategoryArr= Model.instance.getCategoriesAndSubCategories().get(chosenCategory);

                Collections.sort(subcategoryArr);

                subcategoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, subcategoryArr);
                subCategoryAuto.setAdapter(subcategoryAdapter);
                subCategoryAuto.setThreshold(1);
            }
        });
        categoryAuto.setThreshold(1);

        /****** subCategories ******/
        subcategoryTxtIL = view.findViewById(R.id.addnewpost_subcategories_txtil);
        subCategoryAuto = view.findViewById(R.id.addnewpost_subcategory_auto);

        subCategoryAuto.setEnabled(false);
        subcategoryTxtIL.setEnabled(false);

        /****** companies ******/
        companyTxtIl = view.findViewById(R.id.addnewpost_companies_txtil);
        companyAuto = view.findViewById(R.id.addnewpost_company_auto);
//        companyArr = getResources().getStringArray(R.array.companies);
        companiesList = generalModel.instance.map.get("Companies");

        Collections.sort(companiesList);
        companiesList.remove("Other");
        companiesList.add("Other");

        companyAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, companiesList);
        companyAuto.setAdapter(companyAdapter);
        companyAuto.setThreshold(1);

        /****** colors ******/
        colorTxtIl = view.findViewById(R.id.addnewpost_colors_txtil);
        colorAuto = view.findViewById(R.id.addnewpost_color_auto);
//        colorArr = getResources().getStringArray(R.array.colors);
        colorsList = generalModel.instance.map.get("Colors");
        Collections.sort(colorsList);

        colorAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, colorsList);
        colorAuto.setAdapter(colorAdapter);
        colorAuto.setThreshold(1);
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
}