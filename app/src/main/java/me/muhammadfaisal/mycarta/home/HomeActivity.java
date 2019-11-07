package me.muhammadfaisal.mycarta.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.adapter.ViewPagerAdapter;
import me.muhammadfaisal.mycarta.home.fragment.account.AccountFragment;
import me.muhammadfaisal.mycarta.home.fragment.card.CardFragment;
import me.muhammadfaisal.mycarta.home.fragment.home.HomeFragment;
import me.muhammadfaisal.mycarta.home.fragment.money.MoneyManagerFragment;
import me.muhammadfaisal.mycarta.home.fragment.article.ArticleFragment;

public class HomeActivity extends AppCompatActivity {
    ViewPager viewPager;
    AccountFragment accountFragment;
    CardFragment cardFragment;
    ArticleFragment articleFragment;
    HomeFragment homeFragment;
    MoneyManagerFragment moneyManagerFragment;

    BubbleNavigationLinearView bottomLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();

        bottomLinear = findViewById(R.id.bottomLinear);
        viewPager = findViewById(R.id.viewPager);

        bottomNavigationBar();

        setupViewPager(viewPager);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            SweetAlertDialog warning = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            warning.setTitle("Close App");
            warning.setContentText("Are you sure want to quit MyCarta App?");
            warning.setConfirmButton("Quit", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                    System.exit(0);
                }
            });
            warning.setCancelButton("No", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                }
            });
            warning.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = new HomeFragment();
        cardFragment = new CardFragment();
        moneyManagerFragment = new MoneyManagerFragment();
        articleFragment = new ArticleFragment();
        accountFragment = new AccountFragment();

        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(cardFragment);
        viewPagerAdapter.addFragment(moneyManagerFragment);
        viewPagerAdapter.addFragment(articleFragment);
        viewPagerAdapter.addFragment(accountFragment);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomLinear.setCurrentActiveItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void bottomNavigationBar() {
        bottomLinear.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                viewPager.setCurrentItem(position);
            }
        });
    }
}
