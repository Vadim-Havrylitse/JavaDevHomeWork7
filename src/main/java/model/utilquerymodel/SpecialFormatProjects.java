package model.utilquerymodel;

import jakarta.persistence.Column;

public class SpecialFormatProjects {
    @Column(name = "release")
    private String release;
    @Column(name = "name")
    private String name;
    @Column(name = "count")
    private int countDevelopers;
}
