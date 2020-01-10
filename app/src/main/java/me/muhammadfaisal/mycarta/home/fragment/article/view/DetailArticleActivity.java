package me.muhammadfaisal.mycarta.home.fragment.article.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import me.muhammadfaisal.mycarta.R;

public class DetailArticleActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageFeature;
    TextView textSubtitle, textTitle;
    CardView cardShare;

    String shareMessage, shareContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        initView();
    }

    private void initView() {

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String image = getIntent().getStringExtra("image");
        int currentPosition = getIntent().getIntExtra("position", 0);

        int position = currentPosition + 1;

        shareContent = content.substring(0, 125).concat("... Readmore on MyCarta");
        shareMessage = title + "\n" + Html.fromHtml(shareContent) + "\n\n Download MyCarta on Google Play \n\n https://play.google.com/store/apps/details?id="+getPackageName();

        textSubtitle = findViewById(R.id.textContent);
        imageFeature = findViewById(R.id.imageViewCollapsing);
        textTitle = findViewById(R.id.textTitle);
        cardShare = findViewById(R.id.cardShare);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Detail Article | " + position);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textSubtitle.setText(Html.fromHtml(content));
        textTitle.setText(title);
        Glide.with(this)
                .load(image)
                .into(imageFeature);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        cardShare.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == cardShare){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(i);
        }
    }
}
