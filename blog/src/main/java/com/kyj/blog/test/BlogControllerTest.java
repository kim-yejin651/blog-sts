package com.kyj.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kyj.blog.model.RoleType;
import com.kyj.blog.model.User;
import com.kyj.blog.repository.UserRepository;

//스프링이 com.kyj.blog 패키지 이하를 스캔해서 모든 파일을 메모리에 new 하는 것은 아님.
//특정 어노테이션이 붙어있는 클래스 파일들을 new해서(IoC) 스프링 컨테이너에 관리해줌.
// html파일이 아니라 data를 리턴.
@RestController
public class BlogControllerTest {
	
	// 의존성 주입(DI)
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("join id: " + user.getId());
		System.out.println("join username: " + user.getUsername());
		System.out.println("join password: " + user.getPassword());
		System.out.println("join email: " + user.getEmail());
		System.out.println("join role: " + user.getRole());
		System.out.println("join createDate: " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		
		return "회원가입이 완료되었습니다."; 
	}
	
	// 주소로 파라미터를 전달받을 수 있음.
	@GetMapping("/dummy/user/{id}")
	public User user(@PathVariable int id) {
		// Optional로 User객체를 감싸서 null인지 아닌지 판단해서 return
		// 람다식
		/*
		User user = userRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);
		}); 
		*/
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id: " + id);	
			}
		}); 
		
		return user; 
	}
	
	@GetMapping("/dummy/user")
	public List<User> list() {
		List<User> users = userRepository.findAll(); 
		return users; 
	}
	
	// 한 페이지 당 2건을 받아볼 예정.
	@GetMapping("/dummy/user/page")
	public Page<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		Page<User> users =  userRepository.findAll(pageable);
		return users; 
	}
	
	// save 함수는 id를 전달하지 않으면 insert를 해주고
	// save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해주고
	// save 함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 inesrt를 해줌
	// email, password
	@Transactional
	@PostMapping("/dummy/user/{id}")
	// json 데이터를 요청 -> Java Object(MessageConverter의 Jackson 라이브러리가 변환)로 변환해서 받아줌.
	public  User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("updateUser id: " + id);
		System.out.println("updateUser password: " + requestUser.getPassword());
		System.out.println("updateUser email: " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(() -> {
			return new IllegalArgumentException("수정에 실패하였습니다."); 
		}) ;
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		// userRepository.save(user);
		
		// 더티 체킹
		return null;
	}
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		return "삭제되었습니다. id: "  + id;
		
	}
	
}
