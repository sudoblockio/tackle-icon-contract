package io.contractdeployer.generics.irc3;

import com.iconloop.score.test.Account;
import com.iconloop.score.test.Score;
import com.iconloop.score.test.ServiceManager;
import com.iconloop.score.test.TestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigInteger;

import static io.contractdeployer.generics.irc3.TestHelper.expectErrorMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class IRC3Test extends TestBase {

    private static final String name="IRC3";
    private static final String symbol="irc3";
    private static final BigInteger cap=BigInteger.valueOf(5);
    private static final BigInteger mintCost=BigInteger.valueOf(1);
    private static final ServiceManager sm = getServiceManager();
    private static final Account owner = sm.createAccount();
    private static final Account user = sm.createAccount();
    private static final Account admin = sm.createAccount();
    private static Score ircScore;
    private static IRC3 tokenSpy;

    @BeforeEach
    public void setup() throws Exception {
        ircScore = sm.deploy(owner, IRC3.class,name,symbol,cap,mintCost);
        ircScore.invoke(owner,"setAdmin", admin.getAddress());
        // setup spy object against the ircScore object
        tokenSpy = (IRC3) spy(ircScore.getInstance());
        ircScore.setInstance(tokenSpy);
    }

    @Test
    void setAdmin(){
        Executable call = () -> ircScore.invoke(user,"setAdmin", user.getAddress());
        expectErrorMessage(call, Message.Not.owner());

        ircScore.invoke(owner,"setAdmin", user.getAddress());
        assertEquals(ircScore.call("getAdmin"),user.getAddress());
    }

    @Test
    void setMintCost(){
        Executable call = () -> ircScore.invoke(user,"setMintCost", BigInteger.valueOf(2));
        expectErrorMessage(call, Message.Not.admin());

        ircScore.invoke(admin,"setMintCost", BigInteger.valueOf(2));
        assertEquals(ircScore.call("getMintCost"),BigInteger.valueOf(2));
    }

    @Test
    void getCap(){
        assertEquals(ircScore.call("getCap"),cap);
    }

    @Test
    void getTokenId(){
        assertEquals(ircScore.call("getTokenId"),BigInteger.valueOf(0));
    }

    @Test
    void mint(){
        doReturn(BigInteger.valueOf(5)).when(tokenSpy).getPaidValue();
        Executable call = () -> ircScore.invoke(user,"mint","uri");
        expectErrorMessage(call, Message.priceMismatch());

        doReturn(BigInteger.valueOf(1)).when(tokenSpy).getPaidValue();
        for(int i=0;i<5;i++){
            ircScore.invoke(user,"mint","uri");
        }

        call = () -> ircScore.invoke(user,"mint","uri");
        expectErrorMessage(call, Message.Exceeded.cap());

        assertEquals(ircScore.call("balanceOf",user.getAddress()),5);
        assertEquals(ircScore.call("getTokenUri",BigInteger.valueOf(1)),"uri");
    }

    @Test
    void burn(){
        doReturn(BigInteger.valueOf(1)).when(tokenSpy).getPaidValue();
        ircScore.invoke(user,"mint", "uri1");
        ircScore.invoke(user,"mint", "uri2");

        Executable call = () -> ircScore.invoke(admin,"burn", BigInteger.valueOf(1));
        expectErrorMessage(call, Message.Not.operatorApproved());

        call = () -> ircScore.invoke(user,"burn", BigInteger.valueOf(3));
        expectErrorMessage(call,"Reverted(0): "+Message.noToken());

        ircScore.invoke(user,"approve", admin.getAddress(),BigInteger.valueOf(1));
        ircScore.invoke(admin,"burn", BigInteger.valueOf(1));
        assertEquals(ircScore.call("balanceOf",user.getAddress()),1);

        ircScore.invoke(user,"burn", BigInteger.valueOf(2));
        assertEquals(ircScore.call("balanceOf",user.getAddress()),0);
    }

}
