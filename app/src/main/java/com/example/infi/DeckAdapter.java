package com.example.infi;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.infi.R;
import com.example.infi.entity.Deck;
import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DeckAdapter.DeckViewHolder> {

    private List<Deck> deckList;

    public DeckAdapter(List<Deck> deckList) {
        this.deckList = deckList;
    }

    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.decks_view, parent, false);
        return new DeckViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeckViewHolder holder, int position) {
        Deck deck = deckList.get(position);
        holder.textViewDeckName.setText(deck.getDeck_name());
        holder.textViewLastUpdate.setText(deck.getLast_update());
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }

    static class DeckViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDeckName;
        TextView textViewLastUpdate;

        public DeckViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDeckName = itemView.findViewById(R.id.textViewDeckName);
            textViewLastUpdate = itemView.findViewById(R.id.textViewLastUpdate);
        }
    }
}
