package com.liaozl.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;


/**
 * @author liaozuliang
 * @date 2016-11-17
 */
@Component
@Aspect
public class UserServiceAspect {

    /**
     * 配置切入点,主要为方便同类中其他方法使用此处配置的切入点
     */
    private final String POINT_CUT = "execution(* com.liaozl.aop.service.UserService.*(..))";

    @Pointcut("execution(* com.liaozl.aop.service.UserService.*(..))")
    private void beforePointCut() {

    }

    /**
     * 前置通知[Before advice]：在连接点前面执行，前置通知不会影响连接点的执行，除非此处抛出异常。
     */
    @Before("beforePointCut()")
    public void before(JoinPoint joinPoint) {
        System.out.println("调用的方法：" + joinPoint.getSignature());

        Object[] params = joinPoint.getArgs();
        System.out.println("BankServiceAspect Before advice..., params: " + Arrays.asList(params));
    }

    /**
     * 正常返回通知[After returning advice]：在连接点正常执行完成后执行，如果连接点抛出异常，则不会执行。
     */
    @AfterReturning(value = POINT_CUT, returning = "value")
    public void afterReturning(JoinPoint joinPoint, Object value) {
        Object[] params = joinPoint.getArgs();
        System.out.println("BankServiceAspect AfterReturning advice..., params: " + Arrays.asList(params) + ",  return value: " + value);
    }

    /**
     * 异常返回通知[After throwing advice]：在连接点抛出异常后执行。
     */
    @AfterThrowing(pointcut = POINT_CUT, throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
        Object[] params = joinPoint.getArgs();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        ex.printStackTrace(pw);
        String errorMsg = sw.toString();

        System.out.println("BankServiceAspect AfterThrowing advice..., params: " + Arrays.asList(params) + ", 调用方法：" + joinPoint.getSignature() + "， 出错：" + errorMsg);
    }

    /**
     * 返回通知[After (finally) advice]：在连接点执行完成后执行，不管是正常执行完成，还是抛出异常，都会执行返回通知中的内容。
     */
    @After(POINT_CUT)
    public void after(JoinPoint joinPoint) {
        Object[] params = joinPoint.getArgs();
        System.out.println("BankServiceAspect After advice..., params: " + Arrays.asList(params));
    }

    /**
     * 环绕通知[Around advice]：环绕通知围绕在连接点前后，比如一个方法调用的前后。这是最强大的通知类型，能在方法调用前后自定义一些操作。
     * 环绕通知还需要负责决定是继续处理join point(调用ProceedingJoinPoint的proceed方法)还是中断执行。
     */
    @Around(POINT_CUT)
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] params = point.getArgs();
        System.out.println("BankServiceAspect Around(Before) advice..., old params: " + Arrays.asList(params));

        // params = ? 这里可以替换参数
        Object object = point.proceed(params);

        System.out.println("BankServiceAspect Around(After) advice..., new params: " + Arrays.asList(params));

        // object = ? 这里可以替换返回值

        return object;
    }

}
