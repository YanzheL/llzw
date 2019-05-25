FROM node:alpine AS builder

WORKDIR /tmp/build

COPY package.json package-lock.json ./

# Storing node modules on a separate layer will prevent unnecessary npm installs at each build
RUN npm ci \
    && mkdir /ng-app \
    && mv ./node_modules /ng-app

WORKDIR /ng-app

COPY . .

## Build the angular app in production mode and store the artifacts in dist folder
RUN npm run ng build -- --prod --output-path=dist


FROM nginx:alpine

LABEL maintainer="Yanzhe Lee"

RUN rm -rf /usr/share/nginx/html/*

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=builder /ng-app/dist /var/www/dist

EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
