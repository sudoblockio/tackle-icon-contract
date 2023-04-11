from tackle.main import tackle
import pytest

BASE_OVERRIDES = {
    "contract_name": "Foo Contract",
    "project_slug": "output",
    "license": "",
    "ci_enable": True,
    "warning": True,
    "compile_enable": True,
}

CONTRACT_STANDARDS = [
    ("contract"),
    ("irc2"),
    ("irc3"),
    ("irc31"),
]


@pytest.mark.parametrize("contract_standard", CONTRACT_STANDARDS)
def test_defaults(
        change_base_dir,
        assert_paths,
        change_dir,
        test_pytest_output,
        cleanup_output,
        contract_standard,
):
    """
    Setting no_input (ie choose the default value, true for the `confirm` hook), this
    test runs through all the contract standards to generate the code.
    """
    overrides = {
        "contract_standard": contract_standard,
    }
    overrides.update(BASE_OVERRIDES)

    tackle(
        no_input=True,
        override=overrides,
    )

    assert_paths(
        [
            "README.md",
            "requirements-dev.txt",
        ],
        "output",
    )


CONTRACT_FEATURES = [
    ("irc2", {
        "is_token": True,
        "features": {
            "mintable": True,
            "burnable": True,
            "pausable": True,
            "permit": True,
            "votes": True,
            "flash_minting": True,
            "snapshots": True,
        }
    }),
    ("irc3", {
        "is_token": True,
        "features": {
            "mintable": True,
            "auto_increment_ids": True,
            "burnable": True,
            "pausable": True,
            "votes": True,
            "enumerable": True,
            "uri_storage": True,
        }
    }),
    ("irc31", {
        "is_token": True,
        "features": {
            "mintable": True,
            "auto_increment_ids": True,
            "burnable": True,
            "pausable": True,
            "votes": True,
            "enumerable": True,
            "uri_storage": True,
        }
    }),
    ("contract", {
        "is_token": True,
        "features": {
            "pausable": True,
        }
    }),
]


@pytest.mark.parametrize("token_standard,options", CONTRACT_FEATURES)
def test_features(
        change_base_dir,
        assert_paths,
        test_pytest_output,
        cleanup_output,
        token_standard,
        options,
):
    overrides = {
        "contract_standard": token_standard,
        **options,
    }
    overrides.update(BASE_OVERRIDES)

    tackle(override=overrides)

    assert_paths(
        [
            "README.md",
            "requirements-dev.txt",
        ],
        "output",
    )


@pytest.fixture()
def mock_remote(base_dir):
    """
    - Mock get_repo_source -> return path to
    - Copy the provider to the provider dir
    - Input 'test/test'
    - yield
    - Cleanup 'test/test' in providers dir
    """
    from tackle.settings import settings
    import shutil
    import os

    test_org_path = os.path.join(settings.provider_dir, "test")
    test_provider_path = os.path.join(test_org_path, "test")

    shutil.rmtree(path=test_org_path, ignore_errors=True)
    os.makedirs(test_provider_path)
    shutil.copytree(src=base_dir, dst=test_provider_path, dirs_exist_ok=True)

    yield test_provider_path
    shutil.rmtree(path=test_org_path)


@pytest.mark.parametrize("contract_standard", CONTRACT_STANDARDS)
def test_mocked_remote(
        mock_remote,
        mocker,
        contract_standard,
        cleanup_output,
):
    """
    Validate that when provider is callled remotely (ie from a git repo), that it acts
    the same as when it is called locally.
    """
    mocker.patch(
        # Patch the git fetch / clone function
        'tackle.parser.get_repo_source',
        return_value=mock_remote,
    )

    overrides = {
        "contract_standard": contract_standard,
    }
    overrides.update(BASE_OVERRIDES)

    o = tackle(
        "test/test",
        no_input=True,
        override=overrides,
    )

    assert o
