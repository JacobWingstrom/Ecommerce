package model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

public class DatabaseTest {

    @Test
    void getUserByUsernameTest() throws SQLException{
        assertNotNull(Database.getUserByUsername("user01"));

        assertNull(Database.getUserByUsername("user"));
    }

    @Test 
    void authenticateTest() throws SQLException{
        assertNotNull(Database.authenticate("user01", "DesertStorm1!"));

        assertNull(Database.authenticate("user01", "DesertStorm1"));
        assertNull(Database.authenticate("user0", "DesertStorm1!"));
    }

    @Test 
    void addUserToDatabasetest() throws SQLException {
        
        Database.addUserToDatabase(new Account("user", "123"));

        assertNotNull(Database.authenticate("user", "123"));
    }
}