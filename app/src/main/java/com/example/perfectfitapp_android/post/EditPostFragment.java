package com.example.perfectfitapp_android.post;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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


public class EditPostFragment extends Fragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PIC = 2;
    Bitmap mBitmap;

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    ImageView postImg, addPhoto;
    RatingBar sizeAdj, rating;
    Button editBtn, deleteBtn;
    String postId;
    Post post;

    TextInputLayout sizeTxtIL, categoryTxtIL, subcategoryTxtIL, companyTxtIl, colorTxtIl;
    AutoCompleteTextView sizeAutoTv, categoryAuto, subCategoryAuto, companyAuto, colorAuto;
    String[] categoryArr;
    ArrayList<String> subcategoryArr;
    ArrayAdapter<String> sizeAdapter, categoryAdapter, subcategoryAdapter, companyAdapter, colorAdapter;
    String chosenCategory;
    String postSource;
    ArrayList<String> pics = new ArrayList<>();
    List<String> sizesList, companiesList, colorsList;


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

        sizeAdj = view.findViewById(R.id.editpost_sizeadjustment_ratingbar);
        rating = view.findViewById(R.id.editpost_rating_ratingbar);

        postImg = view.findViewById(R.id.editpost_image_imv);
        addPhoto = view.findViewById(R.id.editpost_add_photo_imv);

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
//        post = Model.instance.getPostById(postId);

        post = Model.instance.getPost();

        setAllDropDownMenus(view, post);

        productNameEt.setText(post.getProductName());
        skuEt.setText(post.getSKU());
        descriptionEt.setText(post.getDescription());
        linkEt.setText(post.getLink());
        priceEt.setText(post.getPrice());
        sizeAdj.setRating(Float.parseFloat(post.getSizeAdjustment()));
        rating.setRating(Float.parseFloat(post.getRating()));

        if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0 ) {
            Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                postImg.setImageBitmap(bitmap);
            });
        }

        editBtn = view.findViewById(R.id.editpost_edit_btn);
        editBtn.setOnClickListener(v -> edit());

        deleteBtn = view.findViewById(R.id.editpost_delete_btn);
        deleteBtn.setOnClickListener(v -> showDialog());

        addPhoto.setOnClickListener(v -> showImagePickDialog());

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
                //TODO: dialog
                deleteBtn.setEnabled(true);
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialog(){
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText("Are you sure you want to delete this post?");

        Button btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> dialog.dismiss());

        Button btnYes = dialog.findViewById(R.id.btn_yes);
        btnYes.setOnClickListener(v -> {
            delete();
            dialog.dismiss();
        });

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void edit() {

        editBtn.setEnabled(false);
        String productName, sku, size, company, color, category, subCategory, description;
        String link, price;
        String sizeAdjS, ratingS;

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
        ratingS = String.valueOf(rating.getRating());
        sizeAdjS = String.valueOf(sizeAdj.getRating());

        boolean flag = true;

        if(category.isEmpty()){
            categoryAuto.setError("Please enter category.");
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

        if(flag) {

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
            post.setRating(ratingS);
            post.setSizeAdjustment(sizeAdjS);

            if (mBitmap != null) {
                Model.instance.uploadImage(mBitmap, getActivity(), url -> {
                    pics.add(pics.size(), url);
                    post.setPicturesUrl(pics);
                    editPage();
                });
            } else {
                pics = null;
                editPage();
            }
        }
        else {
            editBtn.setEnabled(true);
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
                //TODO: dialog
                editBtn.setEnabled(true);

            }
        });
    }

    private void setAllDropDownMenus(View view, Post post) {

        /****** size ******/
        sizeTxtIL = view.findViewById(R.id.editpost_size_txtil);
        sizeAutoTv = view.findViewById(R.id.editpost_size_auto);
//        sizeArr = getResources().getStringArray(R.array.sizes);
        sizesList = generalModel.instance.map.get("Sizes");

        sizeAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, sizesList);
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

        Arrays.sort(categoryArr);

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
                Collections.sort(subcategoryArr);

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
        companiesList = generalModel.instance.map.get("Companies");

        Collections.sort(companiesList);
        companiesList.remove("Other");
        companiesList.add("Other");

        companyAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, companiesList);
        companyAuto.setText(post.getCompany());
        companyAuto.setAdapter(companyAdapter);
        companyAuto.setThreshold(1);

        /****** colors ******/
        colorTxtIl = view.findViewById(R.id.editpost_colors_txtil);
        colorAuto = view.findViewById(R.id.editpost_color_auto);
        colorsList = generalModel.instance.map.get("Colors");
        Collections.sort(colorsList);

        colorAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, colorsList);
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