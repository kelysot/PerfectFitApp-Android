package com.example.perfectfitapp_android.post.add_post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.generalModel;
import com.example.perfectfitapp_android.sub_category.SubCategoryViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class AddNewPostFragment extends Fragment {

    EditText productNameEt, skuEt, descriptionEt, linkEt, priceEt;
    Button postBtn;
    String img = "";
    RatingBar ratingBar, sizeAdjSk;

    TextInputLayout sizeTxtIL, categoryTxtIL, subcategoryTxtIL, companyTxtIl, colorTxtIl;
    AutoCompleteTextView sizeAutoTv, categoryAuto, subCategoryAuto, companyAuto, colorAuto;
    String[] categoryArr;
    ArrayList<String> subcategoryArr;
    ArrayList<String> pics = new ArrayList<>();
    ArrayAdapter<String> sizeAdapter, categoryAdapter, subcategoryAdapter, companyAdapter, colorAdapter;
    String chosenCategory;
    List<String> sizesList, companiesList, colorsList;

    int check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_new_post, container, false);

        img = AddNewPostFragmentArgs.fromBundle(getArguments()).getEncodedImage();

        if(!img.equals("")) pics.add(pics.size(), img);
        else pics = null;

        productNameEt = view.findViewById(R.id.addnewpost_productname_et);
        skuEt = view.findViewById(R.id.addnewpost_sku_et);
        descriptionEt = view.findViewById(R.id.addnewpost_description_et);
        linkEt = view.findViewById(R.id.addnewpost_link_et);
        priceEt = view.findViewById(R.id.addnewpost_price_et);

        sizeAdjSk = view.findViewById(R.id.addnewpost_sizeadjustment_ratingbar);
        ratingBar = view.findViewById(R.id.addnewpost_rating_ratingbar);

        postBtn = view.findViewById(R.id.addnewpost_post_btn);
        postBtn.setOnClickListener(v -> post(view));

        setAllDropDownMenus(view);

        return view;
    }

    //TODO: Make all fields require.
    private void post(View view) {

        postBtn.setEnabled(false);

        String productName, sku, size, company, color, category, subCategory, description;
        String link, price;
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
            subCategoryAuto.setError("Please enter sub-category");
            flag = false;
        }
        if(company.isEmpty()){
            companyAuto.setError("Please enter company");
            flag = false;
        }
        if(color.isEmpty()){
            colorAuto.setError("Please enter color");
            flag = false;
        }
        if(size.isEmpty()){
            sizeAutoTv.setError("Please enter size");
            flag = false;
        }
        if(price.isEmpty()){
            priceEt.setError("Please enter price");
            flag = false;
        }
        if(productName.isEmpty()){
            productNameEt.setError("Please enter product name");
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

        if(flag){

            String date = "";

            StringBuilder count = new StringBuilder();
            count.append(Model.instance.getCount());
            Model.instance.setCount(Model.instance.getCount() + 1);

            Post newPost = new Post(count.toString(), Model.instance.getProfile().getUserName(), productName, sku, size, company, color, category,
                    subCategory, description, date, link, sizeAdj, rating, pics, price, null, null, "false");


//        newPost.setPicturesUrl(Model.instance.getNewPost().getPicturesUrl());

            Model.instance.addNewPost(newPost, post -> {
                if (post != null) {
                    Model.instance.addPost(post);
                    Model.instance.setNewPost(new Post());
                    Model.instance.getProfile().getMyPostsListId().add(post.getPostId());
                    Navigation.findNavController(view)
                            .navigate(AddNewPostFragmentDirections.actionGlobalHomePageFragment());
                } else {
                    //TODO: dialog and progressbar
                    postBtn.setEnabled(true);
                    Toast.makeText(MyApplication.getContext(), "Post didn't saved",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
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
}