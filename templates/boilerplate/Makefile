.DEFAULT_GOAL := help

.PHONY: clean-tox
clean-tox: ## Remove tox testing artifacts
	@echo "+ $@"
	@rm -rf .tox/

.PHONY: clean
clean: clean-tox ## Remove all file artifacts

.PHONY: test
test:  ## Run tests quickly with the default Python
	@echo "+ $@"
	@tox -e py

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-16s\033[0m %s\n", $$1, $$2}'
