# Elhub Polling Service

See https://confluence.embriq.no/display/NET/ElhubPollingService

## Install and use 

### Install or upgrade Docker 

https://docs.docker.com/installation/ubuntulinux/

```
wget -qO- https://get.docker.com/ | sh
```

###  Install application 
```
sudo docker pull embriq/elhub-polling-service
sudo docker run -d -p 8080:8080 --name elhub-polling-service embriq/elhub-polling-service
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
docker build -t embriq/elhub-polling-service Docker
docker run -d -p 8080:8080 --name elhub-polling-service embriq/elhub-polling-service
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
