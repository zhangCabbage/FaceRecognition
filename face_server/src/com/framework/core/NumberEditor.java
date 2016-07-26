/**
 * 
 */
package com.framework.core;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

/**
 * @author dranson 2010-12-28
 */
public class NumberEditor extends PropertyEditorSupport {
	
	@SuppressWarnings("rawtypes")
	private final Class numberClass;

	private final NumberFormat numberFormat;

	private final boolean allowEmpty;

	/**
	 * Create a new CustomNumberEditor instance, using the default
	 * <code>valueOf</code> methods for parsing and <code>toString</code>
	 * methods for rendering.
	 * <p>
	 * The "allowEmpty" parameter states if an empty String should be allowed
	 * for parsing, i.e. get interpreted as <code>null</code> value. Else, an
	 * IllegalArgumentException gets thrown in that case.
	 * 
	 * @param numberClass
	 *            Number subclass to generate
	 * @param allowEmpty
	 *            if empty strings should be allowed
	 * @throws IllegalArgumentException
	 *             if an invalid numberClass has been specified
	 * @see org.springframework.util.NumberUtils#parseNumber(String, Class)
	 * @see Integer#valueOf
	 * @see Integer#toString
	 */
	@SuppressWarnings("rawtypes")
	public NumberEditor(Class numberClass, boolean allowEmpty) throws IllegalArgumentException {
		this(numberClass, null, allowEmpty);
	}

	/**
	 * Create a new CustomNumberEditor instance, using the given NumberFormat
	 * for parsing and rendering.
	 * <p>
	 * The allowEmpty parameter states if an empty String should be allowed for
	 * parsing, i.e. get interpreted as <code>null</code> value. Else, an
	 * IllegalArgumentException gets thrown in that case.
	 * 
	 * @param numberClass
	 *            Number subclass to generate
	 * @param numberFormat
	 *            NumberFormat to use for parsing and rendering
	 * @param allowEmpty
	 *            if empty strings should be allowed
	 * @throws IllegalArgumentException
	 *             if an invalid numberClass has been specified
	 * @see org.springframework.util.NumberUtils#parseNumber(String, Class,
	 *      java.text.NumberFormat)
	 * @see java.text.NumberFormat#parse
	 * @see java.text.NumberFormat#format
	 */
	public NumberEditor(Class<?> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
		if (numberClass == null || !Number.class.isAssignableFrom(numberClass)) {
			throw new IllegalArgumentException("Property class must be a subclass of Number");
		}
		this.numberClass = numberClass;
		this.numberFormat = numberFormat;
		this.allowEmpty = allowEmpty;
	}

	/**
	 * Parse the Number from the given text, using the specified NumberFormat.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			if (this.allowEmpty && !StringUtils.hasText(text)) {
				// Treat empty String as null value.
				setValue(null);
			} else if (this.numberFormat != null) {
				// Use given NumberFormat for parsing text.
				setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
			} else {
				// Use default valueOf methods for parsing text.
				setValue(NumberUtils.parseNumber(text, this.numberClass));
			}
		} catch (Exception e) {
			setValue(null);
		}
	}

	/**
	 * Coerce a Number value into the required target class, if necessary.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setValue(Object value) {
		if (value instanceof Number) {
			super.setValue(NumberUtils.convertNumberToTargetClass((Number) value, this.numberClass));
		} else {
			super.setValue(value);
		}
	}

	/**
	 * Format the Number as String, using the specified NumberFormat.
	 */
	@Override
	public String getAsText() {
		Object value = getValue();
		if (value == null) {
			return "";
		}
		if (this.numberFormat != null) {
			// Use NumberFormat for rendering value.
			return this.numberFormat.format(value);
		} else {
			// Use toString method for rendering value.
			return value.toString();
		}
	}
}
