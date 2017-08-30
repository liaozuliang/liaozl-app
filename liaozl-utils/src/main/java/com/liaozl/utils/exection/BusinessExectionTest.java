package com.liaozl.utils.exection;

/**
 * @author liaozuliang
 * @date 2017-08-30
 */
public class BusinessExectionTest {

    public static void main(String[] args) {

        try {
            throw new BusinessExection("测试一下异常1, 会打印堆栈信息", true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            throw new BusinessExection("测试一下异常2，不打印堆栈信息", false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            throw new BusinessExection("测试一下异常3，不打印堆栈信息", null, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
