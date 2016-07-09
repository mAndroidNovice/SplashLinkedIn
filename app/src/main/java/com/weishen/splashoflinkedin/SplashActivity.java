package com.weishen.splashoflinkedin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weishen.splashoflinkedin.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jack Dawson on 2016/7/7.
 * qq mail: 651203654@qq.com
 * 163 mail: wyw_weishen@163.com
 */
public class SplashActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    int[] resId = new int[]{R.layout.fragment_splash_first, R.layout.fragment_splash_second,
            R.layout.fragment_splash_third, R.layout.fragment_splash_fourth};
    int[] resBg = new int[]{0xffE5E9EC, 0xFF069FD8, 0xFF07A0D9, 0xFF069FD8};

    //注册的资源设置
    int[] textRegisteColors = new int[]{0xFFD4ECF3, 0xFF636363, 0xFF636363, 0xFF636363};
    int[] bgRegisteColors = new int[]{0xFF008CC8, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF};
    int[] borderResgisteColors = new int[]{};//和背景同色
    //登录的资源设置
    int[] textLoginColors = new int[]{0xFF2799CB, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF};
    int[] bgLoginColors = new int[]{0xFFE5E9EC, 0xFF069FD8, 0xFF07A0D9, 0xFF069FD8};
    int[] borderLoginColors = new int[]{0xFF2799CB, 0xFFFFFFFF, 0xFFFFFFFF, 0xFFFFFFFF};

    //registe的距离数据
    int startBgXRegs, endBgXRegs, startTextXRegs, endTextXRegs;
    //login的距离数据
    int startBgXLog, endBgXLog, startTextXLog, endTextXLog;

    float lastOffset;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ball1)
    ImageView ball1;
    @BindView(R.id.ball2)
    ImageView ball2;
    @BindView(R.id.ball3)
    ImageView ball3;
    @BindView(R.id.ball4)
    ImageView ball4;
    @BindView(R.id.registe)
    ColorTrackView registe;
    @BindView(R.id.login)
    ColorTrackView login;
    @BindView(R.id.lin_ball)
    LinearLayout linBall;

    List<Fragment> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        mDatas = new ArrayList<>();
        for (int i = 0; i < resId.length; i++) {
            SplashFragment fragment = new SplashFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("resId", resId[i]);
            bundle.putInt("background", resBg[i]);
            fragment.setArguments(bundle);
            mDatas.add(fragment);
        }
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), mDatas);
        viewpager.setAdapter(adapter);
        viewpager.addOnPageChangeListener(this);
    }

    /**
     * 记录数据
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int windowWidth = metrics.widthPixels;
        int[] locationRegs = new int[2];
        registe.getLocationOnScreen(locationRegs);
        startBgXRegs = windowWidth - locationRegs[0] - registe.getWidth();
        endBgXRegs = windowWidth - locationRegs[0];
        startTextXRegs = windowWidth - locationRegs[0] - registe.mTextStartX - registe.mTextWidth;
        endTextXRegs = windowWidth - locationRegs[0] - registe.mTextStartX;
        int[] locationLog = new int[2];
        login.getLocationOnScreen(locationLog);
        startBgXLog = windowWidth - locationLog[0] - login.getWidth();
        endBgXLog = windowWidth - locationLog[0];
        startTextXLog = windowWidth - locationLog[0] - login.mTextStartX - login.mTextWidth;
        endTextXLog = windowWidth - locationLog[0] - login.mTextStartX;
        super.onWindowFocusChanged(hasFocus);
    }
    /**
     * 滑动
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            //判断向左滑动还是向右滑动
            boolean isNext = lastOffset - positionOffset <= 0;
            int direction = isNext ? ColorTrackView.DIRECTION_RIGHT : ColorTrackView.DIRECTION_LEFT;
            setViewRegisite(isNext, direction, position, positionOffsetPixels);
            setViewLogin(isNext, direction, position, positionOffsetPixels);
            lastOffset = positionOffset;
        }
    }

    private void setViewLogin(boolean isNext, int direction, int position, int positionOffsetPixels) {
        login.setDirection(direction);
        //重新设置progress
        float bgProgress = 0;
        if (positionOffsetPixels >= startBgXLog && positionOffsetPixels <= endBgXLog) {
            int length = Math.abs(positionOffsetPixels - startBgXLog);
            bgProgress = length * 1.0f / login.getWidth();
        } else if (positionOffsetPixels < startBgXLog && isNext) {
            bgProgress = 0;
        } else if (positionOffsetPixels < startBgXLog && !isNext) {
            bgProgress = 0.000001f;
        } else if (positionOffsetPixels > endBgXLog) {
            bgProgress = 1f;
        }
        float textProgress = 0;
        if (positionOffsetPixels >= startTextXLog && positionOffsetPixels <= endTextXLog) {
            int length = Math.abs(positionOffsetPixels - startTextXLog);
            textProgress = length * 1f / login.mTextWidth;
        } else if (positionOffsetPixels < startTextXLog && isNext) {
            textProgress = 0;
        } else if (positionOffsetPixels < startTextXLog && !isNext) {
            textProgress = 0.0000001f;
        } else if (positionOffsetPixels > endTextXLog) {
            textProgress = 1f;
        }
        //设置背景
        if (bgProgress == 0 && isNext || bgProgress > 0) {
            if (isNext) {
                login.setBgProgress(bgProgress);
                login.setmBgOriginColor(bgLoginColors[position]);
                login.setmBgChangeColor(bgLoginColors[position + 1]);
                login.setmBorderOriginColor(borderLoginColors[position]);
                login.setmBorderChangeColor(borderLoginColors[position + 1]);
            } else {
                if (bgProgress < 0.1f) {
                    bgProgress = 0;
                }
                login.setBgProgress(1 - bgProgress);
                login.setmBgOriginColor(bgLoginColors[position + 1]);
                login.setmBgChangeColor(bgLoginColors[position]);
                login.setmBorderOriginColor(borderLoginColors[position + 1]);
                login.setmBorderChangeColor(borderLoginColors[position]);
            }
        }
        //设置文字
        if (textProgress == 0 && isNext || textProgress > 0) {
            if (isNext) {
                login.setProgress(textProgress);
                login.setTextOriginColor(textLoginColors[position]);
                login.setTextChangeColor(textLoginColors[position + 1]);
            } else {
                if (textProgress < 0.1f) {
                    textProgress = 0;
                }
                login.setProgress(1 - textProgress);
                login.setTextOriginColor(textLoginColors[position + 1]);
                login.setTextChangeColor(textLoginColors[position]);
            }
        }
    }

    private void setViewRegisite(boolean isNext, int direction, int position, int positionOffsetPixels) {
        registe.setDirection(direction);
        //重新设置progress
        float bgProgress = 0;
        if (positionOffsetPixels >= startBgXRegs && positionOffsetPixels <= endBgXRegs) {
            int length = Math.abs(positionOffsetPixels - startBgXRegs);
            bgProgress = length * 1.0f / registe.getWidth();
        } else if (positionOffsetPixels < startBgXRegs && isNext) {
            bgProgress = 0;
        } else if (positionOffsetPixels < startBgXRegs && !isNext) {
            bgProgress = 0.000001f;
        } else if (positionOffsetPixels > endBgXRegs) {
            bgProgress = 1f;
        }
        float textProgress = 0;
        if (positionOffsetPixels >= startTextXRegs && positionOffsetPixels <= endTextXRegs) {
            int length = Math.abs(positionOffsetPixels - startTextXRegs);
            textProgress = length * 1f / registe.mTextWidth;
        } else if (positionOffsetPixels < startTextXRegs && isNext) {
            textProgress = 0;
        } else if (positionOffsetPixels < startTextXRegs && !isNext) {
            textProgress = 0.0000001f;
        } else if (positionOffsetPixels > endTextXRegs) {
            textProgress = 1f;
        }
        Log.d("SplashActivity", "bgProgress:" + bgProgress);
        //设置背景
        if (bgProgress == 0 && isNext || bgProgress > 0) {
            if (isNext) {
                registe.setBgProgress(bgProgress);
                registe.setmBgOriginColor(bgRegisteColors[position]);
                registe.setmBgChangeColor(bgRegisteColors[position + 1]);
                registe.setmBorderOriginColor(bgRegisteColors[position]);
                registe.setmBorderChangeColor(bgRegisteColors[position + 1]);
            } else {
                //如果不设置 会只绘制0.001 留有一定的距离
                if (bgProgress < 0.001f) {
                    bgProgress = 0;
                }
                registe.setBgProgress(1 - bgProgress);
                registe.setmBgOriginColor(bgRegisteColors[position + 1]);
                registe.setmBgChangeColor(bgRegisteColors[position]);
                registe.setmBorderOriginColor(bgRegisteColors[position + 1]);
                registe.setmBorderChangeColor(bgRegisteColors[position]);
            }
        }

        //设置文字
        try {
            if (textProgress == 0 && isNext || textProgress > 0) {
                if (isNext) {
                    registe.setProgress(textProgress);
                    registe.setTextOriginColor(textRegisteColors[position]);
                    registe.setTextChangeColor(textRegisteColors[position + 1]);
                } else {
                    if (textProgress < 0.001f) {
                        textProgress = 0;
                    }
                    registe.setProgress(1 - textProgress);
                    registe.setTextOriginColor(textRegisteColors[position + 1]);
                    registe.setTextChangeColor(textRegisteColors[position]);
                }
            }
        } catch (Exception e) {
            Log.d("SplashActivity", "数组越界");
        }
    }


    @Override
    public void onPageSelected(int position) {
        int count = linBall.getChildCount();
        //设置小圆点
        if (position == 0) {
            for (int i = 0; i < count; i++) {
                ImageView child = (ImageView) linBall.getChildAt(i);
                if (i == 0) {
                    child.setImageResource(R.drawable.ball_big_gray);
                } else {
                    child.setImageResource(R.drawable.ball_small_gray);
                }
            }
        } else {
            for (int i = 0; i < count; i++) {
                ImageView child = (ImageView) linBall.getChildAt(i);
                if (position == i) {
                    child.setImageResource(R.drawable.ball_big_white);
                } else {
                    child.setImageResource(R.drawable.ball_small_white);
                }
            }
        }
        //关闭所有页面动画
        int size = mDatas.size();
        for (int i = 0; i < size; i++) {
            ((SplashFragment) mDatas.get(i)).endAnim();
        }
        //启动动画
        SplashFragment splashFragment = (SplashFragment) mDatas.get(position);
        splashFragment.startAnim(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
