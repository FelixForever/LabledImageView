package com.felix.labledimageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/10/29.
 */
public class LabledImageView extends ImageView
{
    enum Gravity
    {
        LEFT(1),
        TOP(2),
        RIGHT(3),
        BOTTOM(4);
        private int mGravity;

        private Gravity(int gravity)
        {
            this.mGravity = gravity;
        }
        public int getGravity()
        {
            return this.mGravity;
        }
        public void setGravity(Gravity gravity)
        {
            this.mGravity=gravity.getGravity();
        }
        public void setGravity(int gravity)
        {
            this.mGravity=gravity;
        }

    }

    private String mText;
    private int mSrcId;
    private int text_margin;
    private int text_padding;
    private Gravity text_gravity;
    private int text_size;
    private int text_color;
    private boolean isCenterVert;
    private boolean isCenterHorz;

    public LabledImageView(Context context)
    {
        super(context);
    }

    public LabledImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.LabledImageView);
        final int NUM = types.getIndexCount();
        isCenterHorz =false;
        isCenterVert =false;
        final int defFontSize=getDefaultFontSize(context);
//        final int MARGIN=R.styleable.LabledImageView_text_margin;
//        final int PADDING=R.styleable.LabledImageView_text_padding;
//        final int GRAVITY=R.styleable.LabledImageView_text_gravity;
//        final int CENTER_HORZ=R.styleable.LabledImageView_centerInHorizontal;
//        final int CENTER_VERT=R.styleable.LabledImageView_centerInVertical;
//        final int CENTER_ALL=R.styleable.LabledImageView_centerInAll;
        for (int i = 0; i < NUM; i++)
        {
            int attr = types.getIndex(i);
            switch (attr)
            {
                case R.styleable.LabledImageView_text_margin:
                    text_margin = types.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_padding:
                    text_padding = types.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.LabledImageView_text_gravity:
                    text_gravity.setGravity(types.getInt(attr, 1));
                    break;
                case R.styleable.LabledImageView_centerInHorizontal:
                    if(!isCenterHorz)
                        isCenterHorz = types.getBoolean(attr,false);
                    break;
                case R.styleable.LabledImageView_centerInVertical:
                    if(!isCenterVert)
                        isCenterVert = types.getBoolean(attr,false);
                    break;
                case R.styleable.LabledImageView_centerInAll:
                    if(!isCenterHorz)
                        isCenterHorz = types.getBoolean(attr,false);
                    if(!isCenterVert)
                        isCenterVert = types.getBoolean(attr,false);
                    break;
                case R.styleable.LabledImageView_text_color:
                    text_color=types.getColor(attr,Color.WHITE);
                    break;
                case R.styleable.LabledImageView_text_size:
                    text_size=types.getDimensionPixelOffset(attr,defFontSize);
                    break;
                default:break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

    }
    public int getDefaultFontSize(Context ctx,int dp)
    {
        DisplayMetrics dm = new DisplayMetrics();
        dm = ctx.getApplicationContext().getResources().getDisplayMetrics();
        return (int)dm.density*dp;
    }
    public int getDefaultFontSize(Context ctx)
    {
        return getDefaultFontSize(ctx,16);
    }
}

