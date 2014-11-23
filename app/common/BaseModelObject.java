package common;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import models.spellchecker.SolrDao;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.solr.common.SolrInputDocument;

@MappedSuperclass
public abstract class BaseModelObject implements IModelObject,
		Comparable<IModelObject> {

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED")
	private Date created;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer _id) {
		id = _id;
	}

	/*
	 * @PrePersist private void ensureId(){
	 * this.setId(UUID.randomUUID().toString()); }
	 */
	@Override
	public int compareTo(IModelObject o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != this.getClass())
			return false;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.getId(), ((IModelObject) other).getId());
		return eb.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	@Override
	public SolrInputDocument getSolrDoc() {
		return null;
	}

	public void putSolrDoc() {
		SolrInputDocument doc = this.getSolrDoc();
		if (doc != null)
			new SolrDao().putDoc(doc);
	}

	@PrePersist
	protected void onCreate() {
		created = new Date();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	
	
}