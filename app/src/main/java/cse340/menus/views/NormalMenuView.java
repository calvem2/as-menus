package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.text.Layout;
import android.view.ViewGroup;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public class NormalMenuView extends MenuExperimentView {

    /** Class constant used to determine the size of the normal menu */
    private static final float CELL_HEIGHT_RATIO = 0.104f;
    private static final float CELL_WIDTH_RATIO = 0.277f;
    private static final float TEXT_OFFSET_RATIO = 0.055f;

    /**
     * The height of each menu cell, in pixels. This is set to (CELL_HEIGHT_RATIO) * the device's
     * smaller dimension.
     */
    private float CELL_HEIGHT;

    /**
     * The width of each menu cell, in pixels. This is set to (CELL_WIDTH_RATIO) * the device's
     * smaller dimension.
     */
    private float CELL_WIDTH;

    /**
     * When adding text to your menu cells, TEXT_OFFSET should be added to both the X and Y
     * coordinates of the menu cell. This will ensure that text is "contained" by the menu.
     * For experimentation, try leaving this property off when drawing your menus.
     */
    private float TEXT_OFFSET;

    /** Offset from edges of container where menu starts */
    private float MENU_OFFSET;


    public NormalMenuView(Context context, List<String> items) {
        super(context, items);
    }

    public NormalMenuView(Context context, ExperimentTrial trial) {
        super(context, trial);
    }

    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    @Override
    protected void setup() {
        // TODO: set initial state to START
        mState = State.START;

        // Determine the dimensions of the normal menu
        CELL_HEIGHT = CELL_HEIGHT_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        CELL_WIDTH = CELL_WIDTH_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        TEXT_OFFSET = TEXT_OFFSET_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);

        // TODO: set layout parameters with proper width and height
        this.setLayoutParams(new ViewGroup.LayoutParams((int) (CELL_WIDTH + getHighlightPaint().getStrokeWidth()),
                (int) (getItems().size() * CELL_HEIGHT + getHighlightPaint().getStrokeWidth())));

        // TODO: initialize any fields you need to (you may create whatever you need)
        MENU_OFFSET = getHighlightPaint().getStrokeWidth() / 2f;

    }

    /**
     * Calculates the index of the menu item using the current finger position
     * This is specific to your menu's geometry, so override it in your Pie and Normal menu classes
     * If the finger has moved less than MIN_DIST, or is outside the bounds of the menu,
     * return -1.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    protected int essentialGeometry(PointF p) {
        /*
         * TODO: Complete the essentialGeometry function for the normal menu.
         * Remember: you should not be altering the state of your application in this function --
         * you should only return the result.
         */
        if (getDistance(p) >= MIN_DIST && 0 <= p.x && p.x <= getWidth() && MENU_OFFSET <= p.y && p.y <= getHeight() - MENU_OFFSET) {
            return (int) (p.y  / (CELL_HEIGHT + getBorderPaint().getStrokeWidth()));
        }

        return -1;  // Temporary
    }

    /**
     * This must be menu specific so override it in your menu class for Pie, Normal, & Custom menus
     * In either case, you can assume (0,0) is the place the user clicked when you are drawing.
     *
     * @param canvas Canvas to draw on.
     */
    @Override
    protected void onDraw(Canvas canvas) {

        /*
         * TODO: Draw the menu.
         * If an option is currently selected, that option should be highlighted.
         *
         * You may change the paint properties for the menu if desired.
         * You can also choose to draw the text horizontally instead of vertically.
         */
        for (int i = 0; i < getItems().size(); i++) {
            canvas.drawRect(MENU_OFFSET, i * CELL_HEIGHT + MENU_OFFSET, CELL_WIDTH, i * CELL_HEIGHT + CELL_HEIGHT + MENU_OFFSET, getBorderPaint());
            canvas.drawText(getItems().get(i), TEXT_OFFSET, i * CELL_HEIGHT + TEXT_OFFSET, getTextPaint());
        }
        if (getCurrentIndex() != -1) {
            canvas.drawRect(MENU_OFFSET, getCurrentIndex() * CELL_HEIGHT + MENU_OFFSET, CELL_WIDTH, getCurrentIndex() * CELL_HEIGHT + CELL_HEIGHT + MENU_OFFSET, getHighlightPaint());
        }
    }
}
