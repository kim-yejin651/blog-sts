package com.kyj.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kyj.blog.model.User;

// DAO
// 자동으로 bean등록이 됨.
// @Repository 생략 가능함.
public interface UserRepository extends JpaRepository<User, Integer>{

}
