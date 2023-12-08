package com.ufc.explorequixada.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ufc.explorequixada.Entity.UserEntity;
import com.ufc.explorequixada.Fragment.SettingsFragment;
import com.ufc.explorequixada.R;
import com.ufc.explorequixada.Repository.UserDAO;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    UserDAO userDAO;
    TextInputEditText editTextEmail, editTextUsername, editTextPassword;
    TextView loginNow;
    Button btnRegister;
    ProgressBar progressBar;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        userDAO = new UserDAO();

        editTextEmail = findViewById(R.id.email);
        editTextUsername = findViewById(R.id.username);
        editTextPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);
        loginNow = findViewById(R.id.loginNow);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginActivity();
            }
        });;
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void register() {
        String email, username, password;
        email = String.valueOf(editTextEmail.getText());
        username = String.valueOf(editTextUsername.getText());
        password = String.valueOf(editTextPassword.getText());

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        if (!checkEmptyFields(email, password)){
            verifyDuplicate(user);
        }
    }

    public void createUser(UserEntity user) {
        progressBar.setVisibility(View.VISIBLE);
        btnRegister.setVisibility(View.GONE);
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        btnRegister.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            userDAO.createUser(user, new UserDAO.OnUserCreatedListener() {
                                @Override
                                public void onUserCreated(boolean isSuccess) {
                                    if(isSuccess) {
                                        Toast.makeText(RegisterActivity.this, "Cadastro bem sucedido.",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Ocorreu um erro durante o cadastro.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    btnRegister.setVisibility(View.VISIBLE);
                                }
                            });
                            goToMainActivity();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Ocorreu um erro durante o cadastro.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnRegister.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    public void goToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void verifyDuplicate(UserEntity user) {
        userDAO.findByEmail(user.getEmail(), new UserDAO.OnUserFindedListener() {
            @Override
            public void onUserFinded(UserEntity userFinded) {
                if (userFinded != null) {
                    Toast.makeText(RegisterActivity.this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
                } else {
                    userDAO.findByUsername(user.getUsername(), new UserDAO.OnUserFindedListener() {
                        @Override
                        public void onUserFinded(UserEntity userFinded) {
                            if(userFinded != null) {
                                Toast.makeText(RegisterActivity.this, "Nome de usuário já cadastrado", Toast.LENGTH_SHORT).show();
                            }else{
                                createUser(user);
                            }
                        }
                    });
                }
            }
        });
    }

    public boolean checkEmptyFields(String email, String password) {
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password) ) {
            Toast.makeText(RegisterActivity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return true;
        } else if(TextUtils.isEmpty(email)) {
            Toast.makeText(RegisterActivity.this, "Por favor, coloque seu email.", Toast.LENGTH_SHORT).show();
            return true;
        } else if(TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Por favor, coloque uma senha.", Toast.LENGTH_SHORT).show();
            return true;
        } else if(password.length() < 6){
            Toast.makeText(RegisterActivity.this, "Sua senha precisa conter mais de 5 digitos.", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}