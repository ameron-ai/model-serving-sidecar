IMAGE_NAME = ameron/sidecar:1.0.0

default:
	cat Makefile

test:
	mvn clean test

dist:
	mvn clean package

image: dist
	docker build -f docker/Dockerfile -t ${IMAGE_NAME} .

run: image
	docker run --name sidecar --env-file docker/local.env ${IMAGE_NAME}

stop:
	docker stop sidecar && docker rm sidecar

run-supporting:
	docker-compose -f docker/supporting-docker-compose.yml up

stop-supporting:
	docker-compose -f docker/supporting-docker-compose.yml down

run-all: image
	docker-compose -f docker/local-docker-compose.yml up

stop-all:
	docker-compose -f docker/local-docker-compose.yml down
