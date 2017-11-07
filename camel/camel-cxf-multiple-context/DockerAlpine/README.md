## DockerAlpine environment variables

####Credentials
Task-roles should be used, but you may also pass in the Access key id and access key through env-variables.
For application/logging to CW
* AWS_ACCESS_KEY_ID
* AWS_SECRET_ACCESS_KEY

####Logging
Where to put logs. See aws-cloudwatch.conf and config_override/logback.xml
* AWS_CLOUDWATCH_LOGGING_ENABLED default to false. Set to "true" if you wish to enable
* AWS_LOG_GROUP (Log group to create log stream for app-log)
* AWS_INOUT_LOG_GROUP (Log group to create inout-log stream)
* LOGBACK_EMBRIQ_LEVEL (Loglevel og no.embriq logs. Defaults to info if not set)

####Configservice
If nothing is set, the application tries with whatever is put in src/main/resources/configservice.properties.
If it fails default properties are used. If configservice.allow.fallback.to.local.config is set to false the application fails during startup if any CS problems occurs while fetching configuration.
* configservice.url
* configservice.username
* configservice.password
* configservice.artifactid
* configservice.clientid
* configservice.configuration.store.directory
* configservice.allow.fallback.to.local.config

####Misc
* JAVA_PARAMS_OVERRIDE (e.g. "-Xmx512m)
* JOLOKIA_PASSWORD (sets password of jolokia)
