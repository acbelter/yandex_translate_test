/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import java.util.concurrent.atomic.AtomicInteger;

public class PresentersHub {
    private static PresentersHub sInstance = new PresentersHub();

    private AtomicInteger mPresenterIdGenerator;
    private SparseArray<Presenter> mIdPresenterMap;

    private PresentersHub() {
        mPresenterIdGenerator = new AtomicInteger();
        mIdPresenterMap = new SparseArray<>();
    }

    public static PresentersHub getInstance() {
        return sInstance;
    }

    /**
     * Add presenter to hub if it isn't there and return it's id.
     * If presenter is already in hub, just returns it's id.
     * @param presenter Presenter to add
     * @return Presenter id in hub or -1
     */
    public int addPresenter(Presenter presenter) {
        if (presenter == null) {
            return -1;
        }

        for (int i = 0; i < mIdPresenterMap.size(); i++) {
            if (mIdPresenterMap.valueAt(i) == presenter) {
                return mIdPresenterMap.keyAt(i);
            }
        }

        int newPresenterId = mPresenterIdGenerator.getAndIncrement();
        mIdPresenterMap.append(newPresenterId, presenter);
        presenter.setId(newPresenterId);
        return newPresenterId;
    }

    public Presenter getPresenterById(int presenterId) {
        int index = mIdPresenterMap.indexOfKey(presenterId);
        if (index >= 0) {
            return mIdPresenterMap.valueAt(index);
        }
        return null;
    }

    public void removePresenterById(int presenterId) {
        mIdPresenterMap.remove(presenterId);
    }

    public static void attach(FragmentManager fragmentManager) {
        PresentersHubHolderFragment holderFragment =
                (PresentersHubHolderFragment) fragmentManager
                        .findFragmentByTag(PresentersHubHolderFragment.tag());
        if (holderFragment == null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(new PresentersHubHolderFragment(), PresentersHubHolderFragment.tag());
            ft.commit();
        }
    }
}
