package com.dinushi.travelexplorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private ImageView selectedImageView;
    private Button choosePhotoButton;
    private EditText postText;
    private EditText locationEditText;
    private Button postButton;

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        selectedImageView = findViewById(R.id.selectedImageView);
        choosePhotoButton = findViewById(R.id.choosePhotoButton);
        postText = findViewById(R.id.postText);
        locationEditText = findViewById(R.id.locationEditText);
        postButton = findViewById(R.id.postButton);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("post_images");

        choosePhotoButton.setOnClickListener(v -> openImageChooser());

        postButton.setOnClickListener(v -> {
            String description = postText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please write something before posting.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (imageUri == null) {
                Toast.makeText(this, "Please choose a photo.", Toast.LENGTH_SHORT).show();
                return;
            }

            postButton.setEnabled(false);
            postButton.setText("Uploading...");

            String filename = UUID.randomUUID().toString() + ".jpg";
            StorageReference imageRef = storageRef.child(filename);

            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String imageUrl = uri.toString();
                                    String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";

                                    PostModel post = new PostModel(
                                            UUID.randomUUID().toString(),
                                            description,
                                            imageUrl,
                                            location,
                                            userId
                                    );

                                    db.collection("posts")
                                            .document(post.title)
                                            .set(post)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(this, "Post uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                finish(); // close activity
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Failed to upload post: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                postButton.setEnabled(true);
                                                postButton.setText("Post");
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Failed to get image URL: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    postButton.setEnabled(true);
                                    postButton.setText("Post");
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        postButton.setEnabled(true);
                        postButton.setText("Post");
                    });
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            selectedImageView.setImageURI(imageUri);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }
}





