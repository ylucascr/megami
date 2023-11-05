FROM node:18.17.1-alpine as build-step

ARG API_URL

WORKDIR /usr/app

COPY package.json .

RUN npm install

COPY . .

RUN mkdir -p environments && \
    cd environments && \
    echo "export const environment = { \"apiUrl\": \"$API_URL\" };" > environments.ts && \
    cd ..

RUN npm run build --prod

FROM nginx:1.25.2-alpine

COPY ./nginx.conf /etc/nginx/conf.d/default.conf

COPY --from=build-step /usr/app/dist/megami-frontend /usr/share/nginx/html