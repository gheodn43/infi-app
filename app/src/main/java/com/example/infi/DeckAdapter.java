package com.example.infi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infi.Interface.DeckClickListener;
import com.example.infi.entity.Deck;

import java.util.List;

public class DeckAdapter extends RecyclerView.Adapter<DecksViewHolder>{
    Context context;
    List<Deck> deckList;
    DeckClickListener listener;
    public DeckAdapter(Context context, List<Deck> deckList, DeckClickListener listener) {
        this.context = context;
        this.deckList = deckList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DecksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DecksViewHolder(LayoutInflater.from(context).inflate(R.layout.deck_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DecksViewHolder holder, int position) {
        Deck deck = deckList.get(position);
        holder.deckNameTxt.setText(deck.getDeck_name());
        int total_card = deck.getNew_count() + deck.getLearning_count() + deck.getReview_count();
        holder.totalCardTxt.setText(String.valueOf(total_card)); // Chuyển đổi thành String
        holder.lastUpdateTxt.setText(deck.getLast_update());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(deck);
            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.longPress(deck, holder.cardView);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return deckList.size();
    }
}

class DecksViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;
    TextView deckNameTxt, totalCardTxt, lastUpdateTxt;

    public DecksViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.deck_container);
        deckNameTxt = itemView.findViewById(R.id.text_view_deck_name);
        totalCardTxt = itemView.findViewById(R.id.text_view_total_card);
        lastUpdateTxt = itemView.findViewById(R.id.text_view_last_update);
    }
}