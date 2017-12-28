package damo.com.spring_activiti.domain.hotel;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;


/**
 * The persistent class for the rent_history database table.
 * 
 */
@Entity
@Table(name = "rent_history")
@Multitenant
@TenantDiscriminatorColumn(name="tenant_id", contextProperty="tenant.id")
@NamedQueries({ 
	@NamedQuery(
		name="find_renthistory_all",
		query="select h from RentHistory h order by h.createTime DESC"
	),
	@NamedQuery(
			name="find_renthistory_by_id",
			query="select h from RentHistory h where h.id=:id order by h.createTime DESC"
	),
	@NamedQuery(
			name="find_renthistory_by_room_id",
			query="select h from RentHistory h where h.roomId=:roomId order by h.createTime DESC"
	),
	@NamedQuery(
			name="find_renthistory_by_room_id_and_amount_not_checkout",
			query="select h from RentHistory h where h.roomId=:roomId and h.amount=-1 order by h.createTime DESC"
	),
	@NamedQuery(
		name="find_renthistory_by_hotel_guest_id",
		query="select h from RentHistory h where h.hotelGuestId=:hotelGuestId order by h.createTime DESC"
	),
	@NamedQuery(
			name="find_renthistory_by_hotel_guest_name",
			query="select h from RentHistory h, HotelGuest g where h.hotelGuestId=g.id and g.name=:hotelGuestName order by h.createTime DESC"
	),
	@NamedQuery(
			name="find_renthistory_by_room_id_and_hotel_guest_id",
			query="select h from RentHistory h where h.hotelGuestId=:hotelGuestId and h.roomId=:roomId order by h.createTime DESC"
	),
	@NamedQuery(
			name="find_renthistory_by_room_id_and_hotel_guest_id_and_amount_not_checkout",
			query="select h from RentHistory h where h.hotelGuestId=:hotelGuestId and h.roomId=:roomId and h.amount=-1 order by h.createTime DESC"
	)
})
public class RentHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique=true/*, nullable=false*/)
	private int id;

	@Column(nullable=false)
	private double amount;

	@Column(name="create_time", nullable=false)
	private Timestamp createTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time", nullable=false)
	private Date endTime;

	@Column(name="hotel_guest_id", nullable=false)
	private int hotelGuestId;

	@Column(name="room_id", nullable=false)
	private int roomId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time", nullable=false)
	private Date startTime;

//	@Column(name="tenant_id", nullable=false, insertable=false, updatable=false, length=50)
//	private String tenantId;

	public RentHistory() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getAmount() {
		return this.amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public int getHotelGuestId() {
		return this.hotelGuestId;
	}

	public void setHotelGuestId(int hotelGuestId) {
		this.hotelGuestId = hotelGuestId;
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
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
		return "RentHistory [id=" + id + ", amount=" + amount + ", "
				+ (createTime != null ? "createTime=" + createTime + ", " : "")
				+ (endTime != null ? "endTime=" + endTime + ", " : "")
				+ "hotelGuestId=" + hotelGuestId + ", roomId=" + roomId + ", "
				+ (startTime != null ? "startTime=" + startTime + ", " : "")
				+ "]";
	}

}
//@TenantDiscriminatorColumns({
//@TenantDiscriminatorColumn(name = "tenant_id", contextProperty = "tenant.id"),
//@TenantDiscriminatorColumn(name = "hotel_guest_id", contextProperty = "hotel.guest.id")
//})