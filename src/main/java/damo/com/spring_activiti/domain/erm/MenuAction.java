package damo.com.spring_activiti.domain.erm;


import com.fasterxml.jackson.annotation.JsonIgnore;
import damo.com.spring_activiti.domain.AbstractEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/12/7.
 */
@Entity
@Table(name = "t_erm_menu_action")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MenuAction extends AbstractEntity {

    /*ActionValue*/
    @Column(name = "value")
    private String value;
    /*ActionText*/
    @Column(name = "text")
    private String text;

    /*MenuId*/
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="menu_id")
    @JsonIgnore
    private Menu menu;

    /*MenuIcon*/
    @Column(name = "icon")
    private String icon;

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
