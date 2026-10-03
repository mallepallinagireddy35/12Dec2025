package com.nic.svpnpa.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@Setter
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Setter
	@Getter
	@Column(name="role_name",length = 20,unique=true)
	@NotNull
	private ERole name;
	@Transient
	private String role_name;

	public Role() {

	}
	@Column(name = "role_desc", columnDefinition = "VARCHAR(255)")
	private String role_desc;
	
	public Role(String role_name) {

		this.role_name = role_name;
	}

	public Role(ERole name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getName() {
		return name;
	}

	public void setName(ERole name) {
		this.name = name;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	

}