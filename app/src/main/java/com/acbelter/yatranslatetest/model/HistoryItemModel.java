/*
 * Created by acbelter <acbelter@gmail.com>
 */

package com.acbelter.yatranslatetest.model;

public class HistoryItemModel {
    // cupboard needs the fields to be public
    public Long _id;    // field for cupboard
    public String langFromCode;
    public String langToCode;
    public String originalText;
    public String translationText;
    public boolean isFavorite;
    public long timestamp;
}
