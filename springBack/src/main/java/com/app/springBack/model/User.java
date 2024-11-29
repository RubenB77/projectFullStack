package com.app.springBack.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "Username must contain only letters and numbers")
    @Size(min = 3, message = "Username must be at least 3 characters long")
    @Column(unique=true)
    @NotNull(message = "Username cannot be null")
    private String username;

    @NotNull(message = "Password cannot be null")
    private String password;

    @Pattern(regexp = "^(USER|ADMIN)$", message = "Role must be either 'USER' or 'ADMIN'")
    private String role = "USER";

	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Enter a valid email adress")
    @Column(unique=true)
	@NotNull(message= "Email cannot be null")
	private String email;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "categoryUser", cascade = CascadeType.ALL)
    private List<Category> userCateogries;

    // No-arg constructor for Hibernate
    protected User() {}

    public User(String username, String password, String role, String email) {
        this.username = username;
        this.password = password;
        this.role = role;
		this.email = email;
    }

    @PrePersist
    public void onInsert() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Europe/Paris")).truncatedTo(ChronoUnit.SECONDS);
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = ZonedDateTime.now(ZoneId.of("Europe/Paris")).truncatedTo(ChronoUnit.SECONDS);
    }

    public Integer getId() {
        return this.userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

	public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreationTime() {
        return this.createdAt != null ? this.createdAt.toString() : null;
    }

    public String getUpdateDateTime() {
        return this.updatedAt != null ? this.updatedAt.toString() : null;
    }

}
