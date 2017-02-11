package ovh.exception.watchdogzz.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by begarco on 19/11/2016.
 */

public class WDSurfaceView extends GLSurfaceView implements Observer {

    private WDRenderer mRenderer;

    // position doigt
    private float mPreviousX;           // memoire x
    private float mPreviousY;           // memoire y
    private FingerAction mState;        // action en cours
    private float mSpaceBetweenFingers; // espace entre les doigts
    private float mScale;        // echelle de zoom

    private enum FingerAction {
        NONE,
        IS_MOVING,
        IS_ZOOMING
    }

    public WDSurfaceView(Context context) {
        this(context, null);
    }

    public WDSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mState = FingerAction.NONE;
        mSpaceBetweenFingers = 0.0f;
        mScale = 1.0f;
    }

    @Override
    public void setRenderer(Renderer renderer) {
        super.setRenderer(renderer);
        this.mRenderer = (WDRenderer) renderer;
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        this.mRenderer.getMap().addObserver(this);
    }

    public void setRenderer(WDRenderer renderer) {
        super.setRenderer(renderer);
        this.mRenderer = renderer;
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        this.mRenderer.getMap().addObserver(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean b = super.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:       /** si les doigts bougent **/
                this.mState = this.mState ==FingerAction.IS_ZOOMING && (event.getPointerCount() > 1) ? FingerAction.IS_ZOOMING : FingerAction.IS_MOVING;
                if(this.mState == FingerAction.IS_ZOOMING && (event.getPointerCount() > 1)) {   /** on zoom **/
                    float x1 = event.getX(event.findPointerIndex(event.getPointerId(0))) - event.getX(event.findPointerIndex(event.getPointerId(event.getPointerCount()-1)));
                    float y1 = event.getY(event.findPointerIndex(event.getPointerId(0))) - event.getY(event.findPointerIndex(event.getPointerId(event.getPointerCount()-1)));
                    float distance = (float) Math.sqrt(x1 * x1 + y1 * y1);
                    float zoom = this.mSpaceBetweenFingers/(distance+1);
                    this.mScale = this.mScale * zoom;
                    this.mRenderer.zoomCamera(zoom);
                    this.mSpaceBetweenFingers = distance;
                } else if(this.mState == FingerAction.IS_MOVING) {        /** on deplace **/
                    float x = event.getX();
                    float y = event.getY();

                    float dx = x - this.mPreviousX;
                    float dy = y - this.mPreviousY;
                    mPreviousX = event.getX();
                    mPreviousY = event.getY();
                    this.mRenderer.moveCamera(2 * dx / getWidth() * this.mScale, -2 * dy / getHeight() * this.mScale, 0);
                }
                /** rafraichissement **/
                requestRender();

                break;
            case MotionEvent.ACTION_DOWN:   /** si on appuie un doigt **/
                mPreviousX = event.getX();
                mPreviousY = event.getY();
                this.mState = FingerAction.NONE;
                b = true;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:   /** si on ajoute un doigt de plus **/
                if(this.mState!=FingerAction.IS_ZOOMING) {
                    float x1 = event.getX(event.findPointerIndex(event.getPointerId(0))) - event.getX(event.findPointerIndex(event.getPointerId(event.getPointerCount()-1)));
                    float y1 = event.getY(event.findPointerIndex(event.getPointerId(0))) - event.getY(event.findPointerIndex(event.getPointerId(event.getPointerCount()-1)));
                    this.mSpaceBetweenFingers = (float) Math.sqrt(x1 * x1 + y1 * y1);
                    this.mState = FingerAction.IS_ZOOMING;
                }
                b = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:     /** si on lève un doigt on sort du mode zoom **/

                this.mState = FingerAction.NONE;
                if(event.getPointerCount()>0) {
                    mPreviousX = event.getX(event.findPointerIndex(event.getPointerId(0)));
                    mPreviousY = event.getY(event.findPointerIndex(event.getPointerId(0)));
                }
                if(event.getPointerCount()>1) {
                    float x1 = event.getX(event.findPointerIndex(event.getPointerId(0))) - event.getX(event.findPointerIndex(event.getPointerId(event.getPointerCount()-1)));
                    float y1 = event.getY(event.findPointerIndex(event.getPointerId(0))) - event.getY(event.findPointerIndex(event.getPointerId(event.getPointerCount()-1)));
                    this.mSpaceBetweenFingers = (float) Math.sqrt(x1 * x1 + y1 * y1);
                    this.mState = FingerAction.IS_ZOOMING;
                }

                break;
            default:
                break;
        }

        return b;
    }

    @Override
    public void update(Observable observable, Object data) {
        requestRender();
    }
}
