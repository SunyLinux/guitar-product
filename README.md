
Base Frame:
1. Fork from the [jhipster-sample-app],for original proj:https://github.com/jhipster/jhipster-sample-app /[org.address; www.jhipster.tech]</br>
   a. Core Feature: a tiny erp prototype with workflow. light protocol with http. REST Api. idea of DDD design.</br>
   b. Init Des: Spring Boot + Angular.js[+H5] + JWT (Web-App) with font&back-end, Component of MicroServices.</br>
   c. Basic spring: IoC,AOP,Annotation; reflection, proxy of cglib. Aspect transaction & logging...</br>
   d. Ex-Functions: Metrics,Pic Upload, swagger api doc, ect</br>
   e. ...
2. MicroServices Deploy: [http://www.jhipster.tech/images/microservices_architecture_2.png]
3. Undo List: Netty,Linux...


Design Plan:
1. No Database,...
2. Import RocketMQ/Kafka
3. Support TCP/IP,Dubbo Protocol


Notice
1. When init the proj.，pls edit [activiti.cfg.xml] *<property name="databaseSchemaUpdate" value="true" />* set value=true,
   IF not, pls set *value=none*. To void ["table activiti already exists"] error when start. 
2. Atfer started ,pls visit：localhost:9701 user_name：admin pass_word：admin .
3. Adding new feature of &EclipseLink: "Multi-Tenancy-Attch".(Testing)...
