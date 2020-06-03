package uk.ac.qub.eeecs.gage;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.CreateAccountScreen;
import uk.ac.qub.eeecs.Babette.User;

public class CreateAccountScreenUnitTest {
    // Tests to check identical password check method is working.
    // Jake Smyth 40204021
    @Test
    public void testTwoPasswordsIdentical(){
        String password1 = "PASSWORD1";
        String password2 = "PASSWORD1";
        assertTrue(CreateAccountScreen.passwordEntriesIdentical(password1, password2));
    }

    @Test
    public void testTwoPasswordsNotIdentical(){
        String password1 = "PASSWORD1";
        String password2 = "PASSWORD2";
        assertTrue(!CreateAccountScreen.passwordEntriesIdentical(password1, password2));
    }

    // Tests to check textbox fill check method is working.
    // Jake Smyth 40204021
    @Test
    public void testAllTextboxesFull(){
        String textbox1 = "text";
        String textbox2 = "text";
        String textbox3 = "text";
        assertTrue(CreateAccountScreen.allTextboxesFilled(textbox1,textbox2, textbox3));
    }

    @Test
    public void testNoTextboxesFull(){
        String textbox1 = "";
        String textbox2 = "";
        String textbox3 = "";
        assertTrue(!CreateAccountScreen.allTextboxesFilled(textbox1,textbox2, textbox3));
    }

    @Test
    public void testTwoTextboxesFull(){
        String textbox1 = "text";
        String textbox2 = "text";
        String textbox3 = "";
        assertTrue(!CreateAccountScreen.allTextboxesFilled(textbox1,textbox2, textbox3));
    }

    // Tests to check password strength check method is working.
    // Jake Smyth 40204021
    @Test
    public void testEightCharsOneNumber(){
        String password = "ABCDEFG1";
        assertTrue(CreateAccountScreen.strongPasswordSubmitted(password));
    }

    @Test
    public void testEightCharsNoNumber(){
        String password = "ABCDEFGH";
        assertTrue(!CreateAccountScreen.strongPasswordSubmitted(password));
    }

    @Test
    public void testSevenCharsOneNumber(){
        String password = "ABCDEF1";
        assertTrue(!CreateAccountScreen.strongPasswordSubmitted(password));
    }

    // Tests to check existing user check method is working.
    // Jake Smyth 40204021
    @Test
    public void testUserAlreadyExists(){
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("USER1", "yellow", "Avatar1", "PASSWORD1"));
        userList.add(new User("USER2", "yellow", "Avatar1", "PASSWORD1"));
        userList.add(new User("USER3", "yellow", "Avatar1", "PASSWORD1"));
        userList.add(new User("USER4", "yellow", "Avatar1", "PASSWORD1"));

        User newUser = new User("USER4", "yellow", "Avatar1", "PASSWORD1");

        assertTrue(!CreateAccountScreen.userDoesNotAlreadyExist(newUser.getUsername(), userList));
    }

    @Test
    public void testUserDoesNotAlreadyExist(){
        ArrayList<User> userList = new ArrayList<>();
        userList.add(new User("USER1", "yellow", "Avatar1", "PASSWORD1"));
        userList.add(new User("USER2", "yellow", "Avatar1", "PASSWORD1"));
        userList.add(new User("USER3", "yellow", "Avatar1", "PASSWORD1"));
        userList.add(new User("USER4", "yellow", "Avatar1", "PASSWORD1"));

        User newUser = new User("USER5", "yellow", "Avatar1", "PASSWORD1");

        assertTrue(CreateAccountScreen.userDoesNotAlreadyExist(newUser.getUsername(), userList));
    }

    // Tests to check avatarStringForUser method is working.
    // Jake Smyth 40204021
    @Test
    public void testReturnAvatarOneString(){
        assertTrue(CreateAccountScreen.avatarStringForUser(1).equals("Avatar1"));
    }

    @Test
    public void testReturnAvatarFiveString(){
        assertTrue(CreateAccountScreen.avatarStringForUser(5).equals("Avatar5"));
    }
}