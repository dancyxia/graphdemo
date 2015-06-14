package com.dancy.graphdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Random;


/**
 * TODO: document your custom view class.
 */
public class GraphView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Graph graph;
    private int[] sortedNode;
    private Rect[] nodeList;
    private Bitmap bm;
    private int nodeSize;
    private int edgeColor = Color.BLUE; // TODO: use a default from R.color...

//    private Drawable[] mNodeDrawable;

//    private TextPaint mTextPaint;
//    private float mTextWidth;
//    private float mTextHeight;

    public GraphView(Context context, Graph graph) {
        super(context);
        this.graph = graph;
        sortedNode = new int[graph.V()];
        nodeList = new Rect[graph.V()];
        init(null, 0);
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setGraph(Graph g) {
        graph = g;
        sortedNode = new int[g.V()];
        nodeList = new Rect[g.V()];
        bGraphDataReady = false;
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = this.getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.GraphView,0,0);
        Drawable nodeDrawable = a.getDrawable(R.styleable.GraphView_nodeShape);
        edgeColor = a.getColor(R.styleable.GraphView_edgeColor, Color.BLUE);
        nodeSize = nodeDrawable.getIntrinsicWidth();

        bm = Bitmap.createBitmap(nodeSize, nodeSize, Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(bm);
        cv.drawARGB(0, 255, 255, 255);
        nodeDrawable.setBounds(0, 0, nodeSize-1, nodeSize-1);
//        Paint pt = new Paint();
//        pt.setColor(Color.rgb(0,0,255));
//        pt.setAntiAlias(true);
        nodeDrawable.draw(cv);
//        cv.drawCircle(iNodeRadius,iNodeRadius,iNodeRadius, pt);
    }


    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int contentWidth;
    private int contentHeight;
    private boolean bGraphDataReady = false;


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        Paint pt = new Paint();
        pt.setColor(edgeColor);
        if (!bGraphDataReady) {
            Random rand = new Random();
            rand.setSeed(Calendar.getInstance().getTime().getTime());

            // allocations per draw cycle.
            for (int i = 0; i < graph.V(); i++) {
                int x = rand.nextInt(contentWidth - nodeSize);
                int y = rand.nextInt(contentHeight - nodeSize);

                insertNodeAt(new Rect(x, y, x + nodeSize - 1, y + nodeSize - 1), i);
                ColorFilter filter = new LightingColorFilter(0, Color.BLACK);
                pt.setColorFilter(filter);
                canvas.drawBitmap(bm, x, y, pt);
            }
            bGraphDataReady = true;
        } else {
//            if (isDragging) {
//                canvas.save();
//                canvas.clipRect(lastPos.x-iNodeRadius, lastPos.y-iNodeRadius, lastPos.x+iNodeRadius, lastPos.y+iNodeRadius);
//                canvas.clipRect(sortedNode[touchNode].x - iNodeRadius, sortedNode[touchNode].y - iNodeRadius, sortedNode[touchNode].x + iNodeRadius, sortedNode[touchNode].y + iNodeRadius, Region.Op.UNION);
////                canvas.drawBitmap(bm, sortedNode[touchNode].x, sortedNode);
//            }

            for (int node : sortedNode) {
                canvas.drawBitmap(bm, nodeList[node].left, nodeList[node].top, pt);
                for (Graph.Edge edge : graph.getEdges(node)) {
                    canvas.drawLine(nodeList[node].centerX(), nodeList[node].centerY(), nodeList[edge.end].centerX(), nodeList[edge.end].centerY(), pt);
                }
            }

            if (runningNode != null) {
                canvas.drawLine(nodeList[touchNode].centerX(), nodeList[touchNode].centerY(), runningNode.centerX(), runningNode.centerY(), pt);
            }
        }
    }

    private void insertNodeAt(Rect rect, int pos) {
        nodeList[pos] = rect;
        int i = pos;
        while (i > 0 && nodeList[sortedNode[i-1]].left > rect.left) {
            sortedNode[i] = sortedNode[i-1];
            i--;
        }
        sortedNode[i] = pos;
    }

    private int touchNode = -1;
    private int touchedNodeIdx = -1;
    private boolean isDragging = false;
    private Point lastPos = new Point();
    private long lastTimeStamp;

    public enum OPERATION  {
        MOVE_NODE, LINK_NODE
    }

    private OPERATION operationID = OPERATION.LINK_NODE;

    public void setOperationID(OPERATION op) {
        operationID = op;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
            if (operationID == OPERATION.LINK_NODE) {
                return onTouchEventForLink(event);
            } else {
                return onTouchEventForMove(event);
            }
    }

    private Rect runningNode = null;
    private boolean onTouchEventForLink(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isDragging)
                    return true;
                prepareDragging(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isDragging)
                    prepareDragging(event);
                long now = System.currentTimeMillis();
                if (isDragging && now - lastTimeStamp > 60 ) {
                    int cx = (int)event.getX();
                    int cy = (int)event.getY();
                    Rect rect = new Rect(nodeList[touchNode]);
                    runningNode.offset(cx-lastPos.x, cy-lastPos.y);
                    rect.union(runningNode);
                    lastPos.set(cx, cy);
                    invalidate();
                    lastTimeStamp = now;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isDragging)
                    return false;
                int endNodeIdx = GraphViewHelper.getTouchNode(sortedNode, nodeList, new Point((int)event.getX(), (int)event.getY()));
                if (endNodeIdx > -1) {
                    graph.addEdge(touchNode, sortedNode[endNodeIdx]);
                }
                invalidate();
                touchNode = -1;
                touchedNodeIdx = -1;
                runningNode = null;
                isDragging = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void prepareDragging(MotionEvent event) {
        touchedNodeIdx = GraphViewHelper.getTouchNode(sortedNode, nodeList, new Point((int)event.getX(), (int)event.getY()));
        if (touchedNodeIdx > -1) {
            touchNode = sortedNode[touchedNodeIdx];
            lastPos = new Point((int)event.getX(), (int)event.getY());
            lastTimeStamp = System.currentTimeMillis();
            isDragging = true;
            runningNode = new Rect(nodeList[touchNode]);
        }
    }

    private boolean onTouchEventForMove(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isDragging)
                    return true;
                prepareDraggingForNodeMove(event);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (!isDragging)
                    prepareDraggingForNodeMove(event);
                else {
                    long now = System.currentTimeMillis();
                    if (isDragging && now - lastTimeStamp > 60) {
                        int cx = (int) event.getX();
                        int cy = (int) event.getY();
                        Rect rect = new Rect(nodeList[touchNode]);
                        nodeList[touchNode].offset(cx - lastPos.x, cy - lastPos.y);
                        touchedNodeIdx = GraphViewHelper.resortNodeFor(sortedNode, nodeList, touchedNodeIdx);
                        rect.union(nodeList[touchNode]);
                        for (Graph.Edge e : graph.getEdges(touchNode)) {
                            rect.union(nodeList[e.end]);
                        }
                        lastPos.set(cx, cy);
                        invalidate(rect);
                        lastTimeStamp = now;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                touchNode = -1;
                touchedNodeIdx = -1;
                isDragging = false;
                break;
            default:
                break;
        }
        return true;
    }

    private void prepareDraggingForNodeMove(MotionEvent event){
        touchedNodeIdx = GraphViewHelper.getTouchNode(sortedNode, nodeList, new Point((int)event.getX(), (int)event.getY()));
        if (touchedNodeIdx > -1) {
            touchNode = sortedNode[touchedNodeIdx];
            lastPos = new Point((int)event.getX(), (int)event.getY());
            lastTimeStamp = System.currentTimeMillis();
            isDragging = true;
        }
    }
}
