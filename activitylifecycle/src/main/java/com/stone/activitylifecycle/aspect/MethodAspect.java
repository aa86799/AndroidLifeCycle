package com.stone.activitylifecycle.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 对于方法的横切
 */
@Aspect //AspectJ 在编译时会查找被 @Aspect 注解的 class，然后 AOP 的过程会自动完成。
public class MethodAspect {
    private static final String TAG = "MethodAspect";

    /*
    合并 Pointcut 和 Advice
     */
    @Before("call(* com.stone.activitylifecycle.MainActivity.onCreate(..))")
    public void beforeMethodCall(JoinPoint joinPoint) {
        Log.e(TAG, "before->" + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
        Log.e(TAG, joinPoint.getThis().toString());
    }

    /*
    call 调用点  joinPoint.getThis() => 调用方
    execution 执行点  joinPoint.getThis() => 实际的执行方

    pointcut 多个条件可使用 &&、||、!
     */
    @After("execution(public * com.stone.aspectj.activitylifecycle..*(..)) || ")
    public void afterMethodCall(JoinPoint joinPoint) {
        Log.e(TAG, "after->" + joinPoint.getTarget().toString() + "#" + joinPoint.getSignature().getName());
        Log.e(TAG, joinPoint.getThis().toString());
    }

    /**
     * 任意类的任意方法(成员方法和构造方法)，抛出异常，而在调用端不论是否处理该异常都能切入
     *
     * @param throwable
     */
    @AfterThrowing(pointcut = "call(* *..*(..))", throwing = "throwable")
//    @AfterThrowing(pointcut = "call(* com.stone..*.*(..))", throwing = "throwable")
    public void anyFuncThrows(Throwable throwable) {
        Log.e(TAG, "methodThrows: ", throwable);
    }


}