package com.cainiao.funcommonlibrary.util;

import android.net.http.SslCertificate;
import android.os.Bundle;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

public class HttpsUtil {

    /**
     * SSL证书错误，手动校验https证书
     *
     * @param cert      https证书
     * @param sha256Str sha256值
     * @return true通过，false失败
     */
    public static boolean isSSLCertOk(SslCertificate cert, String sha256Str) {
        byte[] SSLSHA256 = hexToBytes(sha256Str);
        Bundle bundle = SslCertificate.saveState(cert);
        if (bundle != null) {
            byte[] bytes = bundle.getByteArray("x509-certificate");
            if (bytes != null) {
                try {
                    CertificateFactory cf = CertificateFactory.getInstance("X.509");
                    Certificate ca = cf.generateCertificate(new ByteArrayInputStream(bytes));
                    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                    byte[] key = sha256.digest(((X509Certificate) ca).getEncoded());
                    return Arrays.equals(key, SSLSHA256);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * hexString转byteArr
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     * @param hexString
     * @return 字节数组
     */
    public static byte[] hexToBytes(String hexString) {

        if (hexString == null || hexString.trim().length() == 0)
            return null;

        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] bytes = new byte[length];
        String hexDigits = "0123456789abcdef";
        for (int i = 0; i < length; i++) {
            int pos = i * 2; // 两个字符对应一个byte
            int h = hexDigits.indexOf(hexChars[pos]) << 4; // 注1
            int l = hexDigits.indexOf(hexChars[pos + 1]); // 注2
            if (h == -1 || l == -1) { // 非16进制字符
                return null;
            }
            bytes[i] = (byte) (h | l);
        }
        return bytes;
    }
}
