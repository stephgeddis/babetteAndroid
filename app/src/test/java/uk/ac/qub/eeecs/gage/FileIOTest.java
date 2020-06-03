package uk.ac.qub.eeecs.gage;

import org.junit.Test;

import java.util.ArrayList;

import uk.ac.qub.eeecs.Babette.User;

import static org.junit.Assert.assertTrue;

public class FileIOTest {

    @Test
    public void testObjectToString() {

        User testUser = new User("Adam", "Blue", "Avatar1", "Password1");
        String newFormat = testUser.toTextFormat();
        String expectedFormat = "Adam:Blue:Avatar1:" + testUser.getSalt() + ":" + testUser.getPassword() + ":0";

        assertTrue(newFormat.equals(expectedFormat));
    }

    @Test
    public void testObjectListToStream() {

        ArrayList<User> arr = new ArrayList<>();
        arr.add(new User("A", "Blue", "Ava2", "pass"));
        arr.add(new User("B", "Orange", "Ava4", "pass"));
        arr.add(new User("C", "Red", "Ava1", "pass"));

        String expectedFormat = "A:Blue:Ava2:" + arr.get(0).getSalt() + ":" + arr.get(0).getPassword() + ":0£" +
                "B:Orange:Ava4:" + arr.get(1).getSalt() + ":" + arr.get(1).getPassword() + ":0£" +
                "C:Red:Ava1:" + arr.get(2).getSalt() + ":" + arr.get(2).getPassword() + ":0£";

        assertTrue(User.getFileString(arr).equals(expectedFormat));
    }
}
