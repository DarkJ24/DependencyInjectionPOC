package com.app;

import com.darkj24.ioc.annotations.Bean;
import com.darkj24.ioc.enums.AutowiringMode;

@Bean(autowire = AutowiringMode.BY_NAME)
public class BeanClassExample {

    public BeanClassExample() {
    }
}
