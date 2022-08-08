package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Proxy;

import java.sql.Types;
import java.time.LocalDate;

@Data
@Setter
@Getter
@Entity
@Table(name = "projects")
@JsonIgnoreProperties(ignoreUnknown = true)
@Proxy(lazy = false)
public class Projects implements Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "budget")
    private Long budget;
    @Column(name = "release_date", columnDefinition = "DATE")
    @ColumnDefault("")
    private LocalDate releaseDate;
    @ManyToOne
    @JoinColumn(name = "companies_id",referencedColumnName = "id")
    private Companies company;
    @ManyToOne
    @JoinColumn(name = "customers_id",referencedColumnName = "id")
    private Customers customer;
}