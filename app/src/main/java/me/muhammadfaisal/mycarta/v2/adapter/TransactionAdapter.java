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

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v1.home.fragment.money.bottom_sheet.DetailMoneyManagerBottomSheet;
import me.muhammadfaisal.mycarta.v2.bottomsheet.TransactionBottomSheetFragment;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.TransactionModel;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private ArrayList<TransactionModel> transactionModels;
    private TransactionBottomSheetFragment transactionBottomSheetFragment;

    public TransactionAdapter(ArrayList<TransactionModel> transactionModels, TransactionBottomSheetFragment transactionBottomSheetFragment) {
        this.transactionModels = transactionModels;
        this.transactionBottomSheetFragment = transactionBottomSheetFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_manager, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final TransactionModel transactionModel = transactionModels.get(position);

        holder.textName.setText(CartaHelper.decryptString(transactionModel.getName()));

        if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))){
            holder.textAmount.setTextColor(transactionBottomSheetFragment.getActivity().getResources().getColor(R.color.colorIncome));
        }else{
            holder.textAmount.setTextColor(transactionBottomSheetFragment.getActivity().getResources().getColor(R.color.colorExpense));
        }

        holder.textAmount.setText(NumberFormat.getCurrencyInstance(CartaHelper.idr()).format(Long.valueOf(CartaHelper.decryptString(transactionModel.getAmount()))));

        this.setImageCategory(transactionModel, holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("transaction", transactionModel);
                DetailMoneyManagerBottomSheet detailMoneyManagerBottomSheet = new DetailMoneyManagerBottomSheet();
                detailMoneyManagerBottomSheet.setArguments(bundle);
                detailMoneyManagerBottomSheet.show(transactionBottomSheetFragment.getActivity().getSupportFragmentManager(), Constant.TAG.TRANSACTION_ADAPTER);

            }
        });
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
        return transactionModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textAmount;
        private ImageView imageCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textAmount = itemView.findViewById(R.id.textIncomeOrExpense);
            this.textName = itemView.findViewById(R.id.textName);
            this.imageCategory = itemView.findViewById(R.id.imageCategory);
        }
    }
}
