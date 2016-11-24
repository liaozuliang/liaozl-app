package com.liaozl.aop.service.impl;

import com.liaozl.aop.service.MaxCalculator;

/**
 * @author liaozuliang
 * @date 2016-11-18
 */
public class MaxCalculatorImpl implements MaxCalculator {

    @Override
    public double max(double a, double b) {
        double result = a <= b ? b : a;
        System.out.println("max(" + a + "," + b + ")=" + result);
        return result;
    }

}