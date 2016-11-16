package com.fendany.utils.xlsx;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by moilions on 2016/11/11.
 */
public class ReadExcel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadExcel.class);

    /**
     * 读取office 2003 xls
     */
    public void loadXls(String filePath) {

        try {
            InputStream input = new FileInputStream(filePath);
            POIFSFileSystem fs = new POIFSFileSystem(input);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0);
            // Iterate over each row in the sheet
            Iterator rows = sheet.rowIterator();
            while (rows.hasNext()) {
                HSSFRow row = (HSSFRow) rows.next();
                System.out.println("Row #" + row.getRowNum());
                // Iterate over each cell in the row and print out the cell"s
                // content
                Iterator cells = row.cellIterator();
                while (cells.hasNext()) {
                    HSSFCell cell = (HSSFCell) cells.next();
                    System.out.println("Cell #" + cell.getCellFormula());
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC:

                            System.out.println(cell.getNumericCellValue());
                            break;

                        case HSSFCell.CELL_TYPE_STRING:

                            System.out.println(cell.getStringCellValue());
                            break;

                        case HSSFCell.CELL_TYPE_BOOLEAN:

                            System.out.println(cell.getBooleanCellValue());
                            break;

                        case HSSFCell.CELL_TYPE_FORMULA:

                            System.out.println(cell.getCellFormula());
                            break;

                        default:

                            System.out.println("unsuported sell type");
                            break;

                    }

                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 读取xlsx文本
     *
     * @param filePath
     */

    public void loadXlsxText(String filePath) {

        File inputFile = new File(filePath);
        try {
            POITextExtractor extractor = ExtractorFactory.createExtractor(inputFile);
            System.out.println(extractor.getText());
        } catch (IOException | OpenXML4JException | XmlException e) {
            e.printStackTrace();
        }

    }

    /**
     * 读取 xlsx
     * @param filePath
     */
    public JSONObject loadXlsx(String filePath) {
        // 构造 XSSFWorkbook
        try (XSSFWorkbook xwb = new XSSFWorkbook(filePath)) {
            JSONObject sheets = new JSONObject();
            int sheets_num = xwb.getNumberOfSheets();
            System.out.println("【sheets num】" + sheets_num);
            // 循环每一个sheet页
            for (int num = 0; num < sheets_num; num++) {
                String sheet_name = xwb.getSheetName(num);
                System.out.println(sheet_name);
                XSSFSheet sheet = xwb.getSheetAt(num);

                // 取标头第一行
                XSSFRow row_sheet = sheet.getRow(0);
                String[] heads = new String[row_sheet.getLastCellNum()];
                for (int j = row_sheet.getFirstCellNum(); j < row_sheet.getPhysicalNumberOfCells(); j++) {
                    heads[j] = row_sheet.getCell(j).toString();
                }
                System.out.println("【heads】" + Arrays.toString(heads));
                JSONArray rows_json = new JSONArray();
                // 循环输出表格中的内容
                for (int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    JSONObject row_json = new JSONObject();
                    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                        row_json.put(heads[j], row.getCell(j).toString());
                    }
                    rows_json.add(row_json);
                }
                sheets.put(sheet_name, rows_json);
            }
            System.out.println("【result】" + sheets.toString());
            return sheets;
        } catch (IOException e) {
            System.out.println("读取文件出错");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取 xlsx
     * @param filePath
     */
    public Map<String,Map<String,JSONObject>> loadXlsx2Map(String filePath) {
        // 构造 XSSFWorkbook
        try (XSSFWorkbook xwb = new XSSFWorkbook(filePath)) {
            JSONArray jsonArray = new JSONArray();
            int sheets_num = xwb.getNumberOfSheets();
            System.out.println("【sheets num】" + sheets_num);
            // 循环每一个sheet页
            for (int num = 0; num < sheets_num; num++) {
                String sheet_name = xwb.getSheetName(num);
                System.out.println(sheet_name);
                XSSFSheet sheet = xwb.getSheetAt(num);

                // 取标头第一行
                XSSFRow row_sheet = sheet.getRow(0);
                String[] heads = new String[row_sheet.getLastCellNum()];
                for (int j = row_sheet.getFirstCellNum(); j < row_sheet.getPhysicalNumberOfCells(); j++) {
                    heads[j] = row_sheet.getCell(j).toString();
                }
                System.out.println("【heads】" + Arrays.toString(heads));
                JSONArray rows_json = new JSONArray();
                // 循环输出表格中的内容
                for (int i = sheet.getFirstRowNum() + 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = sheet.getRow(i);
                    for (int j = row.getFirstCellNum(); j < row.getPhysicalNumberOfCells(); j++) {
                        JSONObject row_json = new JSONObject();
                        row_json.put(heads[j], row.getCell(j).toString());
                        rows_json.add(row_json);
                    }
                }
                JSONObject sheets = new JSONObject();
                sheets.put(sheet_name, rows_json);
                jsonArray.add(sheets);
            }
            System.out.println("【result】" + jsonArray.toString());
            return null;
        } catch (IOException e) {
            System.out.println("读取文件出错");
            e.printStackTrace();
        }
        return null;
    }

}
