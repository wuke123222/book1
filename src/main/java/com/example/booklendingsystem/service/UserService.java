package com.example.booklendingsystem.service;

import com.example.booklendingsystem.model.User;
import com.example.booklendingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id, User user) {
        userRepository.deleteById(id);
    }

    public boolean findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    public Long countAllByIsLoggedIn() {
        return userRepository.countAllByIsLoggedIn();
    }
}
