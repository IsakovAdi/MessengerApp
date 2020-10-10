package com.example.messengerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messengerapp.models.User;
import com.example.messengerapp.ui.main.SectionPagerAdapterMain;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {


    CircleImageView profileImage;
    TextView userName;
    FirebaseUser firebaseUser; // нужен для того чтобы узнать кто зашел
    DatabaseReference reference;  //
    MediaPlayer player;
    @Override
    protected void onStart() {

        super.onStart();
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.userNameMain);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getUsername());
                if (user.getImageUrl().equals("default")){
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Picasso.get().load(user.getImageUrl()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        SectionPagerAdapterMain sectionPagerAdapterMain = new SectionPagerAdapterMain(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager_main);
        viewPager.setAdapter(sectionPagerAdapterMain);
        TabLayout tabLayout = findViewById(R.id.main_tab);
        tabLayout.setupWithViewPager(viewPager);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, AuthActivity.class));
            finish();
            player = MediaPlayer.create(this, R.raw.logout);
            player.start();
            Toast.makeText(MainActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}