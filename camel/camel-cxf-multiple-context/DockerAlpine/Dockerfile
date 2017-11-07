FROM 944493252375.dkr.ecr.eu-west-1.amazonaws.com/embriq/alpine-java:openjdk-8-jre-113
MAINTAINER Henrik Steen <henrist@henrist.net>

RUN apk --no-cache add \
    groff \
    python2 \
    bash \
  && apk --no-cache add --virtual build-dep \
      python2-dev \
      py2-pip \
  && pip install awscli-cwlogs \
  && pip install --upgrade awscli \
  && pip install boto3 \
  && apk del build-dep \
  && rm -rf ~/.cache/pip

ENV USER=appuser
ENV HOME=/home/$USER \
    AWS_REGION="eu-west-1"
RUN adduser -S $USER

## Download Jolokia jvm agent (used for debugging purposes)
ADD http://search.maven.org/remotecontent?filepath=org/jolokia/jolokia-jvm/1.3.5/jolokia-jvm-1.3.5-agent.jar $HOME/jolokia-jvm-agent.jar

## Copy configuration overrides and run script
## Copy application itself
## PREREQUISITE:
##    Docker cannot access contents outside, so this must be run when building:
##    $ cp -p "../target/$(ls -t ../target/*.jar | grep -v /orig | head -1)" app.jar
COPY config_override/* $HOME/config_override/
COPY toRoot/* app.jar $HOME/

## Create directory for holding application logs and configure permissions
## Configure permissions
RUN mkdir -p $HOME/logs && \
    chmod 755 $HOME/*.sh && \
    chmod 755 $HOME/*.py && \
    chown -R $USER $HOME

## Expose application port
EXPOSE 8089 8778

USER $USER
WORKDIR /home/$USER
CMD ["./runapp.sh"]
