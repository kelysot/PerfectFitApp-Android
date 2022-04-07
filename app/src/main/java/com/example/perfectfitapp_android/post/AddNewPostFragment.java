package com.example.perfectfitapp_android.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.perfectfitapp_android.sub_category.SubCategoryViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class AddNewPostFragment extends Fragment {

    EditText productNameEt, skuEt, descriptionEt, linkEt, priceEt;
    SeekBar sizeAdjSk, ratingSk;
    Button postBtn;
    //TODO: date

    TextInputLayout sizeTxtIL, categoryTxtIL, subcategoryTxtIL, companyTxtIl, colorTxtIl;
    AutoCompleteTextView sizeAutoTv, categoryAuto, subCategoryAuto, companyAuto, colorAuto;
    String[] sizeArr, companyArr, categoryArr, colorArr;
    ArrayList<String> subcategoryArr;
    ArrayAdapter<String> sizeAdapter, categoryAdapter, subcategoryAdapter, companyAdapter, colorAdapter;
    String chosenCategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_post, container, false);

        productNameEt = view.findViewById(R.id.addnewpost_productname_et);
        skuEt = view.findViewById(R.id.addnewpost_sku_et);
        descriptionEt = view.findViewById(R.id.addnewpost_description_et);
        linkEt = view.findViewById(R.id.addnewpost_link_et);
        priceEt = view.findViewById(R.id.addnewpost_price_et);

        sizeAdjSk = view.findViewById(R.id.addnewpost_sizeadjustment_seekbar);
        ratingSk = view.findViewById(R.id.addnewpost_rating_seekbar);

        postBtn = view.findViewById(R.id.addnewpost_post_btn);
        postBtn.setOnClickListener(v -> post(view));

        setAllDropDownMenus(view);

        return view;
    }

    //TODO: Make all fields require.
    //TODO: Send sizeAdj and rating to server.
    private void post(View view) {

        postBtn.setEnabled(false);

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

        String date = "26/3/2022";
        sizeAdj = "";
        rating = "";

        StringBuilder count = new StringBuilder();
        count.append(Model.instance.getCount());
        Model.instance.setCount(Model.instance.getCount()+1);

        Post newPost = new Post(count.toString(), Model.instance.getProfile().getUserName(), productName, sku, size, company, color, category,
                subCategory, description, date,link, sizeAdj, rating, price);

//        newPost.setPicturesUrl(Model.instance.getNewPost().getPicturesUrl());

        Model.instance.addNewPost(newPost, post -> {
            if(post != null){
                Model.instance.addPost(post);
                Model.instance.setNewPost(new Post());
                Navigation.findNavController(view)
                        .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());
            }
            else {
                postBtn.setEnabled(true);
                Toast.makeText(MyApplication.getContext(), "Post didn't saved",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setAllDropDownMenus(View view){

        /****** size ******/
        sizeTxtIL = view.findViewById(R.id.addnewpost_size_txtil);
        sizeAutoTv = view.findViewById(R.id.addnewpost_size_auto);
        sizeArr = getResources().getStringArray(R.array.sizes);

        sizeAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, sizeArr);
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

        categoryAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, categoryArr);
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
        subcategoryTxtIL = view.findViewById(R.id.addnewpost_subcategories_txtil);
        subCategoryAuto = view.findViewById(R.id.addnewpost_subcategory_auto);

        subCategoryAuto.setEnabled(false);
        subcategoryTxtIL.setEnabled(false);

        /****** companies ******/
        companyTxtIl = view.findViewById(R.id.addnewpost_companies_txtil);
        companyAuto = view.findViewById(R.id.addnewpost_company_auto);
        companyArr = getResources().getStringArray(R.array.companies);

        companyAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, companyArr);
        companyAuto.setAdapter(companyAdapter);
        companyAuto.setThreshold(1);

        /****** colors ******/
        colorTxtIl = view.findViewById(R.id.addnewpost_colors_txtil);
        colorAuto = view.findViewById(R.id.addnewpost_color_auto);
        colorArr = getResources().getStringArray(R.array.colors);

        colorAdapter = new ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, colorArr);
        colorAuto.setAdapter(colorAdapter);
        colorAuto.setThreshold(1);
    }
}