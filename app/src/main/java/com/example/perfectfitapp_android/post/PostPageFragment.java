package com.example.perfectfitapp_android.post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.perfectfitapp_android.HomePageFragmentDirections;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;

public class PostPageFragment extends Fragment {

    TextView productNameEt;
    EditText skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    TextView categoryTv, subCategoryTv;
    //TODO: date
    SeekBar sizeAdj, rating;
    ImageView image;
    String postId;
    ImageButton editBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        postId = PostPageFragmentArgs.fromBundle(getArguments()).getPostId();
        Post post = Model.instance.getPostById(postId);

        productNameEt = view.findViewById(R.id.postpage_productname_et);
        skuEt = view.findViewById(R.id.postpage_sku_et);
        sizeEt = view.findViewById(R.id.postpage_size_et);
        companyEt = view.findViewById(R.id.postpage_company_et);
        colorEt = view.findViewById(R.id.postpage_color_et);
        categoryTv = view.findViewById(R.id.postpage_category_tv);
        subCategoryTv = view.findViewById(R.id.postpage_subcategort_tv);
        descriptionEt = view.findViewById(R.id.postpage_description_et);
        priceEt = view.findViewById(R.id.postpage_price_et);
        linkEt = view.findViewById(R.id.postpage_link_et);
        sizeAdj = view.findViewById(R.id.postpage_sizeadjestment_seekBar);
        rating = view.findViewById(R.id.postpage_rating_seekBar);
        image = view.findViewById(R.id.postpage_image_imv);

        editBtn = view.findViewById(R.id.postpage_edit_btn);
        editBtn.setOnClickListener(v-> editPost(view));

        /***************************** set *****************************/

        productNameEt.setText(post.getProductName());
        skuEt.setText(post.getSKU());
        sizeEt.setText(post.getSize());
        companyEt.setText(post.getCompany());
        colorEt.setText(post.getColor());
        categoryTv.setText(post.getCategoryId());
        subCategoryTv.setText(post.getSubCategoryId());
        descriptionEt.setText(post.getDescription());
        priceEt.setText(post.getPrice());
        linkEt.setText(post.getLink());

        productNameEt.setEnabled(false);
        skuEt.setEnabled(false);
        sizeEt.setEnabled(false);
        companyEt.setEnabled(false);
        colorEt.setEnabled(false);
        categoryTv.setEnabled(false);
        subCategoryTv.setEnabled(false);
        descriptionEt.setEnabled(false);
        priceEt.setEnabled(false);
        linkEt.setEnabled(false);

        //TODO: sizeAdj, rating, image

        return view;
    }

    private void editPost(View view) {

        Navigation.findNavController(view).navigate(PostPageFragmentDirections.actionPostPageFragmentToEditPostFragment(postId));


    }
}