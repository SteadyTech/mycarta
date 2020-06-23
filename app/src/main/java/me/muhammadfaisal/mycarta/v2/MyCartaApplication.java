package me.muhammadfaisal.mycarta.v2;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReferenceArray;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.activity.DetailCardActivity;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.TransactionModel;

public class MyCartaApplication extends Application {

    private ArrayList<TransactionModel> transactionModels;

    {
        this.transactionModels = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainApplication", "onCreate();");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() != null) {
            Query query = databaseReference.child(Constant.TRANSACTION_PATH).child(auth.getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        TransactionModel transactionModel = snapshot.getValue(TransactionModel.class);

                        transactionModels.add(transactionModel);
                    }
                    long income = 0L;
                    long expense = 0L;
                    long balance = 0L;
                    for (TransactionModel transactionModel : transactionModels){

                        if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))){
                            income += Long.valueOf(CartaHelper.decryptString(transactionModel.getAmount()));
                        }else if (transactionModel.getType().equals(CartaHelper.encryptString(Constant.CODE.EXPENSE))){
                            expense += Long.valueOf(CartaHelper.decryptString(transactionModel.getAmount()));
                        }

                        balance = income - expense;

                        Log.d("Key-Preference", transactionModel.getCardNumber());
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFERENCE.TRANSACTION, MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putLong(transactionModel.getCardNumber(), balance);
                        editor.apply();

                        Log.d("Sum Income", String.valueOf(income));
                        Log.d("Sum Expense", String.valueOf(expense));
                        Log.d("Sum Expense", String.valueOf(balance));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
