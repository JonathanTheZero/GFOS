<<<<<<< HEAD
### STAGE 1: Build ###
### $ docker build -t av-app-multistage-image . ###
FROM node:12.7-alpine AS build
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

### STAGE 2: Run ###
### $ docker run --name av-app-multistage-container -d -p 8888:80 av-app-multistage-image ###
FROM nginx:1.17.1-alpine
=======
### STAGE 1: Build ###
FROM node:12.7-alpine AS build
WORKDIR /usr/src/app
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

### STAGE 2: Run ###
FROM nginx:1.17.1-alpine
>>>>>>> 8f1191d84acddbe526bc897327ae7fd8ae2af8b8
COPY --from=build /usr/src/app/dist/aston-villa-app /usr/share/nginx/html