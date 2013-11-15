package com.zy17.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.zy17.R;
import com.zy17.ResponsiveUIActivity;

public class EngMenuFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String[] birds = getResources().getStringArray(R.array.eng_menu);
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, birds);
        setListAdapter(colorAdapter);
    }

    @Override
    public void onListItemClick(ListView lv, View v, int position, long id) {
        if (position == 1) {
            Intent intent = new Intent(getActivity(), CreateCardActivity.class);
            startActivity(intent);
        }
        Fragment newContent = new CardListFragment(position);
        if (newContent != null)
            switchFragment(newContent);
    }

    // the meat of switching the above fragment
    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;

        if (getActivity() instanceof ResponsiveUIActivity) {
            ResponsiveUIActivity ra = (ResponsiveUIActivity) getActivity();
            ra.switchContent(fragment);
        }
    }
}
