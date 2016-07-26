/**
 * 
 */
package com.framework.dao.impl;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.framework.core.Page;
import com.framework.dao.IGenericHBDao;

/**
 * @author dranson 2010-11-23
 */
@SuppressWarnings("unchecked")
public class GenericHBDao extends HibernateDaoSupport implements IGenericHBDao {

	@Override
	public Session getHBSession() {
		return getSession();
	}

	@Override
	public <T> List<T> findAll(Class<T> c) {
		return getHibernateTemplate().find("from " + c.getSimpleName());
	}

	@Override
	public <T> T findTByCriteria(DetachedCriteria criteria) {
		List<T> list = getHibernateTemplate().findByCriteria(criteria);
		if (list.size() != 0)
			return list.get(0);
		return null;
	}

	@Override
	public <T> List<T> findTsByCriteria(DetachedCriteria criteria) {
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public Double findSumByCriteria(final DetachedCriteria criteria, final String property) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Double>() {
			public Double doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				prepareCriteria(executableCriteria);
				Double sum = (Double) executableCriteria.setProjection(Projections.sum(property)).uniqueResult();
				return sum;
			}
		});
	}

	@Override
	public Integer findCountByCriteria(final DetachedCriteria criteria) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				prepareCriteria(executableCriteria);
				Object totalRows = executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
				return Integer.valueOf(totalRows.toString());
			}
		});
	}

	@Override
	public <T> List<T> findTs(T t) {
		return getHibernateTemplate().findByExample(t);
	}

	@Override
	public <T> List<T> findTs(T t, String property, Date start, Date end) {
		return findPage(null, t, property, start, end);
	}

	@Override
	public <T> List<T> findTsByHQL(String hql, Object... args) {
		return getHibernateTemplate().find(hql, args);
	}

	@Override
	public List<Map<String, Object>> findCertainMapByHQL(final String hql, final int count) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				query.setFirstResult(0);
				query.setMaxResults(count);
				return query.list();
			}
		});
	}

	@Override
	public <T> T findTByHQL(String hql, Object... args) {
		List<T> list = getHibernateTemplate().find(hql, args);
		if (list.size() != 0)
			return list.get(0);
		return null;
	}

	@Override
	public <T> T findTById(Class<T> c, Object id) {
		List<T> list = getHibernateTemplate().find("from " + c.getSimpleName() + " where id=?", id);
		if (list.size() != 0)
			return list.get(0);
		return null;
	}

	@Override
	public <T> List<T> findPage(Page page, T t) {
		Assert.notNull(t);
		DetachedCriteria criteria = DetachedCriteria.forClass(t.getClass());
		criteria.add(Example.create(t));
		return findPageByCriteria(page, criteria);
	}

	@Override
	public <T> List<T> findPage(Page page, T t, String property, Date start, Date end) {
		Assert.notNull(t);
		DetachedCriteria criteria = DetachedCriteria.forClass(t.getClass());
		criteria.add(Example.create(t));
		return findPage(page, criteria, property, start, end);
	}

	@Override
	public <T> List<T> findPage(Page page, DetachedCriteria criteria, String property, Date start, Date end) {
		if (start != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date date;
			if (start instanceof Timestamp) {
				date = new Timestamp(cal.getTimeInMillis());
			} else if (start instanceof Time) {
				date = new Time(cal.getTimeInMillis());
			} else {
				date = new java.sql.Date(cal.getTimeInMillis());
			}
			criteria.add(Restrictions.ge(property, date));
		}
		if (end != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(end);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			Date date;
			if (end instanceof Timestamp) {
				date = new Timestamp(cal.getTimeInMillis());
			} else if (end instanceof Time) {
				date = new Time(cal.getTimeInMillis());
			} else {
				date = new java.sql.Date(cal.getTimeInMillis());
			}
			criteria.add(Restrictions.le(property, date));
		}
		if (page != null)
			return findPageByCriteria(page, criteria);
		return getHibernateTemplate().findByCriteria(criteria);
	}

	@Override
	public <T> List<T> findPageByCriteria(final Page page, final DetachedCriteria criteria) {
		Assert.notNull(page);
		if (page.getOrderColumn() != null && page.getOrderColumn().length() != 0)
			criteria.addOrder(page.isOrderASC() ? Order.asc(page.getOrderColumn()) : Order.desc(page.getOrderColumn()));
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) throws HibernateException {
				Criteria executableCriteria = criteria.getExecutableCriteria(session);
				prepareCriteria(executableCriteria);
				Object totalRows = executableCriteria.setProjection(Projections.rowCount()).uniqueResult();
				executableCriteria.setProjection(null);
				page.setTotalRows(Integer.valueOf(totalRows.toString()));
				executableCriteria.setFirstResult(page.getPageSize() * (page.getCurrentPage() - 1));
				executableCriteria.setMaxResults(page.getPageSize());
				return executableCriteria.list();
			}
		});
	}

	@Override
	public <T> T save(T t) {
		getHibernateTemplate().save(t);
		return t;
	}

	@Override
	public <T> void saveOrUpdate(T t) {
		getHibernateTemplate().saveOrUpdate(t);
	}

	@Override
	public <T> void saveOrUpdateAll(List<T> list) {
		getHibernateTemplate().saveOrUpdateAll(list);
	}

	@Override
	public <T> void update(T t) {
		getHibernateTemplate().update(t);
	}

	@Override
	public <T> void delete(T t) {
		getHibernateTemplate().delete(t);
	}

	@Override
	public <T> void deleteAll(List<T> list) {
		getHibernateTemplate().deleteAll(list);
	}

	@Override
	public Integer executeHQL(final String hql, final Object... args) {
		// return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
		// public Integer doInHibernate(Session session) throws HibernateException, SQLException {
		// SessionImpl imp = (SessionImpl) session;
		// HQLQueryPlan plan = imp.getFactory().getQueryPlanCache().getHQLQueryPlan(hql, false, imp.getEnabledFilters());
		// return genericJDBCDao.execute(plan.getSqlStrings()[0], args);
		// }
		// });
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public Integer executeSQL(final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.executeUpdate();
			}
		});
	}

	@Override
	public <T> List<T> findTsBySQL(final Class<T> c, final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql).addEntity(c);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.list();
			}
		});
	}

	@Override
	public List<Map<String, Object>> findMapBySQL(final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.list();
			}
		});
	}

	@Override
	public List<List<Object>> findListBySQL(final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<List<Object>>>() {
			public List<List<Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.TO_LIST);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.list();
			}
		});
	}

	@Override
	public <T> List<T> findTsBySQL(final Class<T> c, final Page page, final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				String countSQL = " select count(*) " + removeSelect(removeOrders(sql));
				Integer count = Integer.valueOf(findObjectBySQL(countSQL, args).toString());
				Query query = session.createSQLQuery(sql).addEntity(c);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				page.setTotalRows(count);
				query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				return query.list();
			}
		});
	}

	@Override
	public List<Map<String, Object>> findMapBySQL(final Page page, final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				String countSQL = " select count(*) " + removeSelect4Nesting(removeOrders(sql));
				Integer count = Integer.valueOf(findObjectBySQL(countSQL, args).toString());
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				page.setTotalRows(count);
				query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				return query.list();
			}
		});
	}

	@Override
	public List<List<Object>> findListBySQL(final Page page, final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<List<Object>>>() {
			public List<List<Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				String countSQL = " select count(*) " + removeSelect(removeOrders(sql));
				Integer count = Integer.valueOf(findObjectBySQL(countSQL, args).toString());
				Query query = session.createSQLQuery(sql);
				query.setResultTransformer(Transformers.TO_LIST);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				page.setTotalRows(count);
				query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				return query.list();
			}
		});
	}

	@Override
	public <T> List<T> findTsByHQL(final Page page, final String hql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<T>>() {
			public List<T> doInHibernate(Session session) throws HibernateException, SQLException {
				String countSQL = " select count(*) " + removeSelect(removeOrders(hql));
				Integer count = Integer.valueOf(findObjectByHQL(countSQL, args).toString());
				Query query = session.createQuery(hql);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				page.setTotalRows(count);
				query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				return query.list();
			}
		});
	}

	@Override
	public List<Map<String, Object>> findMapByHQL(final Page page, final String hql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				String countSQL = " select count(*) " + removeSelect(removeOrders(hql));
				Integer count = Integer.valueOf(findObjectByHQL(countSQL, args).toString());
				Query query = session.createQuery(hql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				page.setTotalRows(count);
				query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				return query.list();
			}
		});
	}

	@Override
	public List<Map<String, Object>> findMapByHQL(final String hql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<Map<String, Object>>>() {
			public List<Map<String, Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.list();
			}
		});
	}

	@Override
	public List<List<Object>> findListByHQL(final Page page, final String hql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<List<List<Object>>>() {
			public List<List<Object>> doInHibernate(Session session) throws HibernateException, SQLException {
				String countSQL = " select count(*) " + removeSelect(removeOrders(hql));
				Integer count = Integer.valueOf(findObjectByHQL(countSQL, args).toString());
				Query query = session.createQuery(hql);
				query.setResultTransformer(Transformers.TO_LIST);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				page.setTotalRows(count);
				query.setFirstResult((page.getCurrentPage() - 1) * page.getPageSize());
				query.setMaxResults(page.getPageSize());
				return query.list();
			}
		});
	}

	@Override
	public Object findObjectBySQL(final String sql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createSQLQuery(sql);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.uniqueResult();
			}
		});
	}

	@Override
	public Object findObjectByHQL(final String hql, final Object... args) {
		return getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < args.length; i++)
					query.setParameter(i, args[i]);
				return query.uniqueResult();
			}
		});
	}

	protected void prepareCriteria(Criteria criteria) {
		if (getHibernateTemplate().isCacheQueries()) {
			criteria.setCacheable(true);
			if (getHibernateTemplate().getQueryCacheRegion() != null)
				criteria.setCacheRegion(getHibernateTemplate().getQueryCacheRegion());
		}
		if (getHibernateTemplate().getFetchSize() > 0)
			criteria.setFetchSize(getHibernateTemplate().getFetchSize());
		if (getHibernateTemplate().getMaxResults() > 0)
			criteria.setMaxResults(getHibernateTemplate().getMaxResults());
		SessionFactoryUtils.applyTransactionTimeout(criteria, getSessionFactory());
	}

	/**
	 * 去除hql的select 子句，未考虑union的情况,用于pagedQuery.
	 * @see #pagedQuery(String,int,int,Object[])
	 * @param sql sql 语句
	 * @return String 处理后的hql语句
	 */
	public String removeSelect(String sql) {
		Assert.hasText(sql);
		int beginPos = sql.toLowerCase().lastIndexOf("from");
		Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	/**
	 * 处理含有嵌套及普通查询的条件(select * from x where field =(select something form some table where --))
	 * @param sql
	 * @return
	 */
	public String removeSelect4Nesting(String sql) {
		Assert.hasText(sql);
		int beginPos = sql.toLowerCase().indexOf("from");
		Assert.isTrue(beginPos != -1, " sql : " + sql + " must has a keyword 'from'");
		return sql.substring(beginPos);
	}

	/**
	 * 去除hql的orderby 子句
	 * @param sql hql 语句
	 * @return String 处理后的sql语句
	 */
	public static String removeOrders(String sql) {
		Assert.hasText(sql);
		Pattern p = Pattern.compile("order\\s*by[^\\)&&[\\w|\\W|\\s|\\S]]*", Pattern.CASE_INSENSITIVE);
		// Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*^\\)", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(sql);
		StringBuffer sb = new StringBuffer();
		while (m.find())
			m.appendReplacement(sb, "");
		m.appendTail(sb);
		return sb.toString();
	}
	
	
}
