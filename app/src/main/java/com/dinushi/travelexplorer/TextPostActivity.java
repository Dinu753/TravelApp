package com.dinushi.travelexplorer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class TextPostActivity extends AppCompatActivity {

    private EditText textDescription, textLocation;
    private Button submitTextPostButton;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_post);

        textDescription = findViewById(R.id.textDescription);
        textLocation = findViewById(R.id.textLocation);
        submitTextPostButton = findViewById(R.id.submitTextPostButton);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        submitTextPostButton.setOnClickListener(v -> {
            String description = textDescription.getText().toString().trim();
            String location = textLocation.getText().toString().trim();

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "anonymous";

            PostModel post = new PostModel(
                    UUID.randomUUID().toString(),
                    description,
                    null,  // no image for text-only
                    location,
                    userId
            );

            db.collection("posts")
                    .document(post.title)
                    .set(post)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Text post uploaded!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to upload", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}

