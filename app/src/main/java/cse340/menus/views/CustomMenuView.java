package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public class CustomMenuView extends MenuExperimentView {

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
        return 0;
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
    }
}
