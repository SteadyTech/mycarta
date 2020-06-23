package me.muhammadfaisal.mycarta.v2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.activity.DetailCardActivity;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<CardModel> cards;
    private Activity activity;

    public HomeAdapter(ArrayList<CardModel> cards, Activity context) {
        this.cards = cards;
        this.activity = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_card, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CardModel card = cards.get(position);

        Log.d("Get Key-Preference", card.getCardNumber());

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.PREFERENCE.TRANSACTION, Context.MODE_PRIVATE);
        long balance = sharedPreferences.getLong(card.getCardNumber(), 0);

        holder.textBalance.setText(CartaHelper.currencyFormat(balance));
        holder.textOwner.setText(CartaHelper.decryptString(card.getCardOwner()));
        holder.textCardNumber.setText(CartaHelper.decryptString(card.getCardNumber()).substring(CartaHelper.decryptString(card.getCardNumber()).length() - 4));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("card", card);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.cardView, "CardView");
                activity.startActivity(new Intent(activity, DetailCardActivity.class).putExtras(bundle), activityOptionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textBalance;
        private TextView textOwner;
        private TextView textCardNumber;

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.textBalance = itemView.findViewById(R.id.textBalance);
            this.textOwner = itemView.findViewById(R.id.textOwner);
            this.textCardNumber = itemView.findViewById(R.id.textCardNumber);
            this.cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
