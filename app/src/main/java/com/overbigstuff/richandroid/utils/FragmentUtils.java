package com.overbigstuff.richandroid.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    public static <T extends Fragment> void addFragment(FragmentManager fragmentManager, Class<T> clazz, Bundle args, Fragment targetFragment, int containerId) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            T fragment = clazz.newInstance();
            fragment.setArguments(args);
            fragment.setTargetFragment(targetFragment, 0);
            transaction.add(containerId, fragment, clazz.getCanonicalName());
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Instantiation failed!", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Illegal Access!", e);
        }

    }

    public static <T extends Fragment> void replaceFragment(FragmentManager fragmentManager, Class<T> clazz, Bundle args, Fragment targetFragment, int containerId) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            T fragment = clazz.newInstance();
            fragment.setArguments(args);
            fragment.setTargetFragment(targetFragment, 0);
            transaction.replace(containerId, fragment, clazz.getCanonicalName());
            transaction.commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("Instantiation failed!", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Illegal Access!", e);
        }
    }

    public static <T extends Fragment> void addFragment(FragmentManager fragmentManager, Class<T> clazz, Bundle args, int containerId) {
        addFragment(fragmentManager, clazz, args, null, containerId);
    }

    public static <T extends Fragment> void replaceFragment(FragmentManager fragmentManager, Class<T> clazz, Bundle args, int containerId) {
        replaceFragment(fragmentManager, clazz, args, null, containerId);
    }

    public static void popAllFragments(FragmentManager fragmentManager) {
        int count = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            fragmentManager.popBackStack();
        }
    }
}
