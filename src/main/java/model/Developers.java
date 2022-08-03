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
@NamedNativeQueries(value = {
    @NamedNativeQuery(
        name = "all_middle_dev",
        query = "SELECT dv.* " +
                "FROM Developers dv " +
                "LEFT JOIN developers_skills dvsk " +
                "ON dvsk.developers_id = dv.id " +
                "LEFT JOIN skills sk " +
                "ON dvsk.skills_id = sk.id " +
                "WHERE sk.degree = 'Middle' " +
                "GROUP BY dv.id " +
                "ORDER BY dv.id;",
        resultSetMapping = "simple_mapping"
        ),
    @NamedNativeQuery(
        name = "all_java_dev",
        query = "SELECT dev.* " +
                "FROM Developers dev " +
                "LEFT JOIN developers_skills devsk " +
                "ON devsk.developers_id = dev.id " +
                "LEFT JOIN skills sk " +
                "ON devsk.skills_id = sk.id " +
                "WHERE sk.industry = 'Java' " +
                "GROUP BY dev.id " +
                "ORDER BY dev.id",
        resultSetMapping = "simple_mapping"
    ),
    @NamedNativeQuery(
            name = "all_dev_in_some_projects ",
            query = "SELECT dev " +
                    "FROM developers_projects devpr " +
                    "LEFT JOIN developers dev " +
                    "ON devpr.developers_id = dev.id " +
                    "WHERE devpr.projects_id = :projectId ",
            resultSetMapping = "simple_mapping"
    )
})
@SqlResultSetMappings(value = {
    @SqlResultSetMapping(
    name = "simple_mapping",
    classes =
        @ConstructorResult(
            targetClass = Developers.class,
            columns = {
                @ColumnResult(name = "id"),
                @ColumnResult(name = "name"),
                @ColumnResult(name = "surname"),
                @ColumnResult(name = "age"),
                @ColumnResult(name = "sex"),
                @ColumnResult(name = "salary"),
            }
        )
    )
})
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