/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public class PresentersHubHolderFragment extends Fragment {
    private PresentersHub mPresentersHub;

    public PresentersHubHolderFragment() {
        mPresentersHub = PresentersHub.getInstance();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static String tag() {
        return PresentersHubHolderFragment.class.getSimpleName();
    }
}
