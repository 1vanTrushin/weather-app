package org.v.weatherapp.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public class TrackMetricBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final MeterRegistry meterRegistry;


    public TrackMetricBeanPostProcessor(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        final Class<?> targetClass = AopUtils.getTargetClass(bean);
        Map<Method, TrackMetric> annotatedMethods = MethodIntrospector.selectMethods(targetClass,
                (MethodIntrospector.MetadataLookup<TrackMetric>) method ->
                        AnnotatedElementUtils.findMergedAnnotation(method, TrackMetric.class));
        if (annotatedMethods.isEmpty()) {
            return bean;
        }
        ProxyFactory proxyFactory = new ProxyFactory(bean);
        proxyFactory.addAdvice((MethodInterceptor) invocation -> {

            Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
            TrackMetric annotation = AnnotatedElementUtils.findMergedAnnotation(specificMethod, TrackMetric.class);

            if (annotation != null) {
                String metricName = StringUtils.hasText(annotation.value())
                        ? annotation.value()
                        : targetClass.getName() + "." + specificMethod.getName();

                String description = StringUtils.hasText(annotation.description())
                        ? annotation.description()
                        : "Execution time for " + metricName;

                Iterable<Tag> tags = Tags.of(Arrays.stream(annotation.extraTags())
                        .map(tagPair -> {
                            String[] parts = tagPair.split(":", 2);
                            return Tag.of(parts[0], parts.length > 1 ? parts[1] : "");
                        })
                        .collect(Collectors.toList()));

                Timer.Sample sample = Timer.start(meterRegistry);
                Timer timer = Timer.builder(metricName)
                        .description(description)
                        .tags(tags)
                        .publishPercentiles(0.5, 0.95, 0.99)
                        .register(meterRegistry);

                try {

                    return invocation.proceed();
                } finally {
                    sample.stop(timer);

                }
            }
            return invocation.proceed();
        });

        return proxyFactory.getProxy();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
