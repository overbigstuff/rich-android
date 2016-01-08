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

/**
 * Fragment class with retain instance and ButterKnife binding.
 * The class also has convenient methods for adding and replacing the current fragment.
 * Use with {@link com.overbigstuff.richandroid.activity.RichActivity RichActivity} class.
 * Set {@link #setRecreateView(boolean)} to true (in {@link #prepare()} method) if you need to recreate the view every time, when the orientation changes.
 * If {@link #setRecreateView(boolean)} set to true, use {@link #createContent(View)} for creating fragment data (e.g. Adapters, Lists).
 * Override {@link #initView(View)} for initializing fragment view with fragment data.
 * If {@link #setRecreateView(boolean)} set to false, {@link #initView(View)} will be invoked once at the first initialization.
 * Invoke {@link #setToolbarBack()} in {@link #createContent(View)} if you need fully functional back button on the ToolBar.
 * ToolBar resource id - R.id.fragment_toolbar
 * Activity ToolBar resource id - R.id.main_toolbar
 */
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
        initAnyway(mView);
    }

    private boolean isRecreateView(Bundle savedInstanceState) {
        return savedInstanceState == null || mRecreateView;
    }

    @Override
    public void onDestroy() {
        if (getTargetFragment() != null) {
            if (getTargetFragment() instanceof RichFragment) {
                ((RichFragment) getTargetFragment()).resume();
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

    /**
     *
     * @param id - child view id in the root fragment view
     * @param <T> - type of the child view
     * @return - view with certain id
     */
    public <T extends View> T findById(int id) {
        return ButterKnife.findById(mView, id);
    }

    /**
     * Invoke in {@link #createContent(View)} if you need fully functional back button on the ToolBar
     */
    protected void setToolbarBack() {
        mToolBarBack = true;
    }

    /**
     * Invoke in {@link #createContent(View)} if you need menu button on the ToolBar
     */
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
        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title);
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

    /**
     * Invoked the moment of creating fragment. Override method if you need to change some initial conditions.
     *
     * @see #setRecreateView(boolean)
     */
    public void prepare() {

    }

    /**
     * Invoked anyway. Use this method if you need to initialize some things every time the orientation changes.
     * Invoked after {@link #createContent(View) createContent}, {@link #initView(View)  initView}
     */
    protected void initAnyway(View view) {

    }

    /**
     * Method for initializing view with fragment data
     * Invoked every time when fragment re-created, if the {@link #setRecreateView(boolean)} is set to true.
     * Invoked once time (When the view for the first time created), if the {@link #setRecreateView(boolean)} is set to false.
     * Invoked after {@link #createContent(View)}
     * Invoked before {@link #initAnyway(View)}
     *
     * @param view - Inflated fragment view.
     */
    protected abstract void initView(View view);

    /**
     * Override for fragment data creating(e.g. Adapters, Lists)
     * Invoked before {@link #initView(View)}, {@link #initAnyway(View)}
     * Invoked once time (When the view for the first time created)
     * @param view - Inflated fragment view.
     */
    protected void createContent(View view) {

    }

    /**
     * Invoked when the upper fragment was destroyed
     */
    public void resume() {
        if (getActivity().findViewById(R.id.main_toolbar) != null) {
            if (mToolBarBack) {
                getRichActivity().setBackStrategy(getToolbar());
            } else {
                getRichActivity().setMenuStrategy(getToolbar());
            }
            getActivity().setTitle(getActionBarTitle());
            if (getToolbar() != null) {
                TextView title = (TextView) getToolbar().findViewById(R.id.toolbar_title);
                if (title != null) {
                    title.setText(getCenterTitle());
                }
            }
        }
    }

    /**
     * @return centered title on the ToolBar
     */
    protected String getCenterTitle() {
        return "";
    }

    /**
     * @return standard title on the ToolBar
     */
    protected String getActionBarTitle() {
        return "";
    }

    /**
     * Override for own layout resource
     *
     * @return own fragment layout resource id
     */
    protected abstract int getLayoutId();

}
