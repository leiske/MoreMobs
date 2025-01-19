GAME_PATH = /mnt/e/SteamLibrary/steamapps/common/Necesse
CONTAINER_NAME = necesse-mod-dev

# first time setup and stuff
init:
	@mkdir -p $(GAME_PATH)/mods
	@echo "Update your Steam launch options to include '-dev -mod mods'"

shell:
	docker compose run --remove-orphans --rm $(CONTAINER_NAME) /bin/bash

build:
	docker compose run --remove-orphans --rm $(CONTAINER_NAME) ./gradlew buildModJar
	cp build/jar/* $(GAME_PATH)/mods

task:
	docker compose run --remove-orphans --rm $(CONTAINER_NAME) ./gradlew $(task)

clean:
	rm -rf build/jar/*.jar

client:
	@(cd $(GAME_PATH); ./jre/bin/javaw.exe -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Xms512m -Xmx4G -jar Necesse.jar)
