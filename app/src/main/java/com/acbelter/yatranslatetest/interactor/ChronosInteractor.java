/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.interactor;

import android.content.Context;

import com.acbelter.yatranslatetest.model.LanguageModel;
import com.acbelter.yatranslatetest.operation.InitDataOperation;
import com.acbelter.yatranslatetest.operation.TranslateOperation;
import com.redmadrobot.chronos.gui.ChronosConnectorWrapper;

/**
 * Класс, управляющий запуском асинхронных операций через библиотеку
 * @see <a href="https://github.com/RedMadRobot/Chronos">Chronos</a>
 */
public class ChronosInteractor implements Interactor {
    private ChronosConnectorWrapper mChronosConnectorWrapper;

    public ChronosInteractor(ChronosConnectorWrapper wrapper) {
        mChronosConnectorWrapper = wrapper;
    }

    @Override
    public void startInitData(Context context) {
        // Запуск асинхронной операции загрузки необходимых данных
        mChronosConnectorWrapper.runOperation(new InitDataOperation(context),
                InitDataOperation.class.getSimpleName());
    }

    @Override
    public void startTranslation(String text,
                                 LanguageModel langFrom,
                                 LanguageModel langTo) {
        if (text == null || langTo == null) {
            return;
        }

        // При запуске нового перевода нужно отменить выполнение предыдущего перевода
        cancelTranslation();
        // Запуск асинхронной операции перевода текста
        mChronosConnectorWrapper.runOperation(
                new TranslateOperation(text, langFrom, langTo),
                TranslateOperation.class.getSimpleName());
    }

    @Override
    public void cancelTranslation() {
        // Отмена асинхронной операции
        mChronosConnectorWrapper.cancelOperation(TranslateOperation.class.getSimpleName());
    }
}
