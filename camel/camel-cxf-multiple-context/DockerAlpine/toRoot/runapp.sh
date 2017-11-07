#!/bin/bash

APP=app.jar

date +" --- RUNNING $(basename $0) %Y-%m-%d_%H:%M:%S --- "
set -x

if [ "$AWS_PARAMETER_STORE_ENABLED" = "true" ]; then
    python GetPropertiesFromParameterStore.py $AWS_PARAMETER_STORE_PATH $AWS_PARAMETER_STORE_OUTPUTPATH >> logs/issuite-adapter-bimplus.log
    if [ $? -eq 0 ]; then
        echo 'GetPropertiesFromParameterStore.py exited successfully!'
    else
        echo 'GetPropertiesFromParameterStore.py exited with error (non null exit code)'
        exit 1
    fi
    AWS_DEFAULT_REGION=$AWS_REGION JOLOKIA_PASSWORD=$(aws ssm get-parameters --names common/JOLOKIA_PASSWORD --with-decryption | jq -r '.Parameters[].Value')
fi

JAVA_PARAMS="-Dlogback.configurationFile=./config_override/logback.xml -javaagent:jolokia-jvm-agent.jar=host=0.0.0.0,user=admin,password=$JOLOKIA_PASSWORD -Xms128m -Xmx512m"

if [ "$AWS_CLOUDWATCH_LOGGING_ENABLED" = "true" ]; then
    # Enables aws cloudwatch plugin in aws cli
    AWS_CONFIG_FILE="/home/appuser/.cwlogs/config" aws configure set plugins.cwlogs cwlogs
    # Sets container id used by as part of log-stream-name
    CONTAINER_ID="$(cat /proc/self/cgroup | grep docker | grep -o -E '[0-9a-f]{64}' | head -n 1 | cut -c1-12)"
    #replaces log-group and log-stream name in aws-cloudwatch.conf
    sed -i -e "s/{CONTAINER_ID}/$CONTAINER_ID/g" aws-cloudwatch.conf
    sed -i -e "s/{AWS_LOG_GROUP}/$AWS_LOG_GROUP/g" aws-cloudwatch.conf
    sed -i -e "s/{AWS_INOUT_LOG_GROUP}/$AWS_INOUT_LOG_GROUP/g" aws-cloudwatch.conf
    if [ -z "$LOGBACK_EMBRIQ_LEVEL" ]; then
        sed -i -e "s/{LOGBACK_EMBRIQ_LEVEL}/info/g" config_override/logback.xml
    else
        sed -i -e "s/{LOGBACK_EMBRIQ_LEVEL}/$LOGBACK_EMBRIQ_LEVEL/g" config_override/logback.xml
    fi

    java_pid=()
    awslogs_pid=()
    gotsigchld=false
    #Using trap on CHLD and EXIT to run clean exit of java-process.
    trap '
    if ! "$gotsigchld"; then
        gotsigchld=true
        kill "${java_pid[@]}"
        #When java process dies, give awslogs time to send last batch.
        wait "${java_pid[@]}"
        sleep 10
        kill "${awslogs_pid[@]}"
    fi
    ' CHLD EXIT

    /usr/bin/java $JAVA_PARAMS $JAVA_PARAMS_OVERRIDE -jar $APP & java_pid+=$!
    AWS_CONFIG_FILE="/home/appuser/.cwlogs/config" aws logs push --region=$AWS_REGION --config-file aws-cloudwatch.conf & awslogs_pid+=$!
    set -m
    wait
    set +m
else
    #If not running with cloudwatch enabled, just execute jar.
    /usr/bin/java $JAVA_PARAMS $JAVA_PARAMS_OVERRIDE -jar $APP
fi





