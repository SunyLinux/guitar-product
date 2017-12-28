package damo.com.spring_activiti.domain.erm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import damo.com.spring_activiti.domain.BaseEntity;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/7.
 */
@Entity
@Table(name = "t_erm_role_menu")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleMenu  extends BaseEntity {

    /*Role*/
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="role_id")
    @JsonIgnore
    private Role role;

    /*Menu*/
    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="menu_id")
    @JsonIgnore
    private Menu menu;


    /*Actions of Role*/
    @OneToMany(mappedBy = "roleMenu",cascade= CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<RoleMenuAction> actions= new HashSet<RoleMenuAction>();


    /*Pessions of Role*/
    @OneToMany(mappedBy = "roleMenu",cascade= CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<RolePermission> permissions= new HashSet<RolePermission>();

    /*cofig*/
    @Column(name = "isconfig")
    private Boolean isConfig=false;


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }


    public Set<RoleMenuAction> getActions() {
        return actions;
    }

    public void setActions(Set<RoleMenuAction> actions) {
        this.actions = actions;
    }

    public Set<RolePermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public Boolean getIsConfig() {
        return isConfig;
    }

    public void setIsConfig(Boolean isConfig) {
        this.isConfig = isConfig;
    }
}
