package org.example.jwt_authentication_api.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "representatives")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Representative extends User{
    @Column(name = "company_name")
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "warehouse_code")
    private WareHouse wareHouse;
}
