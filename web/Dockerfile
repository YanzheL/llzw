FROM nginx:alpine

LABEL maintainer="Yanzhe Lee"

COPY nginx.conf /etc/nginx/nginx.conf

COPY dist /var/www/dist

RUN rm -rf /usr/share/nginx/html/*

EXPOSE 4200

CMD ["nginx", "-g", "daemon off;"]
