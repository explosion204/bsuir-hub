FROM explosion204/bsuirhub-server

WORKDIR /opt/jboss/wildfly/
ADD build/libs/bsuirhub.war standalone/deployments/
COPY --chown=jboss src/main/webapp/static static
COPY server/standalone.xml standalone/configuration/standalone.xml
