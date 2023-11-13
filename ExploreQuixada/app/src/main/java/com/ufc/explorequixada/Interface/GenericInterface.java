package com.ufc.explorequixada.Interface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public interface GenericInterface<T> {
    void createDocument(T data);
    void getDocument(String documentId, OnSuccessListener<DocumentSnapshot> onSuccessListener, OnFailureListener onFailureListener);
    void updateDocument(String documentId, T data, OnCompleteListener<Void> onCompleteListener);
    void deleteDocument(String documentId, OnCompleteListener<Void> onCompleteListener);
    void getAllDocuments(OnSuccessListener<QuerySnapshot> onSuccessListener, OnFailureListener onFailureListener);
    void getDocumentsByField(String fieldName, Object value, OnSuccessListener<QuerySnapshot> onSuccessListener, OnFailureListener onFailureListener);
}
