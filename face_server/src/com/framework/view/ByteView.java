/**
 * 
 */
package com.framework.view;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**
 * @author Administrator
 *
 */
public class ByteView extends AbstractView {
	
	private static final String CONTENT_TYPE = "application/octet-stream";
	
	private byte[] bytes;
	
	public ByteView(byte[] bytes) {
		setContentType(CONTENT_TYPE);
		this.bytes = bytes;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ByteArrayOutputStream baos = createTemporaryOutputStream();
		buildFileStream(baos, bytes);
		baos.close();
		writeToResponse(response, baos);
	}
	
	private void buildFileStream(OutputStream os, byte[] bytes) {
		try {
			os.write(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
