/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class PresentersHub {
    private static PresentersHub sInstance = new PresentersHub();

    private AtomicInteger mPresenterIdGenerator;
    private Map<Integer, Presenter> mIdPresenterMap;

    private PresentersHub() {
        mPresenterIdGenerator = new AtomicInteger();
        mIdPresenterMap = new HashMap<>();
    }

    public static PresentersHub getInstance() {
        return sInstance;
    }

    public int addPresenter(Presenter presenter) {
        for (Map.Entry<Integer, Presenter> entry : mIdPresenterMap.entrySet()) {
            if (entry.getValue().equals(presenter)) {
                return entry.getKey();
            }
        }

        int newPresenterId = mPresenterIdGenerator.getAndIncrement();
        mIdPresenterMap.put(newPresenterId, presenter);
        presenter.setId(newPresenterId);
        return newPresenterId;
    }

    public Presenter getPresenterById(int presenterId) {
        if (mIdPresenterMap.containsKey(presenterId)) {
            return mIdPresenterMap.get(presenterId);
        }
        return null;
    }

    public void removePresenterById(int presenterId) {
        mIdPresenterMap.remove(presenterId);
    }
}
