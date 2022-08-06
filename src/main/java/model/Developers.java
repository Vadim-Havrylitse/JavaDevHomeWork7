package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import java.util.Set;

@Data
@Setter
@Getter
@Entity
@Table(name = "developers")
@Proxy(lazy = false)
@Transactional
public class Developers implements Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "age")
    private Long age;
    @Column(name = "sex")
    private String sex;
    @Column(name = "salary")
    private Integer salary;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "developers_skills",
        joinColumns = @JoinColumn(name = "developers_id"),
        inverseJoinColumns = @JoinColumn(name = "skills_id"))
    @JsonIgnoreProperties
    private Set<Skills> skills;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "developers_projects",
            joinColumns = @JoinColumn(name = "developers_id"),
            inverseJoinColumns = @JoinColumn(name = "projects_id"))
    @JsonIgnoreProperties()
    private Set<Projects> projects;

    @ManyToOne
    @JoinColumn(name = "companies_id", referencedColumnName = "id")
    @JsonIgnoreProperties
    private Companies company;
}