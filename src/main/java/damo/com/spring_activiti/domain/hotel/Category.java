package damo.com.spring_activiti.domain.hotel;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the category database table.
 * 
 */
@Entity
@Table(name="category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	@Column(unique=true, nullable=false)
	private int id;

	@Column(name="bed_num")
	private int bedNum;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Column(nullable=false, length=255)
	private String name;

	private double price;

	@Column(name="tenant_id", nullable=false, length=50)
	private String tenantId;

	public Category() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBedNum() {
		return this.bedNum;
	}

	public void setBedNum(int bedNum) {
		this.bedNum = bedNum;
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

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", bedNum=" + bedNum + ", "
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (name != null ? "name=" + name + ", " : "") + "price="
				+ price + "]";
	}

}