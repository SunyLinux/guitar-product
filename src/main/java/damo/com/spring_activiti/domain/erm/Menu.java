package damo.com.spring_activiti.domain.erm;


import damo.com.spring_activiti.domain.AbstractEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/12/7.
 */
@Entity
@Table(name = "t_erm_menu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Menu  extends AbstractEntity {

    /*MenuId*/
    @Column(name = "code")
    private String code;

    /*MenuName*/
    @Column(name = "name")
    private String name;

    /*MenuIcon*/
    @Column(name = "icon")
    private String icon;

    /*Menurl*/
    @Column(name = "path")
    private String path;

    /*MenuType（ group  MenuGroup  item MenuItem）*/
    @Column(name = "type")
    private String type;


    /*ParentMenu*/
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private Menu parent;


    /*Menu of Roles*/
    @OneToMany(mappedBy = "menu",cascade = CascadeType.ALL,orphanRemoval = true,fetch=FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MenuAction> actions= new HashSet<MenuAction>();

    /*param*/
    private String param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MenuAction> getActions() {
        return actions;
    }

    public void setActions(Set<MenuAction> actions) {
        this.actions = actions;
    }


    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Menu() {


    }
}
