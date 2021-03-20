package com.citi.xiaoruirui.service.excel;

import com.citi.xiaoruirui.entity.UserInfoDTO;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.filechooser.FileSystemView;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PoiUnit {
    private UserInfoDTO userInfoDTO;

    public PoiUnit(UserInfoDTO userInfoDTO) {
        this.userInfoDTO = userInfoDTO;
    }

    public void readExcel() throws Exception {

        String destPath = copyFileUsingFileStreams(userInfoDTO.getExcelPath());
        changeExcel(destPath);
  /*      Map<String, List<List<String>>> dataMap = new LinkedHashMap<String, List<List<String>>>();
        InputStream is = null;
        try {
            //验证文件是否合法
            if (!validateExcel(destPath)) {
                return;
            }
            // 判断文件的类型，是2003还是2007
            boolean isExcel2003 = true;
            if (isExcel2007(destPath)) {
                isExcel2003 = false;
            }
            // 调用本类提供的根据流读取的方法
            File file = new File(destPath);
            is = new FileInputStream(file);
            dataMap = read(is, isExcel2003);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }

        writeExcel(dataMap, destPath);*/
    }

    public void createAllExcel(String directoryPath) throws IOException {
        List<UserInfoDTO> allInfo = getAllInfo(directoryPath);

        String[] title = {"用户名", "手机号", "快递单号"};
        //创建HSSF工作薄,文档对象HSSFWorkbook
        HSSFWorkbook workbook = new HSSFWorkbook();
        //创建一个Sheet页，HSSFSheet 表单对象,需要几个sheet页自己看着修改即可
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //创建第一行（一般是表头）
        HSSFRow row0 = sheet.createRow(0);//sheet1
        //创建列，HSSFCell列
        HSSFCell cell = null;
        //设置表头，循环上面的数组，希望朋友不要忘记数组是length,集合是size();
        for (int i = 0; i < title.length; i++) {
            cell = row0.createCell(i);
            cell.setCellValue(title[i]);
        }
        //填充20行数据
        for (int i = 1; i < allInfo.size()+1; i++) {
            UserInfoDTO userInfoDTO = allInfo.get(i - 1);
            HSSFRow row = sheet.createRow(i);//新建第一行
            HSSFCell cell1 = row.createCell(0);//新建第一个单元格
            cell1.setCellValue(userInfoDTO.getUserName());
            HSSFCell cell2 = row.createCell(1);//新建第二个单元格
            cell2.setCellValue(userInfoDTO.getUserPhoneNum());
            HSSFCell cell3 = row.createCell(2);//新建第三个单元格
            cell3.setCellValue(userInfoDTO.getExpressNum());
        }
        //保存到本地
        File file = new File(directoryPath+"\\单号汇总.xlsx");
        if (!file.exists()){
            file.createNewFile();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        //将Excel写入输出流中
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();

    }


    public  List<UserInfoDTO> getAllInfo(String directoryPath){
        List<UserInfoDTO> userInfoDTOList = new ArrayList<>();
        File file = new File(directoryPath);        //获取其file对象
        File[] fs = file.listFiles();    //遍历path下的文件和目录，放在File数组中
        for (File f : fs) {                    //遍历File[]数组
            if (f.isFile())        //若非目录(即文件)，则打印
            {
                String fullName = f.getName();
                String fileName = fullName.substring(0, fullName.indexOf("."));
                String[] info = fileName.split("-");
                UserInfoDTO userInfoDTO = new UserInfoDTO(info[0], info[1], "", info[2], "");
                userInfoDTOList.add(userInfoDTO);
            }

        }
        return userInfoDTOList;
    }

    private static Map<String, List<List<String>>> read(InputStream inputStream, boolean isExcel2003) {
        Map<String, List<List<String>>> dataMap = null;
        try {
            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            if (isExcel2003) {
                wb = new HSSFWorkbook(inputStream);
            } else {
                wb = new XSSFWorkbook(inputStream);
            }
            dataMap = read(wb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    private static Map<String, List<List<String>>> read(Workbook wb) {
        int totalRows = 0;
        Map<String, List<List<String>>> dataMap = new LinkedHashMap<String, List<List<String>>>();
        int sheetNums = wb.getNumberOfSheets();
        for (int i = 0; i < sheetNums; i++) {
            List<List<String>> sheetLst = new ArrayList<List<String>>();

            // 得到一个shell
            Sheet sheet = wb.getSheetAt(i);
            String sheetName = sheet.getSheetName();
            // 得到Excel的行数
            totalRows = sheet.getPhysicalNumberOfRows();
            //遍历行
            for (int r = 0; r < totalRows; r++) {
                Row row = sheet.getRow(r);
                if (row == null) {
                    continue;
                }
                List<String> rowLst = new ArrayList<String>();
                int totalCells = row.getLastCellNum();
                //遍历列
                for (int c = 0; c < totalCells; c++) {
                    Cell cell = row.getCell(c);
                    String cellValue = "";
                    if (null != cell) {
                        // 以下是判断数据的类型
                        switch (cell.getCellType()) {
                            case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                                cellValue = cell.getNumericCellValue() + "";
                                break;
                            case HSSFCell.CELL_TYPE_STRING: // 字符串
                                cellValue = cell.getStringCellValue();
                                break;
                            case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                                cellValue = cell.getBooleanCellValue() + "";
                                break;
                            case HSSFCell.CELL_TYPE_FORMULA: // 公式
                                cellValue = "=" + cell.getCellFormula();
                                break;
                            case HSSFCell.CELL_TYPE_BLANK: // 空值
                                cellValue = "";
                                break;
                            case HSSFCell.CELL_TYPE_ERROR: // 故障
                                cellValue = "非法字符";
                                break;
                            default:
                                cellValue = "未知类型";
                                break;
                        }
                    }
                    System.out.printf("%10s", cellValue);
                    rowLst.add(cellValue);
                }
                // 保存第r行的第c列
                System.out.printf("\n");
                sheetLst.add(rowLst);
            }
            dataMap.put(sheetName, sheetLst);
        }
        return dataMap;
    }

    private static boolean validateExcel(String filePath) {
        // 检查文件名是否为空或者是否是Excel格式的文件
        if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
            return false;
        }
        // 检查文件是否存在
        File file = new File(filePath);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }

    private static boolean isExcel2003(String filePath) {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    private static boolean isExcel2007(String filePath) {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static void createNewExcel(List<List<String>> dataList, String outFilePath) throws IOException {
        // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建工作表
        HSSFSheet sheet = workbook.createSheet("sheet1");

        //写一行
        for (int j = 0; j < dataList.size(); j++) {
            Row row = sheet.createRow(j);

            //写一列
            List<String> datas = dataList.get(j);
            for (int k = 0; k < datas.size(); k++) {
                row.createCell(k).setCellValue(datas.get(k));
            }
        }

        File xlsFile = new File(outFilePath);
        FileOutputStream xlsStream = new FileOutputStream(xlsFile);
        workbook.write(xlsStream);
    }

    public static void writeExcel(Map<String, List<List<String>>> dataMap, String filePath) throws Exception {
        List<Integer> needsChangedRow = Arrays.asList(5, 7, 9);
        OutputStream out = null;
        try {

            Workbook workBook = getWorkbok(new File((filePath)));

            List<String> sheets = new ArrayList(dataMap.keySet());
            for (int i = 0; i < 1; i++) {
                // sheet 对应一个工作页
                Sheet sheet = workBook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                //删除原有数据，除了属性列
                int rowNumber = sheet.getLastRowNum();

                for (int j = 0; j <= rowNumber; j++) {
                    Row row = sheet.getRow(j);
                    if (row != null && needsChangedRow.contains(j)) {
                        sheet.removeRow(row);
                    }
                }

                // 创建文件输出流，输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(filePath);
                workBook.write(out);

                //写一行
                List<List<String>> sheetList = dataMap.get(sheetName);
                for (int j = 0; j < sheetList.size(); j++) {
                    Row row = sheet.createRow(j);
                    if (needsChangedRow.contains(j)) {

                    }
                    //写一列
                    List<String> cellData = sheetList.get(j);
                    for (int k = 0; k < cellData.size(); k++) {
                        row.createCell(k).setCellValue(cellData.get(k));
                    }
                }

                // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(filePath);
                workBook.write(out);
                System.out.println("数据导出成功");
            }
        } catch (Exception e1) {
            System.out.println("请创建一个空文件");
            e1.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeExcel(String filePath) throws Exception {
        OutputStream out = null;
        try {

            Workbook workBook = getWorkbok(new File((filePath)));

            for (int i = 0; i < 1; i++) {
                // sheet 对应一个工作页
                Sheet sheet = workBook.getSheetAt(i);
                String sheetName = userInfoDTO.getExpressNum();
//                workBook.setSheetName(1,sheetName);

                Row userNameRow = sheet.getRow(5);
                Cell userNameCell = userNameRow.getCell(3);
                userNameCell.setCellValue(userInfoDTO.getUserName());

                Row userAddRow = sheet.getRow(7);
                Cell userAddCell = userAddRow.getCell(1);
                userAddCell.setCellValue(userInfoDTO.getUserAdd());

                Row userPhoneNumRow = sheet.getRow(9);
                Cell userPhoneNumCell = userPhoneNumRow.getCell(3);
                userPhoneNumCell.setCellValue(userInfoDTO.getUserPhoneNum());


                // 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
                out = new FileOutputStream(filePath);
                workBook.write(out);
                System.out.println("数据导出成功");
            }
        } catch (Exception e1) {
            System.out.println("请创建一个空文件");
            e1.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    private static Workbook getWorkbok(File file) throws IOException {
        Workbook wb = null;
        FileInputStream in = new FileInputStream(file);
        if (file.getName().endsWith(EXCEL_XLS)) {     //Excel&nbsp;2003
            wb = new HSSFWorkbook(in);
        } else if (file.getName().endsWith(EXCEL_XLSX)) {    // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    private String copyFileUsingFileStreams(String resPath)
            throws IOException {
        // 读取Excel文档
        String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        File resFile = new File(resPath);
        String fileName = resFile.getName();
        String destPath = FileSystemView.getFileSystemView().getHomeDirectory().getPath() + "\\" + "excel\\" + currentDate + "\\" + userInfoDTO.getUserName() + "-" + userInfoDTO.getUserPhoneNum() + "-" + userInfoDTO.getExpressNum() + fileName.substring(fileName.lastIndexOf("."));
        File destFile = new File(destPath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(resFile);
            output = new FileOutputStream(destFile);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
        return destPath;
    }

}

