package ovh.exception.watchdogzz.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

import ovh.exception.watchdogzz.data.User;
import ovh.exception.watchdogzz.data.UserManager;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDSurfaceView extends GLSurfaceView {

    private WDRenderer mRenderer;
    private UserManager users;

    // position doigt
    private float mPreviousX;
    private float mPreviousY;
    private boolean isZooming;
    private float mSpaceBetweenFingers;
    private int mFirstFinger;
    private int mSecondFinger;

    public WDSurfaceView(Context context) {
        this(context, null);
    }

    public WDSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isZooming = false;
        mSpaceBetweenFingers = 0.0f;
        users = new UserManager();
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        this.mRenderer = (WDRenderer) renderer;
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        users.addObserver(this.mRenderer.getMap());
    }

    public void setRenderer(WDRenderer renderer) {
        super.setRenderer(renderer);
        this.mRenderer = renderer;
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        users.addObserver(this.mRenderer.getMap());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean b = super.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:
                isZooming = isZooming && (event.getPointerCount() > 1);
                if(this.isZooming){
                    float x1 = event.getX(this.mFirstFinger) - event.getX(this.mSecondFinger);
                    float y1 = event.getY(this.mFirstFinger) - event.getY(this.mSecondFinger);
                    float distance = (float) Math.sqrt(x1 * x1 + y1 * y1);
                    this.mRenderer.zoomCamera(this.mSpaceBetweenFingers/(distance+1));
                    this.mSpaceBetweenFingers = distance;
                } else {
                    float x = event.getX();
                    float y = event.getY();

                    float dx = x - this.mPreviousX;
                    float dy = y - this.mPreviousY;
                    mPreviousX = event.getX();
                    mPreviousY = event.getY();

                    this.mRenderer.moveCamera(2 * dx / getWidth(), -2 * dy / getHeight(), 0);
                }

                requestRender();

                break;
            case MotionEvent.ACTION_DOWN:
                this.mFirstFinger = event.getActionIndex();
                mPreviousX = event.getX();
                mPreviousY = event.getY();
                this.isZooming = false;
                b = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(this.isZooming==false) {
                    mSecondFinger = event.getActionIndex();
                    float x1 = event.getX(this.mFirstFinger) - event.getX(this.mSecondFinger);
                    float y1 = event.getY(this.mFirstFinger) - event.getY(this.mSecondFinger);
                    this.mSpaceBetweenFingers = (float) Math.sqrt(x1 * x1 + y1 * y1);
                    this.isZooming = true;
                }
                b = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getActionIndex() == this.mSecondFinger)
                    this.isZooming = false;
                break;
            default:
                break;
        }

        return b;
    }

}
