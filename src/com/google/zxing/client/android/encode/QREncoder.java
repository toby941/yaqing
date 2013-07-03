package com.google.zxing.client.android.encode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

import com.airAd.framework.util.LogUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.result.BarCode;
import com.google.zxing.common.BitMatrix;

/**
 * 二维码对象
 * 
 * @author pengfan
 */
public class QREncoder {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;
    private static final int margin = 0;

    private BarcodeFormat format = BarcodeFormat.QR_CODE;
    private int size;

    public QREncoder(int size) {
        this.size = size;
    }

    /**
     * 生成文件到哪个文件夹下
     * 
     * @param folderName
     * @param content
     */
    public void encode(String folderName, BarCode barcode) {
        Bitmap bitmap;
        try {
            bitmap = encodeAsBitmap(barcode.getContent());
        } catch (WriterException we) {
            Log.e(QREncoder.class.toString(), we.getMessage());
            return;
        }
        if (bitmap == null) {
            return;
        }
        // File bsRoot = new File(TicketService.parentFolderPath + folderName);
        // File barcodeFile = new File(bsRoot, ConfigUtil.QR_CODE_FILE_NAME);
        File barcodeFile = null;
        barcodeFile.delete();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(barcodeFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
        } catch (FileNotFoundException fnfe) {
            LogUtil.w(QREncoder.class, "Couldn't access file " + barcodeFile + " due to " + fnfe);
            return;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ioe) {
                    // do nothing
                }
            }
        }

    }

    /**
     * 生成图片
     * 
     * @param contentsToEncode
     * @return
     * @throws WriterException
     */
    Bitmap encodeAsBitmap(String contentsToEncode) throws WriterException {
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
        hints.put(EncodeHintType.MARGIN, margin);
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contentsToEncode, format, size, size, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }
}
