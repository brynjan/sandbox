# Elhub Polling Service


## Install and use 

### Install or upgrade Docker 

https://docs.docker.com/installation/ubuntulinux/

```
wget -qO- https://get.docker.com/ | sh
```

###  Install application 
```
```

### Test that service is running 
```
curl http://localhost:8080 - TODO
```

## Backup 

See https://docs.docker.com/userguide/dockervolumes/#backup-restore-or-migrate-data-volumes


## Development 

### Build and run for development

```
```

* To stop and remove ALL containers: 
```
docker stop $(sudo docker ps -a -q) && sudo docker rm $(sudo docker ps -a -q)
```

* To log in to take a look: 
```
docker ps -a
docker exec -it elhub-polling-service bash
```
