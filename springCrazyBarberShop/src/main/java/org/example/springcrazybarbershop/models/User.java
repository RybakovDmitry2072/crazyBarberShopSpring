package org.example.springcrazybarbershop.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    private String password;

    private String phone;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private File image;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @Transient
    public Set<Role> getRoles() {
        return userRoles.stream()
                .map(UserRole::getRole)
                .collect(java.util.stream.Collectors.toSet());
    }

    public void setRoles(Set<Role> roles) {
        this.userRoles = roles.stream()
                .map(role -> {
                    UserRole userRole = new UserRole();
                    userRole.setUser(this);
                    userRole.setRole(role);
                    return userRole;
                })
                .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}