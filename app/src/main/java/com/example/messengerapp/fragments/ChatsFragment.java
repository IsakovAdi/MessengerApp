package com.example.messengerapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.messengerapp.MessageActivity;
import com.example.messengerapp.R;
import com.example.messengerapp.models.Chat;
import com.example.messengerapp.models.User;
import com.example.messengerapp.ui.main.PeopleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    List<User> userList = new ArrayList<>();
    FirebaseUser firebaseUser; // нужен для того чтобы узнать кто зашел
    DatabaseReference reference;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    PeopleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerViewChats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadChatPeople();
        return view;
    }

    public void loadChatPeople() {
        userList.clear();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSenderId().equals(firebaseUser.getUid()) || chat.getReceiver().equals(firebaseUser.getUid())) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snapshotUsers : snapshot.getChildren()) {
                                    User user = snapshotUsers.getValue(User.class);
                                    if (!user.getId().equals(firebaseUser.getUid())) {
                                        if (user.getId().equals(chat.getSenderId()) || user.getId().equals(chat.getReceiver())) {
                                            for (int i = 0; i < userList.size(); i++) {
                                                if (!userList.get(i).getId().equals(user.getId())) {
                                                    userList.add(user);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                adapter = new PeopleAdapter(getContext(), userList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new PeopleAdapter.RecyclerOnClickListener() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getContext(), MessageActivity.class);
                        String userID = userList.get(position).getId();
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}