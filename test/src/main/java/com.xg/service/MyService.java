package com.xg.service;

import com.xg.beans.Bean;

@Bean
public class MyService
{
    public Integer calculateSalary(Integer experience)
    {
        return experience*5000;
    }
}
