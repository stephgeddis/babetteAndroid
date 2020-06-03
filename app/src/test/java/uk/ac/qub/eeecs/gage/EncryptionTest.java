package uk.ac.qub.eeecs.gage;

import org.junit.Test;

import uk.ac.qub.eeecs.Babette.User;

import static org.junit.Assert.assertTrue;

public class EncryptionTest {

    @Test
    public void testEncryptionIsConstantForEachValue() {

        User user = new User("a","b","c", "d");
        boolean flag = true;
        String pass1, pass2;

        String[] arr = new String[]{
                "bad password",
                "another bad password",
                "password1",
                "hKgJ4$ytw",
            };


        for (String pass : arr) {
            user.setPassword(pass);
            pass1 = user.getPassword();
            user.setPassword(pass);
            pass2 = user.getPassword();


            if (!pass1.equals(pass2))
                flag = false;
        }

        assertTrue(flag);
    }

    @Test
    public void testSaltsAreUnique() {

        boolean flag = false;

        String[] arr = new String[]{
                "bad password",
                "another bad password",
                "password1",
                "hKgJ4$ytw",
        };

        for (String pass : arr) {

            User a = new User("a", "b", "c", pass);
            User b = new User("a", "b", "c", pass);

            if (a.getPassword() != b.getPassword())
                flag = true;
        }

        assertTrue(flag);
    }

    @Test
    public void testCorrectPasswordMethod() {

        boolean flag = true;
        User user = new User("Adam", "Blue", "ava", "s-Tr0Ng_^-p455w0Rd");

        if (!user.correctPassword("s-Tr0Ng_^-p455w0Rd"))
            flag = false;
        if (user.correctPassword("random_password"))
            flag = false;

        assertTrue(flag);
    }
}
