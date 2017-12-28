package damo.com.spring_activiti.domain.hotel;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the tenant database table.
 * 
 */
@Entity
@Table(name="tenant")
public class Tenant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(name="tenant_id", unique=true, nullable=false, length=30)
	private String tenantId;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Column(length=1024)
	private String description;

	@Column(nullable=false, length=255)
	private String name;

	public Tenant() {
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Tenant ["
				+ (tenantId != null ? "tenantId=" + tenantId + ", " : "")
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (description != null ? "description=" + description + ", "
						: "") + (name != null ? "name=" + name : "") + "]";
	}

}