package com.overbigstuff.richandroid.fragment;

public abstract class RichRecreateViewFragment extends RichFragment {

    @Override
    public void prepare() {
        setRecreateView(true);
    }
}
