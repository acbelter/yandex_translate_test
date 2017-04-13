/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

public class LanguageModel {
    public Long _id; // for cupboard
    public String code;
    public String description;

    public LanguageModel() {}

    public LanguageModel(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
