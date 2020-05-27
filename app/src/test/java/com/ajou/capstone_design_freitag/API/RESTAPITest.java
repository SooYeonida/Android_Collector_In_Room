package com.ajou.capstone_design_freitag.API;

import org.junit.Ignore;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RESTAPITest {

    @Ignore
    @Test
    public void getInstance() {
        assertNotNull(RESTAPI.getInstance());
    }

    @Ignore
    @Test
    public void login() {
        assertTrue(RESTAPI.getInstance().login("test","1234"));
    }

    @Ignore
    @Test
    public void notRegisterdUserlogin() {
        assertFalse(RESTAPI.getInstance().login("dddd","1234"));
    }

    @Ignore
    @Test
    public void randomIdSignup() {
        String randomId = UUID.randomUUID().toString().substring(0,7);
        assertTrue(RESTAPI.getInstance().signup(randomId, "1234", "testtest", "01027540421", "wodnd999999@ajou.ac.kr", "Ajou Univ."));
    }

    @Ignore
    @Test
    public void tooLongIdSignup() {
        String randomId = UUID.randomUUID().toString();
        assertFalse(RESTAPI.getInstance().signup(randomId, "1234", "testtest", "01027540421", "wodnd999999@ajou.ac.kr", "Ajou Univ."));
    }

    @Ignore
    @Test
    public void redundantSignup() {
        assertFalse(RESTAPI.getInstance().signup("test", "1234", "testtest", "01027540421", "wodnd999999@ajou.ac.kr", "Ajou Univ."));
    }

    @Test
    public void downloadCat() throws FileNotFoundException {
        OutputStream outputStream = new FileOutputStream("/Users/choejaeung/Desktop/download/cat.jpg");
        assertTrue(RESTAPI.getInstance().downloadObject("woneyhoney1", "cat.jpg", outputStream));
    }

}