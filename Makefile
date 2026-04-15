.DEFAULT_GOAL := help

SHELL := /bin/bash

GRADLEW := ./drujite-server/gradlew
GRADLE_PROJECT := drujite-server
ENV_FILE := .env

.PHONY: help mr dev run db-up db-logs lint ktlint-check ktlint-format detekt format check

help:
	@echo "Workflow:"
	@echo "  make mr     - lint, db-up, build (no tests), run server"
	@echo "  make dev    - db-up, then run server (fast local start)"
	@echo ""
	@echo "App:"
	@echo "  make run    - Ktor (loads $(ENV_FILE))"
	@echo ""
	@echo "Database:"
	@echo "  make db-up  - Postgres via Docker Compose (--wait)"
	@echo "  make db-logs - tail Postgres logs"
	@echo ""
	@echo "Quality:"
	@echo "  make lint          - ktlintCheck + detekt"
	@echo "  make ktlint-check  - ktlintCheck"
	@echo "  make ktlint-format - ktlintFormat"
	@echo "  make detekt        - detekt"
	@echo "  make format        - same as ktlint-format"
	@echo "  make check         - Gradle check (tests + ktlint + detekt)"
	@echo "  make help          - this text"

mr: lint
	set -a && source ./$(ENV_FILE) && set +a && \
	$(GRADLEW) -p $(GRADLE_PROJECT) build -x test && \
	$(GRADLEW) -p $(GRADLE_PROJECT) run

dev: db-up
	set -a && source ./$(ENV_FILE) && set +a && $(GRADLEW) -p $(GRADLE_PROJECT) run

run:
	set -a && source ./$(ENV_FILE) && set +a && $(GRADLEW) -p $(GRADLE_PROJECT) run

db-up:
	docker compose up -d --wait

db-logs:
	docker compose logs -f postgres

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
