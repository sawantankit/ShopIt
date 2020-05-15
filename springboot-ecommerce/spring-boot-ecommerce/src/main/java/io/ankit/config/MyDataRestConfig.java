package io.ankit.config;


import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
//import javax.persistence.metamodel.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

import io.ankit.entity.Product;
import io.ankit.entity.ProductCategory;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{
	
	
	private EntityManager entityManager;
	
	@Autowired
    public MyDataRestConfig(EntityManager theEntityManager) {
        entityManager = theEntityManager;
    }

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		
		HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};
		
		//Disable Http method for Product: put, post and delete
		
		config.getExposureConfiguration()
		.forDomainType(Product.class)
		.withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
		.withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
		
		//Disable Http method for ProductCategory:  put, post and delete
		config.getExposureConfiguration()
		.forDomainType(ProductCategory.class)
		.withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
		.withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
		
		
		// Exposing ID's to JSON Response		
		
//		config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream()
//				.map(Type::getJavaType)
//				.toArray(Class[]::new));
		
//		    call an internal helper method
        exposeIds(config);
		
	}
	
	
	
	 private void exposeIds(RepositoryRestConfiguration config) {

	        // expose entity ids
	        //

	        // - get a list of all entity classes from the entity manager
	        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

	        // - create an array of the entity types
	        List<Class> entityClasses = new ArrayList<>();

	        // - get the entity types for the entities
	        for (EntityType tempEntityType : entities) {
	            entityClasses.add(tempEntityType.getJavaType());
	        }

	        // - expose the entity ids for the array of entity/domain types
	        Class[] domainTypes = entityClasses.toArray(new Class[0]);
	        config.exposeIdsFor(domainTypes);
	    }

	

}
