package ru.protasov_dev.letmecodeinterviewtask.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //Храним фрагменты
    private final List<Fragment> mFragmentList = new ArrayList<>();
    //Храним заголовки к фрагментам
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //Конструктор
    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    //Метод для получения фрагмента
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    //Метод для получения количества фрагментов
    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //Метод для получения заголовка
    @Override
    public CharSequence getPageTitle(int position){
        return mFragmentTitleList.get(position);
    }

    //Метод для добавления фрагмента и его заголовка
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
}
