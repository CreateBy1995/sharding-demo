//package pers.sharding;
//
//import org.springframework.core.KotlinDetector;
//import org.springframework.core.ReactiveAdapter;
//import org.springframework.lang.Nullable;
//import org.springframework.transaction.*;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.interceptor.DelegatingTransactionAttribute;
//import org.springframework.transaction.interceptor.TransactionAspectSupport;
//import org.springframework.transaction.interceptor.TransactionAttribute;
//import org.springframework.transaction.interceptor.TransactionAttributeSource;
//import org.springframework.transaction.support.AbstractPlatformTransactionManager;
//import org.springframework.transaction.support.CallbackPreferringPlatformTransactionManager;
//
//import java.lang.reflect.Method;
//
///**
// * @Author: dongcx
// * @CreateTime: 2024-05-28
// * @Description:
// */
//public class Test {
//
//    protected Object invokeWithinTransaction(Method method, @Nullable Class<?> targetClass,
//                                             final TransactionAspectSupport.InvocationCallback invocation) throws Throwable {
//
//        // 1.获取事务注解（@Transactional）配置的信息，例如隔离级别、事务管理器等信息。
//        final TransactionAttribute txAttr = (tas != null ? tas.getTransactionAttribute(method, targetClass) : null);
//
//        // 1.事务开启 是否开启事务，取决于事务的传播方式
//        // PROPAGATION_REQUIRED，如果当前无事务，则开启事务；如果存在事务，则加入已有的事务。
//        // PROPAGATION_REQUIRES_NEW，不论是否存在事务都会开启新事务，且将之前的事务（如果存在）挂起，挂起的原理其实就是将当前的事务信息绑定到当前上下文，同时记录旧的事务信息。
//        // 详见AbstractPlatformTransactionManager#suspend方法
//        TransactionAspectSupport.TransactionInfo txInfo = createTransactionIfNecessary(ptm, txAttr, joinpointIdentification);
//
//        Object retVal;
//        try {
//            // 2.业务方法调用
//            retVal = invocation.proceedWithInvocation();
//        } catch (Throwable ex) {
//            // 4.异常处理，提交或者回滚，取决于事务注解配置
//            // 比如业务方法抛出的异常，不属于@Transactional配置的需要回滚的异常，则仍旧提交事务。
//            // 默认情况下@Transactional需要回滚的异常为RuntimeException&Error，而对于检查性异常，事务将不会回滚。
//            completeTransactionAfterThrowing(txInfo, ex);
//            throw ex;
//        }
//        // 5.判断是否提交事务，取决于事务的传播级别。
//        // PROPAGATION_REQUIRED，如果当前为父事务，则提交；如果是子事务，则不提交，由外层的父事务进行提交。
//        // PROPAGATION_REQUIRES_NEW， 提交事务，同时恢复之前的事务（如果存在），恢复的原理其实就是将当前的事务信息与当前上下文解绑，同时将之前记录的旧事务信息绑定到上下文。
//        // 详见AbstractPlatformTransactionManager#resume方法
//        commitTransactionAfterReturning(txInfo);
//        return retVal;
//    }
//
//
//    protected TransactionAspectSupport.TransactionInfo createTransactionIfNecessary(@Nullable PlatformTransactionManager tm,
//                                                                                    @Nullable TransactionAttribute txAttr, final String joinpointIdentification) {
//        // 1.判定是否开启事务
//        TransactionStatus status = tm.getTransaction(txAttr);
//        // 2.将事务信息绑定到当前线程
//        return prepareTransactionInfo(tm, txAttr, joinpointIdentification, status);
//    }
//
//    public final TransactionStatus getTransaction(@Nullable TransactionDefinition definition)
//            throws TransactionException {
//
//        // 1.当前已经存在事务，则根据传播级别判定开启事务还是加入现有事务。
//        if (isExistingTransaction(transaction)) {
//            return handleExistingTransaction(def, transaction, debugEnabled);
//        }
//        // 2.当前不存在事务，且传播级别为PROPAGATION_REQUIRED/PROPAGATION_REQUIRES_NEW/PROPAGATION_NESTED
//        else if (def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRED ||
//                def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_REQUIRES_NEW ||
//                def.getPropagationBehavior() == TransactionDefinition.PROPAGATION_NESTED) {
//            // 3.获取连接并且开启事务，同时更新当前线程上下文中事务的状态。
//            return startTransaction(def, transaction, debugEnabled, suspendedResources);
//        }
//    }
//
//}
