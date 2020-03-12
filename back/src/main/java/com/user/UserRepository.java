package com.user;

import javax.transaction.Transactional;

import com.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByName(String name);
	public User findById(long id);

	@Query(value = "SELECT user.id, user.name, user.password_hash FROM user JOIN user_roles on user.id = user_roles.user_id AND user_roles.roles <> 'ROLE_ADMIN'", nativeQuery = true)
	public List<User> findStudents();

	@Query(value = "SELECT user.id, user.name, user.password_hash FROM user JOIN user_roles on user.id = user_roles.user_id AND user_roles.roles <> 'ROLE_ADMIN' AND user.name LIKE CONCAT('%',?1,'%')", nativeQuery = true)
	public List<User> findStudentByNameContaining(String name);
}