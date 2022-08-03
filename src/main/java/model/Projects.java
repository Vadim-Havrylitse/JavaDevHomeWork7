package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import model.utilquerymodel.SpecialFormatProjects;
import model.utilquerymodel.SumOfProjectsSalary;
import org.hibernate.annotations.Proxy;

@Data
@Setter
@Getter
@Entity
@Table(name = "projects")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy = false)
@NamedNativeQueries(value = {
        @NamedNativeQuery(
            name = "sum_salary",
            query = "SELECT projects.name AS name, SUM(salary) AS sum" +
                    "FROM projects pr" +
                    "LEFT JOIN developers_projects devpr" +
                    "ON pr.id = devpr.projects_id " +
                    "LEFT JOIN developers dev" +
                    "ON devpr.developers_id = dev.id " +
                    "WHERE pr.id = :projectId "+
                    "GROUP BY pr.name",
            resultSetMapping = "mapping"
        ),
        @NamedNativeQuery(
                name = "spec_format_projects",
                query = "SELECT projects.release_date AS release, projects.name AS name, COUNT(developers_id) AS count " +
                        "FROM projects pr " +
                        "LEFT JOIN developers_projects devpr " +
                        "ON devpr.developers_id = pr.id " +
                        "GROUP BY pr.id",
                resultSetMapping = "mapping2"
        ),
})
@SqlResultSetMappings(value = {
        @SqlResultSetMapping(
                name = "mapping",
                classes =
                @ConstructorResult(
                        targetClass = SumOfProjectsSalary.class,
                        columns = {
                                @ColumnResult(name = "name"),
                                @ColumnResult(name = "sum")
                        }
                )
        ),
        @SqlResultSetMapping(
                name = "mapping2",
                entities ={
                        @EntityResult(
                                entityClass = SpecialFormatProjects.class,
                                fields = {
                                        @FieldResult(name = "release", column = "release"),
                                        @FieldResult(name = "name", column = "name"),
                                        @FieldResult(name = "count", column = "count")
                                }
                        )
                }
        )
})
public class Projects implements Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "budget")
    private Long budget;
    @Column(name = "release_date")
    private String releaseDate;
    @ManyToOne
    @JoinColumn(name = "companies_id",referencedColumnName = "id")
    private Companies company;
    @ManyToOne
    @JoinColumn(name = "customers_id",referencedColumnName = "id")
    private Customers customer;
}