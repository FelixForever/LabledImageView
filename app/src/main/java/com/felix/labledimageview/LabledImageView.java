package com.felix.labledimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;


/**
 * Created by felix on 2015/10/29.
 */
public class LabledImageView extends ImageView
{
    private String mText;
    private int mTxtWidth;
    private int mTxtHeight;
    private int mImgLayoutWidth;
    private int mImgLayoutHeight;

    private int mSrcId;

    private int text_max_length;
    private boolean text_single_line;
    private int text_margin;
    private int text_padding;
    private int text_padding_left;
    private int text_padding_top;
    private int text_padding_right;
    private int text_padding_bottom;
    private Direction text_direction;
    private int text_size;
    private int text_color;
    private Gravity text_gravity;
    private Gravity img_gravity;

    private Paint mPaint;

    public LabledImageView(Context context)
    {
        super(context);
    }

    Context mContext;
    AttributeSet mAttrs;
    public LabledImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext=context;
        mAttrs=attrs;
        initLabledImageView(context, attrs);
    }
    private void initLabledImageView(Context context,AttributeSet attrs) {
        final int defFontSize=getDefaultFontSize(context);
        final Drawable drawable=getDrawable();
        final Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
        mImgLayoutHeight =bitmap.getHeight();
        mImgLayoutWidth =bitmap.getWidth();
        //bitmap.recycle();


        text_max_length=-1;
        text_single_line=true;
        text_margin=0;
        text_padding=0;
        text_padding_left=0;
        text_padding_right=0;
        text_padding_top=0;
        text_padding_bottom=0;
        text_direction=Direction.BOTTOM;
        text_size=defFontSize;
        text_color=Color.BLACK;
        text_gravity=Gravity.START;
        img_gravity=Gravity.START;
        mPaint=new Paint();
        mPaint.setTextSize(text_size);
        mPaint.setColor(text_color);

        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.LabledImageView);
        final int NUM = types.getIndexCount();
        for (int i = 0; i < NUM; i++)
        {
            int attr = types.getIndex(i);
            switch (attr)
            {
                case R.styleable.LabledImageView_text:
                    mText=types.getString(attr);
                    break;
                case R.styleable.LabledImageView_text_size:
                    text_size=types.getDimensionPixelSize(attr, defFontSize);
                    Log.i("text_size",""+text_size);
                    break;
                case R.styleable.LabledImageView_text_color:
                    text_color=types.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.LabledImageView_text_maxlength:
                    text_max_length=types.getDimensionPixelSize(attr, -1);
                    break;
                case R.styleable.LabledImageView_text_singleline:
                    text_single_line=types.getBoolean(attr, true);
                    break;
                case R.styleable.LabledImageView_text_margin:
                    text_margin = types.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_padding:
                    text_padding = types.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_padding_left:
                    text_padding_left = types.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_padding_top:
                    text_padding_top = types.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_padding_right:
                    text_padding_right = types.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_padding_bottom:
                    text_padding_bottom = types.getDimensionPixelSize(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_direction:
                    text_direction=Direction.values()[(types.getInt(attr, 4))];
                    break;
                case R.styleable.LabledImageView_text_gravity:
                    text_gravity=Gravity.values()[types.getInt(attr,0)];
                    break;
                case R.styleable.LabledImageView_image_gravity:
                    img_gravity=Gravity.values()[types.getInt(attr,0)];
                    break;
                default:break;
            }
        }
        if(text_padding_left<=0)
            text_padding_left=text_padding;
        if(text_padding_top<=0)
            text_padding_top=text_padding;
        if(text_padding_right<=0)
            text_padding_right=text_padding;
        if(text_padding_bottom<=0)
            text_padding_bottom=text_padding;

        mPaint.setColor(text_color);
        mPaint.setTextSize(text_size);

        final int size=mText.length()+1;
        float[]widths=new float[size];
        mPaint.getTextWidths(("口"+mText),widths);
        mTxtWidth=0;
        for(int i=1;i<size;i++)
        {
            mTxtWidth+=widths[i];
        }
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mTxtHeight=(int)(Math.ceil(fm.descent - fm.top)*3/4);
       // mTxtHeight=(int)(mPaint.getTextSize()*3/4);
        Rect rect=new Rect();
        mPaint.getTextBounds(mText,0,mText.length(),rect);
        mTxtHeight=rect.height();
        mTxtWidth=rect.width();
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        final int LAYOUT_WIDTH = getWidth();
        final int LAYOUT_HEIGHT = getHeight();
        int bmpLeft=0;
        int bmpTop=0;
        int txtLeft=text_padding_left;
        int txtTop=mTxtHeight+text_padding_top;
        switch (text_direction)
        {
            case LEFT:
                bmpLeft=mTxtWidth+text_padding_left+text_padding_right;
                break;
            case RIGHT:
                txtLeft=mImgLayoutWidth;
                break;
            case TOP:
                bmpTop=mTxtHeight;
                break;
            case BOTTOM:
                txtTop= txtTop+mImgLayoutHeight;
                break;
            default:break;
        }
        switch(text_gravity)
        {
            case START:break;
            case CENTER:
                if(text_direction==Direction.LEFT||text_direction==Direction.RIGHT)
                {
                    txtTop+=(LAYOUT_HEIGHT-mTxtHeight-text_padding_top-text_padding_bottom)/2;
                }
                else
                {
                    txtLeft+=(LAYOUT_WIDTH-mTxtWidth-text_padding_left-text_padding_right)/2;
                }
                break;
            case END:
                if(text_direction==Direction.LEFT||text_direction==Direction.RIGHT)
                {
                    txtTop+=(LAYOUT_HEIGHT-mTxtHeight-text_padding_top-text_padding_bottom);
                }
                else
                {
                    txtLeft+=(LAYOUT_WIDTH-mTxtWidth-text_padding_left-text_padding_right);
                }
                break;
            default:break;
        }

        if(text_max_length==-1||text_max_length>=mTxtWidth)
        {
            canvas.drawText(mText, txtLeft,txtTop, mPaint);
        }
        else
        {
            Rect rect=new Rect();
            mPaint.getTextBounds(mText,0,1,rect);
            int cellWidth=rect.width();
            int index=(mTxtWidth-text_max_length)/cellWidth;
            canvas.drawText(mText.substring(0,index), txtLeft,txtTop, mPaint);
        }
        logInfo("onDraw width="+getWidth()+" height="+getHeight());
        final Drawable drawable=getDrawable();
        final Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
        final Bitmap drawBmp=zoomImage(bitmap,mImgLayoutWidth,mImgLayoutHeight);
        canvas.drawBitmap(drawBmp, bmpLeft, bmpTop, mPaint);
        //canvas.drawLine(0, (float) txtTop, (float) getWidth(), (float) txtTop, mPaint);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int newWidth=mImgLayoutWidth;
        int newHeight=mImgLayoutHeight;
        final int txtWidthWithPadding=mTxtWidth+text_padding_left+text_padding_right;
        final int txtHeightWithPadding=mTxtHeight+text_padding_top+text_padding_bottom;
        switch (text_direction)
        {
            case LEFT:
            case RIGHT:
                if(text_max_length!=-1)
                {
                    newWidth+=text_max_length>mTxtWidth?mTxtWidth:text_max_length;
                }
                else
                {
                    newWidth+=mTxtWidth;
                }
                newWidth+=text_padding_left+text_padding_right;
                newHeight=newHeight>txtHeightWithPadding?newHeight:txtHeightWithPadding;
                break;
            case TOP:
            case BOTTOM:
                if(text_max_length!=-1)
                {
                    newWidth=newWidth>text_max_length?newWidth:text_max_length;
                }
                else
                {
                    newWidth=newWidth>txtWidthWithPadding?newWidth:txtWidthWithPadding;
                }

                newHeight+=txtHeightWithPadding;
                break;
            default:break;
        }
        int width=newWidth;
        int height=newHeight;
        /*if (widthMode == MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;

        }
        if(heightMode==MeasureSpec.EXACTLY)
        {
            height=heightSize;
        }*/
        setMeasuredDimension(width, height);
    }

    public int getDefaultFontSize(Context ctx,int dp)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return (int)dm.density*dp;

    }
    public int getDefaultFontSize(Context ctx)
    {
        return getDefaultFontSize(ctx,18);
    }


    public enum Direction
    {
        LEFT(0),
        TOP(1),
        RIGHT(2),
        BOTTOM(3);
        private int mDirection;
        private Direction()
        {
            this.mDirection=4;
        }
        private Direction(int direction)
        {
            this.mDirection=direction;
        }
        public int getDirection()
        {
            return this.mDirection;
        }
        public void setDirection(int direction)
        {
            this.mDirection=direction;
        }
    }

    public enum Gravity
    {
        START(0),
        CENTER(1),
        END(2);
        private int mGravity;
        private Gravity()
        {
            this.mGravity=1;
        }
        private Gravity(int gravity)
        {
            this.mGravity=gravity;
        }
        public int getGravity()
        {
            return this.mGravity;
        }
        public void setGravity(int gravity)
        {
            this.mGravity=gravity;
        }
    }
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    private final String TAG=this.getClass().getName().substring(this.getClass().getName().lastIndexOf("."));
    private void logInfo(String msg)
    {
        Log.i(TAG,msg);
    }
    private void logDebug(String msg)
    {
        Log.d(TAG, msg);
    }
    private void logWarning(String msg)
    {
        Log.w(TAG, msg);
    }
    private void logError(String msg)
    {
        Log.e(TAG, msg);
    }

}


