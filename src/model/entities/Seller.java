package model.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Seller implements Serializable {
    @Serial
    private static final long serialVersionUID=1L;
    private Integer id;
    private String name;
    private String email;
    private LocalDate birthDate;
    private Double baseSalary;
    private Department department;

    public Seller() {
    }

    public Seller(Integer id, String name, String email, LocalDate birthDate, Double baseSalary, Department department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.baseSalary = baseSalary;
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Department getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Seller seller)) return false;

        return getId().equals(seller.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Seller{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", birthDate=").append(birthDate);
        sb.append(", baseSalary=").append(baseSalary);
        sb.append(", department=").append(department);
        sb.append('}');
        return sb.toString();
    }
}
