from tackle.main import tackle
import pytest

BASE_OVERRIDES = {
    "project_name": "output",
    "github_org": "sudoblockio",
    "license": "",
    "ci_enable": True,
    "warning": True,
    "compile_enable": True,
    "contract_path": [
        "com",
        "sudoblockio",
        "output",
    ]
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
        "create_contract",
        no_input=True,
        override=overrides,
    )

    assert_paths(
        [
            "README.md",
            "gradlew",
        ],
        "output",
    )


CONTRACT_FEATURES = [
    ("irc2", {
        "is_token": True,
        "features": {
            "mintable": True,
            "stable": True,
            # "burnable": True,
            # "pausable": True,
            # "permit": True,
            # "votes": True,
            # "flash_minting": True,
            # "snapshots": True,
        }
    }),
    ("irc3", {
        "is_token": True,
        "features": {
            # "mintable": True,
            # "auto_increment_ids": True,
            # "burnable": True,
            # "pausable": True,
            # "votes": True,
            # "enumerable": True,
            # "uri_storage": True,
        }
    }),
    ("irc31", {
        "is_token": True,
        "features": {
            # "mintable": True,
            # "auto_increment_ids": True,
            # "burnable": True,
            # "pausable": True,
            # "votes": True,
            # "enumerable": True,
            # "uri_storage": True,
        }
    }),
    ("contract", {
        "is_token": True,
        "features": {
            # "pausable": True,
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

    tackle(
        "create_contract",
        override=overrides,
    )

    assert_paths(
        [
            "README.md",
            "gradlew",
        ],
        "output",
    )


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
        "create_contract",
        no_input=True,
        override=overrides,
    )

    assert o
