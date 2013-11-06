package com.zy17.ui;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zy17.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams;

public class ListViewLoader extends ListActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data
    ListAdapter mAdapter;

    private List<Map<String, Object>> mData;

    // These are the Contacts rows that we will retrieve
    static final String[] PROJECTION = new String[]{ContactsContract.Data._ID,
            ContactsContract.Data.DISPLAY_NAME};

    // This is the select criteria
    static final String SELECTION = "((" +
            ContactsContract.Data.DISPLAY_NAME + " NOTNULL) AND (" +
            ContactsContract.Data.DISPLAY_NAME + " != '' ))";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a progress bar to display while the list loads
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                Gravity.CENTER));
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);

        // Must add the progress bar to the root of the layout
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = {ContactsContract.Data.DISPLAY_NAME};
        int[] toViews = {android.R.id.text1}; // The TextView in simple_list_item_1

        //生成动态数组，并且转载数据
        ArrayList<HashMap<String, Object>> mylist = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 30; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("EnglishAnswer", "EnglishAnswer");
            Button button = new Button(getApplicationContext());
            button.setText("Button" + i);
            map.put("soundButton", button);
            mylist.add(map);
        }
        //生成适配器，数组===》ListItem
        mAdapter = new SimpleAdapter(this, //没什么解释
                mylist,//数据来源
                R.layout.testlayout,//ListItem的XML实现
                //动态数组与ListItem对应的子项
                new String[]{"EnglishAnswer", "soundButton"},
                //ListItem的XML文件里面的两个TextView ID
                new int[]{R.id.EnglishAnswer, R.id.soundButton}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stubfinal int mPosition = position;

                convertView = super.getView(position, convertView, parent);
                Button soundButton = (Button) convertView
                        .findViewById(R.id.soundButton);// id为你自定义布局中按钮的id
                soundButton.setText("soundButton"+position);
                return convertView;
            }
        };
        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
//        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData());
        mAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                fromColumns, toViews, 0);
//        mAdapter = new MyAdapter(this);
        setListAdapter(mAdapter);


        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getLoaderManager().initLoader(0, null, this);
    }

    // Called when a new Loader needs to be created
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
//        mAdapter.swapCursor(data);
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
//        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }


}