package com.fendany.utils.security;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by QIANDUO875 on 2016-04-22.
 */
public class ImgCodeUtils {

    private static final char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    private ImgCodeUtils() {}

    public static String genRamdonString(int lenght) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < lenght; i++) {
            sb.append(codeSequence[RandomUtils.nextInt(36)]);
        }
        return sb.toString();
    }

    public static byte[] genImgCodeToBytes(int width, int height, String code) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        genImgCodeToOutputStream(os, width, height, code);
        return os.toByteArray();
    }

    public static byte[] genImgCodeToBytes(String code) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        genImgCodeToOutputStream(os, 80, 32, code);
        return os.toByteArray();
    }

    public static void genImgCodeToFile(File file, String code) throws IOException {
        genImgCodeToFile(file, 80, 32, code);
    }

    public static void genImgCodeToFile(File file, int width, int height, String code) throws IOException {
        BufferedImage img = genImgCode(width, height, code);
        ImageIO.write(img, "JPEG", file);
    }

    public static void genImgCodeToOutputStream(OutputStream os, int width, int height, String code)
            throws IOException {
        BufferedImage img = genImgCode(width, height, code);
        ImageIO.write(img, "JPEG", os);
    }

    public static void genImgCodeToOutputStream(OutputStream os, String code)
            throws IOException {
        genImgCodeToOutputStream(os, 80, 32, code);
    }

    private static BufferedImage genImgCode(int width, int height, String code) {
        int length = code.length();
        int fontHeight = height - 2;
        int x = width / (length + 1);
        int codeY = height - 4;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.PLAIN, fontHeight));
        for (int i = 0; i < 100; i++) {
            g.setColor(getRandColor(160, 200));
            int rx = RandomUtils.nextInt(width);
            int ry = RandomUtils.nextInt(height);
            int xl = RandomUtils.nextInt(12);
            int yl = RandomUtils.nextInt(12);
            g.drawLine(rx, ry, rx + xl, ry + yl);
        }
        StringBuilder sRand = new StringBuilder("");
        for (int i = 0; i < length; i++) {
//            String rand = String.valueOf(codeSequence[RandomUtils.nextInt(36)]);
            String rand = String.valueOf(code.charAt(i));
            sRand.append(rand);
            g.setColor(new Color(20 + RandomUtils.nextInt(110), 20 + RandomUtils.nextInt(110), 20 + RandomUtils.nextInt(110)));
            g.drawString(rand, x / 8 + i * x, codeY);
        }
        g.dispose();
        return image;
    }

    private static Color getRandColor(int fc1, int bc1) {
        int fc = fc1;
        int bc = bc1;
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + RandomUtils.nextInt(bc - fc);
        int g = fc + RandomUtils.nextInt(bc - fc);
        int b = fc + RandomUtils.nextInt(bc - fc);
        return new Color(r, g, b);
    }
}
