# tackle-icon-contract

[![loopchain](https://img.shields.io/badge/ICON-API-blue?logoColor=white&logo=icon&labelColor=31B8BB)](https://shields.io)
![](https://github.com/sudoblockio/tackle-icon-sc-poc/workflows/push-main/badge.svg?branch=main)

Code generate a contract / token on the ICON Blockchain or generate your own smart contract code generator all using [tackle](https://github.com/sudoblockio/tackle).

### Usage

**Install tackle**

```shell
python -m venv env 
source env/bin/activate
pip install tackle 
```

**Remote**

```shell
tackle sudoblockio/tackle-icon-contract
```

**Local**

```shell
git clone https://github.com/sudoblockio/tackle-icon-contract
cd tackle-icon-contract
tackle 
```

**Dialogue**

```text
? What do you want to do? Create a contract / token
? Is this a token / NFT? Yes
? What standard do you want to build on? IRC2 Fungible Token
? What is your github org? sudoblockio
? What is the name of this project? Big Deal Contract
? What is your org name? sudoblockio
? Top level directory for contract (ie java) - typically com / network / io? com
? Mid level directory for contract (ie java/com) - typically your github org name? sudoblockio
? Bottom level directory for contract (ie java/com/sudoblockio) - typically your repo name? bigdealcontract
? Do you want a fourth level directory? No
? Is this token mintable? status=WIP Yes
? Is this token stable (ie pegged to an oracle)? status=WIP Yes
? license_type >>> 
❯ Apache 2.0
  MIT
  GPL Version 3
  BSD Version 3
  Closed source
? Who are the license holders? Me, myself, and Irene
? What year to end the license? (current year is fine) 2022
? Do you want to generate CI Yes
```

**Resulting File Structure**

```text
├── .github
├── .gitignore
├── build.gradle
├── CHANGELOG.md
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── LICENSE
├── Makefile
├── README.md
├── settings.gradle
└── src
    ├── main
    │    └── java
    │        └── com
    │            └── sudoblockio
    │                └── bigdealcontract
    │                    └── BigDealContract.java
    └── test
        └── java
            └── com
                └── sudoblockio
                    └── bigdealcontract
                        └── BigDealContractTest.java
```

## Contributing

This project is searching for smart contract developers to improve the functionality of this tool. Each type of token standard needs a baseline set of contract methods to reach feature parity tooling (see [similar tools](#similar-tools)). 

We ask developers in the ecosystem to contribute to this repo to add features per the table below. See the [adding a feature](#adding-a-feature) section to see more. 

[//]: # (DO NOT MODIFY - Generate with `tackle features create_table`)
[//]: # (--start--)

| Features |  IRC2 | IRC3 | IRC31 | Contract |
| --- | --- |  --- |  --- |  --- | 
| mintable | [WIP](https://) |  Todo |  Todo |  - | 
| stable | [WIP](https://) |  - |  - |  - | 
| auto_increment_ids | - |  Todo |  Todo |  - | 
| burnable | Todo |  Todo |  Todo |  - | 
| pausable | Todo |  Todo |  Todo |  Todo | 
| permit | Todo |  Todo |  Todo |  Todo | 
| votes | Todo |  Todo |  Todo |  Todo | 
| flash_minting | Todo |  - |  - |  - | 
| snapshots | Todo |  - |  - |  - | 
| enumerable | - |  Todo |  Todo |  - | 
| uri_storage | - |  Todo |  Todo |  - | 

[//]: # (--end--)

### Similar Tools 

Each of these tools aims to deliver a similar set of features for the Ethereum ecosystem as it relates to the table above. 

- [OpenZeppelin](https://www.openzeppelin.com/)
- [create-web3-dapp](https://github.com/alchemyplatform/create-web3-dapp)

### Architecture

This tool is built with [tackle](https://github.com/sudoblockio/tackle) where you can find the logic in the [tackle.yaml](./tackle.yaml) in this directory. The general flow in the tackle script is it generates some context of key / value pairs which it can then use when rendering underlying templates.

There are a two types of templates:

- Common ones that are shared regardless of the contract type (ie irc2 vs irc3) and include boilerplate such as readme files ([see templates/boilerplate](./templates/boilerplate))
- Templates that are specific to the contract and contain templating to enable features ([see templates/irc2](./templates/irc2/main))

These templates are rendered and then placed in the output directory that the tool prompts you for. Things like building custom directory trees per java best practices are done for you with prompts. 

### Running Tests

```shell
tackle test 
# OR 
pip install -r requirements-dev.txt
pytest tests 
```

- Generates each token standard
  - If generating contact / token 
    - [x] Compiles the contract
    - [ ] Runs [drogon](https://github.com/icon-community/drogon) on each generated contract
  - If generating a code generator 
    - [x] Generates the code
    - [x] Runs the generated tests 
      - [x] Compiles the generated code 
    - [ ] Runs [drogon](https://github.com/icon-community/drogon) on each generated contract

### Adding a Feature 

A feature is something that can be conditionally added to a contract such as whether a token can be `mintable` or `pausable` (see [similar tools](#similar-tools) to see comparable features). To add these features to the code generator per the table above, you need to follow a couple of steps. 

Create an issue in this repo that you would like to implement this feature
Make the changes to the underlying contract template with jinja templating to enable the feature. For a good example, see the [irc2 template](./templates/irc2/main/) where you can see jinja templating enabling features, ie (`mintable` and `stable`).

In general templating looks like:

```text
{% if mintable %}
// Your cde here 
{% endif %}
```

To then make the feature exposed to the frontend, modify the [`features.yaml`](./features.yaml) for the specific feature / template and update the `status` field from `Todo` to `WIP` or some other status. Additionally you can add a field `issue` for the url to the github issue and will create a hyperlink in the table above.

By default, the feature will be enabled in the tests. 

### Local Development

Running the above tests will generate the code to an `output` directory and then delete that directory after the tests. This is helpful for automation but makes it difficult to iterate on the templates as the generated code is temporary. To generate the code to a directory that is not deleted, run:

```shell
tackle quick_gen
cd outputs-all
```

There you will find each contract type generated. Best practice workflow is to:

- `cd` into each generated contract
- Look at the code and diagnose the error
- Either
  - Make the change to the template + Regenerate the code with `make quick-gen`
  - Make change to the actual generated code then do the above

### TTD

Very early projection of what would need to be done to make this project usable.

- [ ] Make it so the contracts can run tests + be deployed once they are generated
    - [ ] CI run generator with inputs and then run tests
- [ ] Update `settings.gradle` with actual contract name
- [ ] Add more types of contracts available
  in [java-score-examples](https://github.com/icon-project/java-score-examples)
    - [ ] Multi-sig
    - [ ] ?
- [ ] Add deployment helper scripts
    - [ ] Prompt to use Makefile or tackle for helper scripts
        - Could add a lot of work -> pick one or the other
    - [ ] Create new wallet, automatically hit faucet, deploy to selectable network
- [ ] Add drogon test suite

### License

Apache 2.0

TODO: Unauditted 