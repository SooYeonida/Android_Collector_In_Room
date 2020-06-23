package com.ajou.capstone_design_freitag.API;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RESTAPITest {

    @Test
    public void getInstance() {
        assertNotNull(RESTAPI.getInstance());
    }

    @Test
    public void login() {
        assertEquals(RESTAPI.LOGIN_SUCCESS, RESTAPI.getInstance().login("test","1234"));
    }

    @Test
    public void notRegisterdUserlogin() {
        assertEquals(RESTAPI.LOGIN_FAIL, RESTAPI.getInstance().login("dddd","1234"));
    }

    @Test
    public void randomIdSignup() {
        String randomId = UUID.randomUUID().toString().substring(0,7);
        assertEquals(RESTAPI.REGISTER_SUCCESS, RESTAPI.getInstance().signup(randomId, "1234", "testtest", "01027540421", "wodnd999999@ajou.ac.kr", "Ajou Univ."));
    }

    @Test
    public void tooLongIdSignup() {
        String randomId = UUID.randomUUID().toString();
        assertEquals(RESTAPI.REGISTER_FAIL, RESTAPI.getInstance().signup(randomId, "1234", "testtest", "01027540421", "wodnd999999@ajou.ac.kr", "Ajou Univ."));
    }

    @Test
    public void redundantSignup() {
        assertEquals(RESTAPI.REGISTER_FAIL, RESTAPI.getInstance().signup("test", "1234", "testtest", "01027540421", "wodnd999999@ajou.ac.kr", "Ajou Univ."));
    }

    @Test
    public void downloadCat() throws FileNotFoundException {
        OutputStream outputStream = new FileOutputStream("/Users/sooyeon/Desktop/data/흐엉.jpg");
        assertTrue(RESTAPI.getInstance().downloadObject("pury90", "흐엉.jpg", outputStream));
    }

}