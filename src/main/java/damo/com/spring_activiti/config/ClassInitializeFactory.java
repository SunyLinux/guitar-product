package damo.com.spring_activiti.config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import damo.com.spring_activiti.config.annotation.Description;
import damo.com.spring_activiti.domain.enums.CustomEnum;
import damo.com.spring_activiti.domain.enums.IEnumBean;
import damo.com.spring_activiti.domain.enums.SystemEnum;
import damo.com.spring_activiti.domain.erm.Authority;
import damo.com.spring_activiti.domain.erm.Menu;
import damo.com.spring_activiti.domain.erm.User;
import damo.com.spring_activiti.repository.AuthorityRepository;
import damo.com.spring_activiti.repository.erm.MenuRepository;
import damo.com.spring_activiti.repository.erm.UserRepository;
import damo.com.spring_activiti.security.AuthoritiesConstants;
import damo.com.spring_activiti.security.SecurityUtils;
import damo.com.spring_activiti.service.SystemEnumService;
import damo.com.spring_activiti.service.erm.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import damo.com.spring_activiti.config.annotation.Description;
import damo.com.spring_activiti.domain.enums.CustomEnum;
import damo.com.spring_activiti.domain.enums.IEnumBean;
import damo.com.spring_activiti.domain.enums.SystemEnum;
import damo.com.spring_activiti.domain.erm.Authority;
import damo.com.spring_activiti.domain.erm.Menu;
import damo.com.spring_activiti.domain.erm.User;
import damo.com.spring_activiti.repository.AuthorityRepository;
import damo.com.spring_activiti.repository.erm.MenuRepository;
import damo.com.spring_activiti.repository.erm.UserRepository;
import damo.com.spring_activiti.security.AuthoritiesConstants;
import damo.com.spring_activiti.security.SecurityUtils;
import damo.com.spring_activiti.service.SystemEnumService;
import damo.com.spring_activiti.service.erm.MenuService;

/**
 * System Init Data
 */
@Component
public  class  ClassInitializeFactory  implements InitializingBean {

    private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

