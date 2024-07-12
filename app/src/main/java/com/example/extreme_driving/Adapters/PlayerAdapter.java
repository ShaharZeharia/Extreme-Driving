package com.example.extreme_driving.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.extreme_driving.Interfaces.PlayerCallback;
import com.example.extreme_driving.Models.Player;
import com.example.extreme_driving.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private final List<Player> players;
    private PlayerCallback playerCallback;

    public PlayerAdapter(List<Player> players) {
        this.players = players;
    }

    public void setPlayerCallback(PlayerCallback playerCallback) {
        this.playerCallback = playerCallback;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.highscores_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = players.get(position);
        holder.highscores_item_LBL_rank.setText(String.format((position + 1) + "."));
        holder.highscores_item_LBL_score.setText(String.valueOf(player.getScore()));
    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    private Player getItem(int position) {
        return players.get(position);
    }

    public class PlayerViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView highscores_item_LBL_rank;
        MaterialTextView highscores_item_LBL_score;
        MaterialButton highscores_item_BTN_location;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            highscores_item_LBL_rank = itemView.findViewById(R.id.highscores_item_LBL_rank);
            highscores_item_LBL_score = itemView.findViewById(R.id.highscores_item_LBL_score);
            highscores_item_BTN_location = itemView.findViewById(R.id.highscores_item_BTN_location);
            highscores_item_BTN_location.setOnClickListener(v -> {
                if (playerCallback != null) {
                    playerCallback.handlePlayerSelection(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }
}