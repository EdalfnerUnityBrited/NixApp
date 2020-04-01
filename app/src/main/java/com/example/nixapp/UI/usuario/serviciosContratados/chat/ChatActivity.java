package com.example.nixapp.UI.usuario.serviciosContratados.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nixapp.DB.Messages;
import com.example.nixapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private ImageButton imSendMessage, imsendImage;
    private EditText etMessage;
    private RecyclerView userMesagesList;
    private TextView twDisplayMessages;
    private FirebaseAuth mAuth;
    private String currentChat, currentDate, currentTime, messageReciverid, messageSenderid, messagesender, myUrl;
    private final List<Messages> messagesList= new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;
    private DatabaseReference groupNameRef, groupMessageKeyRef, RootRef;
    private  String checker= "";
    private Uri fileUri;
    private UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth= FirebaseAuth.getInstance();





        RootRef= FirebaseDatabase.getInstance().getReference();
        imSendMessage= findViewById(R.id.sendMessage);
        etMessage= findViewById(R.id.input_message);
        messageReciverid=getIntent().getExtras().get("reciver").toString();
        messageSenderid=getIntent().getExtras().get("sender").toString();
        messageAdapter= new MessageAdapter(messagesList, messageSenderid);
        userMesagesList=(RecyclerView) findViewById(R.id.recyclerChats);
        linearLayoutManager= new LinearLayoutManager(this);
        userMesagesList.setLayoutManager(linearLayoutManager);
        userMesagesList.setAdapter(messageAdapter);
        imsendImage=findViewById(R.id.sendImage);
        imSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInfoToDatabase();
            }
        });
        imsendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[]= new CharSequence[]{
                    "Images"
                };
                AlertDialog.Builder builder= new AlertDialog.Builder(ChatActivity.this);
                builder.setTitle("Select Image");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                        checker="Images";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("Images/*");
                            startActivityForResult(intent.createChooser(intent, "Select Image"), 438);

                        }
                    }
                });
                builder.show();
            }
        });


        //getSupportActionBar().setTitle(messageReciverid);
    }


    @Override
    protected void onStart() {
        super.onStart();

        RootRef.child("Messages").child(messageSenderid).child(messageReciverid)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Messages messages=dataSnapshot.getValue(Messages.class);
                        messagesList.add(messages);
                        messageAdapter.notifyDataSetChanged();
                        userMesagesList.smoothScrollToPosition(userMesagesList.getAdapter().getItemCount());
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==438&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            fileUri=data.getData();
            if (!checker.equals("Images")){

            }
            else if (checker.equals("Images")){
                StorageReference storageReference= FirebaseStorage.getInstance().getReference().child("Image Files");
                DatabaseReference userMessagekeyRef=RootRef.child("Chats")
                        .child(messageSenderid)
                        .child(messageReciverid).push();

                final String messageSenderRef="Messages/"+messageSenderid+"/"+messageReciverid;
                final String messagereciverRef="Messages/"+messageReciverid+"/"+messageSenderid;
                final String messagePushID= userMessagekeyRef.getKey();

                final StorageReference filePath= storageReference.child(messagePushID+"."+"jpg");
                uploadTask= filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {
                       if (!task.isSuccessful()){
                           throw task.getException();
                       }

                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Uri downloadUrl= task.getResult();
                            myUrl=downloadUrl.toString();

                            Map messageInfoMap= new HashMap<>();
                            messageInfoMap.put("message",myUrl);
                            messageInfoMap.put("name",fileUri.getLastPathSegment());
                            messageInfoMap.put("type", checker);
                            messageInfoMap.put("from",messageSenderid);

                            Map messageDetails= new HashMap();
                            messageDetails.put(messageSenderRef+"/"+messagePushID, messageInfoMap);
                            messageDetails.put(messagereciverRef+"/"+messagePushID, messageInfoMap);


                            RootRef.updateChildren(messageDetails).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ChatActivity.this, "Enviado",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(ChatActivity.this, "Error",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    etMessage.setText("");
                                }
                            });
                        }
                    }
                });
            }
            else{
                Toast.makeText(ChatActivity.this,"Nada Seleccionado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void SaveMessageInfoToDatabase() {
    String message=etMessage.getText().toString();
    String messageKey= RootRef.push().getKey();
        if (message.isEmpty()){
            Toast.makeText(ChatActivity.this, "Inserte texto", Toast.LENGTH_SHORT).show();
        }else{
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate= currentFormat.format(calForDate.getTime());

            Calendar calForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat= new SimpleDateFormat("hh:mm a");
            currentTime= currentTimeFormat.format(calForTime.getTime());



            DatabaseReference userMessagekeyRef=RootRef.child("Chats")
                    .child(messageSenderid)
                    .child(messageReciverid).push();

            String messageSenderRef="Messages/"+messageSenderid+"/"+messageReciverid;
            String messagereciverRef="Messages/"+messageReciverid+"/"+messageSenderid;
            String messagePushID= userMessagekeyRef.getKey();



            Map messageInfoMap= new HashMap<>();
            messageInfoMap.put("message",message);
            messageInfoMap.put("type", "text");
            messageInfoMap.put("from",messageSenderid);

            Map messageDetails= new HashMap();
            messageDetails.put(messageSenderRef+"/"+messagePushID, messageInfoMap);
            messageDetails.put(messagereciverRef+"/"+messagePushID, messageInfoMap);


            RootRef.updateChildren(messageDetails).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ChatActivity.this, "Enviado",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(ChatActivity.this, "Error",
                                Toast.LENGTH_SHORT).show();
                    }
                    etMessage.setText("");
                }
            });
        }
    }
}
