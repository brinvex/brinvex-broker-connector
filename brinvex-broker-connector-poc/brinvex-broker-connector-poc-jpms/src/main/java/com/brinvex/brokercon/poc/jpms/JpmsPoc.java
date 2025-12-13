package com.brinvex.brokercon.poc.jpms;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class JpmsPoc {

    static void main() {
        /*
        2025-12-13
        Hibernate Validator just doesn't work with JPMS.

        I tried also to get help from various AI, but none of them was able to make it work.

        jakarta.validation-api-3.1.1
        hibernate-validator-9.0.1.Final
        tomcat-embed-el-11.0.14

        Exception in thread "main" jakarta.validation.ValidationException: HV000183: Unable to initialize 'jakarta.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator.buildExpressionFactory(ResourceBundleMessageInterpolator.java:209)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator.<init>(ResourceBundleMessageInterpolator.java:92)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.internal.engine.AbstractConfigurationImpl.getDefaultMessageInterpolator(AbstractConfigurationImpl.java:576)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.internal.engine.AbstractConfigurationImpl.getDefaultMessageInterpolatorConfiguredWithClassLoader(AbstractConfigurationImpl.java:835)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.internal.engine.AbstractConfigurationImpl.getMessageInterpolator(AbstractConfigurationImpl.java:482)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.internal.engine.ValidatorFactoryImpl.<init>(ValidatorFactoryImpl.java:156)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.HibernateValidator.buildValidatorFactory(HibernateValidator.java:36)
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.internal.engine.AbstractConfigurationImpl.buildValidatorFactory(AbstractConfigurationImpl.java:450)
            at jakarta.validation@3.1.1/jakarta.validation.Validation.buildDefaultValidatorFactory(Validation.java:103)
            at brinvex.broker.connector.poc.jpms/com.brinvex.brokercon.poc.jpms.JpmsPoc.main(JpmsPoc.java:10)
        Caused by: java.lang.IllegalAccessError: class org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator (in module org.hibernate.validator) cannot access class jakarta.el.ELManager (in module org.apache.tomcat.embed.el) because module org.hibernate.validator does not read module org.apache.tomcat.embed.el
            at org.hibernate.validator@9.0.1.Final/org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator.buildExpressionFactory(ResourceBundleMessageInterpolator.java:189)
            ... 9 more
         */
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
        }
    }

}
