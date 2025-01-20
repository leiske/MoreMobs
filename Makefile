-include .env
export

ifndef GAME_PATH
$(error GAME_PATH is not set, run "cp .env.dist .env" and edit the file)
endif

CONTAINER_NAME = necesse-mod-dev
DEV_CONTAINER_COMPOSE = docker compose run --rm --remove-orphans -v $(GAME_PATH):/home/gradle/Necesse $(CONTAINER_NAME)

DECOMPILER_VERSION = 1.6.6

# We start the java debugger for Necesse on the Windows host, so we need to reach out to the host IP which corresponds to your machines hostname + '.local'
HOST_IP=$(shell dig +short $(shell hostname) | head -n 1)

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
	@(cd $(GAME_PATH); ./jre/bin/java.exe -jar jd-gui-$(DECOMPILER_VERSION).jar Necesse.jar &)

build: clean
	$(DEV_CONTAINER_COMPOSE) ./gradlew buildModJar
	@cp build/jar/* $(GAME_PATH)/mods
	@echo "Mod built and copied to $(GAME_PATH)/mods"

task:
	$(DEV_CONTAINER_COMPOSE) ./gradlew $(TASK)

clean:
	@rm -rf build

kill:
	@docker compose down

disable-debug-client:
	@cp $(GAME_PATH)/Necesse.json $(GAME_PATH)/Necesse.json.bak
	@cp RegularClientArgs.json $(GAME_PATH)/Necesse.json

client: disable-debug-client
	@(cd $(GAME_PATH); ./jre/bin/javaw.exe -jar Necesse.jar)

enable-debug-client:
	@cp $(GAME_PATH)/Necesse.json $(GAME_PATH)/Necesse.json.bak
	@cp DebugClientArgs.json $(GAME_PATH)/Necesse.json

kill-client:
	@if tasklist.exe | grep Necesse.exe > /dev/null; then taskkill.exe /IM Necesse.exe /F; fi

debug-client: enable-debug-client
	@if tasklist.exe | grep Necesse.exe > /dev/null; then taskkill.exe /IM Necesse.exe /F; fi
	@(cd $(GAME_PATH); ./jre/bin/javaw.exe -jar Necesse.jar)

attach-debug:
	@docker compose run --rm $(CONTAINER_NAME) jdb -attach $(HOST_IP):5005 -sourcepath /home/gradle/project/build/mod/moremobs

hot-reload:
	@docker compose exec $(CONTAINER_NAME) ./gradlew buildModJar
	@./hotreload.sh $(HOST_IP)

logs:
	@test -f $(LOG_PATH)/latest-log.txt || echo "No logs found or LOG_PATH not set in .env"
	tail -F $(LOG_PATH)/latest-log.txt
