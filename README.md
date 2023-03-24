# tackle-icon-contract

[![loopchain](https://img.shields.io/badge/ICON-API-blue?logoColor=white&logo=icon&labelColor=31B8BB)](https://shields.io) 
![](https://github.com/sudoblockio/tackle-icon-sc-poc/workflows/push-main/badge.svg?branch=main) 

Create a contract on the ICON  generate smart contract code scaffolding using [tackle](https://github.com/sudoblockio/tackle). 

> WARNING -> Still a WIP and basically a POC. Work will be ongoing to create good templates.

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
? Is this a token / NFT? Yes
? What standard do you want to build on? 
❯ IRC2 Fungible Token
  IRC3 Non-Fungible Token
  IRC31 Non-Fungible Token
? What is the token name? Big Deal Token
? Is this token mintable? Yes
? Is this token burnable? Yes
? Is this token pausable? Yes
? Is this token permissioned? Yes
? Will there be voting enabled for this token? Yes
? Will there be flash minting enabled for this token? Yes
? Will there be snapshotting of this token? Yes
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
.
├── build.gradle
├── gradlew
├── gradlew.bat
├── LICENSE
├── README.md
├── requirements-dev.txt
├── settings.gradle
└── src
    ├── main
    │ └── java
    │     └── com
    │         └── iconloop
    │             └── score
    │                 └── example
    │                     └── BigDealToken.java
    └── test
        └── java
            └── com
                └── iconloop
                    └── score
                        └── example
                            └── BigDealTokenTest.java

13 directories, 9 files
```

### Running Tests 

- Generates each token standard
- Compiles the contract 
- TODO: Runs [drogon](https://github.com/icon-community/drogon) on each generated contract 

```shell
pip install -r requirements-dev.txt
make test 
```

### Local Development 

Running the above tests will generate the code to an `output` directory and then delete that directory after the tests. This is helpful for automation but makes it difficult to iterate on the templates as the generated code is temporary. To generate the code to a directory that is not deleted, run: 

```shell
make quick-gen
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
- [ ] Add more types of contracts available in [java-score-examples](https://github.com/icon-project/java-score-examples)
  - [ ] Multi-sig 
  - [ ] ?
- [ ] Add deployment helper scripts 
  - [ ] Prompt to use Makefile or tackle for helper scripts
    - Could add a lot of work -> pick one or the other 
  - [ ] Create new wallet, automatically hit faucet, deploy to selectable network 
- [ ] Add drogon test suite 


### License 

Apache 2.0
