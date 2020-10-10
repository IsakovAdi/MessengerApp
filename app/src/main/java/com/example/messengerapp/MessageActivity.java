package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messengerapp.models.Chat;
import com.example.messengerapp.models.User;
import com.example.messengerapp.ui.main.MessageAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image_message;
    TextView userNameMessage;
    RecyclerView recyclerView;
    EditText sendText;
    ImageButton sendButton;

    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Intent intent;
    List<Chat> chatList = new ArrayList<>();
    MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        sendText = findViewById(R.id.sendText);
        sendButton = findViewById(R.id.sendButton);
        userNameMessage = findViewById(R.id.userNameMessage);
        profile_image_message = findViewById(R.id.profile_image_message);
        recyclerView = findViewById(R.id.recyclerViewMessage);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        Toolbar toolbar = findViewById(R.id.toolbarMessage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        intent = getIntent();
        final String userID = intent.getStringExtra("userId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userNameMessage.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    profile_image_message.setImageResource(R.mipmap.ic_launcher_round);
                }
                else {
                    Picasso.get().load(user.getImageUrl()).into(profile_image_message);
                }
                getMessage(firebaseUser.getUid(), userID, user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").push();
                Chat chat = new Chat();
                chat.setMessage(sendText.getText().toString());
                chat.setReceiver(userID);
                chat.setSenderId(firebaseUser.getUid());

                reference.setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        MediaPlayer player = MediaPlayer.create(MessageActivity.this, R.raw.sentmessage);
                        player.start();
                    }
                });
                sendText.setText("");
            }
        });
    }



    public void getMessage(final String myId, final String senderId, final String imageUrl){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myId)&& chat.getSenderId().equals(senderId) ||
                    chat.getSenderId().equals(myId)&&chat.getReceiver().equals(senderId)){
                        chatList.add(chat);
                        Log.i("GETMESSAGE", chat.getMessage());
                    }
                }
                adapter = new MessageAdapter(MessageActivity.this, chatList, imageUrl);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MessageActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}