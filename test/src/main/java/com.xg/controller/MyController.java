package com.xg.controller;

import com.xg.beans.AutoWired;
import com.xg.service.MyService;
import com.xg.web.mvc.Controller;
import com.xg.web.mvc.RequestMapping;
import com.xg.web.mvc.RequestParam;

@Controller
public class MyController
{
    @AutoWired
    private MyService myService;

    @RequestMapping("/getSalary")
    public Integer getMoney(@RequestParam("name")String name, @RequestParam("year")String experience)
    {
        System.out.println(name);
        System.out.println(experience);
        int year=0;
        if (experience!=null && experience.matches("[0-9]+([.][0-9]+)?")) year= (int) Math.floor(Double.parseDouble(experience));
        return myService.calculateSalary(year);
    }
}
