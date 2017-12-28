package damo.com.spring_activiti.service.erm;

import damo.com.spring_activiti.repository.erm.RolePermissionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import damo.com.spring_activiti.domain.erm.RolePermission;
import damo.com.spring_activiti.repository.erm.RolePermissionRepository;

/**
 * Created by Administrator on 2016/12/8.
 */
@Service
@Transactional
public class RolePermissionService {

    private final Logger log = LoggerFactory.getLogger(RolePermission.class);

    @Autowired
    private RolePermissionRepository rolePermissionRepository;


}
