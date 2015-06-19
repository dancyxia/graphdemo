package com.dancy.graphdemo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by dancy on 6/8/2015.
 */

public class Graph implements Serializable {


    public enum DIRECT {DIRECTED, UNDIRECTED};
    private final List<Edge>[] edges;
    private DIRECT direct;

    public final static int DEFAULT_COUNT = 10;
    public final static int MINNODENUM = 2;
    public final static int MAXNODENUM = 50;
//    private static final long serialVersionUID = ;

    public Graph() {
        this(DEFAULT_COUNT, DIRECT.UNDIRECTED);
    }

    public Graph(int size) {
        this(size, DIRECT.UNDIRECTED);
    }

    public Graph(int size, DIRECT direct) {
        edges = (List<Edge>[]) new LinkedList[size];
        for (int i = 0; i < edges.length; i++)
            edges[i] = new LinkedList<Edge>();
        this.direct = direct;
    }

    public int V() {
        return edges.length;
    }

    public int degree(int v) {
        validateNode(v);
        return edges[v].size();
    }

    public List<Edge> getEdges(int v) {
        return edges[v];
    }

    public void addEdge(int v, int w) {
        validateNode(v);
        validateNode(w);
        edges[v].add(new Edge(w));
        if (direct == DIRECT.UNDIRECTED)
            edges[w].add(new Edge(v));
    }

    private void validateNode(int v) {
        if (v >= edges.length)
            throw new IndexOutOfBoundsException(String.format("vertex %d is not between %d and %d %n", v, 0, edges.length - 1));
    }

    public static class Edge implements Serializable {
        int end;
        int weight;

        public Edge(int end, int w) {
            this.end = end;
            this.weight = w;
        }
        public Edge(int end) {
            this(end, 0);
        }
    }
}
