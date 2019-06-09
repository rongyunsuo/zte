
package com.itheima.crm.controller;

import com.itheima.crm.service.CustomerService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;


    //入口
    @RequestMapping(value = "/customer/list")
//	public String list(Model model)
    @Test
    public void list() {
        /*通过mybatis活动数据*/
        Integer selectByMybatis = customerService.selectByMybatis();
        System.out.println("selectByMybatis结果:"+selectByMybatis);
    }


}
