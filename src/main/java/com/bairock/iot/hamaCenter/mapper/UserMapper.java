package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.user.User;

public interface UserMapper {

    int insert(User user);

    User findById(Long userId);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
