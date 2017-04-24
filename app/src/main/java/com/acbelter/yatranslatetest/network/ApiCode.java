/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.network;

public class ApiCode {
    // Операция выполнена успешно
    public static final int CODE_OK = 200;
    // Неправильный API-ключ
    public static final int CODE_API_KEY_INVALID = 401;
    // API-ключ заблокирован
    public static final int CODE_API_KEY_BLOCKED = 402;
    // Превышено суточное ограничение на объем переведенного текста
    public static final int CODE_LIMIT_EXCEEDED = 404;
    // Превышен максимально допустимый размер текста
    public static final int CODE_MAX_TEXT_LIMIT = 413;
    // Превышен максимально допустимый размер текста (в строке запроса)
    public static final int CODE_MAX_URL_LIMIT = 414;
    // Текст не может быть переведен
    public static final int CODE_UNABLE_TO_TRANSLATE = 422;
    // Заданное направление перевода не поддерживается
    public static final int CODE_UNSUPPORTED_TRANSLATE_DIRECTION = 501;
    // Ошибка при получении данных с сервера
    public static final int CODE_SERVER_ERROR = 500;
}
