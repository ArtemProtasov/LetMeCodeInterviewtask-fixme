package ru.protasov_dev.letmecodeinterviewtask.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import ru.protasov_dev.letmecodeinterviewtask.Fragments.CriticsFragment;
import ru.protasov_dev.letmecodeinterviewtask.Fragments.CriticsFragmentV2;
import ru.protasov_dev.letmecodeinterviewtask.Fragments.ReviewesFragmentV2;
import ru.protasov_dev.letmecodeinterviewtask.R;
import ru.protasov_dev.letmecodeinterviewtask.Fragments.ReviewesFragment;
import ru.protasov_dev.letmecodeinterviewtask.Adapters.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Устанавливаем наш кастомный тулбар
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager();

        TabLayout tabLayout = findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReviewesFragmentV2(), "Reviewes"); //Добавляем фрагмент ReviewesFragment
        adapter.addFragment(new CriticsFragmentV2(), "Critics");
        //adapter.addFragment(new CriticsFragment(), "Critics"); //и CriticsFragment
        viewPager.setAdapter(adapter);
    }
}
