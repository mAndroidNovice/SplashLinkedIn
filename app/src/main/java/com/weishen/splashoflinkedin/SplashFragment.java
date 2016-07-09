package com.weishen.splashoflinkedin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Jack Dawson on 2016/7/7.
 * qq mail: 651203654@qq.com
 * 163 mail: wyw_weishen@163.com
 */
public class SplashFragment extends Fragment {

    private final int SECOND_DURATION = 600;
    private final int THIRD_DURATION = 400;
    private final int FOURTH_DURATION = 1000;

    //second view
    TextView secondAnimView;
    ImageView secondImg;
    TextView secondText;
    //third  view
    View thirdAnimView1;
    View thirdAnimView2;
    View thirdAnimView3;
    //fourth view
    View fourthAnimView;

    View containerView;
    Animator currentSecond, currentThird, currentFourth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getArguments().getInt("resId", 0);
        this.containerView = inflater.inflate(resId, null);
        int backgroundColor = getArguments().getInt("background");
        try {
            ImageView headview = (ImageView) this.containerView.findViewById(R.id.image_head);
            Glide.with(getActivity()).load(R.mipmap.growth_prereg_jiali_chen_photo).bitmapTransform(new CropCircleTransformation(new LruBitmapPool((int) (Runtime.getRuntime().maxMemory() / 8)))).into(headview);
        } catch (Exception e) {
        }
        this.containerView.setBackgroundColor(backgroundColor);
        return this.containerView;
    }

    public void startAnimSecond() {
        if (getActivity() == null) return;
        LinearLayout measuredView = (LinearLayout) getActivity().findViewById(R.id.measureWidth);
        secondAnimView = (TextView) containerView.findViewById(R.id.text_anim);
        secondImg = (ImageView) containerView.findViewById(R.id.img_left);
        secondText = (TextView) containerView.findViewById(R.id.text);
        ViewWrapper Wrapper = new ViewWrapper(secondAnimView);
        ObjectAnimator startAnim = ObjectAnimator.ofInt(Wrapper, "width", 0, measuredView.getWidth()).setDuration(SECOND_DURATION);
        final ObjectAnimator endAnim = ObjectAnimator.ofInt(Wrapper, "width", measuredView.getWidth(), 0).setDuration(SECOND_DURATION);
        startAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                secondAnimView.setVisibility(View.VISIBLE);
                secondImg.setImageResource(R.mipmap.btn_blue_plus_follow);
                secondText.setText(getResources().getString(R.string.splash_second_unanim));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                endAnim.start();
                currentSecond = endAnim;
                secondImg.setImageResource(R.mipmap.check_icon);
                secondText.setText(getResources().getString(R.string.splash_second_anim));
            }
        });
        endAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                secondAnimView.setVisibility(View.INVISIBLE);

            }
        });
        startAnim.start();
        currentSecond = startAnim;
    }

    public void startAnimThird() {
        if (getActivity() == null) return;
        thirdAnimView1 = getActivity().findViewById(R.id.anim1);
        thirdAnimView2 = getActivity().findViewById(R.id.anim2);
        thirdAnimView3 = getActivity().findViewById(R.id.anim3);
        ObjectAnimator zoomXIn1 = ObjectAnimator.ofFloat(thirdAnimView1, View.SCALE_X, 1f, 1.3f);
        ObjectAnimator zoomYIn1 = ObjectAnimator.ofFloat(thirdAnimView1, View.SCALE_Y, 1f, 1.3f);
        ObjectAnimator zoomXOut1 = ObjectAnimator.ofFloat(thirdAnimView1, View.SCALE_X, 1.3f, 1f);
        ObjectAnimator zoomYOut1 = ObjectAnimator.ofFloat(thirdAnimView1, View.SCALE_Y, 1.3f, 1f);
        ObjectAnimator zoomXIn2 = ObjectAnimator.ofFloat(thirdAnimView2, View.SCALE_X, 0, 1.4f);
        ObjectAnimator zoomYIn2 = ObjectAnimator.ofFloat(thirdAnimView2, View.SCALE_Y, 0, 1.4f);
        ObjectAnimator zoomXOut2 = ObjectAnimator.ofFloat(thirdAnimView2, View.SCALE_X, 1.4f, 1f);
        ObjectAnimator zoomYOut2 = ObjectAnimator.ofFloat(thirdAnimView2, View.SCALE_Y, 1.4f, 1f);
        AnimatorSet setIn1 = new AnimatorSet();
        setIn1.setDuration(THIRD_DURATION).playTogether(zoomXIn1, zoomYIn1);

        AnimatorSet setIn2 = new AnimatorSet();
        setIn2.setDuration(THIRD_DURATION).playTogether(zoomXIn2, zoomYIn2);

        AnimatorSet setIn3 = setIn2.clone();
        setIn3.setTarget(thirdAnimView3);
//        AnimatorSet setIn3 = new AnimatorSet();
//        setIn3.setDuration(400).playTogether(zoomXIn3, zoomYIn3);

        AnimatorSet setOut1 = new AnimatorSet();
        setOut1.setDuration(THIRD_DURATION).playTogether(zoomXOut1, zoomYOut1);

        AnimatorSet setOut2 = new AnimatorSet();
        setOut2.setDuration(THIRD_DURATION).playTogether(zoomXOut2, zoomYOut2);
        AnimatorSet setOut3 = setOut2.clone();
        setOut3.setTarget(thirdAnimView3);
//        AnimatorSet setOut3 = new AnimatorSet();
//        setOut3.setDuration(400).playTogether(zoomXOut3, zoomYOut3);
        AnimatorSet set = new AnimatorSet();
        currentThird = set;
        set.playSequentially(setIn1, setOut1, setIn2, setOut2, setIn3, setOut3);
        setIn1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                thirdAnimView1.setVisibility(View.VISIBLE);
                thirdAnimView2.setVisibility(View.INVISIBLE);
                thirdAnimView3.setVisibility(View.INVISIBLE);
            }
        });
        setIn2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                thirdAnimView1.setVisibility(View.VISIBLE);
                thirdAnimView2.setVisibility(View.VISIBLE);
                thirdAnimView3.setVisibility(View.INVISIBLE);
            }
        });
        setIn3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                thirdAnimView1.setVisibility(View.VISIBLE);
                thirdAnimView2.setVisibility(View.VISIBLE);
                thirdAnimView3.setVisibility(View.VISIBLE);
            }
        });
        set.start();
    }

    public void startAnimFourth() {
        if (getActivity() == null) return;
        fourthAnimView = getActivity().findViewById(R.id.lin_anim);
        ObjectAnimator animator = ObjectAnimator.ofFloat(fourthAnimView, View.TRANSLATION_Y, 0, fourthAnimView.getHeight()).setDuration(FOURTH_DURATION);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        currentFourth = animator;
    }

    public void startAnim(int posistion) {
        switch (posistion) {
            case 1:
                startAnimSecond();
                break;
            case 2:
                startAnimThird();
                break;
            case 3:
                startAnimFourth();
                break;
        }
    }

    public void endAnim() {
        if (currentSecond != null && currentSecond.isRunning()) {
            currentSecond.end();
            secondImg.setImageResource(R.mipmap.btn_blue_plus_follow);
            secondText.setText(getResources().getString(R.string.splash_second_unanim));
        }
        if (currentThird != null && currentThird.isRunning()) {
            currentThird.end();
            thirdAnimView1.setVisibility(View.INVISIBLE);
            thirdAnimView2.setVisibility(View.INVISIBLE);
            thirdAnimView3.setVisibility(View.INVISIBLE);
        }
        if (currentFourth != null && currentFourth.isRunning()) {
            currentFourth.end();
            fourthAnimView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 必须使用end 否则切换页面时相邻页面有动画效果
     */
    public void cancleAnim() {
        if (currentSecond != null && currentSecond.isRunning()) {
            currentSecond.cancel();
        }
        if (currentThird != null && currentThird.isRunning()) {
            currentThird.cancel();
        }
        if (currentFourth != null && currentFourth.isRunning()) {
            currentFourth.cancel();
        }
    }

    /**
     * 自己封装用来改变view的width
     */
    private static class ViewWrapper {
        private View mTarget;
        private int width;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            width = mTarget.getLayoutParams().width;
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }
    }
}
