package com.example.perfectfitapp_android.post;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.squareup.picasso.Picasso;

public class PostPageFragment extends Fragment {

    TextView productNameEt;
    EditText skuEt, sizeEt, companyEt, colorEt, categoryEt, subCategoryEt, descriptionEt;
    EditText linkEt, priceEt;
    TextView categoryTv, subCategoryTv;
    ImageView image;
    String postId, postSource;
    ImageButton editBtn;
    RatingBar sizeAdj, rating;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post_page, container, false);

        postSource = PostPageFragmentArgs.fromBundle(getArguments()).getSource();

        postId = PostPageFragmentArgs.fromBundle(getArguments()).getPostId();
        //TODO: delete all the Model.instance.post..

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
        sizeAdj = view.findViewById(R.id.postpage_sizeadjestment_ratingBar);
        rating = view.findViewById(R.id.postpage_rating_ratingBar);
        image = view.findViewById(R.id.postpage_image_imv);

        editBtn = view.findViewById(R.id.postpage_edit_btn);
        editBtn.setOnClickListener(v-> editPost(view));

        /***************************** set *****************************/

//        Post post = Model.instance.getPost();
        Model.instance.getPostById(postId, post -> {

            if(post != null){

                if(!Model.instance.getProfile().getUserName().equals(post.getProfileId())){
                    editBtn.setVisibility(View.GONE);
                }

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
                Log.d("TAG22", post.getSizeAdjustment());
                Log.d("TAG22", post.getPostId());
                sizeAdj.setRating(Float.parseFloat(post.getSizeAdjustment()));
                rating.setRating(Float.parseFloat(post.getRating()));

                if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0 ) {
                    Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                        image.setImageBitmap(bitmap);
                    });
                }
                else {
                    Picasso.get()
                            .load(R.drawable.pic1_shirts).resize(250, 180)
                            .centerCrop()
                            .into(image);
                }

                linkEt.setOnClickListener(v -> {
                    if(post.getLink().contains("http")){ // missing 'http://' will cause crashed
                        Uri uri = Uri.parse(post.getLink());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
            }
            else{
                //TODO: dialog
            }
        });

        productNameEt.setEnabled(false);
        skuEt.setEnabled(false);
        sizeEt.setEnabled(false);
        companyEt.setEnabled(false);
        colorEt.setEnabled(false);
        categoryTv.setEnabled(false);
        subCategoryTv.setEnabled(false);
        descriptionEt.setEnabled(false);
        priceEt.setEnabled(false);

        return view;
    }

    private void editPost(View view) {
        Navigation.findNavController(view).navigate(PostPageFragmentDirections.actionPostPageFragmentToEditPostFragment(postId, postSource));
    }
}