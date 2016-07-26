/**
 * 
 */
package com.framework.view;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**

 */
public class TextView extends AbstractView {
	
	/** The content type for an Excel response */
	private static final String CONTENT_TYPE = "text/plain;charset=utf-8";
	
	private String data;
	
	/**
	 * 
	 * @param data	需要写入CSV文件的数据
	 * @param title	CSV文件列名与对象属性名的对应关系
	 * @param fileName	导出的文件名
	 */
	public TextView(String data) {
		setContentType(CONTENT_TYPE);
		this.data = data;
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = createTemporaryOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(baos, "UTF-8");
		writer.write(data);
		writer.close();
		writeToResponse(response, baos);
	}
}
