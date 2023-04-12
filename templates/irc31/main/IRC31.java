package io.contractdeployer.generics.irc31;

import score.Address;
import score.ByteArrayObjectWriter;
import score.Context;
import score.DictDB;
import score.annotation.EventLog;
import score.annotation.External;
import score.annotation.Optional;

import java.math.BigInteger;

import static io.contractdeployer.generics.irc31.Vars.*;

public class IRC31 implements InterfaceIRC31 {

    public IRC31(String _name, String _symbol,BigInteger _cap,BigInteger _maxBatchMintCount) {

        if (name.get() == null) {
            name.set(ensureNotEmpty(_name));
            symbol.set(ensureNotEmpty(_symbol));

            Context.require(_cap.compareTo(BigInteger.ZERO)>0,Message.greaterThanZero("_cap"));
            Context.require(_maxBatchMintCount.compareTo(BigInteger.ZERO)>0,Message.greaterThanZero("_maxBatchMintCount"));

            name.set(_name);
            symbol.set(_symbol);
            cap.set(_cap);
            maxBatchMintCount.set(_maxBatchMintCount);
            totalSupply.set(BigInteger.ZERO);
        }
    }

    private String ensureNotEmpty(String str) {
        Context.require(str != null && !str.trim().isEmpty(), Message.empty("String"));
        assert str != null;
        return str.trim();
    }

    @External(readonly=true)
    public String name() {
        return name.get();
    }

    @External(readonly=true)
    public String symbol() {
        return symbol.get();
    }

    @External(readonly = true)
    public BigInteger getMaxBatchMintCount() {
        return maxBatchMintCount.get();
    }

    @External(readonly = true)
    public BigInteger getTotalSupply() {
        return totalSupply.get();
    }

    @External(readonly = true)
    public BigInteger getCap(){
        return cap.get();
    }

    @External(readonly = true)
    public Address getAdmin() {
        return admin.get();
    }

    @External
    public void setAdmin(Address adminAddress) {
        this.ownerRequired();
        admin.set(adminAddress);
    }

    @External(readonly=true)
    public BigInteger balanceOf(Address _owner, BigInteger _id) {
        return balances.at(_id).getOrDefault(_owner, BigInteger.ZERO);
    }

    @External(readonly=true)
    public BigInteger[] balanceOfBatch(Address[] _owners, BigInteger[] _ids) {
        Context.require(_owners.length == _ids.length, Message.Not.sameSize("_owners","_ids"));

        BigInteger[] balances = new BigInteger[_owners.length];
        for (int i = 0; i < _owners.length; i++) {
            balances[i] = balanceOf(_owners[i], _ids[i]);
        }
        return balances;
    }

    @External(readonly=true)
    public String tokenURI(BigInteger _tokenId) {
        return tokenURIs.get(_tokenId);
    }

    @External
    public void transferFrom(Address _from, Address _to, BigInteger _id, BigInteger _value, @Optional byte[] _data) {
        final Address caller = Context.getCaller();

        Context.require(!_to.equals(ZERO_ADDRESS), Message.Found.zeroAddr("_to"));
        Context.require(_from.equals(caller) || this.isApprovedForAll(_from, caller), Message.Not.operatorApproved());
        Context.require(BigInteger.ZERO.compareTo(_value) <= 0 && _value.compareTo(balanceOf(_from, _id)) <= 0,
                Message.Not.enoughBalance());

        // Transfer funds
        DictDB<Address, BigInteger> balance = balances.at(_id);
        balance.set(_from, balanceOf(_from, _id).subtract(_value));
        balance.set(_to, balanceOf(_to, _id).add(_value));

        // Emit event
        this.TransferSingle(caller, _from, _to, _id, _value);

        if (_to.isContract()) {
            // Call {@code onIRC31Received} if the recipient is a contract
            Context.call(_to, "onIRC31Received", caller, _from, _id, _value, _data == null ? new byte[]{} : _data);
        }
    }

    @External
    public void transferFromBatch(Address _from, Address _to, BigInteger[] _ids, BigInteger[] _values, @Optional byte[] _data) {
        final Address caller = Context.getCaller();

        Context.require(!_to.equals(ZERO_ADDRESS), Message.Found.zeroAddr("_to"));
        Context.require(_ids.length == _values.length, Message.Not.sameSize("_ids","_values"));
        Context.require(_from.equals(caller) || this.isApprovedForAll(_from, caller), Message.Not.operatorApproved());

        for (int i = 0; i < _ids.length; i++) {
            BigInteger _id = _ids[i];
            BigInteger _value = _values[i];

            Context.require(_value.compareTo(BigInteger.ZERO) > 0, Message.greaterThanZero("_value"));

            BigInteger balanceFrom = balanceOf(_from, _id);

            Context.require(_value.compareTo(balanceFrom) <= 0, Message.Not.enoughBalance());

            // Transfer funds
            BigInteger balanceTo = balanceOf(_to, _id);
            DictDB<Address, BigInteger> balance = balances.at(_id);
            balance.set(_from, balanceFrom.subtract(_value));
            balance.set(_to, balanceTo.add(_value));
        }

        // Emit event
        this.TransferBatch(caller, _from, _to, rlpEncode(_ids), rlpEncode(_values));

        if (_to.isContract()) {
            // call {@code onIRC31BatchReceived} if the recipient is a contract
            Context.call(_to, "onIRC31BatchReceived",
                    caller, _from, _ids, _values, _data == null ? new byte[]{} : _data);
        }
    }

