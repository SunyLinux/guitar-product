package damo.com.spring_activiti.repository.hotel.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseRepositoryIm {

	protected EntityManager em = null;
	protected EntityManagerFactory emf = null;
	protected Map<String, String> contextProps = null;
	protected String persistence_unit_name = null;
	
	private static final String TENANT_ID = "tenant.id";
//	private static final String HOTEL_GUEST_ID = "hotel.guest.id";
	
//	protected BaseDao(String pu_name, HotelAdminSessionCookie cookie){
//		this(pu_name);
//		if(cookie != null){
//			contextProps = new HashMap<String, String>();
//			contextProps.put(TENANT_ID, cookie.getTenant_id());
//		}
//	}
//	
//	protected BaseDao(String pu_name, HotelGuestSessionCookie cookie){
//		this(pu_name);
//		if (cookie != null) {
//			contextProps = new HashMap<String, String>();
//			contextProps.put(TENANT_ID, cookie.getTenant_id());
//			contextProps.put(HOTEL_GUEST_ID, cookie.getLogin_id());
//		}
//	}
	
	protected BaseRepositoryIm(String pu_name){
		this.persistence_unit_name = pu_name;
	}
	
	protected BaseRepositoryIm(String pu_name, Map<String, String> props){
		this.persistence_unit_name = pu_name;
		if (props != null) {
			contextProps = props;
		}
	}
	
	public void transactionBegin(){
		em.getTransaction().begin();
	}
	
	public void transactionCommit(){
		em.getTransaction().commit();
	}
	
	public void transactionRollBack(){
		em.getTransaction().rollback();
	}
	
	public String getTenantId(){
		return contextProps.get(TENANT_ID);
	}
	
	public <T> void update(T t){
		//em.getTransaction().begin();
		em.merge(t);
		//em.getTransaction().commit();
	}
	
	public <T> void save(T t) {
		//em.getTransaction().begin();
		em.persist(t);
		//em.getTransaction().commit();
	}
	
	public <T> void saveBulk(List<T> bulk) {
		em.getTransaction().begin();
		for(T t:bulk){
			em.persist(t);
		}
		em.getTransaction().commit();
	}

	public void start() throws Exception{
		if(em == null){
			if (persistence_unit_name == null || "".equals(persistence_unit_name)) {
				throw new Exception("pu is null.");
			}
			synchronized(this){
				if(em == null){
					emf = Persistence
							.createEntityManagerFactory(persistence_unit_name);
					em = emf.createEntityManager(contextProps);
				}
			}
			if(em == null){
				throw new Exception("cannot create entity manager");
			}
		}
	}

	public void end() throws Exception{
		if(em != null){
			em.close();
			em = null;
		}

		if(emf != null){
			emf.close();
			emf = null;
		}
	}
	
	protected <T> T load(Class<T> clazz, Object key) {
		return em.find(clazz, key);
	}
	
	protected <T> List<T> loadAll(Class<T> clazz){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(clazz);
		TypedQuery<T> query = em.createQuery(cq);
		return query.getResultList();
	}
	
	protected <T> List<T> loadAll_tenant(String queryName, Class<T> clazz){
		TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
		return query.getResultList();
	}
	
	protected <T> T querySingleResult(String queryName, Class<T> clazz, String argName, Object argValue){
		TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
		query.setParameter(argName, argValue);
		return query.getSingleResult();
	}
	
	protected <T> List<T> queryResultList(String queryName, Class<T> clazz, String argName, Object argValue){
		TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
		query.setParameter(argName, argValue);
		return query.getResultList();
	}
	
	protected <T> List<T> queryResultList(String queryName, Class<T> clazz, String[] argNames, Object[] argValues){
		TypedQuery<T> query = em.createNamedQuery(queryName, clazz);
		if(argNames != null && argValues != null){
			if(argNames.length != argValues.length){
				return null;
			}
			for(int i = 0; i < argNames.length; i++){
				query.setParameter(argNames[i], argValues[i]);
			}
		}
		return query.getResultList();
	}
	
}
