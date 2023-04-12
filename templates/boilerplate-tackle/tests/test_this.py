from tackle.main import tackle
import pytest

BASE_OVERRIDES = {
    "project_name": "output",
    "license": "",
    "ci_enable": True,
    "compile_enable": True,
}


def test_defaults(
        change_base_dir,
        assert_paths,
        change_dir,
        cleanup_output,
):
    """
    Setting no_input (ie choose the default value, true for the `confirm` hook), this
    test runs through all the contract standards to generate the code.
    """
    overrides = {}
    overrides.update(BASE_OVERRIDES)

    tackle(
        no_input=True,
        override=overrides,
    )

    assert_paths(
        [
            "README.md",
        ],
        "output",
    )


def test_mocked_remote(
        mock_remote,
        mocker,
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

    overrides = {}
    overrides.update(BASE_OVERRIDES)

    o = tackle(
        "test/test",
        no_input=True,
        override=overrides,
    )

    assert o
