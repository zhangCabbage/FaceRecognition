/**
 * 
 */
package com.framework.core;

import java.util.Date;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

/**
 */
public class DataEditorRegistrar implements PropertyEditorRegistrar {

	@Override
	public void registerCustomEditors(PropertyEditorRegistry registry) {
		registry.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		registry.registerCustomEditor(Date.class, new DateEditor(Date.class));
		registry.registerCustomEditor(java.sql.Date.class, new DateEditor(java.sql.Date.class));
		registry.registerCustomEditor(java.sql.Time.class, new DateEditor(java.sql.Time.class));
		registry.registerCustomEditor(java.sql.Timestamp.class, new DateEditor(java.sql.Timestamp.class));
	}
}
