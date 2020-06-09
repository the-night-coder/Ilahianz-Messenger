package com.nightcoder.ilahianz.ChatUI.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcoder.ilahianz.Adapters.ChatListAdapter;
import com.nightcoder.ilahianz.Models.ChatListModel;
import com.nightcoder.ilahianz.R;
import com.nightcoder.ilahianz.SearchActivity;
import com.nightcoder.ilahianz.Supports.Graphics;
import com.nightcoder.ilahianz.Supports.MemorySupports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.nightcoder.ilahianz.Models.Notice.KEY_ID;

public class ChatFragment extends Fragment {

    private Context mContext;
    private LinearLayout noChats;
    private RecyclerView recyclerView;
    private LinearLayout loading;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        readAllChatList();
        noChats = view.findViewById(R.id.no_chats);
        ImageView imageView = view.findViewById(R.id.imageView);
        ImageButton searchButton = view.findViewById(R.id.search_button);
        recyclerView = view.findViewById(R.id.recycler_view);
        loading = view.findViewById(R.id.loading);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SearchActivity.class));
            }
        });
        Graphics.setGifImage(mContext, R.raw.no_chats, imageView);
        noChats.setVisibility(View.GONE);
        return view;
    }


    private void readAllChatList() {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("ChatList").child(MemorySupports.getUserInfo(mContext, KEY_ID));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ChatListModel> chatListModelArrayList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatListModel chatListModel = snapshot.getValue(ChatListModel.class);
                    chatListModelArrayList.add(chatListModel);
                }
                if (!chatListModelArrayList.isEmpty()) {
                    Collections.sort(chatListModelArrayList, new Comparator<ChatListModel>() {
                        @Override
                        public int compare(ChatListModel s1, ChatListModel s2) {
                            return String.valueOf(s1.getTime()).compareToIgnoreCase(String.valueOf(s2.getTime()));
                        }
                    });

                    ChatListAdapter chatListAdapter = new ChatListAdapter(mContext, chatListModelArrayList);
                    recyclerView.setAdapter(chatListAdapter);
                    noChats.setVisibility(View.GONE);
                } else {
                    noChats.setVisibility(View.VISIBLE);
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
