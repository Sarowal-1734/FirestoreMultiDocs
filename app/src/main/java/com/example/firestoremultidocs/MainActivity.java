package com.example.firestoremultidocs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {
    // For key value pair (Firebase)
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    // For Database
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference noteBookRef = db.collection("NoteBook");
    private DocumentReference noteRef = db.document("NoteBook/My First Note");

    private EditText etTitle, etDescription;
    private TextView tvDisplayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        tvDisplayData = findViewById(R.id.tvDisplayData);
    }

    // To store data to firestore on Add_Note button clicked
    public void onAddButtonClick(View view) {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        Note note = new Note(title, description);
        noteBookRef.add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this, "Note Added Successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error while adding note!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Adding listener to show data whenever changing occur in documents whithout clicking Load button
    @Override
    protected void onStart() {
        super.onStart();
        noteBookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                //Check exception
                if (error != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());
                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();
                    data += "ID: "+documentId +"\nTitle: "+title +"\nDescription: "+description +"\n\n";
                }
                tvDisplayData.setText(data);
            }
        });
    }

    // To display data from Firebase on Load button clicked
    public void onLoadButtonClick(View view) {
        noteBookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());
                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            data += "ID: "+documentId +"\nTitle: "+title +"\nDescription: "+description +"\n\n";
                        }
                        tvDisplayData.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}