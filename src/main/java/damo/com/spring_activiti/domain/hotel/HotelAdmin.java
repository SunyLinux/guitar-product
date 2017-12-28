package damo.com.spring_activiti.domain.hotel;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the hotel_admin database table.
 * 
 */
@Entity
@Table(name="hotel_admin")
public class HotelAdmin implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Column(nullable=false, length=255)
	private String email;

	@Column(name="login_id", nullable=false, length=255)
	private String loginId;

	@Column(nullable=false, length=255)
	private String name;

	@Column(nullable=false, length=255)
	private String password;

	@Column(name="tenant_id", nullable=false, length=50)
	private String tenantId;

	public HotelAdmin() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return "HotelAdmin [id=" + id + ", "
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (email != null ? "email=" + email + ", " : "")
				+ (loginId != null ? "loginId=" + loginId + ", " : "")
				+ (name != null ? "name=" + name + ", " : "")
				+ (password != null ? "password=" + password : "") + "]";
	}

}