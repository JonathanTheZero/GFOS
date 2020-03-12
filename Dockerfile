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
COPY --from=build /usr/src/app/dist/aston-villa-app /usr/share/nginx/html
