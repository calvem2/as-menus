package cse340.menus.views;

import android.content.Context;
import android.graphics.PointF;
import android.view.MotionEvent;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

public abstract class MenuExperimentView extends AbstractMenuExperimentView {

    /**
     * Constructor
     *
     * @param context
     * @param trial Experiment trial (contains a list of items)
     */
    public MenuExperimentView(Context context, ExperimentTrial trial) {
        super(context, trial);
//        getHighlightPaint().setStrokeWidth(3);
    }

    /**
     * Constructor
     *
     * @param context
     * @param items Items to display in menu
     */
    public MenuExperimentView(Context context, List<String> items) {
        super(context, items);
//        getHighlightPaint().setStrokeWidth(3);
    }

    /**
     * Calculates the index of the menu item using the current finger position
     * This is specific to your menu's geometry, so override it in your Pie and Normal and Custom menu classes.
     *
     * Note that you should not be altering your menu's state within essentialGeometry.
     * This function should return a value to your touch event handler, and nothing more.
     *
     * @param p the current location of the user's finger relative to the menu's (0,0).
     * @return the index of the menu item under the user's finger or -1 if none.
     */
    protected abstract int essentialGeometry(PointF p);

    /***
     * Handles user's touch input on the screen. It should follow the state machine specified
     * in the spec.
     *
     * @param event Event for touch.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int menuItem = essentialGeometry(event);

        /*
         * TODO: Implement the state machine for all of your views.
         * All of the state logic should be handled here, you won't need to change
         * this for Pie, Normal, and Custom menu to work. Use the state machine defined in the spec for reference.
         *
         * Below is the template for the state machine. You should use the state field to
         * fetch the menu's current state, and process it accordingly.
         */


         switch (mState) {
             case START:
                 if (event.getAction() == MotionEvent.ACTION_DOWN) {
                     mState = State.SELECTING;
                     startSelection(new PointF(event.getX(), event.getY()));
                     return true;
                 }
             case SELECTING:
                 if (event.getAction() == MotionEvent.ACTION_MOVE) {
                     updateModel(menuItem);
                     return true;
                 } else if (event.getAction() == MotionEvent.ACTION_UP) {
                     mState = State.START;
                     endSelection(menuItem, new PointF(event.getX(), event.getY()));
                     return true;
                 }
         }
        return false;
    }

    //////////////////////////////////////////////////
    // These methods are taken directly from the spec's description of the PPS
    //////////////////////////////////////////////////

    /**
     * Start the menu selection by recording the starting point and starting
     * a trial (if in experiment mode).
     * @param point The current position of the mouse
     */
    protected void startSelection(PointF point) {
        // TODO: 1) call trial.startTrial() (only if in experiment mode), passing it the position of the mouse
        // TODO: 2) Make this visible

        if (experimentMode()) {
            getTrial().startTrial(point);
            setVisibility(VISIBLE);
        }

    }

    /**
     * Complete the menu selection and record the trial data if necessary
     * @param menuItem the menu item that was selected by the user
     * @param point The current position of the mouse
     */
    protected void endSelection(int menuItem, PointF point) {
        // TODO: 0) Announce the selection using a Toast (or "Nothing Selected" if it is -1)
        // TODO: 1) notify the menu trial listener
        // TODO:    a) call trial.endTrial(), passing it the pointer position and the currently selected item
        // TODO:    b) call onTrialCompleted(trial)
        // TODO: 2) reset state machine
        // think about what might need to be reset here. What fields are used in your state machine?
        String toast = menuItem != -1 ? "selected " + getItem() : "Nothing Selected";
        announce(toast);
        if (experimentMode()) {
            getTrial().endTrial(point, menuItem);
            this.getTrialListener().onTrialCompleted(getTrial());
        }

        setVisibility(INVISIBLE);
    }

    /**
     * Change the model of the menu and force a redraw, if the current selection has changed.
     * @param menuItem the menu item that is currently selected by the user
     */
    protected void updateModel(int menuItem) {
        // TODO: check if the item selected has changed. If so
        // TODO: 1) update your menu's model
        if (getCurrentIndex() != menuItem) {
            setCurrentIndex(menuItem);
            invalidate();
        }
    }
}
