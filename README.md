# tackle-icon-sc-poc

A POC around how to generate smart contract code scaffolding using tackle. 

```shell
python -m venv env 
source env 
pip install tackle 
tackle sudoblockio/tackle-icon-sc-poc
```

```text
? Is this a token / NFT? Yes
? token_standard >>> irc2
? What is the token name? Big Deal Token
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
    │                     └── BigDealTokenToken.java
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

Very early projection of what to do 

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
