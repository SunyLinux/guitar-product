package damo.com.spring_activiti.web.rest.erm;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import damo.com.spring_activiti.domain.erm.*;
import damo.com.spring_activiti.repository.erm.RoleMenuActionRepository;
import damo.com.spring_activiti.repository.erm.RoleMenuRepository;
import damo.com.spring_activiti.repository.erm.RolePermissionRepository;
import damo.com.spring_activiti.service.erm.*;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

import damo.com.spring_activiti.domain.erm.Menu;
import damo.com.spring_activiti.domain.erm.MenuAction;
import damo.com.spring_activiti.domain.erm.Role;
import damo.com.spring_activiti.domain.erm.RoleMenu;
import damo.com.spring_activiti.domain.erm.RoleMenuAction;
import damo.com.spring_activiti.domain.erm.RolePermission;
import damo.com.spring_activiti.domain.erm.User;
import damo.com.spring_activiti.repository.erm.RoleMenuActionRepository;
import damo.com.spring_activiti.repository.erm.RoleMenuRepository;
import damo.com.spring_activiti.repository.erm.RolePermissionRepository;
import damo.com.spring_activiti.service.erm.AuthorService;
import damo.com.spring_activiti.service.erm.MenuActionService;
import damo.com.spring_activiti.service.erm.MenuService;
import damo.com.spring_activiti.service.erm.RoleService;
import damo.com.spring_activiti.service.erm.UserService;

/**
 * Auth Managment
 */
