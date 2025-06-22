package com.dinushi.travelexplorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class PostFeedActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    PostAdapter adapter;
    ArrayList<PostModel> postList;
    ImageView profileIcon, addPostButton;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_feed);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postList = new ArrayList<>();
        adapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(adapter);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Load posts from Firestore
        fetchPostsFromFirebase();

        // Add default sample posts
        addDefaultPosts();

        // Receive new post data from another activity
        String newPostText = getIntent().getStringExtra("new_post");
        String newImageUri = getIntent().getStringExtra("post_image_uri");
        String newPostLocation = getIntent().getStringExtra("post_location");
        String userId = "guest"; // Replace if using real auth

        if (newPostText != null && newImageUri != null) {
            postList.add(0, new PostModel(
                    "New Post",
                    newPostText,
                    newImageUri,
                    newPostLocation,
                    userId
            ));
        }

        adapter.notifyDataSetChanged();

        // Profile icon click
        profileIcon = findViewById(R.id.profileIcon);
        if (profileIcon == null) {
            Toast.makeText(this, "❌ profileIcon is NULL! Check layout ID!", Toast.LENGTH_LONG).show();
        } else {
            profileIcon.setOnClickListener(v -> {
                Toast.makeText(PostFeedActivity.this, "✅ Profile icon clicked!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostFeedActivity.this, ProfileActivity.class));
            });
        }

        // Add post button click
        addPostButton = findViewById(R.id.addPostButton);
        if (addPostButton == null) {
            Toast.makeText(this, "❌ addPostButton is NULL! Check layout ID!", Toast.LENGTH_LONG).show();
        } else {
            addPostButton.setOnClickListener(v -> {
                Toast.makeText(PostFeedActivity.this, "✅ Add post clicked!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostFeedActivity.this, TextPostActivity.class));
            });
        }
    }

    // Load posts from Firebase Firestore
    private void fetchPostsFromFirebase() {
        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        PostModel post = doc.toObject(PostModel.class);
                        postList.add(post);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load posts from Firebase", Toast.LENGTH_SHORT).show();
                });
    }

    // hardcoded default posts
    private void addDefaultPosts() {
        postList.add(new PostModel(
                "Trip to Ella",
                "It was amazing!",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.tr01).toString(),
                "Ella, Sri Lanka",
                "guest"
        ));

        postList.add(new PostModel(
                "Sunset at Mirissa",
                "So peaceful and beautiful!",
                Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.mirissa).toString(),
                "Mirissa Beach",
                "guest"
        ));
    }
}

















