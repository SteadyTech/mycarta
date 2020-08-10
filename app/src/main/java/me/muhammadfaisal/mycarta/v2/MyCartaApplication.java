package me.muhammadfaisal.mycarta.v2;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.realm.Realm;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.firebase.TransactionModel;
import me.muhammadfaisal.mycarta.v2.model.realm.BalanceDatabase;

public class MyCartaApplication extends Application {

    private ArrayList<TransactionModel> transactionModels;

    {
        this.transactionModels = new ArrayList<>();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MainApplication", "onCreate();");

        Realm.init(this.getApplicationContext());

        final Realm realm = Realm.getDefaultInstance();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() != null) {
            Query query = databaseReference.child(Constant.TRANSACTION_PATH).child(auth.getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TransactionModel transactionModel = snapshot.getValue(TransactionModel.class);

                        transactionModels.add(transactionModel);
                    }

                    for (int i = 0; i < transactionModels.size(); i++){
                        TransactionModel transactionModel = transactionModels.get(i);

                        BalanceDatabase balanceDatabase = realm.where(BalanceDatabase.class).equalTo("cardID", transactionModel.getCardNumber()).findFirst();

                        realm.beginTransaction();
                        if (balanceDatabase != null) {
                            if (balanceDatabase.getCardID().equals(transactionModel.getCardNumber())){
                                long income = 0L;
                                long expense = 0L;

                                for (final TransactionModel transactionModel1 : transactionModels) {
                                    if (transactionModel1.getCardNumber().equals(balanceDatabase.getCardID())){
                                        if (transactionModel1.getType().equals(CartaHelper.encryptString(Constant.CODE.INCOME))) {
                                            income += Long.valueOf(CartaHelper.decryptString(transactionModel1.getAmount()));
                                        } else if (transactionModel1.getType().equals(CartaHelper.encryptString(Constant.CODE.EXPENSE))) {
                                            expense += Long.valueOf(CartaHelper.decryptString(transactionModel1.getAmount()));
                                        }
                                        final long balance = income - expense;

                                        Log.d("MainApplication", balanceDatabase.getCardID());
                                        balanceDatabase = new BalanceDatabase();
                                        balanceDatabase.setCardID(transactionModel.getCardNumber());
                                        balanceDatabase.setBalance(String.valueOf(balance));
                                    }
                                }
                            }
                        }else{

                        }
                        if (balanceDatabase != null) {
                            realm.copyToRealmOrUpdate(balanceDatabase);
                        }
                        realm.commitTransaction();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    realm.close();
                }
            });
        }
    }
}
