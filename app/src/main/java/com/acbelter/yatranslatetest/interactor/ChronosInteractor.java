/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.interactor;

import android.content.Context;

import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.operation.InitDataOperation;
import com.acbelter.yatranslatetest.operation.TranslateOperation;
import com.redmadrobot.chronos.gui.ChronosConnectorWrapper;

public class ChronosInteractor implements Interactor {
    private ChronosConnectorWrapper mChronosConnectorWrapper;

    public ChronosInteractor(ChronosConnectorWrapper wrapper) {
        mChronosConnectorWrapper = wrapper;
    }

    @Override
    public void startInitData(Context context) {
        mChronosConnectorWrapper.runOperation(new InitDataOperation(context),
                InitDataOperation.class.getSimpleName());
    }

    @Override
    public void startTranslation(String text,
                                 LanguageModel langFrom,
                                 LanguageModel langTo) {
        // Cancel previous translation operations
        mChronosConnectorWrapper.cancelOperation(TranslateOperation.class.getSimpleName());
        mChronosConnectorWrapper.runOperation(
                new TranslateOperation(text, langFrom, langTo),
                TranslateOperation.class.getSimpleName());
    }
}
