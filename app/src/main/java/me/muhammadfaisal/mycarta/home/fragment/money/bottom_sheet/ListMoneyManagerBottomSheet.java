package me.muhammadfaisal.mycarta.home.fragment.money.bottom_sheet;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.fragment.money.adapter.MoneyManagerAdapter;
import me.muhammadfaisal.mycarta.home.fragment.money.model.MoneyManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListMoneyManagerBottomSheet extends Fragment {

    ProgressBar progressBar;
    RecyclerView recyclerView;

    MoneyManagerAdapter adapter;
    ArrayList<MoneyManager> moneyManagers  = new ArrayList<>();
    LinearLayoutManager manager;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;
    FirebaseDatabase database;

    public ListMoneyManagerBottomSheet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.bottom_sheet_fragment_list_money_manager, container, false);

        initWidget(v);

        return v;
    }

    private void initWidget(View v) {
        progressBar = v.findViewById(R.id.progressBar);
        recyclerView = v.findViewById(R.id.recyclerMoney);

        adapter = new MoneyManagerAdapter(moneyManagers,getActivity(), null);

        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        ImageView icClose = v.findViewById(R.id.icClose);
    }

    private void initFirebase() {

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        final String userId = auth.getUid();

        reference.child("money_manager").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                methodGettingData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void methodGettingData(DataSnapshot dataSnapshot) {
        moneyManagers.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

            MoneyManager moneyManager = snapshot.getValue(MoneyManager.class);
            moneyManager.setKey(snapshot.getKey());

            moneyManagers.add(moneyManager);
        }

        ItemTouchHelper itemTouchHelper =new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                Toast.makeText(getActivity(), "Moved", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                try {
                    reference.child("money_manager").child(user.getUid()).child(moneyManagers.get(viewHolder.getAdapterPosition()).getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getActivity(), "Success to Delete!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

}