    @Autowired
    private SystemEnumService systemEnumService;

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        loadSystemUser();
        loadSystemDate();
        loadSystemEnum();
        loadCustomEnum();
        loadInitMenu();
    }
    private  void loadSystemDate() throws  Exception{

    }

    private  void loadSystemUser() throws  Exception{
        Optional<User> optional = userRepository.findOneByLogin("admin");
        if(optional.isPresent()){
            return;
        }

        Authority authority = new Authority();
        authority.setName("ROLE_SYS_ADMIN");
        authorityRepository.save(authority);
        authority = new Authority();
        authority.setName("ROLE_TENANT_ADMIN");
        authorityRepository.save(authority);
        authority = new Authority();
        authority.setName("ROLE_TENANT_USER");
        authorityRepository.save(authority);
        authority = new Authority();
        authority.setName("ROLE_ANONYMOUS");
        authorityRepository.save(authority);


        Set<Authority> authorities= new HashSet<>();
        Authority  author = new Authority();
        author.setName("ROLE_SYS_ADMIN");
        authorities.add(author);
        User user= new User();
        user.setName("SysAdmin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setLogin("admin");
        user.setEmail("admin@qq.com");
        user.setActivated(true);
        user.setLangKey("zh-cn");
        user.setLastModifiedBy("admin");
        user.setCreatedBy("admin");
        user.setTenancyCode("admin");
        user.setAuthorities(authorities);
        userRepository.save(user);

    }

    /*Data Dictionary with Enums*/
    private  void loadSystemEnum() throws  Exception{
        List<Class<?>> list =  getAllAssignedClass(SystemEnum.class);
        for (Class<?> objClass : list) {
            String simpleName=objClass.getSimpleName();
            Description description = objClass.getAnnotation(Description.class);
            if(description != null){
                Class<?> status = description.Clazz();
                if(status.isEnum()){
                    Method method = status.getMethod("values");
                    Object[] arr = (Object[]) method.invoke(objClass);
                    Integer i = 0;
                    for (Object obj : arr) {
                        IEnumBean bean = (IEnumBean)obj;
                        SystemEnum system =systemEnumService.findOneByParentAndValue(simpleName, bean.value());
                        if(system == null){
                            system = new SystemEnum();
                            system.setType("system");
                            system.setParam(bean.param());
                            system.setParent(simpleName);
                            system.setText(bean.text());
                            system.setValue(bean.value());
                            systemEnumService.save(system);
                        }else {
                            system.setType("system");
                            system.setParam(bean.param());
                            system.setParent(simpleName);
                            system.setText(bean.text());
                            system.setValue(bean.value());
                            systemEnumService.save(system);
                        }
                    }
                }
            }

            SystemEnum system =systemEnumService.findOneByParentAndValue("system",simpleName);
            if(system==null){
                String  sName = description.Name();
                SystemEnum  parent = new SystemEnum();
                parent.setType("system");
                parent.setText(sName);
                parent.setValue(simpleName);
                parent.setParam("");
                parent.setParent("system");
                systemEnumService.save(parent);
            }
        }
    }


    private  void loadCustomEnum() throws  Exception{
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        Class<?>   objClass =   classloader.loadClass(CustomEnum.class.getName());
        Description description = objClass.getAnnotation(Description.class);
        if(description != null){
            Class<?> status = description.Clazz();
            if(status.isEnum()){
                Method method = status.getMethod("values");
                Object[] arr = (Object[]) method.invoke(objClass);
                for (Object obj : arr) {
                    IEnumBean bean = (IEnumBean)obj;
                    SystemEnum system =systemEnumService.findOneByParentAndValue("custom", bean.value());
                    if(system == null){
                        system = new SystemEnum();
                        system.setType("custom");
                        system.setParam(bean.param());
                        system.setParent("custom");
                        system.setText(bean.text());
                        system.setValue(bean.value());
                        systemEnumService.save(system);
                    }else {
                        system.setType("custom");
                        system.setParam(bean.param());
                        system.setParent("custom");
                        system.setText(bean.text());
                        system.setValue(bean.value());
                        systemEnumService.save(system);
                    }
                }
            }
        }
    }

    public static List<Class<?>> getAllAssignedClass(Class<?> cls) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (Class<?> c : getClasses(cls)) {
            if (cls.isAssignableFrom(c) && !cls.equals(c)) {
                classes.add(c);
            }
        }
        return classes;
    }
    public static List<Class<?>> getClasses(Class<?> cls) throws IOException, ClassNotFoundException {
        String pk = cls.getPackage().getName();
        String path = pk.replace('.', '/');
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL url = classloader.getResource(path);
        return getClasses(new File(url.getFile()), pk);
    }
    private static List<Class<?>> getClasses(File dir, String pk) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!dir.exists()) {
            return classes;
        }
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) {
                classes.addAll(getClasses(f, pk + "." + f.getName()));
            }
            String name = f.getName();
            if (name.endsWith(".class")) {
                classes.add(Class.forName(pk + "." + name.substring(0, name.length() - 6)));
            }
        }
        return classes;
    }

    /*Init Menus*/
    private  void loadInitMenu() throws  Exception {

        /*DelMenus */
        menuService.deleteAllInBatch();

        List<Menu> list = new ArrayList<>();
        /*Sys Manage*/
        getMenuERM(list);
        /*WorkFlow Manage*/
        getMenuWorkflow(list);

        if(list.size()>0){
            String tenantCode = SecurityUtils.getCurrentUserTenant();
            for(Menu menu:list){
                menu.setParam(AuthoritiesConstants.SYS_ADMIN);
            }
            menuService.save(list);
        }
    }
    /*Sys Management*/
    private  void getMenuERM(List<Menu> list) throws  Exception{
        Menu menu =new Menu();
        menu.setCode("10");
        menu.setName("SysManage");
        menu.setIcon("iconfont icon-xitong");
        menu.setType("group");
        menu.setPath("erm");
        list.add(menu);

        Menu child= new Menu();
        child.setCode("10.01");
        child.setName("MenuManage");
        child.setIcon("");
        child.setType("item");
        child.setPath("menu");
        child.setParent(menu);
        list.add(child);

        child= new Menu();
        child.setCode("10.02");
        child.setName("OrgManage");
        child.setIcon("");
        child.setType("item");
        child.setPath("org");
        child.setParent(menu);
        list.add(child);

        child= new Menu();
        child.setCode("10.03");
        child.setName("RoleManage");
        child.setIcon("");
        child.setType("item");
        child.setPath("role");
        child.setParent(menu);
        list.add(child);

        child= new Menu();
        child.setCode("10.04");
        child.setName("AuthManage");
        child.setIcon("");
        child.setType("item");
        child.setPath("auth");
        child.setParam("tenant");
        child.setParent(menu);
        list.add(child);


        child= new Menu();
        child.setCode("10.05");
        child.setName("LogAccess");
        child.setIcon("");
        child.setType("item");
        child.setPath("accesslog");
        child.setParent(menu);
        list.add(child);


        child= new Menu();
        child.setCode("10.06");
        child.setName("OprLog");
        child.setIcon("");
        child.setType("item");
        child.setPath("operationlog");
        child.setParent(menu);
        list.add(child);


        child= new Menu();
        child.setCode("10.10");
        child.setName("UserManage");
        child.setIcon("");
        child.setType("item");
        child.setPath("usermanage");
        child.setParent(menu);
        list.add(child);

    }
    /*WorkFlow Manage*/
    private  void getMenuWorkflow(List<Menu> list) throws  Exception{
        Menu menu =new Menu();
        menu.setCode("100");
        menu.setName("WorkFlowManage");
        menu.setIcon("iconfont icon-sheji");
        menu.setType("group");
        menu.setPath("activiti");
        list.add(menu);

        Menu child= new Menu();
        child.setCode("100.01");
        child.setName("FlowManage&Deploy");
        child.setIcon("");
        child.setType("item");
        child.setPath("workflow");
        child.setParent(menu);
        list.add(child);

        list.add(child);
    }

}
