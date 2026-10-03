package com.nic.svpnpa.model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
public class User {

	private static final long serialVersionUID = 2636936156391265891L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User Name cannot be empty.")
	@Size(max = 20)
	private String username;
	@NotNull(message = "First Name cannot be empty.")
	@Column(name = "first_name")
	private String firstName;
	@NotNull(message = "Last Name cannot be empty.")
	@Column(name = "last_name")
	private String lastName;
	@NotNull
	@Size(max = 50)
	@Email(message = "User's Email cannot be empty.")
	private String email;

	private String resetToken;

	@Column(name = "loggedin", columnDefinition = "boolean default false")
	 private boolean loggedIn;

	@Column(name = "failed_login_attempts", nullable = false, columnDefinition = "integer default 0")
	private Integer failed_login_attempts;

	@Column(name = "last_login_attempt")
	private LocalDateTime lastLoginAttempt;

	private LocalDateTime resetTokenExpiry;
	@NotNull(message = "User Password cannot be empty.")
	@Size(max = 120)
	private String password;

	private String created_by;
	private String modified_by;
	
	@Column(name = "status")
	private String status;

	@Column(name = "locked", columnDefinition = "boolean default false")
	private Boolean locked;

	@Column(name = "created_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Date created_date;

	@Column(name = "modified_date", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
	private Date modified_date;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Collection<Role> roles;

	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "previous_passwords", joinColumns = @JoinColumn(name = "user_id"))
	private Set<String> previousPasswords;
	
	public User() {
	}

	public User(String username, String firstName, String lastName, String email, String password, String created_by) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.created_by = created_by;
		// Set default values
		this.failed_login_attempts = 0;
		this.lastLoginAttempt = LocalDateTime.now();
		this.locked = false;
		this.created_date = new Date();
		this.loggedIn = false;

		// this.modified_date = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
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

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@NotBlank
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

	public String getCreated_by() {
		return created_by;
	}

	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}

	public String getModified_by() {
		return modified_by;
	}

	public void setModified_by(String modified_by) {
		this.modified_by = modified_by;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getModified_date() {
		return modified_date;
	}

	public void setModified_date(Date modified_date) {
		this.modified_date = modified_date;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public LocalDateTime getResetTokenExpiry() {
		return resetTokenExpiry;
	}

	public void setResetTokenExpiry(LocalDateTime resetTokenExpiry) {
		this.resetTokenExpiry = resetTokenExpiry;
	}

	public int getFailed_login_attempts() {
		return failed_login_attempts;
	}

	public LocalDateTime getLastLoginAttempt() {
		return lastLoginAttempt;
	}

	public void setLastLoginAttempt(LocalDateTime lastLoginAttempt) {
		this.lastLoginAttempt = lastLoginAttempt;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public void setFailed_login_attempts(Integer failed_login_attempts) {
		this.failed_login_attempts = failed_login_attempts;
	}
	public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

	public Set<String> getPreviousPasswords() {
		return previousPasswords;
	}

	public void setPreviousPasswords(Set<String> previousPasswords) {
		this.previousPasswords = previousPasswords;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	
	
	// getters and setters

}