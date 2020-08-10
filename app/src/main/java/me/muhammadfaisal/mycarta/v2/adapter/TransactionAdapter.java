package me.muhammadfaisal.mycarta.v2.adapter;

import android.content.res.Resources;
import android.icu.text.NumberFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.bottom_sheet.DetailTransactionBottomSheet;
import me.muhammadfaisal.mycarta.v2.bottomsheet.TransactionBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.TransactionModel;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TransactionModel> transactionModels;
    private TransactionBottomSheetFragment transactionBottomSheetFragment;

    private static final int THIS_DATE = 0;
    private static final int THIS_DATA = 1;

    public TransactionAdapter(ArrayList<TransactionModel> transactionModels, TransactionBottomSheetFragment transactionBottomSheetFragment) {
        this.transactionModels = transactionModels;
        this.transactionBottomSheetFragment = transactionBottomSheetFragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case THIS_DATE:
                return new TransactionAdapter.DateViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_manager_date, parent, false));
            case THIS_DATA:
                return new TransactionAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_manager, parent, false));
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final HashMap<String, Object> hashMap = transactionBottomSheetFragment.map.get(position);

        if (hashMap.get("header") == null) {
            final TransactionModel transactionModel = (TransactionModel) hashMap.get("detail");

            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.textName.setText(CartaHelper.decryptString(transactionModel.getName()));

            if (transactionBottomSheetFragment.getActivity() != null) {
                if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))) {
                    viewHolder.imageCategory.setColorFilter(transactionBottomSheetFragment.getActivity().getResources().getColor(R.color.colorIncomeSoftBlue));
                    viewHolder.imageBackground.setColorFilter(transactionBottomSheetFragment.getActivity().getResources().getColor(R.color.colorIncomeSoftBlue_opacity_50));
                } else {
                    viewHolder.imageCategory.setColorFilter(transactionBottomSheetFragment.getActivity().getResources().getColor(R.color.colorExpenseSoftRed));
                    viewHolder.imageCategory.setColorFilter(transactionBottomSheetFragment.getActivity().getResources().getColor(R.color.colorExpenseSoftRed_opacity_50));
                }

            }

            viewHolder.textDate.setText(CartaHelper.decryptString(transactionModel.getDate()));
            viewHolder.textType.setText(CartaHelper.decryptString(transactionModel.getType()));
            viewHolder.textAmount.setText(NumberFormat.getCurrencyInstance(CartaHelper.idr()).format(Long.valueOf(CartaHelper.decryptString(transactionModel.getAmount()))));

            this.setImageCategory(transactionModel, viewHolder);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("transaction", transactionModel);
                    DetailTransactionBottomSheet detailTransactionBottomSheet = new DetailTransactionBottomSheet();
                    detailTransactionBottomSheet.setArguments(bundle);
                    detailTransactionBottomSheet.show(transactionBottomSheetFragment.getActivity().getSupportFragmentManager(), Constant.TAG.TRANSACTION_ADAPTER);

                }
            });
        } else {
            final Date c = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            final String dateNow = sdf.format(c);
            TransactionAdapter.DateViewHolder dateViewHolder = (TransactionAdapter.DateViewHolder) holder;

            String dataDate = (String) hashMap.get("header");

            if (dataDate.equals(dateNow)) {
                dateViewHolder.textDate.setText("Today");
            } else {
                dateViewHolder.textDate.setText((String) hashMap.get("header"));
            }

        }
    }

    private void setImageCategory(TransactionModel transactionModel, ViewHolder holder) {
        Resources resources = transactionBottomSheetFragment.getActivity().getResources();

        switch (CartaHelper.decryptString(transactionModel.getCategory())) {
            case Constant.CODE.FOOD_AND_DRINK:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_food_drink));
                break;
            case Constant.CODE.BILLS:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_bill));
                break;
            case Constant.CODE.SHOPPING:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_shopping));
                break;
            case Constant.CODE.TRANSPORTATION:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_transportation));
                break;
            case Constant.CODE.ELECTRONICS:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_electronics));
                break;
            case Constant.CODE.HEALTH:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_health));
                break;
            case Constant.CODE.EDUCATION:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_education));
                break;
            case Constant.CODE.OFFICE:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_office));
                break;
            case Constant.CODE.SALARY:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_salary));
                break;
            case Constant.CODE.REWARDS:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_rewards));
                break;
            case Constant.CODE.CASHBACK:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_cashback));
                break;
            case Constant.CODE.INVESTMENT:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_invest));
                break;
            case Constant.CODE.REFUND:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_refund));
                break;
            case Constant.CODE.LOTTERY:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_lottery));
                break;
            default:
                holder.imageCategory.setImageDrawable(resources.getDrawable(R.drawable.ic_more));
                break;
        }
    }


    @Override
    public int getItemCount() {
        return transactionBottomSheetFragment.map.size();
    }

    @Override
    public int getItemViewType(int position) {
        return transactionBottomSheetFragment.map.get(position).get("header") == null ? THIS_DATA : THIS_DATE;
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textAmount;
        private TextView textType;
        private TextView textDate;
        private ImageView imageCategory;
        private ImageView imageBackground;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textAmount = itemView.findViewById(R.id.textAmount);
            this.textName = itemView.findViewById(R.id.textName);
            this.textType = itemView.findViewById(R.id.textType);
            this.textDate = itemView.findViewById(R.id.textDate);
            this.imageCategory = itemView.findViewById(R.id.imageCategory);
            this.imageBackground = itemView.findViewById(R.id.imageBackground);
        }
    }

    private class DateViewHolder extends RecyclerView.ViewHolder {

        TextView textDate;

        private DateViewHolder(@NonNull View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}
