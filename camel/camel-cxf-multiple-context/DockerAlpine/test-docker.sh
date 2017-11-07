#!/bin/bash

set -e

cp -p "../target/$(ls -t ../target/*.jar | grep -v /orig | head -1)" app.jar

docker build -t embriq-quant-flow/issuite-adapter-bimplus .
docker run -it --rm -p 8087:8087 -v $HOME/.aws:/home/appuser/.aws \
    -e AWS_LOG_GROUP="\/embriq\/quant\/flow\/issuite-adapter-bimplus\/devtest" \
    -e AWS_INOUT_LOG_GROUP="\/embriq\/quant\/flow\/inout-messages\/devtest" \
    -e AWS_CLOUDWATCH_LOGGING_ENABLED="true" \
    embriq-quant-flow/issuite-adapter-bimplus
