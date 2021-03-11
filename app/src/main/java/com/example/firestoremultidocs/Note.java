package com.example.firestoremultidocs;

import com.google.firebase.firestore.Exclude;

public class Note {
    private String documentId;
    private String title;
    private String description;

    public Note() {
        // public no-args constructor needed
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Exclude   // That means this column will not appear in firestore document
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
