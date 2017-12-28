package damo.com.spring_activiti.domain.hotel;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;


/**
 * The persistent class for the room database table.
 * 
 */
@Entity
@Table(name="room")
@Multitenant
@TenantDiscriminatorColumn(name="tenant_id", contextProperty="tenant.id")
@NamedQueries({
	@NamedQuery(
		name="find_room_by_id",
		query="select r from Room r where r.id=:id"
	),
	@NamedQuery(
			name="find_room_by_status",
			query="select r from Room r where r.status=:status"
	),
	@NamedQuery(
			name="find_room_by_serialNumber",
			query="select r from Room r where r.serialNumber=:serialNumber"
	),
	@NamedQuery(
			name="find_room_by_status_and_bednum",
			query="select r from Room r, Category c where r.status=:status and r.categoryId = c.id and c.bedNum=:bedNum"
	),	
	@NamedQuery(
			name="find_room_by_status_and_name",
			query="select r from Room r, Category c where r.status=:status and r.categoryId = c.id and c.name=:name"
	)
}
)
public class Room implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="category_id")
	private int categoryId;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Column(length=255)
	private String position;

	@Column(name="serial_number", length=255)
	private String serialNumber;

	@Column(nullable=false, length=50)
	private String status;

//	@Column(name="tenant_id", nullable=false, insertable=false, updatable=false, length=50)
//	private String tenantId;

	public Room() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

//	public String getTenantId() {
//		return this.tenantId;
//	}
//
//	public void setTenantId(String tenantId) {
//		this.tenantId = tenantId;
//	}

	@Override
	public String toString() {
		return "Room [id="
				+ id
				+ ", categoryId="
				+ categoryId
				+ ", "
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (position != null ? "position=" + position + ", " : "")
				+ (serialNumber != null ? "serialNumber=" + serialNumber + ", "
						: "") + (status != null ? "status=" + status : "")
				+ "]";
	}

}