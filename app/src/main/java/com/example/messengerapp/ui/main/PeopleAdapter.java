package com.example.messengerapp.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messengerapp.R;
import com.example.messengerapp.models.User;
import com.squareup.picasso.Picasso;

import java.security.PublicKey;
import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.Holder> {
    Context context;
    RecyclerOnClickListener listener;
    List<User> userList;

    public void setOnItemClickListener(RecyclerOnClickListener listener) {
        this.listener = listener;
    }

    public PeopleAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.people, parent, false);
        return new Holder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (userList.get(position).getImageUrl().equals("default")) {
            holder.people_profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Picasso.get()
                    .load(userList.get(position).getImageUrl()).into(holder.people_profile_image);
        }
        holder.people_name.setText(userList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        ImageView people_profile_image;
        TextView people_name;

        public Holder(@NonNull View itemView, final RecyclerOnClickListener listener) {
            super(itemView);
            people_profile_image = itemView.findViewById(R.id.people_profile_image);
            people_name = itemView.findViewById(R.id.people_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerOnClickListener {
        void onClick(int position);
    }
}
