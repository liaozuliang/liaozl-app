/**
 * 
 */
package com.liaozl.utils.excel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


/**
 * @author liaozuliang
 * @date 2015年4月24日
 */
public class ExcelUtil {

	static final Logger log = Logger.getLogger(ExcelUtil.class);

	public static final int EXCEL_2003 = 1;
	public static final int EXCEL_2007 = 2;


	public static class Person {

		String name;
		int sex;
		double money;


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getSex() {
			return sex;
		}

		public void setSex(int sex) {
			this.sex = sex;
		}

		public double getMoney() {
			return money;
		}

		public void setMoney(double money) {
			this.money = money;
		}

	}


	public static boolean testExportToExcel2003(HttpServletResponse response) throws Exception {
		String fileName = "导出测试2003";
		String sheetName = "导出测试2003";
		String excelColumn = "myExportTest";
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "aa");
		map.put("sex", 1);
		map.put("money", 23.19232);
		dataList.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "a中国a");
		map.put("sex", 2);
		map.put("money", 63.89);
		dataList.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "a123a");
		map.put("sex", 1);
		map.put("money", 100);
		dataList.add(map);

		map = new HashMap<String, Object>();
		map.put("name", "a2132啊啊a");
		map.put("sex", 2);
		map.put("money", 6.5);
		dataList.add(map);

		exportToExcel2003(response, fileName, sheetName, excelColumn, dataList);

		return true;
	}

	public static boolean testExportToExcel2007(HttpServletResponse response) throws Exception {
		String fileName = "导出测试2007";
		String sheetName = "导出测试2007";
		String excelColumn = "myExportTest";
		List<Person> dataList = new ArrayList<Person>();

		Person person = new Person();
		person.setName("a2132啊啊a");
		person.setSex(1);
		person.setMoney(0.78);
		dataList.add(person);

		person = new ExcelUtil.Person();
		person.setName("a123a");
		person.setSex(3);
		person.setMoney(110);
		dataList.add(person);

		person = new ExcelUtil.Person();
		person.setName("a中国a");
		person.setSex(1);
		person.setMoney(6.4);
		dataList.add(person);

		person = new ExcelUtil.Person();
		person.setName("1212aa");
		person.setSex(1);
		person.setMoney(16.4565);
		dataList.add(person);

		exportToExcel2007(response, fileName, sheetName, excelColumn, dataList);

		return true;
	}

	public static boolean exportToExcel2003(HttpServletResponse response, String fileName, String sheetName, String excelColumn, List dataList)
			throws Exception {
		return exportToExcel(response, fileName, EXCEL_2003, sheetName, excelColumn, dataList);
	}

	public static boolean exportToExcel2007(HttpServletResponse response, String fileName, String sheetName, String excelColumn, List dataList)
			throws Exception {
		return exportToExcel(response, fileName, EXCEL_2007, sheetName, excelColumn, dataList);
	}

	public static boolean exportToExcel(HttpServletResponse response, String fileName, int excelFileType, String sheetName, String excelColumn,
			List dataList) throws Exception {
		Workbook workbook = null;

		if (excelFileType == EXCEL_2003) {
			workbook = new HSSFWorkbook();
		} else if (excelFileType == EXCEL_2007) {
			workbook = new XSSFWorkbook();
		} else {
			log.error("参数[excelFileType]错误");
			return false;
		}

		if (dataList == null) {
			log.error("参数[dataList]错误");
			return false;
		}

		if (StringUtils.isEmpty(excelColumn)) {
			log.error("参数[excelColumn]不能为空");
			return false;
		}

		Map<String, Object> excelMap = null;//ExcelUtils.getConfigById(excelColumn);
		if (excelMap == null) {
			log.error("参数[excelColumn]错误，找不到定义的数据");
			return false;
		}

		if (StringUtils.isEmpty(sheetName)) {
			sheetName = "sheet1";
		}

		List<Map<String, Object>> columnlist = (List<Map<String, Object>>) excelMap.get("columns");
		Sheet sheet = (Sheet) workbook.createSheet(sheetName);

		JSONArray dataArray = null;//;JsonUtil.beanToJSON(dataList, columnlist);
		if (dataArray == null) {
			log.error("导出数据转换成json格式错误");
			return false;
		}

		createTableHeader(sheet, columnlist);
		createTableBody(workbook, sheet, dataArray, columnlist);
		downloadExcel(excelFileType, workbook, response, fileName);

		return true;
	}

	public static void downloadExcel(int excelFileType, Workbook workbook, HttpServletResponse response, String fileName) throws Exception {
		if (excelFileType == EXCEL_2003) {
			fileName = fileName + ".xls";
		} else if (excelFileType == EXCEL_2007) {
			fileName = fileName + ".xlsx";
		} else {
			log.error("参数[excelFileType]错误");
			return;
		}

		response.setHeader("Content-disposition", "attachment; filename = " + new String(fileName.getBytes("GB2312"), "ISO8859_1") + "");

		OutputStream stream = response.getOutputStream();
		workbook.write(stream);

		stream.close();
	}

	public static void createTableBody(Workbook workbook, Sheet sheet, JSONArray dataArray, List<Map<String, Object>> columnlist) {
		CellStyle doubleCellStyle = workbook.createCellStyle();
		DataFormat doubleDf = workbook.createDataFormat();
		doubleCellStyle.setDataFormat(doubleDf.getFormat("0.00"));

		CellStyle intCellStyle = workbook.createCellStyle();
		DataFormat intDf = workbook.createDataFormat();
		intCellStyle.setDataFormat(intDf.getFormat("0"));

		CellStyle stringCellStyle = workbook.createCellStyle();
		DataFormat stringDf = workbook.createDataFormat();
		stringCellStyle.setDataFormat(stringDf.getFormat("@"));

		for (int i = 0; i < dataArray.size(); i++) {
			JSONObject object = dataArray.getJSONObject(i);

			int lastRowIndex = sheet.getLastRowNum();
			lastRowIndex += 1;
			Row row = sheet.createRow(lastRowIndex);

			Class clazz = null;
			for (int j = 0; j < columnlist.size(); j++) {
				Map<String, Object> column = columnlist.get(j);
				String key = getKey(column);
				Cell cell = row.createCell(j);
				cell.setCellStyle(stringCellStyle);// 默认文本格式

				try {
					if (column.containsKey("enum")) {
						String enumStr = (String) column.get("enum");
						String defaultKey = (String) column.get("defaultKey");
						String defaultDescription = (String) column.get("defaultDescription");
						if (key != null && (key.equals(defaultKey) || key.equals(defaultDescription))) {

						}
						for (int kk = 0; kk < column.keySet().toArray().length; kk++) {
							String str = (String) column.keySet().toArray()[kk];
							if (!str.equals("defaultKey") && !str.equals("defaultDescription") && !str.equals("width") && !str.equals("enum")) {
								key = str;
								break;
							}
						}
						clazz = Class.forName(enumStr);

						//cell.setCellValue();
					} else if (column.containsKey("dataType")) {
						String dataType = (String) column.get("dataType");
						if (dataType.equals("double")) {
							cell.setCellStyle(doubleCellStyle);
						} else if (dataType.equals("int")) {
							cell.setCellStyle(intCellStyle);
						}

						String objectStr =null;// JsonUtil.getStringJsonValueIgnoreCase(object, key);
						if (objectStr == null || objectStr.equals("") || objectStr.equals("{}")) {
							objectStr = "";
						}

						if (NumberUtils.isNumber(objectStr)) {
							if (dataType.equals("double")) {
								cell.setCellValue(Double.valueOf(objectStr));
							} else if (dataType.equals("int")) {
								cell.setCellValue(Integer.valueOf(objectStr));
							}
						} else {
							cell.setCellValue("");
						}
					} else {
						String objectStr =null;// JsonUtil.getStringJsonValueIgnoreCase(object, key);
						if (objectStr == null || objectStr.equals("") || objectStr.equals("{}")) {
							objectStr = "";
						}
						cell.setCellValue(objectStr);
					}
				} catch (Exception e) {
					e.printStackTrace();
					cell.setCellValue("");
				}
			}
		}

	}

	public static void createTableHeader(Sheet sheet, List<Map<String, Object>> columnlist) {
		int lastRowIndex = sheet.getLastRowNum();
		if (lastRowIndex != 0) {
			lastRowIndex += 1;
		}
		Row row = sheet.createRow(lastRowIndex);

		for (int i = 0; i < columnlist.size(); i++) {
			Map<String, Object> column = columnlist.get(i);

			String key = getKey(column);

			if (StringUtils.isEmpty(key)) {
				key = (String) column.keySet().toArray()[0];
			}

			Cell cell = row.createCell(i);

			cell.setCellValue((String) column.get(key));
			sheet.setColumnWidth(i, column.get("width") == null ? 200 : new BigDecimal((String) column.get("width")).intValue() * 36);
		}
	}

	private static String getKey(Map<String, Object> column) {
		String key = "";
		for (int kk = 0; kk < column.keySet().toArray().length; kk++) {
			String str = (String) column.keySet().toArray()[kk];
			if (!str.equals("defaultKey") && !str.equals("defaultDescription") && !str.equals("width") && !str.equals("enum")
					&& !str.equals("dataType")) {
				key = str;
				break;
			}
		}
		return key;
	}

	public static void read(String filePath) throws IOException {
		String fileType = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());

		InputStream stream = new FileInputStream(filePath);
		Workbook wb = null;

		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook(stream);
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}

		Sheet sheet1 = wb.getSheetAt(0);
		for (Row row : sheet1) {
			for (Cell cell : row) {
				System.out.print(cell.getStringCellValue() + "  ");
			}
			System.out.println();
		}
	}

	public static boolean write(String outPath) throws Exception {
		String fileType = outPath.substring(outPath.lastIndexOf(".") + 1, outPath.length());
		System.out.println(fileType);

		Workbook wb = null;
		if (fileType.equals("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileType.equals("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			System.out.println("您的文档格式不正确！");
			return false;
		}

		Sheet sheet1 = (Sheet) wb.createSheet("sheet1");
		for (int i = 0; i < 5; i++) {
			Row row = (Row) sheet1.createRow(i);
			// 循环写入列数据
			for (int j = 0; j < 8; j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue("测试" + j);
			}
		}

		OutputStream stream = new FileOutputStream(outPath);
		wb.write(stream);
		stream.close();

		return true;
	}
}
