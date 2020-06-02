package com.bairock.iot.hamaCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bairock.iot.hamaCenter.data.UserAuthority;

public interface UserAuthorityRepo extends JpaRepository<UserAuthority, Long> {

	UserAuthority findByUserid(String userid);
}
