package com.agiboard.service;

import com.agiboard.entity.User;

public interface UserServiceInterface {
    User findOne(String username);
    void save(User user);
    void delete(User user);
}
