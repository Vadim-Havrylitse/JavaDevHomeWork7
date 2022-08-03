package model.utilquerymodel;

import jakarta.persistence.Column;

public class SumOfProjectsSalary {
    @Column(name = "name")
    private String projectsName;
    @Column(name = "sum")
    private long sumOfSalary;
}
