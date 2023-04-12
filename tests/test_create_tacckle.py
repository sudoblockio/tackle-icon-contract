from tackle.main import tackle

BASE_TACKLE_OVERRIDES = {
    "project_name": "output",
    "license": "",
    "compile_enable": True,
}


def test_create_tackle(
        change_base_dir,
        cleanup_output,
        assert_paths,
        test_pytest_output,
):
    tackle(
        "create_tackle",
        no_input=True,
        override=BASE_TACKLE_OVERRIDES,
    )

    assert_paths(
        [
            "README.md",
            "requirements-dev.txt",
        ],
        "output",
    )
    test_pytest_output('output')
