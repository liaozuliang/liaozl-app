package com.liaozl.utils.sign;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


/**
 * @ClassName: RSAUtil
 * @Description: RSA签名公共类
 * @author WangJiChao
 * @date 2017年2月6日下午4:38:56
 *
 */
public class RSAUtil {
	/**
     * 签名处理
     * @param prikeyvalue：私钥内容
     * @param srcStr：签名源内容
     * @return
     */
    public static String sign(String prikeyvalue, String srcStr)
    {
        try
        {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(
                    Base64.getBytesBASE64(prikeyvalue));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey myprikey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signet = java.security.Signature
                    .getInstance("MD5withRSA");
            signet.initSign(myprikey);
            signet.update(srcStr.getBytes("UTF-8"));
            byte[] signed = signet.sign();
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signed));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 检查签名
     * @param pubkeyvalue 公钥内容
     * @param srcStr 签名源内容
     * @param signedStr 签名后字符串
     * @return
     */
    public static boolean checksign(String pubkeyvalue, String srcStr,
            String signedStr)
    {
        try
        {
            X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(
                    Base64.getBytesBASE64(pubkeyvalue));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(bobPubKeySpec);
            byte[] signed = Base64.getBytesBASE64(signedStr);
            java.security.Signature signetcheck = java.security.Signature
                    .getInstance("MD5withRSA");
            signetcheck.initVerify(pubKey);
            signetcheck.update(srcStr.getBytes("UTF-8"));
            return signetcheck.verify(signed);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
