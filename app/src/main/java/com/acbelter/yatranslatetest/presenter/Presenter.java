/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.presenter;

public interface Presenter<V> {
    String KEY_PRESENTER_ID = "presenter_id";

    void present(V view);
    void setId(int id);
    int getId();
}
