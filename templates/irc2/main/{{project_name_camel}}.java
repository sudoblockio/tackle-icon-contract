/*
 * Copyright 2020 ICONLOOP Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.iconloop.score.example;

import com.iconloop.score.token.irc2.IRC2Basic;
import score.Context;

import java.math.BigInteger;

public class IRC2BasicToken extends IRC2Basic {
    {% if mintable %}private final String MINTER2 = "ExtraMinter";{% endif %}
    {% if mintable %}protected final VarDB<Address> minter2 = Context.newVarDB(MINTER2, Address.class);{% endif %}

    public IRC2BasicToken(String _name, String _symbol, int _decimals, BigInteger _initialSupply) {
        super(_name, _symbol, _decimals);

        // mint the initial token supply here
        Context.require(_initialSupply.compareTo(BigInteger.ZERO) >= 0);
        _mint(Context.getCaller(), _initialSupply.multiply(pow10(_decimals)));
    }

    {% if mintable %}
    @External
    public void setMinter2(Address _address) {
        onlyOwner();
        isContract(_address);
        minter2.set(_address);
    }

    @External(readonly = true)
    public Address getMinter2() {
        return minter2.get();
    }

    @External
    public void burn(BigInteger _amount) {
        burnFrom(Context.getCaller(), _amount);
    }

    @External
    public void burnFrom(Address _account, BigInteger _amount) {
        checkStatus();
        onlyEither(minter, minter2);
        super.burn(_account, _amount);
    }

    @External
    public void mint(BigInteger _amount, @Optional byte[] _data) {
        mintTo(Context.getCaller(), _amount, _data);
    }

    @External
    public void mintTo(Address _account, BigInteger _amount, @Optional byte[] _data) {
        checkStatus();
        onlyEither(minter, minter2);
        mintWithTokenFallback(_account, _amount, _data);
    }{% endif %}

    {% if stable %}
    @External
    public void setOracle(Address _address) {
        only(governance);
        isContract(_address);
        oracleAddress.set(_address);
    }

    @External(readonly = true)
    public Address getOracle() {
        return oracleAddress.get();
    }

    @External
    public void setOracleName(String _name) {
        only(admin);
        oracleName.set(_name);
    }

    @External(readonly = true)
    public String getOracleName() {
        return oracleName.get();
    }

    @External
    public void setMinInterval(BigInteger _interval) {
        only(admin);
        minInterval.set(_interval);
    }

    @External(readonly = true)
    public BigInteger getMinInterval() {
        return minInterval.get();
    }

    @External(readonly = true)
    public BigInteger getPriceUpdateTime() {
        return priceUpdateTime.getOrDefault(BigInteger.ZERO);
    }

    @SuppressWarnings("unchecked")
    private BigInteger updateAssetValue() {
        Address oracleAddress = this.oracleAddress.get();

        Map<String, Object> priceData = (Map<String, Object>) Context.call(oracleAddress, "get_reference_data",
                USD_BASE, ICX_QUOTE);
        BigInteger priceOfBnusdInIcx = (BigInteger) priceData.get("rate");
        lastPrice.set(priceOfBnusdInIcx);
        priceUpdateTime.set(BigInteger.valueOf(Context.getBlockTimestamp()));
        OraclePrice(USD_BASE + ICX_QUOTE, oracleName.get(), oracleAddress, priceOfBnusdInIcx);
        return priceOfBnusdInIcx;
    }

    @EventLog(indexed = 3)
    public void OraclePrice(String market, String oracle_name, Address oracle_address, BigInteger price) {
    }
    {% endif %}

    @Override
    @External
    public void transfer(Address _to, BigInteger _value, @Optional byte[] _data) {
        checkStatus();
        transfer(Context.getCaller(), _to, _value, _data);
    }
}
