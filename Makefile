.DEFAULT_GOAL := help

clean-tox: ## Remove tox testing artifacts
	@echo "+ $@"
	@rm -rf .tox/

clean: clean-tox ## Remove all file artifacts

test:  ## Run tests quickly with the default Python
	@echo "+ $@"
	@tox -e py

quick-gen:  ## Quickly generate all the contracts to `outputs-all` directory
	@pytest . -k test_manual --manual

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-16s\033[0m %s\n", $$1, $$2}'
