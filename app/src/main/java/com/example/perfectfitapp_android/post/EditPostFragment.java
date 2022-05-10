package com.example.perfectfitapp_android.post;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.google.android.material.textfield.TextInputLayout;

import java.io.InputStream;
import java.util.ArrayList;


public class EditPostFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;
    Bitmap mBitmap;

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    ImageView postImg;
    ImageButton cameraBtn, galleryBtn;
    //TODO: date
    SeekBar sizeAdjSk, ratingSk;
    Button editBtn, deleteBtn;
    String postId;
    Post post;

    TextInputLayout sizeTxtIL, categoryTxtIL, subcategoryTxtIL, companyTxtIl, colorTxtIl;
    AutoCompleteTextView sizeAutoTv, categoryAuto, subCategoryAuto, companyAuto, colorAuto;
    String[] sizeArr, companyArr, categoryArr, colorArr;
    ArrayList<String> subcategoryArr;
    ArrayAdapter<String> sizeAdapter, categoryAdapter, subcategoryAdapter, companyAdapter, colorAdapter;
    String chosenCategory;
    String postSource;
    ArrayList<String> pics = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        postSource = PostPageFragmentArgs.fromBundle(getArguments()).getSource();

        productNameEt = view.findViewById(R.id.editpost_productname_et);
        skuEt = view.findViewById(R.id.editpost_sku_et);
        descriptionEt = view.findViewById(R.id.editpost_description_et);
        linkEt = view.findViewById(R.id.editpost_link_et);
        priceEt = view.findViewById(R.id.editpost_price_et);

        sizeAdjSk = view.findViewById(R.id.editpost_sizeadjustment_seekbar);
        ratingSk = view.findViewById(R.id.editpost_rating_seekbar);

        postImg = view.findViewById(R.id.editpost_image_imv);
        cameraBtn = view.findViewById(R.id.editpost_camera_imv);
        galleryBtn = view.findViewById(R.id.editpost_gallery_imv);

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
//        post = Model.instance.getPostById(postId);

        post = Model.instance.getPost();

        setAllDropDownMenus(view, post);

        productNameEt.setText(post.getProductName());
        skuEt.setText(post.getSKU());
        descriptionEt.setText(post.getDescription());
        linkEt.setText(post.getLink());
        priceEt.setText(post.getPrice());

        if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0 ) {
            Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                postImg.setImageBitmap(bitmap);
            });
        }

        //TODO: sizeAdjSk, ratingSk, price

        editBtn = view.findViewById(R.id.editpost_edit_btn);
        editBtn.setOnClickListener(v -> edit());

        deleteBtn = view.findViewById(R.id.editpost_delete_btn);
        deleteBtn.setOnClickListener(v -> delete());

        cameraBtn.setOnClickListener(v -> openCam());
        galleryBtn.setOnClickListener(v -> openGallery());

        return view;
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
                postImg.setImageBitmap(mBitmap);
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
                    postImg.setImageBitmap(mBitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void delete() {

        deleteBtn.setEnabled(false);
        Model.instance.deletePostFromServer(post.getPostId(), isSuccess -> {
            if(isSuccess){
                toPage();
            }
            else{
                deleteBtn.setEnabled(true);
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void edit() {

        String productName, sku, size, company, color, category, subCategory, description;
        String link, price;
        //TODO: date
        String sizeAdj, rating;

        productName = productNameEt.getText().toString();
        sku = skuEt.getText().toString();
        description = descriptionEt.getText().toString();
        link = linkEt.getText().toString();
        size = sizeAutoTv.getText().toString();
        company = companyAuto.getText().toString();
        color = colorAuto.getText().toString();
        category = categoryAuto.getText().toString();
        subCategory = subCategoryAuto.getText().toString();
        price = priceEt.getText().toString();

        //TODO: postId, profileId, date , sizeadj, rating, price

        String date = "8/3/2022";
        String pictureUrl = "";
        sizeAdj = "";
        rating = "";

        post.setProductName(productName);
        post.setSKU(sku);
        post.setSize(size);
        post.setCompany(company);
        post.setColor(color);
        post.setCategoryId(category);
        post.setSubCategoryId(subCategory);
        post.setDescription(description);
        post.setLink(link);
        post.setPrice(price);


        if (mBitmap != null) {
            Model.instance.uploadImage(mBitmap, getActivity(), url -> {
                pics.add(pics.size(), url);
                post.setPicturesUrl(pics);
                editPage();
            });
        }
        else {
            pics = null;
            editPage();
        }

    }

    public void editPage(){
        Model.instance.editPost(post, isSuccess -> {
            if(isSuccess){
//                Post p = Model.instance.getPostById(post.getPostId());
//                int index = Model.instance.getAllPosts().indexOf(p);
//                Model.instance.getAllPosts().set(index, post);

                //TODO: navigate to homepage or postpage, need o do the refresh
                toPage();
            }
            else{
                //TODO
            }
        });
    }

    private void setAllDropDownMenus(View view, Post post) {

        /****** size ******/
        sizeTxtIL = view.findViewById(R.id.editpost_size_txtil);
        sizeAutoTv = view.findViewById(R.id.editpost_size_auto);
        sizeArr = getResources().getStringArray(R.array.sizes);

        sizeAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, sizeArr);
        sizeAutoTv.setText(post.getSize());
        sizeAutoTv.setAdapter(sizeAdapter);
        sizeAutoTv.setThreshold(1);

        /****** categories ******/

        categoryTxtIL = view.findViewById(R.id.editpost_categories_txtil);
        categoryAuto = view.findViewById(R.id.editpost_category_auto);

        int size = Model.instance.getCategories().size();
        categoryArr = new String[size];
        int i = 0;
        for(String key : Model.instance.getCategoriesAndSubCategories().keySet()){
            categoryArr[i]= key;
            i++;
        }
//        categoryArr = getResources().getStringArray(R.array.categories);

        categoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, categoryArr);
        categoryAuto.setText(post.getCategoryId());
        categoryAuto.setAdapter(categoryAdapter);
        categoryAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenCategory = categoryAuto.getText().toString();

                subCategoryAuto.setEnabled(true);
                subcategoryTxtIL.setEnabled(true);
                subcategoryArr= Model.instance.getCategoriesAndSubCategories().get(chosenCategory);

                subcategoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, subcategoryArr);
                subCategoryAuto.setAdapter(subcategoryAdapter);
                subCategoryAuto.setThreshold(1);
            }
        });
        categoryAuto.setThreshold(1);

        /****** subCategories ******/
        subcategoryTxtIL = view.findViewById(R.id.editpost_subcategories_txtil);
        subCategoryAuto = view.findViewById(R.id.editpost_subcategory_auto);
        subCategoryAuto.setText(post.getSubCategoryId());

        subCategoryAuto.setEnabled(false);
        subcategoryTxtIL.setEnabled(false);
