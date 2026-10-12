package dev.mednikov.accounting.authz.roles.models;

import dev.mednikov.accounting.authz.authorities.models.Authority;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "authz_roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"organization_id", "role_name"})})
public class Role implements Comparable<Role>{

    @Id @GeneratedValue @UuidGenerator(style = UuidGenerator.Style.VERSION_7)
    private UUID id;

    @Column(name = "organization_id", nullable = false)
    private UUID organizationId;

    @Column(name = "role_name", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "authz_role_authority_mapping",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities = new HashSet<>();

    @Override
    public int compareTo(Role other) {
        return this.name.compareTo(other.getName());
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;

        return organizationId.equals(role.organizationId) && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        int result = organizationId.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(UUID organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
}
