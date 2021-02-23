package cse340.menus.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.List;

import cse340.menus.ExperimentTrial;
import cse340.menus.enums.State;

// Documentation
// drawArc: https://thoughtbot.com/blog/android-canvas-drawarc-method-a-visual-guide
// drawTextOnPath: https://developer.android.com/reference/android/graphics/Canvas
public class PieMenuView extends MenuExperimentView {

    /** Class constants used to determine the size of the pie menu */
    private static final float RADIUS_RATIO = 0.347f;
    private static final float TEXT_OFFSET_RATIO = 0.055f;

    /**
     * Degrees by which to offset each menu item to ensure the first item is at the top of the menu
     * and the rest continue clockwise around the menu
     */
    private static final float DEGREE_OFFSET = -90F;

    /** Actual radius of the pie menu once determined by the display metrics */
    private int RADIUS;

    /** Height of menu item text */
    private float FONT_HEIGHT;

    /** Horizontal offset for menu item text */
    private float TEXT_H_OFFSET;

    /** Offset from edges of container where menu starts */
    private float MENU_OFFSET;

    /** Sweep angle for each menu item */
    private float WEDGE_DEGREES;




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
        mState = State.START;

        // Determine the radius of the pie menu
        RADIUS = (int) (RADIUS_RATIO * Math.min(mDisplayMetrics.widthPixels,
                mDisplayMetrics.heightPixels));

        // TODO: set layout parameters with proper width and FONT_HEIGHT
        float size = RADIUS * 2 + getHighlightPaint().getStrokeWidth();
        this.setLayoutParams(new ViewGroup.LayoutParams((int) size, (int) size));

        // TODO: initialize any fields you need to (you may create whatever you need)
        Paint.FontMetrics fm = getTextPaint().getFontMetrics();
        FONT_HEIGHT = fm.descent - fm.ascent;
        TEXT_H_OFFSET = TEXT_OFFSET_RATIO * Math.min(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        MENU_OFFSET = getHighlightPaint().getStrokeWidth() / 2f;
        WEDGE_DEGREES = 360f / getItems().size();
    }

    /**
     * Start the menu selection by recording the starting point and starting
     * a trial (if in experiment mode).
     * @param point The current position of the mouse
     */
    @Override
    protected void startSelection(PointF point) {
        // TODO change the x/y location of this menu so it is centered on the cursor
        this.setX(getX() - RADIUS - MENU_OFFSET);
        this.setY(getY() - RADIUS - MENU_OFFSET);

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
        if (getDistance(p, new PointF(getWidth() / 2f, getHeight() / 2f)) < MIN_DIST) {
            return -1;
        }

        // adjust x,y and get point on the circle
        float x = -(RADIUS + MENU_OFFSET - p.x);
        float y = RADIUS - MENU_OFFSET - p.y;
        double degrees = -Math.toDegrees(Math.atan2(y, x));

        // adjust point on the circle according to layout of menu
        degrees += -(DEGREE_OFFSET - WEDGE_DEGREES / 2f);
        degrees = degrees < 0 ? degrees + 360 : degrees;

        // get corresponding menu item
        return (int) (degrees / WEDGE_DEGREES);
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
        Path textPath = new Path();
        float startAngle;
        for (int i = 0; i < getItems().size(); i++) {
            startAngle = i * WEDGE_DEGREES - WEDGE_DEGREES / 2f + DEGREE_OFFSET;
            // draw menu circle
            canvas.drawArc(MENU_OFFSET, MENU_OFFSET, getWidth() - MENU_OFFSET, getHeight() - MENU_OFFSET, startAngle, WEDGE_DEGREES, false, getBorderPaint());
            canvas.drawArc(2 * FONT_HEIGHT, 2 * FONT_HEIGHT, getHeight() - 2 * FONT_HEIGHT, getHeight() - 2 * FONT_HEIGHT, startAngle, WEDGE_DEGREES, false, getBorderPaint());

            // draw text
            textPath.reset();
            textPath.addArc(MENU_OFFSET + FONT_HEIGHT, MENU_OFFSET + FONT_HEIGHT, getHeight() - MENU_OFFSET - FONT_HEIGHT, getHeight() - MENU_OFFSET - FONT_HEIGHT, startAngle, WEDGE_DEGREES);
            canvas.drawTextOnPath(getItems().get(i), textPath, TEXT_H_OFFSET, 0, getTextPaint());
        }

        // highlight selection
        if (getCurrentIndex() != -1) {
            startAngle = getCurrentIndex() * WEDGE_DEGREES - WEDGE_DEGREES / 2f + DEGREE_OFFSET;
            canvas.drawArc(MENU_OFFSET, MENU_OFFSET, getWidth() - MENU_OFFSET, getHeight() - MENU_OFFSET, startAngle, WEDGE_DEGREES, true, getHighlightPaint());
        }
    }
}
