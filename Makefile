-include .env
export

ifndef GAME_PATH
$(error GAME_PATH is not set, run "cp .env.dist .env" and edit the file)
endif

CONTAINER_NAME = necesse-mod-dev
DEV_CONTAINER_COMPOSE = docker compose run -v $(GAME_PATH):/home/gradle/Necesse --remove-orphans --rm $(CONTAINER_NAME)

DECOMPILER_VERSION = 1.6.6

# first time setup and stuff
init:
	mkdir -p $(GAME_PATH)/mods
	@echo "Ensure your Steam launch options include '-dev -mod mods'"

shell:
	$(DEV_CONTAINER_COMPOSE) /bin/bash

install-decompiler:
	@test -f $(GAME_PATH)/jd-gui-$(DECOMPILER_VERSION).jar || echo "Downloading jd-gui v$(DECOMPILER_VERSION)"
	@(cd $(GAME_PATH); wget --quiet --no-clobber https://github.com/java-decompiler/jd-gui/releases/download/v$(DECOMPILER_VERSION)/jd-gui-$(DECOMPILER_VERSION).jar)

decompiler: install-decompiler
	@(cd $(GAME_PATH); ./jre/bin/java.exe -jar jd-gui-1.6.6.jar Necesse.jar &)

build: clean
	$(DEV_CONTAINER_COMPOSE) ./gradlew buildModJar
	@cp build/jar/* $(GAME_PATH)/mods
	@echo "Mod built and copied to $(GAME_PATH)/mods"

task:
	$(DEV_CONTAINER_COMPOSE) ./gradlew $(TASK)

clean:
	@rm -rf build

client:
	@(cd $(GAME_PATH); ./jre/bin/javaw.exe -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Xms512m -Xmx4G -jar Necesse.jar)
