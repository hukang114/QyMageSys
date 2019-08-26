package com.qymage.sys.common.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 类名：XFCircleImageView
 * 类描述：自定义ImageView
 * 创建人：J.S
 * 修改人：J.S
 * 创建时间：2018/12/3 15:37
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
@SuppressLint("AppCompatCustomView")
public class XFCircleImageView extends ImageView {

    private static final Xfermode MASK_XFERMODE;
    private Bitmap mask;
    private Paint paint;

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }

    public XFCircleImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public XFCircleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XFCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Bitmap createMask() {
        int i = this.getWidth();
        int j = this.getHeight();
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;
        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);
        Canvas localCanvas = new Canvas(localBitmap);
        Paint localPaint = new Paint(1);
        localPaint.setColor(-16777216);
        float f1 = (float) this.getWidth();
        float f2 = (float) this.getHeight();
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);
        localCanvas.drawOval(localRectF, localPaint);
        return localBitmap;
    }

    @SuppressLint({"DrawAllocation"})
    protected void onDraw(Canvas canvas) {
        Drawable localDrawable = this.getDrawable();
        if (localDrawable != null) {
            try {
                if (this.paint == null) {
                    Paint localException = new Paint();
                    this.paint = localException;
                    this.paint.setFilterBitmap(false);
                    Paint localStringBuilder1 = this.paint;
                    Xfermode i = MASK_XFERMODE;
                    localStringBuilder1.setXfermode(i);
                }

                float localException1 = (float) this.getWidth();
                float localStringBuilder2 = (float) this.getHeight();
                @SuppressLint("WrongConstant") int i1 = canvas.saveLayer(0.0F, 0.0F, localException1, localStringBuilder2, (Paint) null, 31);
                int j = this.getWidth();
                int k = this.getHeight();
                localDrawable.setBounds(0, 0, j, k);
                localDrawable.draw(canvas);
                Bitmap localBitmap2;
                if (this.mask == null || this.mask.isRecycled()) {
                    localBitmap2 = this.createMask();
                    this.mask = localBitmap2;
                }
                localBitmap2 = this.mask;
                Paint localPaint3 = this.paint;
                canvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint3);
                canvas.restoreToCount(i1);
            } catch (Exception var10) {
                StringBuilder localStringBuilder = (new StringBuilder()).append("Attempting to draw with recycled bitmap. View ID = ");
                System.out.println("localStringBuilder==" + localStringBuilder);
            }
        }
    }
}
