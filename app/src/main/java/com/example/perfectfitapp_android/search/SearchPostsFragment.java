package com.example.perfectfitapp_android.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.home.HomePageFragmentDirections;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Notification;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.model.generalModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class SearchPostsFragment extends Fragment {

    RecyclerView rv;
    MyAdapter adapter;
    SearchViewModel viewModel;
    EditText searchEt;
    String theSearch;
    LottieAnimationView progressBar, searchBtn;
    LottieAnimationView noPostImg;
    TextView noPostTv;
    int likesSize = 0;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_posts, container, false);

        viewModel.setData(SearchModel.instance.list);

        rv = view.findViewById(R.id.searchposts_rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        noPostImg = view.findViewById(R.id.searchposts_no_post_img);
        noPostTv = view.findViewById(R.id.searchposts_no_post_tv);
        noPostImg.setVisibility(View.GONE);
        noPostTv.setVisibility(View.GONE);

        adapter = new MyAdapter();
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener((v, position) -> {
            String postId = viewModel.getData().get(position).getPostId();
            Model.instance.getPostById(postId, post -> {
                if (post != null) {
                    Model.instance.setPost(post);
                    Navigation.findNavController(v).navigate(SearchPostsFragmentDirections.actionGlobalPostPageFragment(postId, "home"));
                } else {
                    showOkDialog(getResources().getString(R.string.outError));
                }
            });
        });


        searchEt = view.findViewById(R.id.searchposts_text_et);
        if (theSearch != null) {
            if (!theSearch.isEmpty()) {
                List<Post> posts = searchMap();
                viewModel.setData(posts);
                adapter.notifyDataSetChanged();
            }
        }

        searchBtn = view.findViewById(R.id.searchposts_search_btn);
        searchBtn.setOnClickListener(v -> {
            search();
        });

        if (viewModel.getData().size() == 0) {
            noPostImg.setVisibility(View.VISIBLE);
            noPostTv.setVisibility(View.VISIBLE);
            searchEt.setVisibility(View.GONE);
            searchBtn.setVisibility(View.GONE);
        }

        progressBar = view.findViewById(R.id.searchposts_progress_bar);
        progressBar.setVisibility(View.GONE);

        return view;
    }

    public void search() {
        progressBar.setVisibility(View.VISIBLE);
        theSearch = searchEt.getText().toString();
        if (theSearch.isEmpty()) {
            viewModel.setData(SearchModel.instance.list);
        } else {
            List<Post> posts = searchMap();
            viewModel.setData(posts);
        }
        progressBar.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    public List<Post> searchMap() {
        List<Post> posts = new ArrayList<>();
        for (Post p : SearchModel.instance.list) {
            if (p.getProfileId().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getProductName().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getSKU().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getSize().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getCompany().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getPrice().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getColor().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getCategoryId().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getSubCategoryId().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getDescription().contains(theSearch)) {
                posts.add(p);
                continue;
            }
            if (p.getLink().contains(theSearch)) {
                posts.add(p);
                continue;
            }
        }
        return posts;
    }

    private void refresh() {
        Model.instance.getSearchPosts(SearchModel.instance.mapToServer, posts -> {
            if (posts != null) {
                viewModel.setData(posts);
                adapter.notifyDataSetChanged();
            } else {
                showOkDialog(getResources().getString(R.string.outError));
            }
        });
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTv, subCategoryTv, userNameTv, likesNumberTV, timeAgoTv;
        ImageButton addToWishList, addToLikes, commentsBtn;
        ShapeableImageView postPic, userPic;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.listrow_username_tv);
            categoryTv = itemView.findViewById(R.id.listrow_category_tv);
            subCategoryTv = itemView.findViewById(R.id.listrow_subcategory_tv);
            addToWishList = itemView.findViewById(R.id.add_to_wish_list_btn);
            addToLikes = itemView.findViewById(R.id.listrow_post_likes_btn);
            commentsBtn = itemView.findViewById(R.id.listrow_comments_btn);
            postPic = itemView.findViewById(R.id.listrow_post_img);
            userPic = itemView.findViewById(R.id.listrow_avatar_imv);
            likesNumberTV = itemView.findViewById(R.id.listrow_post_likes_number);
            timeAgoTv = itemView.findViewById(R.id.listrow_time_ago_tv);

            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }
    }

    interface OnItemClickListener {
        void onItemClick(View v, int position);
    }


    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.post_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Post post = viewModel.getData().get(position);
            holder.userNameTv.setText(post.getProfileId());
            holder.categoryTv.setText(post.getCategoryId());
            holder.subCategoryTv.setText(post.getSubCategoryId());
            holder.addToWishList.setOnClickListener(v -> addToWishList(holder, post));
            holder.addToLikes.setOnClickListener(v -> addToLikes(holder, post));
            Model.instance.timeSince(post.getDate(), timeAgo -> holder.timeAgoTv.setText(timeAgo));

            Model.instance.getProfileByUserName(post.getProfileId(), profile -> {
                String userImg = profile.getUserImageUrl();
                if (userImg != null && !userImg.equals("")) {
                    Model.instance.getImages(userImg, bitmap -> {
                        holder.userPic.setImageBitmap(bitmap);
                    });
                } else {
                    Picasso.get()
                            .load(R.drawable.user_default).resize(250, 180)
                            .centerCrop()
                            .into(holder.userPic);
                }
            });

            Model.instance.getProfilesByUserNames(post.getLikes(), profilesList -> {
                likesSize = 0;
                for (int i = 0; i < profilesList.size(); i++) {
                    if (profilesList.get(i).getIsDeleted().equals("false")) {
                        likesSize++;
                    }
                }
                holder.likesNumberTV.setText(String.valueOf(likesSize) + " likes");
            });

            if (post.getPicturesUrl() != null && post.getPicturesUrl().size() != 0) {
                Model.instance.getImages(post.getPicturesUrl().get(0), bitmap -> {
                    holder.postPic.setImageBitmap(bitmap);
                });
            } else {
                Picasso.get()
                        .load(R.drawable.coverphotoprofile).resize(250, 180)
                        .centerCrop()
                        .into(holder.postPic);
            }

            if (checkIfInsideWishList(post)) {
                holder.addToWishList.setImageResource(R.drawable.ic_addtowishlistfill);
            } else {
                holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
            }

            if (checkIfInsideLikes(post)) {
                holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
            } else {
                holder.addToLikes.setImageResource(R.drawable.ic_heart1);
            }

            // Move to different pages from post.

            holder.userNameTv.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(SearchPostsFragmentDirections.actionGlobalProfileFragment(post.getProfileId()));
            });

            holder.userPic.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(SearchPostsFragmentDirections.actionGlobalProfileFragment(post.getProfileId()));
            });

            if (post.getLikes().size() != 0) {
                holder.likesNumberTV.setOnClickListener(v -> {
                    Navigation.findNavController(v).navigate(SearchPostsFragmentDirections.actionSearchPostsFragmentToLikesFragment(post.getPostId()));
                });
            } else {
                holder.likesNumberTV.setOnClickListener(v -> {
                }); //So when user click on likes and when its empty he wont get into post page but won't get anything.
            }

            holder.commentsBtn.setOnClickListener(v -> {
                Navigation.findNavController(v).navigate(SearchPostsFragmentDirections.actionSearchPostsFragmentToCommentFragment(post.getPostId()));
            });
        }

        private void addToLikes(MyViewHolder holder, Post post) {
            String userName = Model.instance.getProfile().getUserName();
            if (checkIfInsideLikes(post)) {
                post.getLikes().remove(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if (isSuccess) {
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_heart1);
                    } else {
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });

            } else {
                post.getLikes().add(userName);
                Model.instance.editPost(post, isSuccess -> {
                    if (isSuccess) {
                        holder.likesNumberTV.setText(String.valueOf(post.getLikes().size()) + " likes");
                        holder.addToLikes.setImageResource(R.drawable.ic_full_heart);
                    } else {
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });

                if (!Model.instance.getProfile().getUserName().equals(post.getProfileId())) {
                    Notification notification = new Notification("0", Model.instance.getProfile().getUserName(),
                            post.getProfileId(), "Liked your post.", "10/5/22", post.getPostId(), "false");
                    Model.instance.addNewNotification(notification, notification1 -> {
                    });
                }
            }
        }

        private void addToWishList(MyViewHolder holder, Post post) {

            if (checkIfInsideWishList(post)) {
                Model.instance.getProfile().getWishlist().remove(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if (isSuccess) {
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlist);
                    } else {
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            } else {
                Model.instance.getProfile().getWishlist().add(post.getPostId());
                Model.instance.editProfile(null, Model.instance.getProfile(), isSuccess -> {
                    if (isSuccess) {
                        holder.addToWishList.setImageResource(R.drawable.ic_addtowishlistfill);
                        System.out.println("the posts added to the list");
                        System.out.println(Model.instance.getProfile().getWishlist());
                    } else {
                        showOkDialog(getResources().getString(R.string.outError));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (viewModel.getData() == null) {
                return 0;
            }
            return viewModel.getData().size();
        }

        public boolean checkIfInsideWishList(Post post) {
            return Model.instance.getProfile().getWishlist().contains(post.getPostId());
        }

        public boolean checkIfInsideLikes(Post post) {
            return post.getLikes().contains(Model.instance.getProfile().getUserName());
        }
    }


    public void setMapToServer() {
        Set<String> names = generalModel.instance.map.keySet();
        String[] arr = new String[names.size()];
        arr = names.toArray(arr);
        for (String a : arr) {
            SearchModel.instance.mapToServer.remove(a);
            SearchModel.instance.mapToServer.put(a, new ArrayList<>());
        }
    }

    private void showOkDialog(String text) {
        Dialog dialog = new Dialog(getActivity(), R.style.DialogStyle);
        dialog.setContentView(R.layout.custom_ok_dialog);

        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_window);

        TextView tx = dialog.findViewById(R.id.txtDesc);
        tx.setText(text);

        Button btnOk = dialog.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(v -> dialog.dismiss());

        ImageView btnClose = dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}