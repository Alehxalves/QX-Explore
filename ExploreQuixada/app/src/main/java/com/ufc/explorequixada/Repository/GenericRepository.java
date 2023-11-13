package com.ufc.explorequixada.Repository;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.explorequixada.Interface.GenericInterface;

public abstract class GenericRepository<T> implements GenericInterface<T> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collection;

    public GenericRepository(String collectionName) {
        this.collection = db.collection(collectionName);
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public CollectionReference getCollection() {
        return collection;
    }

    public void createDocument(T data) {
        this.collection.add(data);
    }

    public void getDocument(String documentId, OnSuccessListener<DocumentSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        collection.document(documentId).get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void updateDocument(String documentId, T data, OnCompleteListener<Void> onCompleteListener) {
        collection.document(documentId).set(data)
                .addOnCompleteListener(onCompleteListener);
    }

    public void deleteDocument(String documentId, OnCompleteListener<Void> onCompleteListener) {
        collection.document(documentId).delete()
                .addOnCompleteListener(onCompleteListener);
    }

    public void getAllDocuments(OnSuccessListener<QuerySnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        collection.get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

    public void getDocumentsByField(String fieldName, Object value, OnSuccessListener<QuerySnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        collection.whereEqualTo(fieldName, value)
                .get()
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(onFailureListener);
    }

}

