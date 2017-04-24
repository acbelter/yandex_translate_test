/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Хаб презентеров для хранения презенторов, добавления и получения презентера по его id.
 * У каждого хаба презентеров есть свой уникальный идентификатор7
 */
public class PresentersHub {
    private static PresentersHub sInstance;

    private UUID mHubId;
    private AtomicInteger mPresenterIdGenerator;
    private SparseArray<Presenter> mIdPresenterMap;

    private PresentersHub() {
        mHubId = UUID.randomUUID();
        mPresenterIdGenerator = new AtomicInteger();
        mIdPresenterMap = new SparseArray<>();
    }

    public static PresentersHub getInstance() {
        if (sInstance == null) {
            sInstance = new PresentersHub();
        }
        return sInstance;
    }

    public UUID getHubId() {
        return mHubId;
    }

    public PresenterId getIdForPresenter(Presenter presenter) {
        return new PresenterId(mHubId, presenter.getId());
    }
    /**
     * Добавление презентера в хаб если его там еще нет и получение его id
     * Если презентер уже находится в хабе, то просто возвращается его id
     * @param presenter Презентер нужно добавить в хаб
     * @return Идентификатор презентера в хабе
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

    public Presenter getPresenterById(PresenterId id) {
        if (!mHubId.equals(id.getHubId())) {
            return null;
        }

        int index = mIdPresenterMap.indexOfKey(id.getId());
        if (index >= 0) {
            return mIdPresenterMap.valueAt(index);
        }
        return null;
    }

    public void removePresenterById(int presenterId) {
        mIdPresenterMap.remove(presenterId);
    }

    /**
     * Прикрепление презентера к FragmentManager с помощью retain-фрагмента для его сохранения
     * @param fragmentManager
     */
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
