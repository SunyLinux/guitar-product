package damo.com.spring_activiti.domain.erm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import damo.com.spring_activiti.domain.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Role Perssions
 */
@Entity
@Table(name = "t_erm_role_permission")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RolePermission extends BaseEntity {


    /* Menus of Role */
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="roleMenu_id")
    @JsonIgnore
    private RoleMenu roleMenu;

    /*User*/
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public RoleMenu getRoleMenu() {
        return roleMenu;
    }

    public void setRoleMenu(RoleMenu roleMenu) {
        this.roleMenu = roleMenu;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
