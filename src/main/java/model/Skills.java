package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Data
@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "skills")
@Entity
@Proxy(lazy = false)
public class Skills implements Model{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "industry")
    private String industry;
    @Column(name = "degree")
    private String degree;
}