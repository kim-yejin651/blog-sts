package com.kyj.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
// 빌더 패턴!!
@Builder
//ORM -> Java(다른언어) Object -> 테이블로 매핑하는 기술
//User 클래스가 MySQL에 테이블이 생성됨. 
@Entity
public class User {
	
	 // Primary key
	@Id
	// 프로젝트에서 연결된 DB의 넘버링 전략을 따라감.
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	// 시퀀스, auto_increment
	private int id; 
	
	@Column(nullable = false, length =30)
	// 아이디
	private String username;

	// 비밀번호 암호화
	@Column(nullable = false, length =100)
	//비밀번호
	private String password;
	
	@Column(nullable = false, length =50)
	//이메일
	private String email;
	
	//Enum을 쓰는 게 좋음. (ex) ADMIN, USER)
	//@ColumnDefault("'user'")
	// DB에는 RoleType이라는 게 없음.
	@Enumerated(EnumType.STRING)
	// 권한
	private RoleType role;
	
	@CreationTimestamp
	//가입일
	private Timestamp createDate;
	
}
