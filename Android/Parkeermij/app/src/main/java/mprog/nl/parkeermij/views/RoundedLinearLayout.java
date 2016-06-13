package mprog.nl.parkeermij.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import mprog.nl.parkeermij.R;

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
                getDimensionPixelSize(R.dimen.corner_radius);
        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(mPath);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        RectF r = new RectF(0, 0, w, h);
        mPath = new Path();
        mPath.addRoundRect(r, mCornerRadius, mCornerRadius, Path.Direction.CW);
        mPath.close();
    }
}