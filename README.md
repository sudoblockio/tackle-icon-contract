# tackle-icon-sc-poc

[![loopchain](https://img.shields.io/badge/ICON-API-blue?logoColor=white&logo=icon&labelColor=31B8BB)](https://shields.io) 
![](https://github.com/sudoblockio/tackle-icon-sc-poc/workflows/push-main/badge.svg?branch=main) 

A POC around how to generate smart contract code scaffolding using [tackle](https://github.com/sudoblockio/tackle). 

```shell
python -m venv env 
source env/bin/activate
pip install tackle 
tackle sudoblockio/tackle-icon-sc-poc
```

```text
? Is this a token / NFT? Yes
? token_standard >>> 
❯ irc2
  irc3
  irc31
? What is the token name? Big Deal Token
? features.mintable >>> Yes
? features.burnable >>> Yes
? features.pausable >>> Yes
? features.permit >>> Yes
? features.votes >>> Yes
? features.flash_minting >>> Yes
? features.snapshots >>> Yes
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
