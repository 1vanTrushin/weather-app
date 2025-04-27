package org.v.weatherapp.metrics;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Component
//public class WeatherMetricsBeanPostProcessor implements BeanPostProcessor {
//
//    private final MetricsCollector metricsCollector;
//
//
//    public WeatherMetricsBeanPostProcessor(MetricsCollector metricsCollector) {
//        this.metricsCollector = metricsCollector;
//    }
//
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        Class<?> beanClass = bean.getClass();
//
//        boolean hasTrackMetric = Arrays.stream(beanClass.getDeclaredMethods())
//                .anyMatch(method -> method.isAnnotationPresent(TrackMetric.class));
//
//        if (hasTrackMetric) {
//            Enhancer enhancer = new Enhancer();
//            enhancer.setSuperclass(beanClass);
//            enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
//                TrackMetric trackMetric = method.getAnnotation(TrackMetric.class);
//                if (trackMetric != null) {
//                    long startTime = System.nanoTime();
//                    Object result = proxy.invokeSuper(obj, args);
//                    long duration = System.nanoTime() - startTime;
//                    metricsCollector.recordMetric(trackMetric.value(), duration);
//                    return result;
//                }
//                return proxy.invokeSuper(obj, args);
//            });
//            return enhancer.create();
//        }
//
//        return bean;
//    }
//}
