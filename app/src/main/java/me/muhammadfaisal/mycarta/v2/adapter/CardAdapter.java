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
import me.muhammadfaisal.mycarta.v1.home.fragment.card.model.Card;
import me.muhammadfaisal.mycarta.v2.activity.DetailCardActivity;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.CardModel;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<CardModel> cardModels;
    private Activity activity;

    public CardAdapter(ArrayList<CardModel> cardModels, Activity activity) {
        this.cardModels = cardModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final CardModel cardModel = this.cardModels.get(position);

        Log.d("Get Key-Preference", cardModel.getCardNumber());

        SharedPreferences sharedPreferences = activity.getSharedPreferences(Constant.PREFERENCE.TRANSACTION, Context.MODE_PRIVATE);
        long balance = sharedPreferences.getLong(cardModel.getCardNumber(), 0);

        holder.textCardBalance.setText(CartaHelper.currencyFormat(balance));

        holder.textCardNumber.setText(CartaHelper.dot().concat(CartaHelper.lastCardNumber(cardModel.getCardNumber())));
        holder.textCardName.setText(CartaHelper.decryptString(cardModel.getCardName()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("card", cardModel);
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.cardView, "CardView");
                activity.startActivity(new Intent(activity, DetailCardActivity.class).putExtras(bundle), activityOptionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.cardModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textCardName;
        private TextView textCardBalance;
        private TextView textCardNumber;

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textCardBalance = itemView.findViewById(R.id.textCardBalance);
            this.textCardName = itemView.findViewById(R.id.textCardName);
            this.textCardNumber = itemView.findViewById(R.id.textCardNumber);
            this.cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
