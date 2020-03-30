### only using to set up the frontend, glassfish with docker sucks

### Step 1: $ docker build -t app . ###
FROM node:12.0-alpine AS builder

ENV name Frontend

COPY ./${name} ./${name}

WORKDIR /${name}
RUN pwd


RUN npm i && \
    npm i @angular/cli@9.0.2 -g 
    #&& \
    #node --max_old_space_size=8192 'node_modules/@angular/cli/bin/ng' build --prod --build-optimizer

### Step 2: docker exec -it app sh ###