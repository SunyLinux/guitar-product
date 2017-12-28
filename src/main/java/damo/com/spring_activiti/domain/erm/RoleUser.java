package damo.com.spring_activiti.domain.erm;

import damo.com.spring_activiti.domain.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/7.
 */
@Entity
@Table(name = "t_erm_role_user")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleUser  extends BaseEntity {
    /*Role*/
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="role_id")
    private Role role;

    /*Useer*/
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
