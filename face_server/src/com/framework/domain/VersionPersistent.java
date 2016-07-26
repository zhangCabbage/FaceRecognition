 

package com.framework.domain;


/**
 */
public class VersionPersistent extends PersistentBase {

	/**
	 * 属性 serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 版本号
	 */
	private Integer version;
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
}
