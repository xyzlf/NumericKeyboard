package com.xyzlf.custom.keyboardlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

public class PasswordView extends EditText {

    private static final int DEFAULT_BOARD_SIZE = 1; //默认边框大小
    private static final int DEFAULT_BOARD_RADIUS = 0; //默认边框圆角大小
    private static final int DEFAULT_PWD_CIRCLE_COUNT = 6; //默认密码个数
    private static final int DEFAULT_PWD_CIRCLE_RADIUS = 4; //默认密码半径

    private Paint mPaint;
    private RectF mRectF;

    private int mBgColor;

    private int mBoardColor;
    private int mBoardSize;
    private int mBoardCornerSize;

    private int mDividerColor;
    private int mDividerSize;

    private int mPwdCircleColor;
    private int mPwdCircleRadius;

    private int mPwdCircleCount;
    private int mPwdItemWidth;

    private StringBuilder mPasswordStr;

    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAttributeSet(context, attrs);

        mPasswordStr = new StringBuilder(DEFAULT_PWD_CIRCLE_COUNT);

        setCursorVisible(false);
        setFocusable(false);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mRectF = new RectF();
    }

    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordView);
        mBgColor = array.getColor(R.styleable.PasswordView_pwd_bg_color, Color.WHITE);

        mBoardColor = array.getColor(R.styleable.PasswordView_pwd_board_color, Color.parseColor("#454a4d"));
        mBoardSize = (int) array.getDimension(R.styleable.PasswordView_pwd_board_size, DEFAULT_BOARD_SIZE);
        mBoardCornerSize = (int) array.getDimension(R.styleable.PasswordView_pwd_board_corner_size, DEFAULT_BOARD_RADIUS);

        mDividerColor = array.getColor(R.styleable.PasswordView_pwd_divider_color, Color.parseColor("#454a4d"));
        mDividerSize = (int) array.getDimension(R.styleable.PasswordView_pwd_divider_size, DEFAULT_BOARD_SIZE);

        mPwdCircleColor = array.getColor(R.styleable.PasswordView_pwd_circle_color, Color.parseColor("#454a4d"));
        mPwdCircleRadius = (int) array.getDimension(R.styleable.PasswordView_pwd_circle_radius, DEFAULT_PWD_CIRCLE_RADIUS);

        mPwdCircleCount = (int) array.getDimension(R.styleable.PasswordView_pwd_circle_count, DEFAULT_PWD_CIRCLE_COUNT);

        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPwdItemWidth = (w - (mPwdCircleCount - 1) * mDividerSize) / mPwdCircleCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBg(canvas);

        drawBoard(canvas);

        drawBoardDivider(canvas);

        drawPwd(canvas);
    }

    private void drawBg(Canvas canvas) {
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
        mRectF.set(0, 0, getWidth(), getHeight());

        if (mBoardCornerSize > 0) {
            canvas.drawRoundRect(mRectF, mBoardCornerSize, mBoardCornerSize, mPaint);
        } else {
            canvas.drawRect(mRectF, mPaint);
        }
    }

    private void drawBoard(Canvas canvas) {
        mPaint.setColor(mBoardColor);
        mPaint.setStrokeWidth(mBoardSize);
        mPaint.setStyle(Paint.Style.STROKE);

        mRectF.set(mBoardSize, mBoardSize, getWidth() - mBoardSize, getHeight() - mBoardSize);

        if (mBoardCornerSize > 0) {
            canvas.drawRoundRect(mRectF, mBoardCornerSize, mBoardCornerSize, mPaint);
        } else {
            canvas.drawRect(mRectF, mPaint);
        }
    }

    private void drawBoardDivider(Canvas canvas) {
        mPaint.setColor(mDividerColor);
        mPaint.setStrokeWidth(mDividerSize);
        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mPwdCircleCount - 1; i++) {
            int startX = (i + 1) * mDividerSize + (i + 1) * mPwdItemWidth + mBoardSize;
            canvas.drawLine(startX, mBoardSize, startX, getHeight() - mBoardSize, mPaint);
        }
    }

    private void drawPwd(Canvas canvas) {
        mPaint.setColor(mPwdCircleColor);
        mPaint.setStyle(Paint.Style.FILL);

        int pwdTextLength = getText().length();
        for (int i = 0; i < pwdTextLength; i++) {
            int cx = i * mDividerSize + i * mPwdItemWidth + mPwdItemWidth / 2 + mBoardSize;
            canvas.drawCircle(cx, getHeight() / 2, mPwdCircleRadius, mPaint);
        }
    }

    public void addPassword(int number) {
        if (mPasswordStr.length() >= mPwdCircleCount) {
            return;
        }
        mPasswordStr.append(number);

        String pwd = mPasswordStr.toString();
        setText(pwd);

        if (mPasswordStr.length() == mPwdCircleCount) {
            if (onPasswordListener != null) {
                onPasswordListener.onPasswordComplete(pwd);
            }
        } else {
            if (onPasswordListener != null) {
                onPasswordListener.onPasswordChange(pwd);
            }
        }
    }

    public void deletePassword() {
        int len = mPasswordStr.length();
        if (len <= 0) {
            return;
        }
        mPasswordStr.deleteCharAt(len - 1);
        String pwd = mPasswordStr.toString();
        setText(pwd);

        if (onPasswordListener != null) {
            onPasswordListener.onPasswordChange(pwd);
        }
    }

    public void clearPassword() {
        mPasswordStr.delete(0, mPasswordStr.length());
        setText("");
    }

    private OnPasswordListener onPasswordListener;

    public void setOnPasswordListener(OnPasswordListener onPasswordListener) {
        this.onPasswordListener = onPasswordListener;
    }


    public interface OnPasswordListener {

        void onPasswordChange(String pwd);

        void onPasswordComplete(String pwd);
    }

}
