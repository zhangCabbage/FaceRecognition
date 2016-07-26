/**
 * 
 */
package com.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.framework.core.Page;

/**
 * 通用DAO
 */
public interface IGenericHBDao {
	
	Session getHBSession();
	/**
	 * 查询所有记录
	 * @param <T>
	 * @param c	HIBERNATE 映射对象类
	 * @return
	 */
	<T> List<T> findAll(Class<T> c);
	/**
	 * 以CRITERIA方式自定义条件查询结果对象
	 * @param <T>
	 * @param criteria 组合条件CRITERIA
	 * @return
	 */
	<T> T findTByCriteria(DetachedCriteria criteria);
	/**
	 * 以CRITERIA方式自定义条件查询结果集
	 * @param <T>
	 * @param criteria 组合条件CRITERIA
	 * @return
	 */
	<T> List<T> findTsByCriteria(DetachedCriteria criteria);
	/**
	 * 以CRITERIA方式自定义条件查询指定字段之和
	 * @param criteria
	 * @param property
	 * @return
	 */
	Double findSumByCriteria(DetachedCriteria criteria, String property);
	/**
	 * 以CRITERIA方式自定义条件查询返回结果集数量
	 * @param criteria
	 * @return
	 */
	Integer findCountByCriteria(DetachedCriteria criteria);
	/**
	 * 通过条件集合存储对象查询结果集
	 * @param <T>
	 * @param t	条件集合存储对象
	 * @return
	 */
	<T> List<T> findTs(T t);
	/**
	 * 通过条件集合存储对象及时间段约束查询结果集
	 * @param <T>
	 * @param t	条件集合存储对象
	 * @param property	时间段字段映射属性
	 * @param start	开始时间
	 * @param end	结束时间
	 * @return
	 */
	<T> List<T> findTs(T t, String property, Date start, Date end);
	/**
	 * 通过HQL语句自定义查询结果集
	 * @param <T>
	 * @param hql	HQL语句
	 * @param args	查询条件集合
	 * @return
	 */
	<T> List<T> findTsByHQL(String hql, Object...args);
	/**
	 * 通过HQL语句自定义查询单条记录
	 * @param <T>
	 * @param hql	HQL语句
	 * @param args	查询条件集合
	 * @return
	 */
	<T> T findTByHQL(String hql, Object...args);
	/**
	 * 通过主键ID查询单条记录
	 * @param <T>
	 * @param c
	 * @param id
	 * @return
	 */
	<T> T findTById(Class<T> c, Object id);
	/**
	 * 通过条件集合存储对象分页查询结果集
	 * @param <T>
	 * @param page	分页属性对象
	 * @param t	条件集合存储对象
	 * @return
	 */
	<T> List<T> findPage(Page page, T t);
	/**
	 * 通过条件集合存储对象及时间段约束分页查询结果集
	 * @param <T>
	 * @param page	分页属性对象
	 * @param t	条件集合存储对象
	 * @param property	时间段字段映射属性
	 * @param start	开始时间
	 * @param end	结束时间
	 * @return
	 */
	<T> List<T> findPage(Page page, T t, String property, Date start, Date end);

