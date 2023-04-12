import os
import pytest
from tackle import tackle

# For now use this then later make custom version
from tests.test_create_contract import CONTRACT_FEATURES, BASE_OVERRIDES


# This is only supposed to be run from tackle -> `tackle quick_gen`
#  ie pytest . -k test_manual --manual
# It outputs to `outputs-all/{contract type}`
# Used for quickly generating the outputs of all the token standards so that we can
# debug them.

@pytest.mark.parametrize("token_standard,options", CONTRACT_FEATURES)
@pytest.mark.manual(reason="For manual generation. Leaves outputs in ../all-outputs")
def test_manual(
        change_base_dir,
        token_standard,
        options
):
    overrides = {
        "contract_standard": token_standard,
        "compile_contract": True,
        **options,
    }
    overrides.update(BASE_OVERRIDES)
    overrides['project_slug'] = os.path.join("outputs-all", token_standard)

    tackle("create_contract", override=overrides)
