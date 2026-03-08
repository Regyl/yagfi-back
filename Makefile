.PHONY: help dev test test-unit test-integration checkstyle db-up db-down db-reset clean verify

-include .env
export

help: ## Show available targets
	@awk 'BEGIN {FS = ":.*##"; printf "Usage:\n  make \033[36m<target>\033[0m\n\nTargets:\n"} \
	     /^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2 }' \
	     $(MAKEFILE_LIST)

dev: ## Start DB and run app locally (local profile)
	cd gfi-devops && docker-compose up -d
	./mvnw spring-boot:run -Dspring-boot.run.profiles=local

test: ## Run all tests
	./mvnw test

test-unit: ## Run unit tests only
	./mvnw test -Dgroups=Unit

test-integration: ## Run integration tests only
	./mvnw test -Dgroups=Integration

checkstyle: ## Run checkstyle checks
	./mvnw checkstyle:check

db-up: ## Start all services (PostgreSQL + cdxgen)
	cd gfi-devops && docker-compose up -d

db-down: ## Stop and remove all services
	cd gfi-devops && docker-compose down

db-reset: ## Destroy volumes and restart all services
	cd gfi-devops && docker-compose down -v && docker-compose up -d

clean: ## Clean build artifacts
	./mvnw clean

verify: ## Run full build with checks
	./mvnw verify
