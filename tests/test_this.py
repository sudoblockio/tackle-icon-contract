from tackle.main import tackle
import pytest

BASE_OVERRIDES = {
    "contract_name": "Foo Contract",
    "project_slug": "output",
    "license": "",
    "ci_enable": True,
}

TOKEN_STANDARDS = [
    "irc2",
    "irc3",
    "irc31",
]


@pytest.mark.parametrize("token_standard", TOKEN_STANDARDS)
def test_tokens(
        change_base_dir,
        assert_paths,
        change_dir,
        test_pytest_output,
        cleanup_output,
        token_standard,
):
    overrides = {
        "is_token": True,
        "token_standard": token_standard,
    }
    overrides.update(BASE_OVERRIDES)

    tackle(no_input=True, override=overrides)

    assert_paths(
        [
            "README.md",
            "requirements-dev.txt",
        ],
        "output",
    )
