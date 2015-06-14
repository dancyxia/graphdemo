package com.dancy.graphdemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.dancy.graphdemo.pref.SettingActivity;
import com.dancy.graphdemo.pref.SettingsFragment;


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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class GraphFragment extends Fragment {
        public final static String GraphNodeCount = "count";
        public final static String OperationID = "opid";
        private static GraphFragment f;
        private Graph graph;

        public static GraphFragment getInstance() {
            if (f == null) {
                initFragment(Graph.DEFAULT_COUNT);
            }
            return f;
        }

        public static GraphFragment getInstance(int count) {
            if (f == null) {
                initFragment(count);
            } else {
                regenerateGraph(f);
            }
            return f;
        }

        private static void initFragment(int count) {
            f = new GraphFragment();
            Bundle args = new Bundle();
            args.putInt(GraphNodeCount, count);
            f.setArguments(args);
            f.graph = new Graph(count);
        }

        private static void regenerateGraph(GraphFragment fragment) {
            int count = fragment.getArguments().getInt(GraphNodeCount, Graph.DEFAULT_COUNT);
            fragment.graph = new Graph(count);
            GraphView view = (GraphView)fragment.getView().findViewById(R.id.graphview);
            view.setGraph(fragment.graph);
            view.invalidate();
        }

        public void setMoveOperation(GraphView.OPERATION op) {
            GraphView view = (GraphView)getView().findViewById(R.id.graphview);
            view.setOperationID(op);
        }

        public void setNodeCount(int count) {
            getArguments().putInt(GraphNodeCount, count);
            regenerateGraph(this);
        }

        public int getNodeCount() {
            return getArguments().getInt(GraphNodeCount, Graph.DEFAULT_COUNT);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView =inflater.inflate(R.layout.graph_view, container, false);
            GraphView view = (GraphView)rootView.findViewById(R.id.graphview);
            graph = new Graph(getNodeCount());
            view.setGraph(graph);
            return rootView;
        }
    }
}