@RestController
@RequestMapping("/api/auth")
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    @Autowired
    private AuthorService authorService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuActionService menuActionService;
    @Autowired
    private RoleMenuRepository roleMenuRepository;
    @Autowired
    private RoleMenuActionRepository roleMenuActionRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;
    @Autowired
    private UserService userService;

    /**
     * User QueryLsit
     */
    @RequestMapping(value = "/findAllRole",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Role>> findAllRole(String name) {
        List<Role> list=new ArrayList<>();
        if(StringUtils.isNotBlank(name)){
            list=roleService.findAllByName(name);
        }else{
            list=roleService.findAll();
        }
        return new ResponseEntity(list,HttpStatus.OK);
    }

    /**
     * User QueryMenu & Action
     */
    @RequestMapping(value = "/findAllRoleMenu",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List> findAllRoleMenu(@RequestParam Long roleId) {
    //{id:11,name:"Roles Manage",status:false,parent:{id:1,name:"System Manage",status:false},
    // oprs:[{id:111,name:"Add",flag:"add",status:false},{id:222,name:"Delete",flag:"del",status:false}]}
        List listAll=new ArrayList<>();

//        List<Menu> listMenu=menuService.findAllMenuItems();
        List<Menu> listMenu=menuService.findAllItems();
        List<RoleMenu> list=authorService.findAllRoleMenu(roleId);
        for (Menu menu:listMenu){
            Map<String,Object> map=new HashMap<>();
            Map<String,Object> pmap=new HashMap<>();
            pmap.put("id",menu.getParent().getId());
            pmap.put("name",menu.getParent().getName());
            pmap.put("status",false);

            map.put("id",menu.getId());
            map.put("name",menu.getName());
            map.put("status",false);

            List<Map<String,Object>> listAction=new ArrayList<>();
            for (MenuAction ma: menu.getActions()){
                Map<String,Object> lmap=new HashMap<>();
                lmap.put("id",ma.getId());
                lmap.put("name",ma.getText());
                lmap.put("status",false);
                listAction.add(lmap);
            }

            for (RoleMenu rm : list){
                if(rm.getMenu().getId().equals(menu.getId())){
                    pmap.put("status",true);
                    map.put("status",true);
                    for (RoleMenuAction rma : rm.getActions()){
                        for(Map<String,Object> m : listAction){
                            if(m.get("id").toString().equals(rma.getMenuAction().getId().toString())){
                                m.put("status",true);
                            }
                        }
                    }
                }
            }

            map.put("parent",pmap);
            map.put("actions",listAction);
            map.put("roleId",roleId);
            listAll.add(map);
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * User QueryMenu && Perssions
     */
    @RequestMapping(value = "/findAllRolePermission",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List> findAllRolePermission(@RequestParam Long roleId) {
        List listAll=new ArrayList<>();

        List<Menu> listMenu=menuService.findAllMenuItems();
        List<RoleMenu> list=authorService.findAllRoleMenu(roleId);
        for (Menu menu:listMenu){
            Map<String,Object> map=new HashMap<>();
            map.put("id",menu.getId());
            map.put("name",menu.getName());
            map.put("parentName",menu.getParent().getName());
            map.put("status",false);
            for (RoleMenu rm : list){
                if(rm.getMenu().getId().equals(menu.getId())){
                    if(rm.getIsConfig()!=null && rm.getIsConfig()){
                        map.put("status",true);
                    }
                }
            }
            map.put("roleId",roleId);
            listAll.add(map);
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * QueryUser in Org.
     */
    @RequestMapping(value = "/findPermissionUser",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User>> findPermissionUser(Long orgId) throws URISyntaxException {
        log.debug("REST request to save Menu : {}");
//        {
//            "id" : 2,
//            "code" : "1",
//            "name" : "Leader",
//            "cls" : "iconfont icon-msnui-org",
//            "fullName" : null,
//            "orgzType" : 2,
//            "isParent" : false,
//            "orgzUrl" : "Gun Tech./",
//            "parentId" : 1
//        }
        List listAll=new ArrayList<>();
        List<User> list = userService.findAll(orgId);
        for (User user : list){
            Map<String,Object> map=new HashMap<>();
            map.put("id","user_"+user.getId());
            map.put("name",user.getName());
            map.put("cls","iconfont icon-lingdao");
            map.put("parentId",orgId);
            listAll.add(map);
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * User QueryMenu & PerssionsList
     */
    @RequestMapping(value = "/findRolePermissions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List> findRolePermissions(Long roleId,Long menuId) throws URISyntaxException {
        log.debug("REST request to save Menu : {}");
        List listAll=new ArrayList<>();

        RoleMenu rm=roleMenuRepository.findOneByRoleIdAndMenuId(roleId,menuId);
        for(RolePermission permission:rm.getPermissions()){
            listAll.add("user_" + permission.getUser().getId());
        }

        return new ResponseEntity(listAll,HttpStatus.OK) ;
    }

    /**
     * Save Roles' Menu opr.
     */
    @RequestMapping(value = "/saveRoleMenu",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> saveRoleMenu(String jsondata) throws Exception{

        JSONArray array=new JSONArray(jsondata);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj=array.getJSONObject(i);
            doSave(obj);
        }
        return new ResponseEntity(HttpStatus.OK) ;
    }

    private void doSave(JSONObject jsonObject) throws Exception{
        Long roleId=jsonObject.getLong("roleId");
        Long id=jsonObject.getLong("id");
        boolean status=jsonObject.getBoolean("status");

//        JSONObject parentObject=jsonObject.getJSONObject("parent");
//        Long parentId=parentObject.getLong("id");
//        boolean parentStatus=parentObject.getBoolean("status");

        List<Map<String,Object>> listActions=new ArrayList<>();
        JSONArray actionsArray=jsonObject.getJSONArray("actions");
        for (int i = 0; i < actionsArray.length(); i++) {
            Map<String,Object> map=new HashMap<>();
            JSONObject action=actionsArray.getJSONObject(i);
            Long cId=action.getLong("id");
            boolean cStatus=action.getBoolean("status");
            map.put("id",cId);
            map.put("status",cStatus);
            listActions.add(map);
        }

        RoleMenu roleMenu=authorService.findOneByRoleIdAndMenuId(roleId,id);
        if(roleMenu==null){
            if(status){
                roleMenu=new RoleMenu();
                roleMenu.setRole(roleService.findOne(roleId));
                roleMenu.setMenu(menuService.findOne(id));
                for (Map<String,Object> map : listActions){
                    if(map.get("status").toString().equals("true")){
                        MenuAction ma=menuActionService.findOne(Long.parseLong(map.get("id").toString()));
                        RoleMenuAction rma=new RoleMenuAction();
                        rma.setRoleMenu(roleMenu);
                        rma.setMenuAction(ma);
                        roleMenu.getActions().add(rma);
                    }
                }

                roleMenuRepository.save(roleMenu);
            }
        }else{
            if(status){
                roleMenuActionRepository.deleteInBatch(roleMenu.getActions());

                Set<RoleMenuAction> setRma=new HashSet<>();
                for (Map<String,Object> map : listActions){
                    if(map.get("status").toString().equals("true")){
                        MenuAction ma=menuActionService.findOne(Long.parseLong(map.get("id").toString()));
                        RoleMenuAction rma=new RoleMenuAction();
                        rma.setRoleMenu(roleMenu);
                        rma.setMenuAction(ma);
                        setRma.add(rma);
                    }
                }
                roleMenu.setActions(setRma);

                roleMenuRepository.save(roleMenu);
            }else{
                roleMenuRepository.delete(roleMenu.getId());

            }
        }
    }

    /**
     * Save Roles' Menu & Perssions
     */
    @RequestMapping(value = "/saveRolePermission",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> saveRolePermission(Long roleId,Long menuId,String jsondata,String deldata) throws Exception{
        List<Long> listUser=new ArrayList<>();// User Added
        List<Long> listDelUser=new ArrayList<>();//User deleted

        JSONArray array=new JSONArray(jsondata);
        for (int i = 0; i < array.length(); i++) {
            listUser.add(array.getLong(i));
        }

        JSONArray array2=new JSONArray(deldata);
        for (int j = 0; j < array2.length(); j++) {
            listDelUser.add(array.getLong(j));
        }

        Map<Long,Long> map=new HashMap<>();
        RoleMenu rm=roleMenuRepository.findOneByRoleIdAndMenuId(roleId, menuId);
        if(rm==null){
            rm=new RoleMenu();
            rm.setRole(roleService.findOne(roleId));
            rm.setMenu(menuService.findOne(menuId));
        }
        rm.setIsConfig(true);
        roleMenuRepository.save(rm);

        for(RolePermission permission:rm.getPermissions()){
            Long userId=permission.getUser().getId();
            map.put(userId,permission.getId());
        }

        for (Long id : listUser){
            if(!map.containsKey(id)){
                RolePermission rp=new RolePermission();
                rp.setRoleMenu(rm);
                rp.setUser(userService.getUserWithAuthorities(id));
                rolePermissionRepository.save(rp);
            }
        }

        for (Long id : listDelUser){
            if(map.containsKey(id)){
                Long rpId=map.get(id);
                rolePermissionRepository.deleteById(rpId);
            }
        }

        return new ResponseEntity(HttpStatus.OK) ;
    }


}
