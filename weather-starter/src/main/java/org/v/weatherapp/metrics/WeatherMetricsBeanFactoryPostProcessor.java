package org.v.weatherapp.metrics;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;

@Component
public class WeatherMetricsBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    private final MetricsCollector metricsCollector;


    public WeatherMetricsBeanFactoryPostProcessor(MetricsCollector metricsCollector) {
        this.metricsCollector = metricsCollector;
    }


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> beanClass = beanFactory.getType(beanName);
            if (beanClass != null) {
                Arrays.stream(beanClass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(TrackMetric.class))
                        .forEach(method -> {
                            String methodSignature = beanClass.getName() + "." + method.getName();

                            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                            pointcut.setExpression("execution(* " + methodSignature + "(..))");

                            MethodInterceptor advice = new MethodInterceptor() {
                                @Override
                                public Object invoke(MethodInvocation invocation) throws Throwable {
                                    long startTime = System.currentTimeMillis();
                                    try {
                                        return invocation.proceed();
                                    } finally {
                                        long executionTime = System.currentTimeMillis() - startTime;
                                        metricsCollector.recordExecutionTime(methodSignature, executionTime);
                                        TrackMetric TrackMetric = method.getAnnotation(TrackMetric.class);
                                        String description = TrackMetric.value().isEmpty() ? "" : " (" + TrackMetric.value() + ")";
                                        System.out.println("Method " + methodSignature + description + " executed in " + executionTime + "ms");
                                    }
                                }
                            };

                            Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                            String advisorBeanName = beanName + "_" + method.getName() + "_logExecutionTimeMetricsAdvisor";
                            if (!beanFactory.containsBean(advisorBeanName)) {
                                beanFactory.registerSingleton(advisorBeanName, advisor);
                            }
                        });
            }
        }
    }

    // Метод для получения метрик теперь делегирует вызов
    public Map<String, Map<String, Object>> getMetrics() {
        return metricsCollector.getMetrics();
    }
}



//@Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        for (String beanName : beanFactory.getBeanDefinitionNames()) {
//            Object bean = beanFactory.getBean(beanName);
//            Class<?> beanClass = bean.getClass();
//            // Проверяем методы на наличие аннотации @TrackMetric
//            boolean hasTrackMetric = Arrays.stream(beanClass.getDeclaredMethods())
//                    .anyMatch(method -> method.isAnnotationPresent(TrackMetric.class));
//            if (hasTrackMetric) {
//                Enhancer enhancer = new Enhancer();
//                enhancer.setSuperclass(beanClass);
//                enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
//                    TrackMetric trackMetric = method.getAnnotation(TrackMetric.class);
//                    if (trackMetric != null) {
//                        System.out.println("methodProxy.getName() " + method.getName());
//                        long startTime = System.nanoTime();
//                        Object result = proxy.invokeSuper(obj, args);
//                        long duration = System.nanoTime() - startTime;
//                        metricsCollector.recordMetric(trackMetric.value(), duration);
//                        return result;
//                    }
//                    return proxy.invokeSuper(obj, args);
//                });
//                // Заменяем оригинальный бин прокси
//                Object proxy = enhancer.create();
//                beanFactory.registerSingleton(beanName + "_proxy", proxy);
//            }
//        }
//    }
//}