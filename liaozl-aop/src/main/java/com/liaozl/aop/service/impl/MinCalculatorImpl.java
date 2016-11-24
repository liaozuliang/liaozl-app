package com.liaozl.aop.service.impl;

import com.liaozl.aop.service.MinCalculator;

/**
 * @author liaozuliang
 * @date 2016-11-18
 */
public class MinCalculatorImpl implements MinCalculator {

    @Override
    public double min(double a, double b) {
        double result = a <= b ? a : b;
        System.out.println("min(" + a + "," + b + ")=" + result);
        return result;
    }

}
