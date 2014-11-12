package com.kyron.server.data;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

// TODO: add Json ignore + hibernate relationship
@Entity
@Table(name = "test_user")
@Access(AccessType.FIELD)
public class User implements Serializable {

	private static final long serialVersionUID = 6273989227916640603L;

	// TODO: generate sequence here
	@Id
	@Column(name = "user_id", unique = true, nullable = false)
	private Long id;

	@Column(name = "username")
	private String userName;

	@Column(name = "user_password")
	private String password;

	@Column(name = "email")
	private String email;

	// Hibernate requires default constructor
	public User() {
		super();
	}

	// ease of use, change later when have auto id
	public User(Long id, String name, String pwd, String email) {
		this.id = id;
		setUserName(name);
		setPassword(pwd);
		setEmail(email);
	}
	
	// helper function
	@Override
	public String toString() {
		String s = "Id=" + id.toString() + ",user=" + userName 
				+ "password=" + password + ",email=" + email;
		return s;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getUserName() {
		return userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