    @External
    public void setApprovalForAll(Address _operator, boolean _approved) {
        final Address caller = Context.getCaller();

        operatorApproval.at(caller).set(_operator, _approved);
        this.ApprovalForAll(caller, _operator, _approved);
    }

    @External(readonly=true)
    public boolean isApprovedForAll(Address _owner, Address _operator) {
        return operatorApproval.at(_owner).getOrDefault(_operator, false);
    }

    @External
    public void mint(Address _owner, BigInteger _id, BigInteger _amount,String _uri) {
        this.preMintConditions(_owner,_id,_amount);
        _mintInternal(_owner, _id, _amount,_uri);

        TransferSingle(_owner, ZERO_ADDRESS, _owner, _id, _amount);
    }

    @External
    public void mintBatch(Address _owner, BigInteger[] _ids, BigInteger[] _amounts,String[] _uris) {
        Context.require(_ids.length == _amounts.length, Message.Not.sameSize("_ids","_amounts"));
        Context.require(_ids.length == _uris.length, Message.Not.sameSize("_ids","_uris"));

        for (int i = 0; i < _ids.length; i++) {
            BigInteger id = _ids[i];
            BigInteger amount = _amounts[i];
            this.preMintConditions(_owner,id,amount);
            _mintInternal(_owner, id, amount,_uris[i]);
        }

        // emit transfer event for Mint semantic
        TransferBatch(_owner, ZERO_ADDRESS, _owner, rlpEncode(_ids), rlpEncode(_amounts));
    }

    @External
    public void burn(Address _owner, BigInteger _id, BigInteger _amount) {
        this.preBurnConditions(_owner,_id,_amount);
        _burnInternal(_owner, _id, _amount);

        TransferSingle(_owner, _owner, ZERO_ADDRESS, _id, _amount);
    }

    @External
    public void burnBatch(Address _owner, BigInteger[] _ids, BigInteger[] _amounts) {
        Context.require(_ids.length == _amounts.length, Message.Not.sameSize("_ids","_amounts"));

        for (int i = 0; i < _ids.length; i++) {
            BigInteger id = _ids[i];
            BigInteger amount = _amounts[i];
            this.preBurnConditions(_owner,id,amount);
            _burnInternal(_owner, id, amount);
        }

        TransferBatch(_owner, _owner, ZERO_ADDRESS, rlpEncode(_ids), rlpEncode(_amounts));
    }

    protected static byte[] rlpEncode(BigInteger[] ids) {
        Context.require(ids != null);

        ByteArrayObjectWriter writer = Context.newByteArrayObjectWriter("RLPn");

        writer.beginList(ids.length);
        for (BigInteger v : ids) {
            writer.write(v);
        }
        writer.end();

        return writer.toByteArray();
    }

    private void _setTokenURI(BigInteger _id, String _uri) {
        Context.require(_uri.length() > 0, Message.empty("URI"));
        tokenURIs.set(_id, _uri);
        this.URI(_id, _uri);
    }

    private void _mintInternal(Address owner, BigInteger id, BigInteger amount,String uri) {
        BigInteger balance = balanceOf(owner, id);
        balances.at(id).set(owner, balance.add(amount));
        totalSupply.set(getTotalSupply().add(amount));
        _setTokenURI(id,uri);
    }

    private void _burnInternal(Address owner, BigInteger id, BigInteger amount) {
        BigInteger balance = balanceOf(owner, id);
        balances.at(id).set(owner, balance.subtract(amount));
        totalSupply.set(getTotalSupply().subtract(amount));
    }

    private void ownerRequired() {
        Context.require(Context.getCaller().equals(Context.getOwner()), Message.Not.owner());
    }

    private void adminRequired() {
        Context.require(Context.getCaller().equals(this.getAdmin()) || Context.getCaller().equals(Context.getOwner()),
                Message.Not.admin());
    }

    private void preMintConditions(Address address, BigInteger id, BigInteger amount) {
        adminRequired();
        Context.require(!address.equals(ZERO_ADDRESS), Message.Found.zeroAddr("_owner"));
        Context.require(amount.compareTo(BigInteger.ZERO) > 0 &&
                amount.compareTo(getMaxBatchMintCount())<=0, Message.Exceeded.nftCountPerTxRange());
        Context.require(getTotalSupply().add(amount).compareTo(getCap())<=0,Message.Exceeded.cap());
    }

    private void preBurnConditions(Address address, BigInteger id, BigInteger amount) {
        final Address caller = Context.getOrigin();
        Context.require(!address.equals(ZERO_ADDRESS), Message.Found.zeroAddr("_owner"));
        Context.require(amount.compareTo(BigInteger.ZERO) > 0, Message.greaterThanZero("_amount"));
        Context.require(address.equals(caller) || this.isApprovedForAll(address, caller),
                Message.Not.operatorApproved());
        Context.require(balanceOf(address,id).compareTo(amount)>=0,Message.Not.enoughBalance());
    }

    @EventLog(indexed=3)
    public void TransferSingle(Address _operator, Address _from, Address _to, BigInteger _id, BigInteger _value) {}

    @EventLog(indexed=3)
    public void TransferBatch(Address _operator, Address _from, Address _to, byte[] _ids, byte[] _values) {}

    @EventLog(indexed=2)
    public void ApprovalForAll(Address _owner, Address _operator, boolean _approved) {}

    @EventLog(indexed=1)
    public void URI(BigInteger _id, String _value) {}

}
