package me.muhammadfaisal.mycarta.v1.home.fragment.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.register.model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    TextView textName, textGreeting, textTotalCardSaved, textAwesomeCards, textTotalTransaction, textAwesomeTransaction, textQuote;

    Random random;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference reference;

    CardView cardReportBug, cardOpenTicket;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        initWidget(v);

        getNameUser();

        getQuotes();

        setGreetingMessage();

        getTotalCardSaved();

        getTotalTransaction();

        return v;
    }

    private void getQuotes() {
        final String[] quotes = getResources().getStringArray(R.array.quotes);

        random = new Random();

        String randomQuotes = quotes[random.nextInt(quotes.length)];

        textQuote.setText(randomQuotes);
    }

    private void getTotalTransaction() {
        reference.child("money_manager").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String textTransacion = String.valueOf(dataSnapshot.getChildrenCount());

                textTotalTransaction.setText(textTransacion);

                if (dataSnapshot.getChildrenCount() == 0){
                    textAwesomeTransaction.setText("looks, no anything here!");
                }else if (dataSnapshot.getChildrenCount() <= 25){
                    textAwesomeTransaction.setText("Very Thrifty");
                }else if(dataSnapshot.getChildrenCount() <= 52){
                    textAwesomeTransaction.setText("Thrifty");
                }else{
                    textAwesomeTransaction.setText("Your'e Bussinessman!!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTotalCardSaved() {
        reference.child("card").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String totalCard = String.valueOf(dataSnapshot.getChildrenCount());

                textTotalCardSaved.setText(totalCard);

                if (dataSnapshot.getChildrenCount() == 0){
                    textAwesomeCards.setText("looks, no anything here!");
                }else if (dataSnapshot.getChildrenCount() <= 5){
                    textAwesomeCards.setText("Junior Cartaver!");
                }else if(dataSnapshot.getChildrenCount() <= 10){
                    textAwesomeCards.setText("Senior Cartaver!");
                }else{
                    textAwesomeCards.setText("Awesome Cartaver!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setGreetingMessage() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            textGreeting.setText("Good Morning,");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            textGreeting.setText("Good Afternoon,");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            textGreeting.setText("Good Evening,");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            textGreeting.setText("Good Night,");
        }
    }

    private void getNameUser() {
        reference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel users = dataSnapshot.getValue(UserModel.class);

                textName.setText(users.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initWidget(View v) {
        textGreeting = v.findViewById(R.id.textGreeting);
        textName = v.findViewById(R.id.textName);
        textTotalCardSaved = v.findViewById(R.id.textTotalCardSaved);
        textAwesomeCards = v.findViewById(R.id.textAwesomeCards);
        textTotalTransaction = v.findViewById(R.id.textTotalTransaction);
        textAwesomeTransaction = v.findViewById(R.id.textAwesomeTransaction);
        textQuote = v.findViewById(R.id.textQuote);

        cardReportBug = v.findViewById(R.id.cardReportBugs);
        cardOpenTicket = v.findViewById(R.id.cardOpenTickets);

        //Firebase
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        cardReportBug.setOnClickListener(this);
        cardOpenTicket.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == cardReportBug){
            reportBug();
        }else{
            openTicket();
        }
    }

    private void openTicket() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"muhammadfaisalr17@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Bug on MyCarta APP");
        i.putExtra(Intent.EXTRA_TEXT, "Explain the Bugs here!");
        i.setType("message/rfc822");
        startActivity(Intent.createChooser(i, "Choose an email client"));
    }

    private void reportBug() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"muhammadfaisalr17@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "I have a [problem / questions] about MyCarta APP");
        i.putExtra(Intent.EXTRA_TEXT, "Explain your question here!");
        i.setType("message/rfc822");
        startActivity(Intent.createChooser(i, "Choose an email client"));
    }
}
