package com.dancy.graphdemo;

import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dancy.graphdemo.pref.SettingActivity;

public class MainActivity extends ActionBarActivity {
    public static int[] STATE_ACTIVE = {R.attr.state_active};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int nodeSize = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getResources().getString(R.string.pref_graphnodecount), String.valueOf(Graph.DEFAULT_COUNT)));
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.graphfragmentcontainer, GraphFragment.getInstance(nodeSize))
                    .commit();
        }

       final Context context = this;
        Button button = (Button)findViewById(R.id.gengraph);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphFragment fragment = (GraphFragment)getSupportFragmentManager().findFragmentById(R.id.graphfragmentcontainer);
                int num = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getResources().getString(R.string.pref_graphnodecount), String.valueOf(Graph.DEFAULT_COUNT)));
                fragment.setNodeCount(num);
            }
        });

        button = (Button)findViewById(R.id.movenode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphFragment fragment = (GraphFragment)(getSupportFragmentManager().findFragmentById(R.id.graphfragmentcontainer));
                fragment.setMoveOperation(GraphView.OPERATION.MOVE_NODE);
            }
        });

        button = (Button)findViewById(R.id.linknode);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphFragment fragment = (GraphFragment)(getSupportFragmentManager().findFragmentById(R.id.graphfragmentcontainer));
                fragment.setMoveOperation(GraphView.OPERATION.LINK_NODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