//        subcategoryArr =  getResources().getStringArray(R.array.subcategories_shirts);
//
//        subcategoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, subcategoryArr);
//        subCategoryAuto.setText(post.getSubCategoryId());
//        subCategoryAuto.setAdapter(subcategoryAdapter);
//        subCategoryAuto.setThreshold(1);

        /****** companies ******/
        companyTxtIl = view.findViewById(R.id.editpost_companies_txtil);
        companyAuto = view.findViewById(R.id.editpost_company_auto);
        companyArr = getResources().getStringArray(R.array.companies);

        companyAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, companyArr);
        companyAuto.setText(post.getCompany());
        companyAuto.setAdapter(companyAdapter);
        companyAuto.setThreshold(1);

        /****** colors ******/
        colorTxtIl = view.findViewById(R.id.editpost_colors_txtil);
        colorAuto = view.findViewById(R.id.editpost_color_auto);
        colorArr = getResources().getStringArray(R.array.colors);

        colorAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, colorArr);
        colorAuto.setText(post.getColor());
        colorAuto.setAdapter(colorAdapter);
        colorAuto.setThreshold(1);
    }
    //TODO: add subCategoryDetailsPostsFragment, check EditPostFragmentDirections / AddNewPostFragmentDirections
    public void toPage(){
        if(postSource.equals("home")){
            Navigation.findNavController(editBtn)
                    .navigate(EditPostFragmentDirections.actionGlobalHomePageFragment());

        }
        else if(postSource.equals("profile")){
            Navigation.findNavController(editBtn)
                .navigate(EditPostFragmentDirections.actionGlobalProfileFragment(Model.instance.getProfile().getUserName()));

        }
        else if(postSource.equals("wishlist")){
            Navigation.findNavController(editBtn)
                    .navigate(EditPostFragmentDirections.actionGlobalWishListFragment());

        }
//        else if(postSource.equals("subCategoryDetailsPostsFragment")){
//            Navigation.findNavController(editBtn)
//                    .navigate(AddNewPostFragmentDirections.actionGlobalWishListFragment());
//
//        }


    }

}