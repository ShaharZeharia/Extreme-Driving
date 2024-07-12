package com.example.extreme_driving.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.extreme_driving.Interfaces.Callback_ListItemClicked;
import com.example.extreme_driving.Models.Player;
import com.example.extreme_driving.Adapters.PlayerAdapter;
import com.example.extreme_driving.R;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    private RecyclerView fragment_list_RCW_records;
    Callback_ListItemClicked callbackListItemClicked;
    private final ArrayList<Player> playersList;

    public ListFragment(ArrayList<Player> playersList) {
        this.playersList = playersList;
    }

    public void setCallbackListItemClicked(Callback_ListItemClicked callbackListItemClicked) {
        this.callbackListItemClicked = callbackListItemClicked;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        findViews(v);
        initViews();
        return v;
    }


    private void initViews() {
        PlayerAdapter playerAdapter = new PlayerAdapter(playersList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        fragment_list_RCW_records.setLayoutManager(linearLayoutManager);
        fragment_list_RCW_records.setAdapter(playerAdapter);

        playerAdapter.setPlayerCallback((player, position) -> playerClicked(player.getLat(), player.getLng()));
    }

    private void playerClicked(double lat, double lon) {
        if (callbackListItemClicked != null) {
            callbackListItemClicked.listItemClicked(lat, lon);
        }
    }

    private void findViews(View v) {
        fragment_list_RCW_records = v.findViewById(R.id.fragment_list_RCW_records);
    }
}

