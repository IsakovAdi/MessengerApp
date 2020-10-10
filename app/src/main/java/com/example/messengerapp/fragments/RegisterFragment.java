package com.example.messengerapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.messengerapp.R;
import com.example.messengerapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterFragment extends Fragment {

    MaterialEditText userName, email, password;
    Button btn_register;
    // Нужен для авторизации
    FirebaseAuth auth;
    //нужен для указания места где будет храниться наш объект в базе данных
    DatabaseReference reference;
    ProgressBar progressBarRegister;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email_l);
        password = view.findViewById(R.id.password_l);
        btn_register = view.findViewById(R.id.login_btn);
        progressBarRegister = view.findViewById(R.id.progressBarRegister);

        auth = FirebaseAuth.getInstance();


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarRegister.setVisibility(View.VISIBLE);
                String txt_userName = userName.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();


                if (TextUtils.isEmpty(txt_userName)|| TextUtils.isEmpty(txt_email)|| TextUtils.isEmpty(txt_password)){
                    Toast.makeText(getContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                else if (txt_password.length()<6){
                    Toast.makeText(getContext(), "Пароль должен содержать минимум 6 символов", Toast.LENGTH_SHORT).show();
                }
                else {
                    register(txt_userName, txt_email, txt_password);
                }
            }
        });

        return view;
    }

    public void register(final String userName, final String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = auth.getCurrentUser();
                            String userId = user.getUid();
                            reference = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(userId);


                            User user1 = new User();
                            user1.setId(userId);
                            user1.setEmail(email);
                            user1.setUsername(userName);
                            user1.setImageUrl("default");

                            reference.setValue(user1)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                progressBarRegister.setVisibility(View.GONE);
                                                Toast.makeText(getContext(), "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                                                ViewPager layout = (ViewPager) getActivity().findViewById(R.id.view_pager);
                                                layout.setCurrentItem(0);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}