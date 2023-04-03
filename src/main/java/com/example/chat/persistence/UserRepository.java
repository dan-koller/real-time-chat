package com.example.chat.persistence;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class UserRepository {
    static List<String> users = new CopyOnWriteArrayList<>();

    /**
     * This method is used to get all the users from the database.
     *
     * @return A list of all the users
     */
    public List<String> getUsers() {
        return users;
    }

    /**
     * This method is used to add a user to the database.
     *
     * @param user The user to be added
     */
    public void addUser(String user) {
        users.add(user);
    }

    /**
     * This method is used to remove a user from the database.
     *
     * @param user The user to be removed
     */
    public void removeUser(String user) {
        users.remove(user);
    }
}
