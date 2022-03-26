package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.google.android.material.textfield.TextInputLayout;


public class EditPostFragment extends Fragment {

    EditText productNameEt, skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    //TODO: date
    SeekBar sizeAdjSk, ratingSk;
    Button editBtn, deleteBtn;
    String postId;
    Post post;

    TextInputLayout sizeTxtIL, categoryTxtIL, subcategoryTxtIL, companyTxtIl, colorTxtIl;
    AutoCompleteTextView sizeAutoTv, categoryAuto, subCategoryAuto, companyAuto, colorAuto;
    String[] sizeArr, categoryArr, subcategoryArr, companyArr, colorArr;
    ArrayAdapter<String> sizeAdapter, categoryAdapter, subcategoryAdapter, companyAdapter, colorAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_post, container, false);

        productNameEt = view.findViewById(R.id.editpost_productname_et);
        skuEt = view.findViewById(R.id.editpost_sku_et);
        descriptionEt = view.findViewById(R.id.editpost_description_et);
        linkEt = view.findViewById(R.id.editpost_link_et);
        priceEt = view.findViewById(R.id.editpost_price_et);

        sizeAdjSk = view.findViewById(R.id.editpost_sizeadjustment_seekbar);
        ratingSk = view.findViewById(R.id.editpost_rating_seekbar);

        postId = EditPostFragmentArgs.fromBundle(getArguments()).getPostId();
        post = Model.instance.getPostById(postId);

        setAllDropDownMenus(view, post);

        productNameEt.setText(post.getProductName());
        skuEt.setText(post.getSKU());
        descriptionEt.setText(post.getDescription());
        linkEt.setText(post.getLink());
        priceEt.setText(post.getPrice());

        //TODO: sizeAdjSk, ratingSk, price

        editBtn = view.findViewById(R.id.editpost_edit_btn);
        editBtn.setOnClickListener(v -> edit(view));

        deleteBtn = view.findViewById(R.id.editpost_delete_btn);
        deleteBtn.setOnClickListener(v -> delete(view));

        return view;
    }


    private void delete(View view) {

        deleteBtn.setEnabled(false);
        Model.instance.deletePostByPost(post);
        Model.instance.deletePostFromServer(post.getPostId(), isSuccess -> {
            if(isSuccess){
                Navigation.findNavController(view)
                        .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());
            }
            else{
                deleteBtn.setEnabled(true);
                Toast.makeText(MyApplication.getContext(), "No Connection, please try later",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void edit(View view) {

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

        //TODO: postId, profileId, date. pictureUrl, sizeadj, rating, price

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


        //TODO: navigate to homepage or postpage
        Navigation.findNavController(view)
                .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());
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
        categoryArr = getResources().getStringArray(R.array.categories);

        categoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, categoryArr);
        categoryAuto.setText(post.getCategoryId());
        categoryAuto.setAdapter(categoryAdapter);
        categoryAuto.setThreshold(1);

        /****** subCategories ******/
        subcategoryTxtIL = view.findViewById(R.id.editpost_subcategories_txtil);
        subCategoryAuto = view.findViewById(R.id.editpost_subcategory_auto);
        subcategoryArr =  getResources().getStringArray(R.array.subcategories_shirts);

        subcategoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, subcategoryArr);
        subCategoryAuto.setText(post.getSubCategoryId());
        subCategoryAuto.setAdapter(subcategoryAdapter);
        subCategoryAuto.setThreshold(1);

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
}