package dev.mednikov.accounting.authz.roles.models;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "authz_user_roles")
public class UserRole implements Comparable<UserRole> {

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Role role;

    @Column(name = "is_current", nullable = false)
    private boolean current;

    @Override
    public int compareTo(UserRole other) {
        return this.role.getName().compareTo(other.getRole().getName());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof UserRole userRole)) return false;

        return userId.equals(userRole.userId) && role.equals(userRole.role);
    }

    @Override
    public int hashCode() {
        int result = userId.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
