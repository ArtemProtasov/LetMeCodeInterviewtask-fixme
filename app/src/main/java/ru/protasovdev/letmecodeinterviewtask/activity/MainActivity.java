package ru.protasovdev.letmecodeinterviewtask.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.protasovdev.letmecodeinterviewtask.adapters.ViewPagerAdapter;
import ru.protasovdev.letmecodeinterviewtask.fragments.CriticsFragment;
import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasovdev.letmecodeinterviewtask.fragments.ReviewesFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Устанавливаем наш кастомный тулбар
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkConnection();

    }

    private void checkConnection() {
        if (isOnline()) {
            viewPager = findViewById(R.id.viewpager);
            setupViewPager();

            TabLayout tabLayout = findViewById(R.id.tablayout);
            tabLayout.setupWithViewPager(viewPager);
        } else {
            Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.try_again, tryAgainOnClickListener).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReviewesFragment(), "Reviews"); //Фрагмент ReviewesFragmentV2
        adapter.addFragment(new CriticsFragment(), "Critics");  //Фрагмент CriticsFragmentV2
        viewPager.setAdapter(adapter);
    }

    View.OnClickListener tryAgainOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            checkConnection();
        }
    };
}
