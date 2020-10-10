package com.example.messengerapp.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messengerapp.R;
import com.example.messengerapp.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.Holder> {
    public static final int MESSAGE_LEFT = 0;
    public static final int MESSAGE_RIGHT = 1;

    private Context context;
    private List<Chat> chatList;
    private String imageUrl;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context context, List<Chat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==MESSAGE_RIGHT){
            view = LayoutInflater.from(context).inflate(R.layout.chat_right, parent, false);
        }
        else {
            view = LayoutInflater.from(context).inflate(R.layout.chat_left, parent, false);

        }
        return new MessageAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Chat chat = chatList.get(position);
        holder.chat_sender_text.setText(chat.getMessage());
        if (imageUrl.equals("default")){
            holder.circleChatImageView.setImageResource(R.mipmap.ic_launcher_round);
        }
        else {
            Picasso.get().load(imageUrl).into(holder.circleChatImageView);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        CircleImageView circleChatImageView;
        TextView chat_sender_text;
        public Holder(@NonNull View itemView) {
            super(itemView);
            circleChatImageView = itemView.findViewById(R.id.circleChatImageView);
            chat_sender_text = itemView.findViewById(R.id.chat_text);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSenderId().equals(firebaseUser.getUid())){
            return MESSAGE_RIGHT;
        }
        else {
            return MESSAGE_LEFT;
        }
    }
}
