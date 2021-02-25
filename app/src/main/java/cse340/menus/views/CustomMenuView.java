package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.ViewGroup;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public class CustomMenuView extends MenuExperimentView {
    /** Class constants used to determine the size of the menu */
    private static final float RADIUS_RATIO = 0.347f;
    private static final float SIZE_RATIO = 0.180f;
    private static final float TEXT_OFFSET_RATIO = 0.015f;

    /** Actual radius of the menu once determined by the display metrics */
    private int RADIUS;

    /** Size of each menu item */
    private float ITEM_SIZE;

    /** Horizontal offset for menu item text */
    private float TEXT_H_OFFSET;

    /** Radians between each menu item */
    private double SPACING;

    /** Radians between each menu item */
    private PointF[] MENU_POINTS;

    public CustomMenuView(Context context, ExperimentTrial trial) { super(context, trial); }
    public CustomMenuView(Context context, List<String> items) { super(context, items); }

    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    @Override
    protected void setup() {
        // TODO: set initial state to START
        // TODO: set layout parameters with proper width and height
        // TODO: initialize any fields you need to (you may create whatever you need)
        mState = State.START;

        TEXT_H_OFFSET = TEXT_OFFSET_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        ITEM_SIZE = SIZE_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        RADIUS = (int) (RADIUS_RATIO * Math.min(mDisplayMetrics.widthPixels,
                mDisplayMetrics.heightPixels));
        SPACING = Math.toRadians(360.0 / getItems().size());
        MENU_POINTS = new PointF[getItems().size()];

        // Set layout params
        float size = RADIUS * 2 + ITEM_SIZE + getHighlightPaint().getStrokeWidth();
        this.setLayoutParams(new ViewGroup.LayoutParams((int) size, (int) size));

        // Get points where menu items will be
        float origin = size / 2f;
        for (int i = 0; i < getItems().size(); i++) {
            MENU_POINTS[i] = new PointF(origin + (float) (RADIUS * Math.cos(i * SPACING)),
                    origin - (float) (RADIUS * Math.sin(i * SPACING)));
        }
    }

    /**
     * Start the menu selection by recording the starting point and starting
     * a trial (if in experiment mode).
     * @param point The current position of the mouse
     */
    @Override
    protected void startSelection(PointF point) {
        // TODO change the x/y location of this menu so it is centered on the cursor
        this.setX(getX() - getWidth() / 2f);
        this.setY(getY() - getHeight() / 2f);

        super.startSelection(point);
    }

    /**
     * Calculates the essential geometry for the custom menu.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    protected int essentialGeometry(PointF p) {
        // TODO: implement this
        // Return no selection if finger has moved less than MIN_DIST
        if (getDistance(p, new PointF(getWidth() / 2f, getHeight() / 2f)) < MIN_DIST) {
            return -1;
        }

        // Check to see if pointer is inside any of the menu item containers
        float left, right, top, bottom;
        for (int i = 0; i < MENU_POINTS.length; i++) {
            left = MENU_POINTS[i].x - ITEM_SIZE / 2f;
            right = MENU_POINTS[i].x + ITEM_SIZE / 2f;
            top = MENU_POINTS[i].y - ITEM_SIZE / 2f;
            bottom = MENU_POINTS[i].y + ITEM_SIZE / 2f;

            if (left <= p.x && p.x <= right && top <= p.y && p.y <= bottom) {
                return i;
            }
        }

        return -1;
    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO: implement this
        float x, y;
        for (int i = 0; i < getItems().size(); i++) {
            // get location on menu item
            x = MENU_POINTS[i].x;
            y = MENU_POINTS[i].y;

            canvas.drawRect(x - ITEM_SIZE / 2f, y - ITEM_SIZE / 2f, x + ITEM_SIZE / 2f, y + ITEM_SIZE / 2f, getBorderPaint());
            canvas.drawText(getItems().get(i), x - ITEM_SIZE / 2f + TEXT_H_OFFSET, y, getTextPaint());
        }
        // highlight selection
        if (getCurrentIndex() != -1) {
            x = MENU_POINTS[getCurrentIndex()].x;
            y = MENU_POINTS[getCurrentIndex()].y;
            canvas.drawRect(x - ITEM_SIZE / 2f, y - ITEM_SIZE / 2f, x + ITEM_SIZE / 2f, y + ITEM_SIZE / 2f, getHighlightPaint());
        }
    }
}
