package com.wms.wms.util;

import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerator {

    public static String generateQRCode(String text, String filePath) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);

            // ✅ Ensure directory exists
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // ✅ Write QR image
            ImageIO.write(
                MatrixToImageWriter.toBufferedImage(matrix),
                "PNG",
                file
            );

            return filePath; // return path to save in DB

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("QR Code generation failed");
        }
    }
}