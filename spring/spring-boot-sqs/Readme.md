# Spring-boot-sqs
Receive messages from sqs by using SimpleMessageListenerContainerFactory enabled by spring-boot and spring-cloud-aws-messaging.
Added exponential back for retries.
Tests are using elasic-mq.

### Installing
Requires AWS account for running the application. Also required is an sqs queue. It must be configured in property sqs.in

## Running the tests
AWS account not required for running test. AWS is mocked by elasticmq. 

## Built With
* [Maven](https://maven.apache.org/) - Dependency Management

