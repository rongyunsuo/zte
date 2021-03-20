package com.citi.xiaoruirui.service.panel;

public class a {
    public static void main(String[] args) {
        String filePath = a.class.getClassLoader().getResource("tmp.xls").getFile();
        System.out.println(filePath);

    }
}
