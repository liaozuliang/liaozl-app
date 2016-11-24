package com.liaozl.aop.aspect;

import com.liaozl.aop.service.MaxCalculator;
import com.liaozl.aop.service.MinCalculator;
import com.liaozl.aop.service.impl.MaxCalculatorImpl;
import com.liaozl.aop.service.impl.MinCalculatorImpl;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Component;

/**
 * 引入增强
 * @author liaozuliang
 * @date 2016-11-18
 */
@Component
@Aspect
public class CalculatorIntroductionAspect {

    @DeclareParents(value = "com.liaozl.aop.service.impl.ArithmeticCalculatorImpl",
            defaultImpl = MaxCalculatorImpl.class)
    public MaxCalculator maxCalculator;

    @DeclareParents(value = "com.liaozl.aop.service.impl.ArithmeticCalculatorImpl",
            defaultImpl = MinCalculatorImpl.class)
    public MinCalculator minCalculator;
}
