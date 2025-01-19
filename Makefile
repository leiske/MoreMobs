shell:
	docker compose run --remove-orphans --rm necesse-mod-dev /bin/bash

build:
	docker compose run --remove-orphans --rm necesse-mod-dev ./gradlew buildModJar

clean:
	rm -rf build
