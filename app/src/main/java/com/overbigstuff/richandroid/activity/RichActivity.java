package com.overbigstuff.richandroid.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.overbigstuff.richandroid.R;
import com.overbigstuff.richandroid.utils.FragmentUtils;


public abstract class RichActivity extends AppCompatActivity {
    public static final int DRAWER_ACTIVITY = R.layout.drawer_activity;
    public static final int DRAWER_FRAGMENT_TOOLBAR_ACTIVITY = R.layout.drawer_fragment_toolbar_activity;
    public static final int DRAWER_OVERLAP_FRAGMENT_ACTIVITY = R.layout.drawer_overlap_fragment_activity;
    public static final int FRAGMENT_TOOLBAR_ACTIVITY = R.layout.fragment_toolbar_activity;
    public static final int OVERLAP_TOOLBAR_ACTIVITY = R.layout.overlap_toolbar_activity;

    protected static final String IS_BACK_STRATEGY = RichActivity.class.getCanonicalName() + ".IS_BACK_STRATEGY";

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private boolean mBackStrategy;
    private ActionBarDrawerToggle mActionBarDrawerToggle;

    View.OnClickListener mNavigationMenuListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    };

    View.OnClickListener mNavigationBackListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(getLayoutId(), null);
        setContentView(view);
        View toolbar = view.findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            prepareToolBar((Toolbar) toolbar);
        }
        View drawerLayout = view.findViewById(R.id.drawer_layout);
        View navigation = view.findViewById(R.id.navigation_menu);
        if (drawerLayout != null && navigation != null) {
            mDrawerLayout = (DrawerLayout) drawerLayout;
            mNavigationView = (NavigationView) navigation;
            prepareNavigationMenu(mNavigationView, mDrawerLayout);
        }

        if (savedInstanceState == null) {
            initStartFragment();
        }
    }

    public int getMainContainerId() {
        return R.id.main_container;
    }

    public <T extends Fragment> void addFragment(Class<T> clazz, Bundle args, int containerId) {
        FragmentUtils.addFragment(getSupportFragmentManager(), clazz, args, containerId);
    }

    public <T extends Fragment> void replaceFragment(Class<T> clazz, Bundle args, int containerId) {
        FragmentUtils.replaceFragment(getSupportFragmentManager(), clazz, args, containerId);
    }

    public <T extends Fragment> void addFragment(Class<T> clazz, Bundle args) {
        FragmentUtils.addFragment(getSupportFragmentManager(), clazz, args, getMainContainerId());
    }

    public <T extends Fragment> void replaceFragment(Class<T> clazz, Bundle args) {
        FragmentUtils.replaceFragment(getSupportFragmentManager(), clazz, args, getMainContainerId());
    }

    public <T extends Fragment> void addFragment(Class<T> clazz, Bundle args, Fragment target, int containerId) {
        FragmentUtils.addFragment(getSupportFragmentManager(), clazz, args, target, containerId);
    }

    public <T extends Fragment> void replaceFragment(Class<T> clazz, Bundle args, Fragment target, int containerId) {
        FragmentUtils.replaceFragment(getSupportFragmentManager(), clazz, args, target, containerId);
    }

    public <T extends Fragment> void addFragment(Class<T> clazz, Bundle args, Fragment target) {
        FragmentUtils.addFragment(getSupportFragmentManager(), clazz, args, target, getMainContainerId());
    }

    public <T extends Fragment> void replaceFragment(Class<T> clazz, Bundle args, Fragment target) {
        FragmentUtils.replaceFragment(getSupportFragmentManager(), clazz, args, target, getMainContainerId());
    }


    public <T extends Fragment> void switchFragment(Class<T> clazz, Bundle args, int containerId) {
        FragmentUtils.popAllFragments(getSupportFragmentManager());
        replaceFragment(clazz, args, containerId);
    }

    public <T extends Fragment> void switchFragment(Class<T> clazz, Bundle args) {
        switchFragment(clazz, args, getMainContainerId());
    }

    protected void prepareToolBar(Toolbar toolbar) {
    }

    protected void prepareNavigationMenu(NavigationView navigationView, DrawerLayout drawerLayout) {
    }

    protected void initStartFragment() {
        replaceFragment(getStartFragmentClass(), null);
    }

    public int getLayoutId() {
        return FRAGMENT_TOOLBAR_ACTIVITY;
    }

    protected abstract Class<? extends Fragment> getStartFragmentClass();

    public void setActionBarDrawerToggle(Toolbar toolbar) {
        mBackStrategy = false;
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
        setMenuStrategy(toolbar);
        //calling sync state is necessay or else your hamburger icon wont show up
        mActionBarDrawerToggle.syncState();
    }

    public void setMenuStrategy(Toolbar toolbar) {
        if (mActionBarDrawerToggle != null) {
            hideNavigationButton(toolbar, false);
            toolbar.setNavigationIcon(getBurgerIcon());
            toolbar.setNavigationOnClickListener(mNavigationMenuListener);
        } else {
            toolbar.setEnabled(false);
            toolbar.setNavigationOnClickListener(null);
            hideNavigationButton(toolbar, true);
        }
    }

    private void hideNavigationButton(Toolbar toolbar, boolean hide) {
        if (toolbar.getNavigationIcon() != null) {
            toolbar.setNavigationIcon(null);
        }
    }

    public void setBackStrategy(Toolbar toolbar) {
        hideNavigationButton(toolbar, false);
        toolbar.setNavigationIcon(getArrowIcon());
        toolbar.setNavigationOnClickListener(mNavigationBackListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mActionBarDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mActionBarDrawerToggle != null) {
            mActionBarDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    public Drawable getBurgerIcon() {
        return ContextCompat.getDrawable(this, R.drawable.ic_menu_black_24dp);
    }

    public Drawable getArrowIcon() {
        return ContextCompat.getDrawable(this, R.drawable.ic_arrow_left_black_24dp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
