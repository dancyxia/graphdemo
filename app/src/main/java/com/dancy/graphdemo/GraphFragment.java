package com.dancy.graphdemo;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/**
 * A placeholder fragment containing a simple view.
 */
public class GraphFragment extends android.support.v4.app.Fragment {
    public final static String ARG_SORTEDNODE = "SORTEDNODE";
    public final static String ARG_NODELIST = "NODELIST";
    public final static String ARG_GRAPH    = "GRAPHOBJ";
    public final static String ARG_CONTENTWIDTH = "CONTENTWIDTH";
    public final static String ARG_CONTENTHEIGHT = "CONTENTHEIGHT";

    private static GraphFragment f;
//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphFragment getInstance(int count) {
        if (f == null) {
            initFragment(count);
        }
        return f;
    }

    private static void initFragment(int count) {
        f = new GraphFragment();
        Bundle args = new Bundle();
        Graph graph = new Graph(count);
        Log.d("GraphFragment", "put graph");
        args.putSerializable(ARG_GRAPH, graph);
        f.setArguments(args);
    }

    public GraphFragment() {
        setRetainInstance(false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("GraphFragment", "onCreate is called");
    }

    public void setMoveOperation(GraphView.OPERATION op) {
        GraphView view = (GraphView)getView().findViewById(R.id.graphview);
        view.setOperationID(op);
    }

    public void setNodeCount(int count) {
        Graph graph = new Graph(count);
        getArguments().putSerializable(ARG_GRAPH, graph);

        GraphView view = (GraphView)getView().findViewById(R.id.graphview);
        view.setGraph(graph);
        view.reset();
        view.invalidate();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("GragphFragment", "onPause is called");
        Bundle args = getArguments();
        saveViewState(args);
    }

    private void saveViewState(Bundle args) {
        GraphView view = (GraphView)getView().findViewById(R.id.graphview);
        args.putIntArray(ARG_SORTEDNODE, view.getSortedNode());
        args.putParcelableArray(ARG_NODELIST, view.getNodeList());
        args.putSerializable(ARG_GRAPH, view.getGraph());
        args.putInt(ARG_CONTENTWIDTH, view.getContentWidth());
        args.putInt(ARG_CONTENTHEIGHT, view.getContentHeight());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.graph_view, container, false);
        GraphView view = (GraphView)rootView.findViewById(R.id.graphview);
        Bundle args;
        if (savedInstanceState != null) {
            //try get args from savedInstanceState first, this is when fragement is recreated
            args = savedInstanceState;
        } else {
            //otherwise get args from bundle.
            args = getArguments();
        }

        if (args != null) {
            view.setGraph((Graph) args.getSerializable(ARG_GRAPH));
            if (!args.containsKey(ARG_NODELIST) || !args.containsKey(ARG_SORTEDNODE)) {
                view.reset();
            } else {
                Parcelable[] nodeList = args.getParcelableArray(ARG_NODELIST);
                //don't cast Parcelable[] to Rect[] directly. This is for type safety.
                view.setNodeList(toTypedList(nodeList).toArray(new Rect[nodeList.length]));
                view.setSortedNode(args.getIntArray(ARG_SORTEDNODE));
                view.setContentWidth(args.getInt(ARG_CONTENTWIDTH));
                view.setContentHeight(args.getInt(ARG_CONTENTHEIGHT));
            }
         } else {
            view.setGraph(new Graph(Graph.DEFAULT_COUNT));
        }
        return rootView;
    }

    private <T> List<T> toTypedList(Parcelable[] ori) {
        List<T> dest = new ArrayList<T>();
        for (Parcelable item : ori) {
            dest.add((T)item);
        }
        return dest;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("GraphFragment", "onSaveInstanceState is called");
        saveViewState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("GragphFragment", "onStop is called");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("GragphFragment", "onDestroyView is called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("GragphFragment", "onResume is called");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("GragphFragment", "onConfigurationChanged is called");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("GragphFragment", "onStart is called");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("GragphFragment", "onActivityCreated is called");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("GragphFragment", "onViewCreated is called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("GragphFragment", "onDestroy is called");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("GragphFragment", "onAttach is called");
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("GragphFragment", "onDetach is called");
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
}
