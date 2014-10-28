package common;

import org.apache.solr.common.SolrInputDocument;

public interface IModelObject {
	/**
	 * get object id
	 * 
	 * @return the object id
	 */
	Integer getId();

	/**
	 * set object id
	 * 
	 * @param id
	 *            the object id
	 */
	void setId(Integer id);
	
	SolrInputDocument getSolrDoc();
}
