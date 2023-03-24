package io.contractdeployer.generics.irc3;

import com.iconloop.score.util.EnumerableMap;
import com.iconloop.score.util.IntSet;
import score.*;

import java.math.BigInteger;

import static io.contractdeployer.generics.irc3.Constant.*;
import static score.Context.newVarDB;

public class Vars {

    static final Address ZERO_ADDRESS = new Address(new byte[Address.LENGTH]);
    static final VarDB<String> name = Context.newVarDB(NAME, String.class);
    static final VarDB<String> symbol = Context.newVarDB(SYMBOL, String.class);
    static final VarDB<BigInteger> mintCost = Context.newVarDB(MINT_COST, BigInteger.class);
    static final VarDB<BigInteger> tokenId = Context.newVarDB(TOKEN_ID, BigInteger.class);
    static final VarDB<BigInteger> cap = Context.newVarDB(CAP, BigInteger.class);
    static final DictDB<Address, IntSet> holderTokens = Context.newDictDB(HOLDERS, IntSet.class);
    static final EnumerableMap<BigInteger, Address> tokenOwners = new EnumerableMap<>(TOKEN_OWNERS, BigInteger.class, Address.class);
    static final DictDB<BigInteger, Address> tokenApprovals = Context.newDictDB(APPROVALS, Address.class);
    static final VarDB<Address> admin = newVarDB(ADMIN, Address.class);
    static final DictDB<BigInteger, String> tokenURIs = Context.newDictDB(TOKEN_URI, String.class);
}
