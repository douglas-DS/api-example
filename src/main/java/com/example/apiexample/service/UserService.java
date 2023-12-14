package com.example.apiexample.service;

import com.example.apiexample.entity.User;
import com.example.apiexample.repository.UserRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User saveOne(User user) {
        return userRepository.save(user);
    }

    @Nullable
    public User findOne(Long id) {
        return findOneOrNull(id);
    }

    @Nullable
    public User updateOne(Long id, User user) {
        User userToUpdate = findOneOrNull(id);
        if (userToUpdate != null) {
            userToUpdate.setName(user.getName());
            userToUpdate.setEmail(user.getEmail());
            return userRepository.save(userToUpdate);
        } else {
            return null;
        }
    }

    @Nullable
    public User deleteOne(Long id) {
        User user = findOneOrNull(id);
        if (user != null) {
            userRepository.delete(user);
            return user;
        } else {
            return null;
        }
    }

    @Nullable
    private User findOneOrNull(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
