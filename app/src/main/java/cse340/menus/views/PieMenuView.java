package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.widget.FrameLayout;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public class PieMenuView extends MenuExperimentView {

    /** Class constant used to determine the size of the pie menu */
    private static final float RADIUS_RATIO = 0.347f;

    /** Actual radius of the pie menu once determined by the display metrics */
    private int RADIUS;

    public PieMenuView(Context context, List<String> items) {
        super(context, items);
    }
    // Call super constructor
    public PieMenuView(Context context, ExperimentTrial trial) {
        super(context, trial);
    }

    /**
     * Method that will be called from the constructor to complete any set up for the view.
     * Calls the parent class setup method for initialization common to all menus
     */
    @Override
    protected void setup() {
        // TODO: set initial state to START
        // Determine the radius of the pie menu
        RADIUS = (int) (RADIUS_RATIO * Math.min(mDisplayMetrics.widthPixels,
                mDisplayMetrics.heightPixels));

        // TODO: set layout parameters with proper width and height
        // TODO: initialize any fields you need to (you may create whatever you need)

    }

    /**
     * Start the menu selection by recording the starting point and starting
     * a trial (if in experiment mode).
     * @param point The current position of the mouse
     */
    @Override
    protected void startSelection(PointF point) {
        // TODO change the x/y location of this menu so it is centered on the cursor

        // let the parent handle other standard stuff
        super.startSelection(point);
    }

    /**
     * Calculates the index of the menu item using the current finger position
     * If the finger has moved less than MIN_DIST, return -1.
     *
     * Pie Menus have infinite width, so you should not return -1 if the finger leaves the
     * confines of the menu.
     *
     * Angle for the Pie Menu is 0 degrees at North. It increases in the clockwise direction.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    @Override
    protected int essentialGeometry(PointF p) {
        /*
         * TODO: Complete the essentialGeometry function for the pie menu.
         * Remember: you should not be altering the state of your application in this function --
         * you should only return the result.
         *
         * Hint: Just as in color picker, you should look to the atan function for your pie menu’s essentialGeometry function.
         */
        return -1; // Temporary
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
         * TODO: Draw the options as sectors of a circle.
         * If an option is currently selected, that option should be highlighted.
         *
         * You may change the paint properties for the menu if desired.
         * You can also choose to draw the text horizontally instead of vertically.
         *
         * Hint: drawArc will draw a pizza-pie shaped arc, so you can do things like highlight a menu item with a single method call.
         * Hint: You will need a rotational offset to ensure the top menu item is at the top of the pie (both when drawing and in essential geometry)
         * because angle is traditionally measured from cardinal east. You can add this in radians before converting from angle to index.
         * Hint: Your pie menu text does not need to be centered – as long as it is contained within the outer ring of the pie menu, you are fine.
         */
    }
}
