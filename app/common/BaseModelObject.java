package common;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


@MappedSuperclass
public abstract class BaseModelObject implements IModelObject,
		Comparable<IModelObject> {

	@Id
	@Column(name = "ID")
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		id = _id;
	}

	@PrePersist
	private void ensureId(){
	    this.setId(UUID.randomUUID().toString());
	}
	
	@Override
	public int compareTo(IModelObject o) {
		return this.getId().compareTo(o.getId());
	}

	public boolean equals(Object other) {
		if (other == null || other.getClass() != this.getClass())
			return false;
		EqualsBuilder eb = new EqualsBuilder();
		eb.append(this.getId(), ((IModelObject) other).getId());
		return eb.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

}