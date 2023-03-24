package io.contractdeployer.generics.irc3;

import com.iconloop.score.util.IntSet;
import score.Address;
import score.Context;
import score.annotation.EventLog;
import score.annotation.External;
import score.annotation.Payable;

import java.math.BigInteger;

import static io.contractdeployer.generics.irc3.Vars.*;

public class IRC3 implements InterfaceIRC3 {

    public IRC3(String _name, String _symbol,BigInteger _cap,BigInteger _mintCost) {

        if (name.get() == null) {
            name.set(ensureNotEmpty(_name));
            symbol.set(ensureNotEmpty(_symbol));

            Context.require(_mintCost.compareTo(BigInteger.ZERO)>=0,Message.Found.negative("_mintCost"));
            Context.require(_cap.compareTo(BigInteger.ZERO)>0,Message.Found.zeroNeg("_cap"));

            name.set(_name);
            symbol.set(_symbol);
            tokenId.set(BigInteger.ZERO);
            cap.set(_cap);
            mintCost.set(_mintCost);
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

    @External(readonly=true)
    public BigInteger getMintCost() {
        return mintCost.get();
    }

    @External(readonly=true)
    public BigInteger getTokenId() {
        return tokenId.get();
    }

    @External(readonly=true)
    public String getTokenUri(BigInteger _tokenId) {
        return tokenURIs.get(_tokenId);
    }
    @External(readonly=true)
    public void setMintCost(BigInteger _mintCost) {
        this.adminRequired();
        mintCost.set(_mintCost);
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
    public int balanceOf(Address _owner) {
        Context.require(!ZERO_ADDRESS.equals(_owner), Message.Found.zeroAddr("_owner"));
        var tokens = holderTokens.get(_owner);
        return (tokens != null) ? tokens.length() : 0;
    }

    @External(readonly=true)
    public Address ownerOf(BigInteger _tokenId) {
        return tokenOwners.getOrThrow(_tokenId, Message.noToken());
    }

    @External(readonly=true)
    public Address getApproved(BigInteger _tokenId) {
        return tokenApprovals.getOrDefault(_tokenId, ZERO_ADDRESS);
    }

    @External
    public void approve(Address _to, BigInteger _tokenId) {
        Address owner = ownerOf(_tokenId);
        Context.require(!owner.equals(_to), Message.ownerApproval());
        Context.require(owner.equals(Context.getCaller()), Message.Not.tokenOwner());
        _approve(_to, _tokenId);
    }

    private void _approve(Address to, BigInteger tokenId) {
        tokenApprovals.set(tokenId, to);
        Approval(ownerOf(tokenId), to, tokenId);
    }

    @External
    public void transfer(Address _to, BigInteger _tokenId) {
        Address owner = ownerOf(_tokenId);
        Context.require(owner.equals(Context.getCaller()), Message.Not.tokenOwner());
        _transfer(owner, _to, _tokenId);
    }

    @External
    public void transferFrom(Address _from, Address _to, BigInteger _tokenId) {
        Address owner = ownerOf(_tokenId);
        Address spender = Context.getCaller();
        Context.require(owner.equals(spender) || getApproved(_tokenId).equals(spender), Message.Not.operatorApproved());
        _transfer(_from, _to, _tokenId);
    }

    private void _transfer(Address from, Address to, BigInteger tokenId) {
        Context.require(ownerOf(tokenId).equals(from), Message.Not.tokenOwner());
        Context.require(!to.equals(ZERO_ADDRESS), Message.Found.zeroAddr("to"));

        _approve(ZERO_ADDRESS, tokenId);

        _removeTokenFrom(tokenId, from);
        _addTokenTo(tokenId, to);
        tokenOwners.set(tokenId, to);
        Transfer(from, to, tokenId);
    }

    @External(readonly=true)
    public int totalSupply() {
        return tokenOwners.length();
    }

    @External(readonly=true)
    public BigInteger tokenByIndex(int _index) {
        return tokenOwners.getKey(_index);
    }

    @External(readonly=true)
    public BigInteger tokenOfOwnerByIndex(Address _owner, int _index) {
        var tokens = holderTokens.get(_owner);
        return (tokens != null) ? tokens.at(_index) : BigInteger.ZERO;
    }

    @External
    @Payable
    public void mint(String _uri){
        Context.require(getPaidValue().equals(getMintCost()),Message.priceMismatch());
        Context.require(BigInteger.valueOf(totalSupply()+1).compareTo(getCap())<=0,Message.Exceeded.cap());
        Address caller=Context.getCaller();
        tokenId.set(getTokenId().add(BigInteger.ONE));
        tokenURIs.set(getTokenId(),_uri);
        _mint(caller,getTokenId());
    }

    @External
    public void burn(BigInteger _tokenId){
        Address owner=ownerOf(_tokenId);
        Address caller=Context.getCaller();
        Context.require(owner.equals(caller) || getApproved(_tokenId).equals(caller),Message.Not.operatorApproved());

        _burn(_tokenId);
    }

    protected void _mint(Address to, BigInteger tokenId) {
        Context.require(!ZERO_ADDRESS.equals(to), Message.Found.zeroAddr("to"));
        Context.require(!_tokenExists(tokenId), Message.Found.token());

        _addTokenTo(tokenId, to);
        tokenOwners.set(tokenId, to);
        Transfer(ZERO_ADDRESS, to, tokenId);
    }

    protected void _burn(BigInteger tokenId) {
        Address owner = ownerOf(tokenId);
        _approve(ZERO_ADDRESS, tokenId);

        _removeTokenFrom(tokenId, owner);
        tokenOwners.remove(tokenId);
        Transfer(owner, ZERO_ADDRESS, tokenId);
    }

    protected boolean _tokenExists(BigInteger tokenId) {
        return tokenOwners.contains(tokenId);
    }

    private void _addTokenTo(BigInteger tokenId, Address to) {
        var tokens = holderTokens.get(to);
        if (tokens == null) {
            tokens = new IntSet(to.toString());
            holderTokens.set(to, tokens);
        }
        tokens.add(tokenId);
    }

    private void _removeTokenFrom(BigInteger tokenId, Address from) {
        var tokens = holderTokens.get(from);
        Context.require(tokens != null, Message.noOwnerTokens());
        tokens.remove(tokenId);
        if (tokens.length() == 0) {
            holderTokens.set(from, null);
        }
    }

    protected BigInteger getPaidValue(){
        return Context.getValue();
    }

    private void ownerRequired() {
        Context.require(Context.getCaller().equals(Context.getOwner()), Message.Not.owner());
    }

    private void adminRequired() {
        Context.require(Context.getCaller().equals(this.getAdmin()) || Context.getCaller().equals(Context.getOwner()),
                Message.Not.admin());
    }

    @EventLog(indexed=3)
    public void Transfer(Address _from, Address _to, BigInteger _tokenId) {
    }

    @EventLog(indexed=3)
    public void Approval(Address _owner, Address _approved, BigInteger _tokenId) {
    }
}