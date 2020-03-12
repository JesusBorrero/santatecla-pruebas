package com.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository uRepository;


	public User findOne(long id) {
		return uRepository.findById(id);
	}

	public List<User> findAllUsers() {
		return uRepository.findAll();
	}

	public Page<User> findAllUsers(PageRequest page) {
		return uRepository.findAll(page);
	}

	public void save(User user) {
		uRepository.save(user);
	}

	public void delete(long id) {
		uRepository.deleteById(id);
	}

	public User findByName(String name) {
		return uRepository.findByName(name);
	}

	public List<User> findStudents(){
		return this.uRepository.findStudents();
	}

	public List<User> findStudentByNameContaining(String name){
		return this.uRepository.findStudentByNameContaining(name);
	}

}