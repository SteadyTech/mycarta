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

import io.realm.Realm;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.activity.DetailCardActivity;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.CardModel;
import me.muhammadfaisal.mycarta.v2.model.realm.BalanceDatabase;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<CardModel> cardModels;
    private Activity activity;
    private Realm realm;
    private BalanceDatabase databases;


    public CardAdapter(ArrayList<CardModel> cardModels, Activity activity) {
        this.cardModels = cardModels;
        this.activity = activity;
        this.realm = Realm.getDefaultInstance();
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

        this.databases = realm.where(BalanceDatabase.class).equalTo("cardID", cardModel.getCardNumber()).findFirst();

        if (databases != null) {
            Log.d("Balance Database", databases.getBalance() + " " + databases.getCardID());
            holder.textCardBalance.setText(CartaHelper.currencyFormat(Long.parseLong(databases.getBalance())));
        }

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
