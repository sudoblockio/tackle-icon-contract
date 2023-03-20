# tackle-icon-contract

[![loopchain](https://img.shields.io/badge/ICON-API-blue?logoColor=white&logo=icon&labelColor=31B8BB)](https://shields.io) 
![](https://github.com/sudoblockio/tackle-icon-sc-poc/workflows/push-main/badge.svg?branch=main) 

Create a contract on the ICON  generate smart contract code scaffolding using [tackle](https://github.com/sudoblockio/tackle). 

> WARNING -> Still a WIP and basically a POC. Work will be ongoing to create good templates.

### Usage 

```shell
python -m venv env 
source env/bin/activate
pip install tackle 
tackle sudoblockio/tackle-icon-contract
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

```shell
pip install -r requirements-dev.txt
make test 
```

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
