package com.ufc.explorequixada.Repository;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Interface.UserInterface;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class UserRepository extends GenericRepository<UserEntity> implements UserInterface {

    public UserRepository(String collectionName) {
        super(collectionName);
    }

    public void findByUsername(String username, OnSuccessListener<DocumentSnapshot> onSuccessListener, OnFailureListener onFailureListener) {
        getDocumentsByField("username", username, new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    onSuccessListener.onSuccess(documentSnapshot);
                } else {
                    onFailureListener.onFailure(new Exception("User not found"));
                }
            }
        }, onFailureListener);
    }


    public interface OnUserEntityCompleteListener {
        void onUserEntityComplete(UserEntity userEntity);
        void onUserEntityFailed(Exception e);
    }
    @Override
    public void getByEmail(String email, OnUserEntityCompleteListener listener) {
    }

}
