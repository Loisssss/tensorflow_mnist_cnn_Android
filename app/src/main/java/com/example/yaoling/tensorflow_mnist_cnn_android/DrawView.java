package com.example.yaoling.tensorflow_mnist_cnn_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;



//实现自定义视图
public class DrawView extends View {
    //actual drawing
    private Path path;
    //styles
    private Paint paint;
    private Bitmap bitmap;

    public DrawView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
        path = new Path();
    }


    //设置Paint的一些属性
    private void setupPaint(){
        paint = new Paint();
        //Paint画出的颜色为黑色
        paint.setColor(Color.BLACK);
        //平滑边缘
        paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

    }

    //只要检测到 touch 就会调用此方法
    @Override
    public boolean onTouchEvent(MotionEvent motionEven) {
        //得到x和y坐标的值
        float pointX = motionEven.getX();
        float pointY = motionEven.getY();
        //确定motion的类型
        switch (motionEven.getAction()){
            //检测到用户按下屏幕于具体的x y坐标
            case MotionEvent.ACTION_DOWN:
                path.moveTo(pointX, pointY);
                break;
            // 在用户手触摸屏幕期间， 并且从x 移动到 y， 在x y之间画一条线
            case MotionEvent.ACTION_MOVE: path.lineTo(pointX, pointX);
            break;
            default: return false;
        }
        //告诉Android 画出并显示所有的更新
        postInvalidate();
        return true;
    }

    //当视图大小发生变化时， 调用此方法；此方法仅用作初始化目的
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //调用super并初始化我们的位图
        super.onSizeChanged(w, h, oldw, oldh);
        //绘图将转换为Bitmap，因此Bitmap的大小必须与自定义视图的大小相匹配。
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //告诉canvas画bitmap
        super.onDraw(canvas);
        //用paint对象绘制bitmap从（0,0）（top left）
        canvas.drawBitmap(bitmap, 0, 0, paint);
        //画出路径, 看到用户实际正在绘制的图像
        canvas.drawPath(path, paint);
    }

    public void clearCanvas(){
        //重置bitmap
        bitmap.recycle();
        //初始化bitmap
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        //重置path
        path = new Path();
        //更新视图
        invalidate();
    }

    //转换canvas 为bitmap, so that 转换为原始pixels然后喂给模型
    private Bitmap getBitmap() {
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        draw(canvas);
        return Bitmap.createScaledBitmap(bitmap, canvas.getWidth(), canvas.getHeight(), false);

    }

    //转换bitmap为正确的model输入格式
    public float[] getPixels() {
        Bitmap bitmap = getBitmap();
        // 计算 pixel的大小
        int size = bitmap.getWidth() * bitmap.getHeight();
        System.out.print(size);
        int[] pixels = new int[size];
        //检索pixels
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        //int转为float， 将其传递给model
        float[] bitmapPixels = new float[pixels.length];
        for (int i = 0; i < pixels.length; i++) {
            //0（if white）； 255（if black）
            int pixel = pixels[i];
            int xor = pixel & 0xff;
            //value between 0 and 1
            bitmapPixels[i] = (float)((0xff - xor) / 255.0);
        }
        return bitmapPixels;
    }
}
