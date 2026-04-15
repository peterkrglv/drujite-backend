SHELL := /bin/bash

GRADLEW := ./drujite-server/gradlew
GRADLE_PROJECT := drujite-server
ENV_FILE := .env

.PHONY: help mr dev run db-up db-logs lint ktlint-check ktlint-format detekt format check up

mr: lint
	set -a && source ./$(ENV_FILE) && set +a && \
	$(GRADLEW) -p $(GRADLE_PROJECT) build -x test && \
	$(GRADLEW) -p $(GRADLE_PROJECT) run

dev: db-up
	set -a && source ./$(ENV_FILE) && set +a && $(GRADLEW) -p $(GRADLE_PROJECT) run

run:
	set -a && source ./$(ENV_FILE) && set +a && $(GRADLEW) -p $(GRADLE_PROJECT) run

db-up:
	docker compose up -d --wait postgres

db-logs:
	docker compose logs -f postgres

up:
	docker compose up -d --build

lint: ktlint-check detekt

ktlint-check:
	$(GRADLEW) -p $(GRADLE_PROJECT) ktlintCheck

ktlint-format:
	$(GRADLEW) -p $(GRADLE_PROJECT) ktlintFormat

detekt:
	$(GRADLEW) -p $(GRADLE_PROJECT) detekt

format: ktlint-format

check:
	$(GRADLEW) -p $(GRADLE_PROJECT) check
