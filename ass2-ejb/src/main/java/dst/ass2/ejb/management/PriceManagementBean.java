package dst.ass2.ejb.management;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import dst.ass2.ejb.management.interfaces.IPriceManagementBean;
import dst.ass2.ejb.model.IPrice;
import dst.ass2.ejb.model.impl.Price;

@Singleton
@Local(IPriceManagementBean.class)
@Startup
public class PriceManagementBean implements IPriceManagementBean {
	
	@PersistenceContext private EntityManager entityManager;
	private ConcurrentHashMap<Integer, IPrice> priceMap;
	
	public PriceManagementBean() {
		priceMap = new ConcurrentHashMap<Integer, IPrice>();
	}
	
	@PostConstruct
	private void initPriceMap() {
		@SuppressWarnings("unchecked")
		List<Price> prices = entityManager.createQuery("select p from Price p").getResultList();
		for(Price price : prices) {
			priceMap.put(price.getNrOfHistoricalTasks(), price);
		}
	}
	
	@Override
	public BigDecimal getPrice(Integer nrOfHistoricalTasks) {
		if(priceMap.containsKey(nrOfHistoricalTasks)) {
			return priceMap.get(nrOfHistoricalTasks).getPrice();
		} else {
			return new BigDecimal(0.0);
		}
	}

	@Override
	public void setPrice(Integer nrOfHistoricalTasks, BigDecimal price) {
		if(priceMap.containsKey(nrOfHistoricalTasks)) {
			IPrice p = priceMap.get(nrOfHistoricalTasks);
			if(!p.getPrice().equals(price)) {
				p.setPrice(price);
				entityManager.persist(p);
			}
		} else {
			Price p = new Price();
			p.setNrOfHistoricalTasks(nrOfHistoricalTasks);
			p.setPrice(price);
			entityManager.persist(p);
			priceMap.put(p.getNrOfHistoricalTasks(), p);
		}
	}
	
	
	@Override
	public void clearCache() {
		priceMap.clear();
	}
}
