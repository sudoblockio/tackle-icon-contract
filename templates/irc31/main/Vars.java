package io.contractdeployer.generics.irc31;

import score.*;

import java.math.BigInteger;

import static io.contractdeployer.generics.irc31.Constant.*;
import static score.Context.newVarDB;

public class Vars {

    static final Address ZERO_ADDRESS = new Address(new byte[Address.LENGTH]);
    static final VarDB<String> name = Context.newVarDB(NAME, String.class);
    static final VarDB<String> symbol = Context.newVarDB(SYMBOL, String.class);
    static final VarDB<BigInteger> cap = Context.newVarDB(CAP, BigInteger.class);
    static final VarDB<BigInteger> maxBatchMintCount = Context.newVarDB(MAX_BATCH_MINT, BigInteger.class);
    static final VarDB<BigInteger> totalSupply = Context.newVarDB(TOTAL_SUPPLY, BigInteger.class);
    static final BranchDB<BigInteger, DictDB<Address, BigInteger>> balances = Context.newBranchDB(BALANCES, BigInteger.class);
    static final BranchDB<Address, DictDB<Address, Boolean>> operatorApproval = Context.newBranchDB(APPROVAL, Boolean.class);
    static final DictDB<BigInteger, String> tokenURIs = Context.newDictDB(TOKEN_URI, String.class);
    static final VarDB<Address> admin = newVarDB(ADMIN, Address.class);

}
