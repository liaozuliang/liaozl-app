package com.liaozl.utils.sign;

import sun.misc.BASE64Decoder;

import java.io.UnsupportedEncodingException;

@SuppressWarnings("restriction")
public class Base64{
    public static String getBASE64(String s)
    {
        if (s == null)
            return null;
        try
        {
            return (new sun.misc.BASE64Encoder()).encode(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBASE64(byte[] b)
    {
        return (new sun.misc.BASE64Encoder()).encode(b);
    }

    public static String getFromBASE64(String s)
    {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            byte[] b = decoder.decodeBuffer(s);
            return new String(b, "UTF-8");
        } catch (Exception e)
        {
            return null;
        }
    }
    
    
    public static byte[] getBytesBASE64(String s)
    {
        if (s == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        try
        {
            byte[] b = decoder.decodeBuffer(s);
            return b;
        } catch (Exception e)
        {
            return null;
        }
    }
}
