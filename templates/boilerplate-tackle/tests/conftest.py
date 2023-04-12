import os
import shutil
import pytest
from tackle.settings import settings


@pytest.fixture()
def base_dir():
    """Change dir to the base of the repo."""
    return os.path.join(os.path.abspath(os.path.dirname(__file__)), "..")


@pytest.fixture()
def change_dir():
    """Change to a dir argument."""

    def f(dir: str):
        os.chdir(dir)

    return f


@pytest.fixture()
def change_base_dir(base_dir):
    """Change dir to the base of the repo."""
    os.chdir(base_dir)


@pytest.fixture()
def fixture_dir():
    """Path to fixtures dir from test file."""
    dir = os.path.join(os.path.abspath(os.path.dirname(__file__)), "fixtures")
    return dir


@pytest.fixture()
def assert_paths():
    """Return a function that asserts paths exists."""

    def f(paths: list, base_dir: str = None):
        for file in paths:
            if base_dir is not None:
                file = os.path.join(base_dir, file)

            assert os.path.exists(file)

    return f


@pytest.fixture()
def cleanup_path():
    """Return a function that asserts paths exists."""

    def f(path: str):
        if os.path.exists(path):
            shutil.rmtree(path)

    return f


@pytest.fixture()
def cleanup_output(cleanup_path):
    """Cleanup output dir after test."""
    cleanup_path("output")
    yield
    cleanup_path("output")


@pytest.fixture()
def mock_remote(base_dir):
    """Copy the provider to the provider dir simulating a remote call then cleanup."""
    test_org_path = os.path.join(settings.provider_dir, "test")
    test_provider_path = os.path.join(test_org_path, "test")

    shutil.rmtree(path=test_org_path, ignore_errors=True)
    os.makedirs(test_provider_path)
    shutil.copytree(src=base_dir, dst=test_provider_path, dirs_exist_ok=True)

    yield test_provider_path
    shutil.rmtree(path=test_org_path)
