package com.lab.bottomsheet;

import android.os.Build;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity implements ISlideView {
    private BottomNavigationView bottomNavigation;
    private FrameLayout frameLayout;
    private int height;
    private int heightFrag;
    private RelativeLayout rlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.navigation);
        frameLayout = findViewById(R.id.frag_container);
        rlContainer = findViewById(R.id.rlContainer);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        MainFragment mainFragment = new MainFragment();
        mainFragment.setSlideView(this);
        fragmentTransaction.add(R.id.frag_container, mainFragment);
        fragmentTransaction.commit();

        getHeightOfBottomNavigation();
    }


    private void getHeightOfBottomNavigation() {
        ViewTreeObserver vto = bottomNavigation.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    bottomNavigation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    bottomNavigation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                height = bottomNavigation.getMeasuredHeight();
            }
        });

        ViewTreeObserver fragContainer = frameLayout.getViewTreeObserver();
        fragContainer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    frameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    frameLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                heightFrag = frameLayout.getMeasuredHeight();
            }
        });
    }

    @Override
    public void onSlide(float value) {
        float newValue = value >= 0 ? value : 0;
        float alpha = 1 - newValue;
        bottomNavigation.setAlpha(alpha);
        int newHeight = (int)(height - (newValue * height));
        bottomNavigation.getLayoutParams().height = newHeight;
        bottomNavigation.requestLayout();
    }
}
