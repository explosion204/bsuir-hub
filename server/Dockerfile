FROM jboss/base-jdk:11

ENV WILDFLY_VERSION 24.0.1.Final
ENV JBOSS_HOME /opt/jboss/wildfly
USER root

RUN cd $HOME \
    && curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-preview-$WILDFLY_VERSION.tar.gz \
    && tar xf wildfly-preview-$WILDFLY_VERSION.tar.gz \
    && mv $HOME/wildfly-preview-$WILDFLY_VERSION $JBOSS_HOME \
    && rm wildfly-preview-$WILDFLY_VERSION.tar.gz \
    && chown -R jboss:0 ${JBOSS_HOME} \
    && chmod -R g+rw ${JBOSS_HOME}

ENV LAUNCH_JBOSS_IN_BACKGROUND true
USER jboss

ENTRYPOINT ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]