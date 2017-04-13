/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.interactor;

import android.content.Context;

import com.acbelter.yatranslatetest.operation.InitDataOperation;
import com.redmadrobot.chronos.gui.ChronosConnectorWrapper;

public class ChronosInteractor implements Interactor {
    private ChronosConnectorWrapper mChronosConnectorWrapper;

    public ChronosInteractor(ChronosConnectorWrapper wrapper) {
        mChronosConnectorWrapper = wrapper;
    }

    @Override
    public void initData(Context context) {
        mChronosConnectorWrapper.runOperation(new InitDataOperation(context),
                InitDataOperation.class.getSimpleName());
    }
}
