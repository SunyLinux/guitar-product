package damo.com.spring_activiti.web.rest.erm;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import damo.com.spring_activiti.service.erm.UserService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import damo.com.spring_activiti.service.erm.UserService;

/**
 * WorkFlow Controller
 */
@RestController
@RequestMapping("/api/workflow")
public class ActivitiResource {
    @Autowired
    protected UserService userService;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected IdentityService identityService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    ProcessEngineFactoryBean processEngine;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    /**
     * All Users
     *
     * @return
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> findAllUsers() throws URISyntaxException {
        List list=new ArrayList<>();

        List<User> users = identityService.createUserQuery().list();
        for (User user : users) {
            Map map=new HashMap<>();
            map.put("id",user.getId());
            map.put("pwd",user.getPassword());

            list.add(map);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * workflow defineList
     *
     * @return
     */
    @RequestMapping(value = "/process",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> processList() throws URISyntaxException {
        List list=new ArrayList<>();

        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.list();
        for (ProcessDefinition processDefinition : processDefinitionList) {
            Map map=new HashMap<>();
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();

            map.put("id",processDefinition.getId());
            map.put("name",processDefinition.getName());
            map.put("version",processDefinition.getVersion());
            map.put("key",processDefinition.getKey());
            map.put("deploymentId",processDefinition.getDeploymentId());
            map.put("suspended",processDefinition.isSuspended());
            map.put("resource",processDefinition.getResourceName());
            map.put("image",processDefinition.getDiagramResourceName());

            map.put("deploymentTime",deployment.getDeploymentTime());

            list.add(map);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * read resource by deployID
     *
     * @param processDefinitionId workflow define
     * @param resourceType        resource type(xml|image)
     * @throws Exception
     */
    @RequestMapping(value = "/resource/read")
    public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId, @RequestParam("resourceType") String resourceType,
                                 HttpServletResponse response) throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * ongoing workflowList
     *
     * @return
     */
    @RequestMapping(value = "/runing",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> runingProcess() throws URISyntaxException {
        List list=new ArrayList<>();

        List<ProcessInstance> processInstanceList = runtimeService.createProcessInstanceQuery().list();
        for (ProcessInstance processInstance : processInstanceList) {
            Map map=new HashMap<>();
            map.put("id",processInstance.getId());
            map.put("name",processInstance.getName());
            map.put("definitionId",processInstance.getProcessDefinitionId());
            map.put("deploymentId",processInstance.getDeploymentId());
            map.put("activityId",processInstance.getActivityId());
            map.put("nodeName","");
            map.put("suspended",processInstance.isSuspended());
            
            ProcessDefinitionEntity entity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getProcessDefinition(processInstance.getProcessDefinitionId());
            for (ActivityImpl bean :entity.getActivities()){
                if (bean.getId().equals(processInstance.getActivityId())){
                    map.put("nodeName",bean.getProperty("name"));
                }
            }

            list.add(map);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * Read Pics with traceTag.
     */
    @RequestMapping(value = "/trace")
    public void readResource(String executionId, HttpServletResponse response)
        throws Exception {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        // not using spring
//    ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//    Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

        // spring setter engine
        processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        InputStream imageStream = diagramGenerator.generateDiagram(
            bpmnModel,
            "png",
            activeActivityIds,
            new ArrayList<String>(),
            "宋体",
            "宋体",
            "宋体",
            null,
            1.0);
        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    /**
     * workflowDeploy
     * @param file
     * @return
     */
    @RequestMapping(value = "/deploy",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> deploy(@RequestParam("file") MultipartFile file) {
        Map map=new HashMap<>();
        String fileName = file.getOriginalFilename();

        try {
            InputStream fileInputStream = file.getInputStream();
            Deployment deployment = null;

            String extension = FilenameUtils.getExtension(fileName);
            if (extension.equals("zip") || extension.equals("bar")) {
                ZipInputStream zip = new ZipInputStream(fileInputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zip).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(fileName, fileInputStream).deploy();
            }
            map.put("success",true);
            map.put("id",deployment.getId());
            map.put("name",deployment.getName());
        } catch (Exception e) {
            map.put("success", false);
        }
        return ResponseEntity.ok(map);
    }

    @RequestMapping(value = "/convert-to-model/{processDefinitionId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> convertToModel(@PathVariable("processDefinitionId") String processDefinitionId)
        throws UnsupportedEncodingException, XMLStreamException {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
            .processDefinitionId(processDefinitionId).singleResult();
        InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
            processDefinition.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
        XMLStreamReader xtr = xif.createXMLStreamReader(in);
        BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

        BpmnJsonConverter converter = new BpmnJsonConverter();
        ObjectNode modelNode = converter.convertToJson(bpmnModel);
        Model modelData = repositoryService.newModel();
        modelData.setKey(processDefinition.getKey());
        modelData.setName(processDefinition.getResourceName());
        modelData.setCategory(processDefinition.getDeploymentId());

        ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
        modelData.setMetaInfo(modelObjectNode.toString());

        repositoryService.saveModel(modelData);

        repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));

        Map map=new HashMap<>();
        map.put("success", true);
        map.put("id", modelData.getId());

        return ResponseEntity.ok(map);
    }

    /**
     * 启动workflow
     *
     * @return
     */
    @RequestMapping(value = "/start",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> startProcess(String key) throws URISyntaxException {
        String userId=userService.getUserWithAuthorities().getId().toString();
        identityService.setAuthenticatedUserId(userId);
        Map pmap=new HashMap<>();
        pmap.put("startUser",userId);
        ProcessInstance processInstance= runtimeService.startProcessInstanceByKey(key,pmap);

        Map map=new HashMap<>();
        map.put("success",true);
        map.put("id",processInstance.getId());

        return ResponseEntity.ok(map);
    }

    /**
     * Delete workflowDeploy
     *
     * @return
     */
    @RequestMapping(value = "/deldeploy",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> deleteDeploy(String deploymentId) throws URISyntaxException {
        repositoryService.deleteDeployment(deploymentId, true);

        Map map=new HashMap<>();
        map.put("success",true);

        return ResponseEntity.ok(map);
    }
    /**
     * Delete workflow instance
     *
     * @return
     */
    @RequestMapping(value = "/delinstance",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> deleteInstance(String instanceId) throws URISyntaxException {
        runtimeService.deleteProcessInstance(instanceId,"Delete workflow instance["+instanceId+"]");

        Map map=new HashMap<>();
        map.put("success",true);

        return ResponseEntity.ok(map);
    }

}
