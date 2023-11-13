package com.ufc.explorequixada.Interface;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Repository.UserRepository;

public interface UserInterface extends GenericInterface<UserEntity> {
    void findByUsername(String username, OnSuccessListener<DocumentSnapshot> onSuccessListener, OnFailureListener onFailureListener);

    void getByEmail(String email, UserRepository.OnUserEntityCompleteListener listener);

}
