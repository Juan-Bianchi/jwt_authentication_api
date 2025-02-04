package org.example.jwt_authentication_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewUserDto {
    private Long id;
    @NotBlank(message = "Username must be provided")
    private String userName;
    @NotBlank(message = "Password must be provided")
    private String password;
    @JsonProperty("first_name")
    @NotBlank(message = "First name must be provided")
    private String firstName;
    @JsonProperty("last_name")
    @NotBlank(message = "Last name must be provided")
    private String lastName;
    @JsonProperty("role_id")
    @NotNull(message = "Role id must be provided")
    private Long roleId;
    @JsonProperty("warehouse_code")
    private Long warehouseCode;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("purchase_order_id")
    private String purchaseOrderId;
}
