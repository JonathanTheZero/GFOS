# GFOS

## Using Docker
Create a Docker image:
```
$ docker build -t av-app-multistage-image .
```
Run it:
```
$ docker run --name av-app-multistage-container -d -p 8888:80 av-app-multistage-image
```