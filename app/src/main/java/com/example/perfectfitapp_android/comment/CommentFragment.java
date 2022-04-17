package com.example.perfectfitapp_android.comment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.perfectfitapp_android.HomePageFragment;
import com.example.perfectfitapp_android.HomePageViewModel;
import com.example.perfectfitapp_android.MyApplication;
import com.example.perfectfitapp_android.R;
import com.example.perfectfitapp_android.model.Comment;
import com.example.perfectfitapp_android.model.Model;
import com.example.perfectfitapp_android.model.Post;
import com.example.perfectfitapp_android.post.AddNewPostFragmentDirections;
import com.example.perfectfitapp_android.post.PostPageFragmentArgs;

public class CommentFragment extends Fragment {

    CommentViewModel viewModel;
    MyAdapter adapter;
    String postId;
    Button addBtn;
    EditText commentET;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        viewModel = new ViewModelProvider(this).get(CommentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        postId = CommentFragmentArgs.fromBundle(getArguments()).getPostId();

        RecyclerView postsList = view.findViewById(R.id.comment_rv);
        postsList.setHasFixedSize(true);
        postsList.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        postsList.setAdapter(adapter);

        commentET = view.findViewById(R.id.comment_new_comment_text);

        addBtn = view.findViewById(R.id.comment_add_btn);
        addBtn.setOnClickListener(v -> addPost());

        refresh();

        return view;
    }

    private void addPost() {
        addBtn.setEnabled(false);
        String text, date;

        text = commentET.getText().toString();
        date = "26/3/2022";

        Comment comment = new Comment("0", Model.instance.getProfile().getUserName(), postId, date, text);

        Model.instance.addNewComment(comment, newComment -> {
           if(newComment != null){
               refresh();
               commentET.setText("");
               addBtn.setEnabled(true);
           }
           else{
               addBtn.setEnabled(true);
               Toast.makeText(MyApplication.getContext(), "Comment didn't saved",
                       Toast.LENGTH_LONG).show();
           }

        });
    }

    private void refresh() {
        Model.instance.getCommentsByPostId(postId, commentList -> {
            viewModel.setData(commentList);
            adapter.notifyDataSetChanged();
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTv, textTV, date;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userNameTv = itemView.findViewById(R.id.comment_lr_user_name);
            textTV = itemView.findViewById(R.id.comment_lr_user_text);
            date = itemView.findViewById(R.id.comment_lr_time);

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
            View view = getLayoutInflater().inflate(R.layout.comment_list_row, parent, false);
            MyViewHolder holder = new MyViewHolder(view, listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Comment comment = viewModel.getData().get(position);
            holder.userNameTv.setText(comment.getProfileId());
            holder.textTV.setText(comment.getText());
            holder.date.setText(comment.getDate());
        }
        @Override
        public int getItemCount() {
            if(viewModel.getData() == null){
                return 0;
            }
            return viewModel.getData().size();
        }
    }
}