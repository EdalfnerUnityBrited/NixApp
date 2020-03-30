package com.example.nixapp.UI.serviciosContratados;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessageList;
    private  String messageSender;
    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public MessageAdapter(List<Messages> userMessageList, String messageSender){
        this.userMessageList= userMessageList;
        this.messageSender= messageSender;

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView senderMessageText, receiverMessageText;
        public ImageView senderImage, receiverImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText=(TextView) itemView.findViewById(R.id.senderMessage);
            receiverMessageText=(TextView) itemView.findViewById(R.id.reciverMessage);
            senderImage=itemView.findViewById(R.id.senderImage);
            receiverImage=itemView.findViewById(R.id.reciverImage);
        }

    }
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_message_layout, parent, false);

        mAuth= FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {


        Messages messages= userMessageList.get(position);
        String fromUserId= messages.getFrom();
        String fromMessageType= messages.getType();
        usersRef= FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (fromMessageType.equals("text")){
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.senderMessageText.setVisibility(View.INVISIBLE);
            if (fromUserId.equals(messageSender)){

                holder.senderMessageText.setVisibility(View.VISIBLE);

                holder.senderMessageText.setBackgroundResource(R.drawable.sender_messages_layout);
                holder.senderMessageText.setTextColor(Color.WHITE);
                holder.senderMessageText.setText(messages.getMessage());
            }
            else{
                holder.receiverMessageText.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setBackgroundResource(R.drawable.reciver_messages_layout);
                holder.receiverMessageText.setTextColor(Color.BLACK);
                holder.receiverMessageText.setText(messages.getMessage());

            }
        }
        else if(fromMessageType.equals("Images")){
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.senderMessageText.setVisibility(View.INVISIBLE);
            if (fromUserId.equals(messageSender)){
                holder.senderImage.setVisibility(View.VISIBLE);

                Picasso.get().load(messages.getMessage()).resize(800,300).centerInside().into(holder.senderImage);


            }
            else{
                holder.receiverImage.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).resize(800,300).centerInside().into(holder.senderImage);

            }
        }

    }

    @Override
    public int getItemCount() {
        return userMessageList.size();
    }


}
