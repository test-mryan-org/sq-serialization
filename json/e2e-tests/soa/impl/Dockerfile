FROM docker.bank.swissquote.ch/sq-java8-tomcat8:v19

ADD docker/sq-serialization-json-e2e-tests-soa-soa.xml /softwares/tomcat/conf/Catalina/localhost/sq-serialization-json-e2e-tests-soa-soa.xml

ADD docker/readiness_check /bin/readiness_check
RUN chmod +x /bin/readiness_check

ENV CATALINA_OPTS "$CATALINA_OPTS -Dlog4j.configurationFile=classpath:log4j2-console.xml -Xmx64m -XX:MaxMetaspaceSize=64m"

ARG WEBAPP_DIR

ADD target/${WEBAPP_DIR} /softwares/tomcat/webapps/sq-serialization-json-e2e-tests-soa-soa
