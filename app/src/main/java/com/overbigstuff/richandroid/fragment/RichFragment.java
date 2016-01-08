package com.overbigstuff.richandroid.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.overbigstuff.richandroid.R;
import com.overbigstuff.richandroid.activity.RichActivity;

import butterknife.ButterKnife;

public abstract class RichFragment extends Fragment {
    private View mView;
    private boolean mToolBarBack;
    private boolean mRecreateView;
    private Toolbar mToolbar;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        prepare();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (isRecreateView(savedInstanceState)) {
            mView = inflater.inflate(getLayoutId(), container, false);
            mView.setClickable(true);
            ButterKnife.unbind(this);
            ButterKnife.bind(this, mView);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            createContent(mView);
        }
        if (isRecreateView(savedInstanceState)) {
            initView(mView);
        }

        initToolBar();
        initAnyway();
    }

    private boolean isRecreateView(Bundle savedInstanceState) {
        return savedInstanceState == null || mRecreateView;
    }

    @Override
    public void onDestroy() {
        if (getTargetFragment() != null) {
            if (getTargetFragment() instanceof RichFragment) {
                ((RichFragment)getTargetFragment()).resume();
            }
        }
        super.onDestroy();
    }

    public RichActivity getRichActivity() {
        return (RichActivity) getActivity();
    }

    public <T extends Fragment> void addFragment(Class<T> clazz, Bundle args, int containerId) {
        getRichActivity().addFragment(clazz, args, this, containerId);
    }

    public <T extends Fragment> void replaceFragment(Class<T> clazz, Bundle args, int containerId) {
        getRichActivity().replaceFragment(clazz, args, this, containerId);
    }

    public <T extends Fragment> void addFragment(Class<T> clazz, Bundle args) {
        getRichActivity().addFragment(clazz, args, this);
    }

    public <T extends Fragment> void replaceFragment(Class<T> clazz, Bundle args) {
        getRichActivity().replaceFragment(clazz, args, this);
    }

    public <T extends Fragment> void switchFragment(Class<T> clazz, Bundle args, int containerId) {
        getRichActivity().switchFragment(clazz, args, containerId);
    }

    public <T extends Fragment> void switchFragment(Class<T> clazz, Bundle args) {
        getRichActivity().switchFragment(clazz, args);
    }

    public <T extends View> T findById(int id) {
        return ButterKnife.findById(mView, id);
    }

    protected void setToolbarBack() {
        mToolBarBack = true;
    }

    protected void setToolBarMenu() {
        mToolBarBack = false;
    }

    protected void setRecreateView(boolean isRecreateView) {
        mRecreateView = isRecreateView;
    }

    private Toolbar findToolBar() {
        final AppCompatActivity activity = (AppCompatActivity) getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.main_toolbar);
        if (toolbar == null) {
            toolbar = findById(R.id.fragment_toolbar);
        }
        return toolbar;
    }


    protected void initToolBar() {
        final RichActivity activity = getRichActivity();
        mToolbar = findToolBar();
        if (mToolbar == null) {
            return;
        }
        activity.setTitle(getActionBarTitle());
        TextView title = (TextView)mToolbar.findViewById(R.id.toolbar_title);
        title.setText(getCenterTitle());
        activity.setSupportActionBar(mToolbar);
        if (mToolBarBack) {
            activity.setBackStrategy(mToolbar);
        } else {
            NavigationView navigationView = (NavigationView) activity.findViewById(R.id.navigation_menu);
            if (navigationView != null) {
                activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
                activity.setActionBarDrawerToggle(mToolbar);
            }
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void prepare() {

    }

    protected void initAnyway() {

    }

    protected abstract void initView(View view);

    protected void createContent(View view) {

    }

    public void resume() {
        if (getActivity().findViewById(R.id.main_toolbar) != null) {
            if (mToolBarBack) {
                getRichActivity().setBackStrategy(getToolbar());
            } else {
                getRichActivity().setMenuStrategy(getToolbar());
            }
            getActivity().setTitle(getActionBarTitle());
            if (getToolbar() != null) {
                TextView title = (TextView)getToolbar().findViewById(R.id.toolbar_title);
                if (title != null) {
                    title.setText(getCenterTitle());
                }
            }
        }
    }

    protected String getCenterTitle() {
       return "";
    }

    protected String getActionBarTitle() {
       return "";
    }

    protected abstract int getLayoutId();

}
