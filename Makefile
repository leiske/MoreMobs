-include .env
export

ifndef GAME_PATH
$(error GAME_PATH is not set, run "cp .env.dist .env" and edit the file)
endif

CONTAINER_NAME = necesse-mod-dev
DOCKER_COMPOSE_COMMAND = docker compose run -v $(GAME_PATH):/home/gradle/Necesse --remove-orphans --rm $(CONTAINER_NAME)

# first time setup and stuff
init:
	mkdir -p $(GAME_PATH)/mods
	@echo "Update your Steam launch options to include '-dev -mod mods'"

shell:
	$(DOCKER_COMPOSE_COMMAND) /bin/bash

build: clean
	$(DOCKER_COMPOSE_COMMAND) ./gradlew buildModJar
	cp build/jar/* $(GAME_PATH)/mods

task:
	$(DOCKER_COMPOSE_COMMAND) ./gradlew $(TASK)

clean:
	rm -rf build

client:
	@(cd $(GAME_PATH); ./jre/bin/javaw.exe -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Xms512m -Xmx4G -jar Necesse.jar)
