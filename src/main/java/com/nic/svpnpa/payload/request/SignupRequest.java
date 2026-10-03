package com.nic.svpnpa.payload.request;

import java.math.BigInteger;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class SignupRequest {
	@NotBlank
	@Size(min = 4, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$", message = "Invalid email format")
	private String email;
	@NotNull
	private Set<String> role;
	
	private String captcha;
	
	private Long id;
	

	/**
	 * @param username
	 * @param email
	 * @param role
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param user_id
	 */
	public SignupRequest(@NotBlank @Size(min = 4, max = 20) String username,
			@NotBlank @Size(max = 50) @Email String email,@NotNull Set<String> role, @NotBlank @Size(min = 4) String firstName,
			@NotBlank @Size(min = 4) String lastName, @NotBlank @Size(min = 6, max = 120) String password,Long id ) {
		super();
		this.username = username;
		this.email = email;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.id = id;
	}

	@NotNull
	@Column(name = "first_name")
	@Size(min = 4)
	private String firstName;
	@NotNull
	@Column(name = "last_name")
	@Size(min = 4)
	private String lastName;

	
	
	/*
	 * @Pattern(regexp =
	 * "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()_+{}\\[\\]:;<>,.?~\\\\/-]).{8,}$",
	 * message =
	 * "Password must be at least 8 characters long and include an uppercase letter, a lowercase letter, a digit, and a special character"
	 * )
	 */
	@NotBlank
	@Size(min = 64, max = 64, message = "Invalid password length")
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRole() {
		return this.role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	
	

}