package com.google.zxing.client.android.result;

import java.io.Serializable;

/**
 * 条形码
 * @author pengfan
 *
 */
public class BarCode implements Serializable {

    private static final long serialVersionUID = -819514891787264012L;

    private String content;
    private String encoding;
    private String altText;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getAltText() {
        return altText;
    }

    public void setAltText(String altText) {
        this.altText = altText;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
