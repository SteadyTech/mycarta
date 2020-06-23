package me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.switcher.adapter;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.bottom_sheet.switcher.SwitchCardBottomSheetFragment;
import me.muhammadfaisal.mycarta.v1.home.fragment.card.model.Card;

public class SwitchCardAdapter extends RecyclerView.Adapter<SwitchCardAdapter.ViewHolder>{

    private ArrayList<Card> cards;
    SwitchCardBottomSheetFragment bottomSheetFragment;
    private CardBottomSheetCallback listener;

    public interface CardBottomSheetCallback{
        public void onItemClick(Card card, Bundle bundle);
    }

    public SwitchCardAdapter(ArrayList<Card> cards, CardBottomSheetCallback listener,SwitchCardBottomSheetFragment bottomSheetFragment) {
        this.cards = cards;
        this.bottomSheetFragment = bottomSheetFragment;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_switch_card, parent, false);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Card card = cards.get(position);

        CartaHelper helper = new CartaHelper();

        final Random random = new Random();
        final int color = Color.rgb(random.nextInt(256),random.nextInt(256),random.nextInt(256));

        String decryptedCardNumber = helper.decryptString(card.getNumberCard());
        String decryptedCvv = helper.decryptString(card.getCvv());

        holder.textNumber.setText("**** **** **** " + decryptedCardNumber.substring(decryptedCardNumber.length() - 4));
        holder.textName.setText(helper.decryptString(card.getName()));
        holder.textCvv.setText("**" + decryptedCvv.substring(decryptedCvv.length() - 1));
        holder.textExpiry.setText(helper.decryptString(card.getExp()));

        String type = helper.decryptString(card.getType());

        Resources resources = Objects.requireNonNull(bottomSheetFragment.getActivity()).getResources();

        if (type.equals("Visa")){
            holder.typeCard.setImageDrawable(resources.getDrawable(R.drawable.visa));
        }else if(type.equals("American Express")){
            holder.typeCard.setImageDrawable(resources.getDrawable(R.drawable.american_express));
        }else if(type.equals("Mastercard")){
            holder.typeCard.setImageDrawable(resources.getDrawable(R.drawable.mastercard));
        }else if(type.equals("JCB")){
            holder.typeCard.setImageDrawable(resources.getDrawable(R.drawable.jcb));
        }else{
            holder.typeCard.setImageDrawable(resources.getDrawable(R.drawable.ic_launcher));
        }

        holder.cardBackground.setBackgroundColor(color);

        if (isDark(color)){
            holder.textNumber.setTextColor(Color.WHITE);
            holder.textName.setTextColor(Color.WHITE);
            holder.textCvv.setTextColor(Color.WHITE);
            holder.textExpiry.setTextColor(Color.WHITE);
            holder.hintNumber.setTextColor(Color.WHITE);
            holder.hintCvv.setTextColor(Color.WHITE);
            holder.hintName.setTextColor(Color.WHITE);
            holder.hintExpiry.setTextColor(Color.WHITE);
        }else{
            holder.textNumber.setTextColor(Color.BLACK);
            holder.textName.setTextColor(Color.BLACK);
            holder.textCvv.setTextColor(Color.BLACK);
            holder.textExpiry.setTextColor(Color.BLACK);
            holder.hintNumber.setTextColor(Color.BLACK);
            holder.hintCvv.setTextColor(Color.BLACK);
            holder.hintName.setTextColor(Color.BLACK);
            holder.hintExpiry.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();

                b.putInt("color", color);

                listener.onItemClick(card, b);

                bottomSheetFragment.dismiss();
            }
        });

    }
    public boolean isDark(int color){
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textNumber, textCvv, textExpiry, hintName , hintNumber, hintCvv, hintExpiry;
        RelativeLayout cardBackground;
        ImageView typeCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.textName);
            textNumber = itemView.findViewById(R.id.textNumber);
            textCvv = itemView.findViewById(R.id.textCvv);
            textExpiry = itemView.findViewById(R.id.textExpiry);
            hintCvv = itemView.findViewById(R.id.hintCvv);
            hintExpiry = itemView.findViewById(R.id.hintExpiry);
            hintNumber = itemView.findViewById(R.id.hintCardNumber);
            hintName = itemView.findViewById(R.id.hintCardName);

            typeCard = itemView.findViewById(R.id.typeCard);

            cardBackground = itemView.findViewById(R.id.backgroundCard);

        }
    }
}
