package com.liaozl.utils.sign;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liaozuliang
 * @date 2017-02-10
 */
public class SignTest {

    static String nwPubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEAw9La2u0n6lH8wsSlO62gm5nxmR/jkYlB14zLnpLKHYnYqYZp5JltPnamev3hft8wI9uw2FoV8E7mGxrhDaaxn/FSKuT2IvuwuBjjE+wMOaBh4AqZiKu63edF1bXjAajScjl9U7tH8TtU/+rDPKI7mjcX0N6SQLEybEB0hcIzQIDAQAB";
    static String nwPriKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMQDD0tra7SfqUfzCxKU7raCbmfGZH+ORiUHXjMueksodidiphmnkmW0+dqZ6/eF+3zAj27DYWhXwTuYbGuENprGf8VIq5PYi+7C4GOMT7Aw5oGHgCpmIq7rd50XVteMBqNJyOX1Tu0fxO1T/6sM8ojuaNxfQ3pJAsTJsQHSFwjNAgMBAAECgYBs+x6H2mP5+0ONg25GmJPY2dA01rON0Dbj46LWZiMMoqR+5XRgkzDsZ6D7j9UIvG/FPvMArJT+BLylO+wcI1iP4G22UY/9oGANSCNVdH6rjUu26QRRcsJb7jvlijnidhfQx315D99O0umCsLedRdUUuUsmihDYBHV0p1SyYW2wKQJBAOJfuC2Pjmz/S/jA4cuMeoeUZamZu+gcXrmyCG/5wZ69J2w+CK5MrFWxXY8l/tjd5xrfon0TM87kHp8hyaj3rssCQQDdqh0Z8OdOQTYLo8nPHz69H8aZPKzNVNOulySQ4rjvj7hRnkBQtzNMcRNX6J+PqgykMr3i+tLePRq64nJxxFvHAkEApxZJxm9AjBlABSKxRuGwiyQwiaesd52BGYVcDvKFRFqPn6iGFckIJtQcn41G9uGLNF2+/JzQz7O3GnQA+hOfUwJAQeWfryWR7g6u3Xj8tsloIls+9DE4gfZU09N0+GmeZwGH3yO3pQdBhdkb0geC3/fCuFGpZ7//TjhGKQbG1RF14wJAG3j0YsODVVj2FNU66UB2jWMkFYjW0xNVrPZ5DPJpWM7RghsEDt6SeGsz8KuElY5uZN/xZ6LK6xvjg8/gkvv/KA==";
    static String merchantPubkey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4C9GvLRjYgymywuLOgObm37DAr2uwj/Vq5COnwiEzP67L5oW0xBOwniXLEnkQqJXiBaY/2uuEbJ4urSMs/ACpyKcYSU43ZKUlZc69vYJyeimV7vqHa+Ebu0amsjrDvPfYUSQSuI/tmviFgz/y2QAIt9oW/hL9xs0ilaLUpsrASQIDAQAB";
    static String merchantPriKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALgL0a8tGNiDKbLC4s6A5ubfsMCva7CP9WrkI6fCITM/rsvmhbTEE7CeJcsSeRColeIFpj/a64Rsni6tIyz8AKnIpxhJTjdkpSVlzr29gnJ6KZXu+odr4Ru7RqayOsO899hRJBK4j+2a+IWDP/LZAAi32hb+Ev3GzSKVotSmysBJAgMBAAECgYEAp5KjVaNzBtfN5KSLS8iT9Exhq99M5xL8OloEmN8pyXbHWYHH5cHNx7cG5DIqpt9IsOXB7EwQup8HG6qq529/zYvM0zBfGkuhskkBln1ijuebZBQiBR9+NVjfDqyLZBPOMpf3L8XXdmqCWHdYNCo+2QJguIxEYA9ItoPb4/xKugECQQDaAD3lRUxyr6BuE3O1skYKpN339F8ldhB9zj9r708dCLzqSw9NO7JQH/0z+CkRVPTK4N3SfYK/qZIeLuJP5tbBAkEA2CBttw/IksJ/G8ulQvUh4HKnalknffdTRI5VIPhFN6AbEhmnoE6+TDpxfLk6LT9AWYIIlpKx4SMrpQjSPDGTiQJAbWvXpX++Goaa0QWEe7vKukb8fxuysjvrlSnz3XpVzG35XORl/s8q9lrIHdC+1Es/P6HU1XVYLlmv1OSVhaOJQQJBAKUGNeAWrCbrXdTibUCwHfrccNqyUoZLaAgryfKPv4JWvPTYI/cyyPk5ktgSMsrDxiXSNS6LukjW6wdrseOGU0kCQQCXyGrUUOAfL15mG1ZTNPLg6s3UGRk8KilPI3ny0KMMsbiUGLnSsh8PBlZBWSz+1zc1ePaUeSAIEsYrchwv0/Tu";

    public static void testCreateSign() {
        Map<String, String> params = new HashMap<String, String>();

        // 加签
        String signedStr = ApiSignUtil.createSign(params, nwPriKey);

        System.out.println(signedStr);
    }

    public static void testCheckSign(Map<String, String> params) {
        // 验签
        boolean checkSignResult = ApiSignUtil.checkSign(params, merchantPubkey);
        if (!checkSignResult) {
            System.out.println("验签失败");
        }
    }

    public static void main(String[] args) {
        //BeanMap map = BeanMap.create(null);
        //BeanCopier copier = BeanCopier.create(null, null, false);
    }
}
