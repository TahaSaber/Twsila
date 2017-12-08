package com.example.tahasaber.twsila.AdapterClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tahasaber.twsila.ActivityClasses.ChatActivity;
import com.example.tahasaber.twsila.DataClasses.PostDataClass;
import com.example.tahasaber.twsila.DataClasses.UserDataClass;
import com.example.tahasaber.twsila.R;
import com.example.tahasaber.twsila.UtilityClasses.FirebaseHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * Created by mohamed on 06/02/17.
 */

public class PostsCardViewAdapter extends RecyclerView.Adapter<PostsCardViewAdapter.PostViewHolder> {

    //PostDataClass[] PostDataClasses;
    ArrayList<PostDataClass> PostDataClasses;
    Context mcContext;
    FirebaseHandler shareRequestHandler;
    private FirebaseUser mUser;


    //changed to be dynamic adding and removing posts to the ArrayList
    //so I changed the regular array to ArrayList.
    public PostsCardViewAdapter(/*PostDataClass[] PostDataClasses*/
                                ArrayList<PostDataClass> PostDataClasses, Context mcContext) {
        this.PostDataClasses = PostDataClasses;
        this.mcContext = mcContext;
    }

    @Override
    public int getItemCount() {
        return PostDataClasses.size();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        PostViewHolder pvh = new PostViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(final PostViewHolder postViewHolder, int i) {
        final String postId = PostDataClasses.get(i).getPost_id();
        final String publisherId = PostDataClasses.get(i).getUser_id();

        final String publisherName = PostDataClasses.get(i).getPost_puplisher();

        final int NoOfAcceptance = PostDataClasses.get(i).getacceptance();

        final String postBody = PostDataClasses.get(i).getPost_body();


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        postViewHolder.post_body.setText(PostDataClasses.get(i).getPost_body());
        postViewHolder.post_publisher.setText(PostDataClasses.get(i).getPost_puplisher());
        postViewHolder.post_date.setText(PostDataClasses.get(i).getPost_date());
        postViewHolder.publisher_image.setImageResource(PostDataClasses.get(i).getProfile_picture());
        postViewHolder.category_icon.setImageResource(PostDataClasses.get(i).getCategory_icon());
        postViewHolder.team_counter.setText(String.valueOf(PostDataClasses.get(i).getacceptance()));
        postViewHolder.acceptedNumTextView.setText(String.valueOf(PostDataClasses.get(i).getAcceptedNum()));

        postViewHolder.join_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (publisherId.equals(mUser.getUid())) {
                    Toast.makeText(mcContext, "You are already joined, Start Chat", Toast.LENGTH_LONG).show();
                } else if (NoOfAcceptance <= 0) {
                    Toast.makeText(mcContext, "Sorry! This post is closed", Toast.LENGTH_LONG).show();
                } else {

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("posts_chat").child("posts").child(postId).child("users");


                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (snapshot.hasChild(mUser.getUid())) {
                                Toast.makeText(mcContext, "You are already joined, Start Chat", Toast.LENGTH_LONG).show();

                            } else {
                                shareRequestHandler = new FirebaseHandler();
                                shareRequestHandler.sendShareRequest(publisherId, postId, mUser.getUid(), postBody, mUser.getDisplayName());
                                Toast.makeText(mcContext, "Request has sent successfully", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }


            }
        });

        postViewHolder.msg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // check if he is authorized

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("posts_chat").child("posts").child(postId).child("users");


                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(mUser.getUid())) {

                            Intent intent = new Intent(mcContext, ChatActivity.class);
                            intent.putExtra("publisherName", publisherName);
                            intent.putExtra("postBody", postBody);
                            intent.putExtra("postId", postId);
                            mcContext.startActivity(intent);

                        } else {

                            Toast.makeText(mcContext, "Sorry! Join first", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        postViewHolder.acceptedNumTextView.setOnClickListener(new View.OnClickListener() {

            ArrayList<String> acceptedPersonsIds;
            ArrayList<String> acceptedPersonsNames;
            String Uid = "";

            @Override
            public void onClick(View v) {

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("posts_chat").child("posts").child(postId).child("users");

                acceptedPersonsIds = new ArrayList<String>();
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        acceptedPersonsIds.clear();
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // TODO: handle the post
                            Uid = userSnapshot.getKey();
                            acceptedPersonsIds.add(Uid);
                            // Toast.makeText(mcContext, Uid, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                });


                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
                acceptedPersonsNames = new ArrayList<String>();


                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        acceptedPersonsNames.clear();
                        for (int i = 0; i < acceptedPersonsIds.size(); i++) {
                            if (snapshot.hasChild(acceptedPersonsIds.get(i))) {

                                DatabaseReference userObjectDBRef;
                                userObjectDBRef = FirebaseDatabase.getInstance().getReference().child("Users").child(acceptedPersonsIds.get(i));
                                userObjectDBRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String s = dataSnapshot.child("name").getValue(String.class);
                                        acceptedPersonsNames.add(s);
                                        Toast.makeText(mcContext, s + Integer.toString(acceptedPersonsNames.size()), Toast.LENGTH_SHORT).show();


                                    }


                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        // Getting Post failed, log a message
                                        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                                        // ...
                                    }
                                });


                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                ArrayList<String> e = new ArrayList<String>();
                e.add("a");
                e.add("b");
                e.add("c");



                postViewHolder.spinner.setVisibility(View.VISIBLE);

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mcContext, android.R.layout.simple_spinner_item, acceptedPersonsNames);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                postViewHolder.spinner.setAdapter(dataAdapter);


            }
        });


    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public TextView post_body;
        public TextView post_publisher;
        public TextView post_date;
        public ImageView publisher_image;
        public Button msg_btn;
        public Button join_button;
        public ImageView category_icon;
        public TextView team_counter;
        public TextView acceptedNumTextView;
        public Spinner spinner;


        public PostViewHolder(View itemView) {
            super(itemView);
            post_body = (TextView) itemView.findViewById(R.id.post_body);
            post_publisher = (TextView) itemView.findViewById(R.id.publisher_name);
            post_date = (TextView) itemView.findViewById(R.id.post_date);
            publisher_image = (ImageView) itemView.findViewById(R.id.profile_image);
            msg_btn = (Button) itemView.findViewById(R.id.msg_btn);
            join_button = (Button) itemView.findViewById(R.id.join_btn);
            category_icon = (ImageView) itemView.findViewById(R.id.icon_image);
            team_counter = (TextView) itemView.findViewById(R.id.counter_id);
            acceptedNumTextView = (TextView) itemView.findViewById(R.id.accepted_view);
            spinner = (Spinner) itemView.findViewById(R.id.spinner_accepted);

        }
    }


}
