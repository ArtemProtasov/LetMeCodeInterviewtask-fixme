package ru.protasov_dev.letmecodeinterviewtask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import ru.protasov_dev.letmecodeinterviewtask.ParseTaskManagers.PostModelCritics.Result;

public class CriticsFragment extends Fragment implements CriticsListAdapter.CriticsListener{

    private EditText editTextSearchByName;
    private CriticsListAdapter criticsListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_critics);
        editTextSearchByName = view.findViewById(R.id.criticName);

        //ImageButton
    }

    private void getData(){

    }

    @Override
    public void onCriticsItemClick(Result item) {

    }
}