	/**
	 * 通过自定义条件及时间段约束分页查询结果集
	 * @param <T>
	 * @param page	分页属性对象
	 * @param criteria	
	 * @param property	时间段字段映射属性
	 * @param start	开始时间
	 * @param end	结束时间
	 * @return
	 */
	public <T> List<T> findPage(Page page, DetachedCriteria criteria, String property, Date start, Date end);
	/**
	 * 以CRITERIA方式自定义条件分页查询结果集
	 * @param <T>
	 * @param page	分页属性对象
	 * @param criteria	组合条件CRITERIA
	 * @return 分页查询结果集
	 */
	<T> List<T> findPageByCriteria(Page page, DetachedCriteria criteria);
	/**
	 * 保持对象
	 * @param <T>
	 * @param t	需要保存的对象数据
	 * @return 保存成功后的对象
	 */
	<T> T save(T t);
	/**
	 * 保存或修改对象
	 * @param <T>
	 * @param t	需要保存或修改的对象数据
	 */
	<T> void saveOrUpdate(T t);
	/**
	 * 批量保存或修改对象
	 * @param <T>
	 * @param list	需要保存或修改的对象数据集
	 */
	<T> void saveOrUpdateAll(List<T> list);
	/**
	 * 修改对象
	 * @param <T>
	 * @param t	需要修改的对象数据
	 */
	<T> void update(T t);
	/**
	 * 删除对象
	 * @param <T>
	 * @param t	需要删除的对象数据
	 */
	<T> void delete(T t);
	/**
	 * 批量删除对象
	 * @param <T>
	 * @param list	需要删除的对象数据集
	 */
	<T> void deleteAll(List<T> list);
	/**
	 * 此方法不被spring事务监管，请使用IGenericJDBCDao.execute
	 * 自定义HQL语句执行，主要适用于INSERT/UPDATE/DELETE操作
	 * @param hql	HQL语句
	 * @param args	参数集
	 * @return 成功记录数
	 */
//	@Deprecated
	Integer executeHQL(String hql, Object...args);
	/**
	 * 此方法不被spring事务监管，请使用IGenericJDBCDao.execute
	 * 自定义SQL语句执行，主要适用于INSERT/UPDATE/DELETE操作
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return 成功记录数s
	 */
	Integer executeSQL(String sql, Object...args);
	/**
	 * 通过SQL语句自定义查询
	 * @param <T>
	 * @param c	存储结果集的目标对象类
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	<T> List<T> findTsBySQL(Class<T> c, String sql, Object...args);
	/**
	 * 通过SQL语句自定义查询
	 * @param <T>
	 * @param c	存储结果集的目标对象类
	 * @param page	分页属性对象
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	<T> List<T> findTsBySQL(Class<T> c, Page page, String sql, Object...args);
	/**
	 * 通过SQL语句自定义查询，结果以MAP的方式组装
	 * @param <T>
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	List<Map<String, Object>> findMapBySQL(String sql, Object...args);
	/**
	 * 通过SQL语句自定义查询，结果以MAP的方式组装
	 * @param <T>
	 * @param page	分页属性对象
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	List<Map<String, Object>> findMapBySQL(Page page, String sql, Object...args);
	/**
	 * 通过SQL语句自定义查询，结果以LIST的方式组装
	 * @param <T>
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	List<List<Object>> findListBySQL(String sql, Object...args);
	/**
	 * 通过SQL语句自定义查询，结果以LIST的方式组装
	 * @param <T>
	 * @param page	分页属性对象
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	List<List<Object>> findListBySQL(Page page, String sql, Object...args);
	/**
	 * 通过HQL语句自定义查询
	 * @param <T>
	 * @param page	分页属性对象
	 * @param hql	HQL语句
	 * @param args	参数集
	 * @return
	 */
	<T> List<T> findTsByHQL(Page page, String hql, Object...args);
	/**
	 * 通过HQL语句自定义查询，结果以MAP的方式组装
	 * @param <T>
	 * @param page	分页属性对象
	 * @param hql	HQL语句
	 * @param args	参数集
	 * @return
	 */
	List<Map<String, Object>> findMapByHQL(Page page, String hql, Object...args);
	/**
	 * 通过HQL语句自定义查询，结果以MAP的方式组装
	 * @param <T>
	 * @param hql	HQL语句
	 * @param args	参数集
	 * @return
	 */
	List<Map<String, Object>> findMapByHQL(String hql, Object...args);
	/**
	 * 通过HQL语句自定义查询，结果以LIST的方式组装
	 * @param <T>
	 * @param page	分页属性对象
	 * @param hql	HQL语句
	 * @param args	参数集
	 * @return
	 */
	List<List<Object>> findListByHQL(Page page, String hql, Object...args);
	/**
	 * 通过SQL语句自定义查询，结果以Object的方式组装
	 * @param <T>
	 * @param sql	SQL语句
	 * @param args	参数集
	 * @return
	 */
	Object findObjectBySQL(String sql, Object... args);
	/**
	 * 通过HQL语句自定义查询，结果以Object的方式组装
	 * @param <T>
	 * @param hql	HQL语句
	 * @param args	参数集
	 * @return
	 */
	Object findObjectByHQL(String hql, Object... args);
	/**
	 * 通过HQL语句查询指定行数，结果以List<Map<String, Object>>的方式组装
	 * @param hql
	 * @param count
	 * @return
	 */
	List<Map<String, Object>> findCertainMapByHQL(String hql,int count);
}
