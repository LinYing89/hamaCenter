package com.bairock.iot.hamaCenter.mapper;

import com.bairock.iot.hamalib.user.User;

public interface UserMapper {

    int insert(User user);

    User findById(Long userId);

    Long findIdByUsername(String username);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
}
