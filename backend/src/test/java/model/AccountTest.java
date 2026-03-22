package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    void getterTest() throws SQLException{
        Account acct = Database.getUserByUsername("user01");

        assertEquals("FV9kE9jYa3fllSszWu6Kyv/yHujP9swYtWjDo2t53tI=", acct.getPassword());
        assertEquals("user01", acct.getUsername());
        assertEquals(1, acct.getUserID());
        assertEquals("aj+IvRWzl0PdDsoC32gJHg==", acct.getSalt());
        assertEquals("AreaHere", acct.getArea());
        assertEquals("token_1", acct.getToken());
    }

    @Test
    void saltPasswordTest() throws SQLException {
        Account acct = Database.getUserByUsername("user01");

        assertEquals("FV9kE9jYa3fllSszWu6Kyv/yHujP9swYtWjDo2t53tI=", Account.saltPassword("DesertStorm1!", acct.getSalt()));
    }
}