package com.wxx.encry;

/**
 * @author ：wuxinxi on 2020/5/21 .
 * @packages ：com.wxx.encry .
 * TODO:一句话描述
 */
public class HexUtil {
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

}
