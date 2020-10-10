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
import com.example.messengerapp.models.User;
import com.example.messengerapp.ui.main.PeopleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PeopleFragment extends Fragment {

    List<User> userList = new ArrayList<>();
    FirebaseUser firebaseUser; // нужен для того чтобы узнать кто зашел
    DatabaseReference reference;
    RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    PeopleAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_people, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        refreshLayout = view.findViewById(R.id.swipeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNewPeople();
            }
        });

        loadPeople();
        return view;
    }

    public void loadPeople() {
        refreshLayout.setRefreshing(true);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refreshLayout.setRefreshing(false);
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        userList.add(user);
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

    public void loadNewPeople(){
        refreshLayout.setRefreshing(true);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                refreshLayout.setRefreshing(false);
                userList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    User u = dataSnapshot.getValue(User.class);
                    if (!u.getId().equals(firebaseUser.getUid())) {
                        userList.add(u);
                    }
                }
                adapter = new PeopleAdapter(getContext(), userList);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new PeopleAdapter.RecyclerOnClickListener() {
                    @Override
                    public void onClick(int position) {
                        Toast.makeText(getContext(), userList.get(position).getEmail(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}