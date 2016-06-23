package mprog.nl.parkeermij.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import mprog.nl.parkeermij.R;

/**
 * Custom LinearLayout used for round edges on google maps
 */
public class RoundedLinearLayout extends LinearLayout {

    private Path mPath;
    private float mCornerRadius;

    public RoundedLinearLayout(Context context) {
        super(context);
        init();
    }

    public RoundedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mCornerRadius = getContext().getResources().
                getDimensionPixelSize(R.dimen.corner_radius); // radius
        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    /**
     * Method where the actual rounding happens
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF r = new RectF(0, 0, w, h);
        mPath = new Path();
        mPath.addRoundRect(r, mCornerRadius, mCornerRadius, Path.Direction.CW);
        mPath.close();
    }
}