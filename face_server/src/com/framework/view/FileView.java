/**
 * 
 */
package com.framework.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**
 * @author Administrator
 *
 */
public class FileView extends AbstractView {
	
	private static final String CONTENT_TYPE = "application/octet-stream";
	
	private File file;
	
	public FileView(File file) {
		setContentType(CONTENT_TYPE);
		this.file = file;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = createTemporaryOutputStream();
		buildFileStream(baos, file);
		baos.close();
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("GB2312"), "iso-8859-1"));
		writeToResponse(response, baos);
	}
	
	private void buildFileStream(OutputStream os, File file) {
		FileInputStream is = null;
		try {
			is = new FileInputStream(file);
			byte[] bytes = new byte[1024];
			int pos;
			while ((pos = is.read(bytes)) != -1)
				os.write(bytes, 0, pos);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {}
		}
	}
}
