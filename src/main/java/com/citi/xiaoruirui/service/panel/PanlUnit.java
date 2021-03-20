package com.citi.xiaoruirui.service.panel;


import com.citi.xiaoruirui.entity.UserInfoDTO;
import com.citi.xiaoruirui.service.excel.PoiUnit;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanlUnit {
    //标签1 name
    private JLabel expressNum = new JLabel("当前单号：");
    //输入框1
    private JTextField expressNumValue = new JTextField();

    //标签1 add
    private JLabel addNum = new JLabel("增量：");
    //输入框1 add
    private JTextField addValue = new JTextField("0");

    //输入框2
    //标签1
    private JLabel userInfo = new JLabel("用户信息：");
    private JTextField userInfoValue = new JTextField();

    //标签3 name
    private JLabel userName = new JLabel("用户姓名：");
    //输入框3
    private JTextField userNameValue = new JTextField();


    //标签4 phoneNum
    private JLabel userPhoneNum = new JLabel("用户手机号：");
    //输入框4
    private JTextField userPhoneNumValue = new JTextField();

    //标签5 Add
    private JLabel userAdd = new JLabel("用户地址：");
    //输入框5
    private JTextField userAddValue = new JTextField();


    //fileField1
    private JTextField excelPath = new JTextField(26);

    private int jLabel1Y = 30;
    private int addY = 40;
    private int jLabel2Y = jLabel1Y + addY;
    private int fileY1 = jLabel2Y + 40;
    private int buttonY = fileY1 + 40;

    public void createPanl() {
        //初始化窗口
        JFrame jFrame = new JFrame("大云云爱小睿睿");
        //关闭窗口的事件
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //设置宽度，高度
        jFrame.setSize(700, 550);

        //初始化输入框
        JPanel jPanel = new JPanel();
        jFrame.add(jPanel);
        jPanelDefault(jPanel);

        //设置窗口界面可见
        jFrame.setVisible(true);
    }

    private void jPanelDefault(JPanel jPanel) {

        //面板布局设置为空，就可以手动设置组件坐标和大小了
        jPanel.setLayout(null);

        buildTextField(jPanel);

        buildFileField(jPanel);

        buildInfoButtion(jPanel);

        buildSingleExcelButtion(jPanel);

        buildAllExcelButtion(jPanel);
    }

    private void buildTextField(JPanel jPanel) {
        //标签1 num
        expressNum.setBounds(30, jLabel1Y, 80, 20);
        jPanel.add(expressNum);

        //输入框1 num
        expressNumValue.setColumns(20);
        expressNumValue.setBounds(120, jLabel1Y, 200, 20);
        jPanel.add(expressNumValue);

        //标签1 add
        addNum.setBounds(400, jLabel1Y, 80, 20);
        jPanel.add(addNum);

        //输入框1 add
        addValue.setColumns(20);
        addValue.setBounds(460, jLabel1Y, 110, 20);
        jPanel.add(addValue);

        //row 2
        //user Info
        userInfo.setBounds(30, jLabel1Y + addY, 80, 20);
        jPanel.add(userInfo);

        //user Info input
        userInfoValue.setColumns(20);
        userInfoValue.setBounds(120, jLabel1Y + addY, 450, 20);
        jPanel.add(userInfoValue);

        //row 3 userName
        //userName
        userName.setBounds(30, jLabel1Y + 2 * addY, 80, 20);
        jPanel.add(userName);

        //userName
        userNameValue.setColumns(20);
        userNameValue.setBounds(120, jLabel1Y + 2 * addY, 200, 20);
        jPanel.add(userNameValue);


        //row 4 userPhoneNum
        //userPhoneNum
        userPhoneNum.setBounds(30, jLabel1Y + 3 * addY, 80, 20);
        jPanel.add(userPhoneNum);

        //userPhoneNum
        userPhoneNumValue.setColumns(20);
        userPhoneNumValue.setBounds(120, jLabel1Y + 3 * addY, 200, 20);
        jPanel.add(userPhoneNumValue);


        //row 5 userAdd
        //userAdd
        userAdd.setBounds(30, jLabel1Y + 4 * addY, 80, 20);
        jPanel.add(userAdd);

        //userAdd
        userAddValue.setColumns(20);
        userAddValue.setBounds(120, jLabel1Y + 4 * addY, 450, 20);
        jPanel.add(userAddValue);

    }

    private void buildFileField(JPanel jPanel) {
        excelPath.setText("可以将文件拖入该窗口，或者点击右边的浏览 -->");
        final JButton b1 = new JButton("浏览");
        //拖入文件
        excelPath.setTransferHandler(new TransferHandler() {
            public boolean importData(JComponent comp, Transferable t) {
                try {
                    Object o = t.getTransferData(DataFlavor.javaFileListFlavor);

                    String filepath = o.toString();
                    if (filepath.startsWith("[")) {
                        filepath = filepath.substring(1);
                    }
                    if (filepath.endsWith("]")) {
                        filepath = filepath.substring(0, filepath.length() - 1);
                    }
                    excelPath.setText(filepath);

                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public boolean canImport(JComponent comp, DataFlavor[] flavors) {
                for (DataFlavor flavor : flavors) {
                    if (DataFlavor.javaFileListFlavor.equals(flavor)) {
                        return true;
                    }
                }
                return false;
            }
        });

        //按钮浏览
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc2 = new JFileChooser("./");
                fc2.showOpenDialog(b1);
                String lu = fc2.getSelectedFile().getAbsolutePath();
                excelPath.setText(lu);
            }

        });

        excelPath.setBounds(50, jLabel1Y + 5 * addY, 450, 30);
        b1.setBounds(510, jLabel1Y + 5 * addY, 60, 30);//设置组件大小和位置

        jPanel.add(excelPath);
        jPanel.add(b1);
    }

    private void buildInfoButtion(JPanel jPanel) {
        //confirm button
        JButton confirmButton = new JButton("确认");
        confirmButton.setBounds(250, jLabel1Y + 6 * addY, 60, 30);

        //cancel button
        JButton searchButton = new JButton("搜索");
        searchButton.setBounds(350, jLabel1Y + 6 * addY, 60, 30);

        //按钮事件
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                JOptionPane.showMessageDialog(null, "处理中，请稍后");
                //current tracking num
                Long oldExpressNumValue = Long.valueOf(expressNumValue.getText());
                Long add = Long.valueOf(addValue.getText());
                expressNumValue.setText(String.valueOf(oldExpressNumValue + add));

                String userInfoValueText = userInfoValue.getText();

                String userNameValueText = userInfoValueText.substring(userInfoValueText.indexOf("收货人姓名："), userInfoValueText.indexOf("手机号码：")).replaceAll("收货人姓名：", "").trim();
                userNameValue.setText(userNameValueText);

                String userPhoneNumValueText = userInfoValueText.substring(userInfoValueText.indexOf("手机号码："), userInfoValueText.indexOf("固定号码：")).replaceAll("手机号码：", "").trim();
                userPhoneNumValue.setText(userPhoneNumValueText);

                String userAddValueText = userInfoValueText.substring(userInfoValueText.indexOf("配送地址：")).replaceAll("配送地址：", "").trim();
                userAddValue.setText(userAddValueText);

                JOptionPane.showMessageDialog(null, "请核对信息");

            }
        });
        jPanel.add(confirmButton);

        //按钮事件
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //关闭弹窗
                System.exit(0);
            }
        });
        jPanel.add(searchButton);
    }


    private void buildSingleExcelButtion(JPanel jPanel) {
        //excel button
        JButton excelButton = new JButton("生成单个excel");
        excelButton.setBounds(250, jLabel1Y + 7 * addY + 5, 160, 40);


        //excel 按钮事件
        excelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "单个excel生成中，请稍后");
                UserInfoDTO userInfoDTO = new UserInfoDTO(userNameValue.getText(), userPhoneNumValue.getText(), userAddValue.getText(), expressNumValue.getText(), excelPath.getText());
                if (excelPath.getText() != null) {
                    PoiUnit excelUnit = new PoiUnit(userInfoDTO);
                    try {
                        excelUnit.readExcel();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }
                JOptionPane.showMessageDialog(null, "单个excel已经生成，请检验");


            }
        });
        jPanel.add(excelButton);
    }

    private void buildAllExcelButtion(JPanel jPanel) {
        //excel button
        JButton excelButton = new JButton("生成当日汇总excel");
        excelButton.setBounds(250, jLabel1Y + 8 * addY + 20, 160, 40);


        //excel 按钮事件
        excelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "当日汇总excel生成中，请稍后");
                UserInfoDTO userInfoDTO = new UserInfoDTO("", "", "", "", "");
                if (excelPath.getText() != null) {
                    String directoryPath = excelPath.getText();
                    PoiUnit excelUnit = new PoiUnit(userInfoDTO);
                    try {
                        excelUnit.createAllExcel(directoryPath);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }

                }
                JOptionPane.showMessageDialog(null, "当日汇总excel已经生成，请检验");


            }
        });
        jPanel.add(excelButton);
    }

}
