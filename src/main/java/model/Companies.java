package model;

import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.Proxy;

@Data
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "companies")
@Proxy(lazy = false)
public class Companies implements Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "country")
    private String country;
    @Column(name = "city")
    private String city;
}