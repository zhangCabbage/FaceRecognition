/**
 * 
 */
package com.framework.view;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.framework.data.IRenderValue;

/**

 */
public class ExcelView extends AbstractExcelView {

	private String fileName;
	
	private List<?> data;
	
	private Map<String, String> title;
	
	private Map<String, Object> mapValues;
	
	/**
	 * @param data	需要写入Excel文件的数据
	 * @param title	Excel文件列名与对象属性名的对应关系
	 * @param fileName	导出的文件名
	 */
	public ExcelView(List<?> data, Map<String, String> title, String fileName) {
		super();
		this.fileName = fileName;
		this.data = data;
		this.title = title;
	}
	
	/**
	 * @param data 需要写入Excel文件的数据
	 * @param title	Excel文件列名与对象属性名的对应关系
	 * @param fileName	导出的文件名
	 * @param mapValues	属性映射值
	 */
	public ExcelView(List<?> data, Map<String, String> title, String fileName, Map<String, Object> mapValues) {
		super();
		this.fileName = fileName;
		this.data = data;
		this.title = title;
		this.mapValues = mapValues;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HSSFSheet sheet = workbook.createSheet();
		HSSFFont headFont = workbook.createFont();
		headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headStyle = workbook.createCellStyle();
		headStyle.setFont(headFont);
		headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//		headStyle.setFillForegroundColor(HSSFColor.AQUA.index);
//		headStyle.setBorderLeft((short) 3);
//		headStyle.setBorderRight((short) 3);
		HSSFRow row = sheet.createRow(0);
		String[] titles = new String[title.size()];
		title.keySet().toArray(titles);
		for (int i = 0; i < title.size(); i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(headStyle);
			cell.setCellValue(titles[i]);
		}
		for (int i = 0; i < data.size(); i++) {
			row = sheet.createRow(i + 1);
			for (int j = 0; j < title.size(); j++) {
				Object value = null;
				try {
					Object rowData = data.get(i);
					if (rowData instanceof Map) {
						value = title.get(titles[j]) == null ? "" : ((Map) rowData).get(title.get(titles[j]));
					} else {
						value = title.get(titles[j]) == null ? "" : BeanUtils.getProperty(data.get(i), title.get(titles[j]));
					}
				} catch (Exception e) {
				}
				HSSFCell cell = row.createCell(j);
				if (value != null) {
					if (mapValues != null && mapValues.containsKey(titles[j])) {
						Object v = mapValues.get(titles[j]);
						if (v != null) {
							if (v instanceof Map) {
								value = ((Map) v).get(value);
							} else if (v instanceof IRenderValue) {
								value =  ((IRenderValue) v).getValue(value);
							} else {
								value = v;
							}
						}
					}
					if (value instanceof Number) {
						cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.valueOf(value.toString()));
					} else if (value instanceof Date) {
						cell.setCellValue((Date) value);
					} else {
						cell.setCellValue(value.toString());
					}
				}
			}
		}
		for (int i = 0; i < title.size(); i++) {
			sheet.autoSizeColumn(i, false);
			//中文宽度计算问题
			sheet.setColumnWidth(i, (int) (sheet.getColumnWidth(i) * 1.5));
		}
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso-8859-1") + ".xls");
	}

	public void setMapValues(Map<String, Object> mapValues) {
		this.mapValues = mapValues;
	}
}
