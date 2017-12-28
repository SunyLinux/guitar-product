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
import org.eclipse.persistence.annotations.TenantDiscriminatorColumns;


/**
 * The persistent class for the hotel_guest database table.
 * 
 */
@Entity
@Table(name="hotel_guest")
@Multitenant
@TenantDiscriminatorColumn(name="tenant_id", contextProperty="tenant.id")

@TenantDiscriminatorColumns({
	@TenantDiscriminatorColumn(name="tenant_id", contextProperty="tenant.id"),
	@TenantDiscriminatorColumn(name = "guest_id", contextProperty="guest.id")
})
	
@NamedQueries({
	@NamedQuery(
			name="find_hotelguest_all",
			query="select g from HotelGuest g"
	),
	@NamedQuery(
			name="find_hotelguest_by_id",
			query="select g from HotelGuest g where g.id=:id"
	),
	@NamedQuery(
			name="find_hotelguest_by_name",
			query="select g from HotelGuest g where g.name=:name"
	)
}
)
public class HotelGuest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(nullable=false, length=255)
	private String address;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Column(nullable=false, length=64)
	private String name;

	@Column(nullable=false, length=64)
	private String telephone;

	@Column(name="tenant_id", nullable=false, insertable=false, updatable=false, length=50)
	private String tenantId;

	public HotelGuest() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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
		return "HotelGuest [id=" + id + ", "
				+ (address != null ? "address=" + address + ", " : "")
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (telephone != null ? "telephone=" + telephone + ", " : "")
				+ "]";
	}

}