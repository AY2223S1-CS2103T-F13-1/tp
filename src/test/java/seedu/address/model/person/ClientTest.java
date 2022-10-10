package seedu.address.model.person;

import java.util.ArrayList;

public class ClientTest {
    private static Client clientStub = new Client(new Name("default"), new Phone("98765432"),
            new Email("default@gmail.com"), new ArrayList<>(), new ClientId(1));

    public static Client getClientStub() {
        return clientStub;
    }
}