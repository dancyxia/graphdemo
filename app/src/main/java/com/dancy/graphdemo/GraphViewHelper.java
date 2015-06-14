package com.dancy.graphdemo;

import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by dancy on 6/10/2015.
 */
public class GraphViewHelper {
    public static int resortNodeFor(int[] sortedNode, Rect[] nodeList, int nodeIdx) {
        int other = nodeIdx-1;
        int toCompare = nodeList[sortedNode[nodeIdx]].left;
        boolean sorted = false;
        while(other >= 0 && nodeList[sortedNode[other]].left > toCompare) {
            sorted = true;
            swap(other, other+1, sortedNode);
            other--;
        }
        if (sorted)
            return other+1;
        other = nodeIdx+1;
        while(other < sortedNode.length && nodeList[sortedNode[other]].left < toCompare) {
            sorted = true;
            swap(other, other-1, sortedNode);
            other++;
        }
        return other-1;
    }

    private static void swap(int v, int w, int[] sortedNode) {
        int t = sortedNode[v];
        sortedNode[v] = sortedNode[w];
        sortedNode[w] = t;
    }

    public static int getTouchNode(int[] sortedNode, Rect[] nodeList, Point p) {
        int low = 0;
        int high = sortedNode.length-1;
        return searchNode(p, low, high, sortedNode, nodeList);
    }

    private static int searchNode(Point p,int low, int high, int[] sortedNode, Rect[] nodeList) {
        int mid = (high+low)>>1;
        int idx = mid;
        boolean hit = false;

        while (idx >= low && p.x >= nodeList[sortedNode[idx]].left && p.x <= nodeList[sortedNode[idx]].right) {
//            Log.d("Helper", "x = " + x + ",sortedNode[idx].point.x-radius: " + (sortedNode[idx].point.x - radius) + ", sortedNode[idx].point.x+radius: " + (sortedNode[idx].point.x + radius));
            hit = true;
            if (p.y >= nodeList[sortedNode[idx]].top && p.y <= nodeList[sortedNode[idx]].bottom)
                return idx;
            idx--;
        }
        idx = mid+1;
        while (idx <= high && p.x >= nodeList[sortedNode[idx]].left && p.x <= nodeList[sortedNode[idx]].right) {
            hit = true;
            if (p.y >= nodeList[sortedNode[idx]].top && p.y <= nodeList[sortedNode[idx]].bottom)
                return idx;
            idx++;
        }

        if (hit || low >= high) return -1;

        //search left side
        if (p.x < nodeList[sortedNode[mid]].left) return searchNode(p, low, mid-1, sortedNode, nodeList);
        else return searchNode(p, mid+1, high, sortedNode, nodeList);
    }
}
