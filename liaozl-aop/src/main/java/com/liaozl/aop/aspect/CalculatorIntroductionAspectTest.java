package com.liaozl.aop.aspect;

import com.liaozl.aop.service.ArithmeticCalculator;
import com.liaozl.aop.service.MaxCalculator;
import com.liaozl.aop.service.MinCalculator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liaozuliang
 * @date 2016-11-18
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-app.xml"})
public class CalculatorIntroductionAspectTest implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Test
    public void test() {
        ArithmeticCalculator ac = (ArithmeticCalculator) applicationContext.getBean("arithmeticCalculator");
        ac.add(11, 23);
        ac.div(32, 54);
        ac.mul(23, 6);
        ac.sub(3, 7);

        MinCalculator min = (MinCalculator) ac;
        min.min(2.3, 4.5);

        MaxCalculator max = (MaxCalculator) ac;
        max.max(3, 5);
    }

}

