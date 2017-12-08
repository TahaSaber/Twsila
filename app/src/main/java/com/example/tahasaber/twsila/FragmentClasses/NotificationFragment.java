package com.example.tahasaber.twsila.FragmentClasses;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.tahasaber.twsila.ActivityClasses.MainActivity;
import com.example.tahasaber.twsila.AdapterClasses.NotificationListAdapter;
import com.example.tahasaber.twsila.DataClasses.NotificationDataClass;
import com.example.tahasaber.twsila.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationFragment extends android.app.Fragment {

    ListView listView;
    ArrayList<NotificationDataClass> notifications = new ArrayList<NotificationDataClass>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    NotificationListAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Notifications");

        listView = (ListView) rootView.findViewById(R.id.noti_list);

        DatabaseReference myNotifRef = database.getReference().child("share_request_notification").child(mUser.getUid());
        myNotifRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notifications.clear();
                for (DataSnapshot notification : dataSnapshot.getChildren()) {
                    NotificationDataClass oneNotif = notification.getValue(NotificationDataClass.class);
                    notifications.add(oneNotif);
                }
                if (getActivity() != null) {

                    adapter = new NotificationListAdapter(getActivity(), notifications);
                    listView.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int colorValue = Color.parseColor("#FFFFFF");

                listView.getChildAt(position).setBackgroundColor(colorValue);


            }


        });

//        notifications.add(n1);
//        notifications.add(n2);
//        notifications.add(n3);


        return rootView;
    }
}
