package com.ufc.explorequixada.Interface;

import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Repository.UserDAO;

public interface UserInterface{

    void createUser(UserEntity user, final UserDAO.OnUserCreatedListener listener);

    boolean updateUser(UserEntity user);

    UserEntity findByEmail(String email, final UserDAO.OnUserFindedListener listener);

    boolean deleteById(String id);

}
