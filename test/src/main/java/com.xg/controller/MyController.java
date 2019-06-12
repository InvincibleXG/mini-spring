package com.xg.controller;

import com.xg.web.mvc.Controller;
import com.xg.web.mvc.RequestMapping;
import com.xg.web.mvc.RequestParam;

@Controller
public class MyController
{
    @RequestMapping("/getSalary")
    public Integer getMoney(@RequestParam("name")String name, @RequestParam("year")String experience)
    {
        return 10000;
    }
}
