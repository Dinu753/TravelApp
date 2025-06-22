package com.dinushi.travelexplorer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context context;
    ArrayList<PostModel> posts;

    public PostAdapter(Context context, ArrayList<PostModel> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        PostModel post = posts.get(position);

        holder.titleText.setText(post.getTitle());
        holder.descText.setText(post.getDescription());
        holder.locationText.setText("📍 " + post.getLocation());

        try {
            Uri imageUri = Uri.parse(post.getImageUri());
            holder.postImage.setImageURI(imageUri);
        } catch (Exception e) {
            holder.postImage.setImageResource(R.drawable.cameraicon); // fallback image
        }

        holder.likeIcon.setOnClickListener(v -> {
            Toast.makeText(context, "Liked: " + post.getTitle(), Toast.LENGTH_SHORT).show();
        });

        holder.commentIcon.setOnClickListener(v -> {
            Toast.makeText(context, "Comment on: " + post.getTitle(), Toast.LENGTH_SHORT).show();
        });

        // Add this to open MapActivity on location click
        holder.locationText.setOnClickListener(v -> {
            String location = post.getLocation();
            if (location != null && !location.isEmpty()) {
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("location", location);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "No location found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage, postImage, likeIcon, commentIcon, deleteIcon;
        TextView usernameText, titleText, descText, locationText;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            usernameText = itemView.findViewById(R.id.usernameText);
            titleText = itemView.findViewById(R.id.titleText);
            descText = itemView.findViewById(R.id.descriptionText);
            locationText = itemView.findViewById(R.id.locationText);
            postImage = itemView.findViewById(R.id.postImage);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            commentIcon = itemView.findViewById(R.id.commentIcon);
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}






