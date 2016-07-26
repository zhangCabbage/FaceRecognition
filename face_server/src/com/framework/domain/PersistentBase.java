package com.framework.domain;

import java.io.Serializable;

import com.framework.dao.IDGenerator;

/**
 * 持久化对象基类<br>
 * <p>
 * 项目中，所有持久化对象必须继承此类<br>
 * 继承属性为 id ：业务无关主键<br>
 * 系统原有类为CoreBase.java，此后对象需继承此基类
 * </P>

 */
public class PersistentBase implements IBase, Serializable {
	/**
	 * 属性 serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 业务无关主键
	 */
	private String id = IDGenerator.createID();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || !(obj instanceof PersistentBase)) {
			return false;
		}
		PersistentBase other = (PersistentBase) obj;
		if (other.getId() == null) {
			return false;
		}
		if (id == null) {
			return false;
		}
		return id.equals(other.getId());
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		} else {
			return super.hashCode();
		}
	}

	@Override
	public String toString() {
		return this.getClass().getName() + "[id=" + id + "]";
	}
}
