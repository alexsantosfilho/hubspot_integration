package com.hubspot.integration.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ContactRequestDTO {
    
    @Valid
    @JsonProperty("properties")
    private ContactProperties properties;

    @Data
    public static class ContactProperties {
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        @JsonProperty("email")
        private String email;
        
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 50, message = "Nome deve ter no máximo 10 caracteres")
        @JsonProperty("firstname")
        private String firstName;

        @JsonProperty("lastname")
        private String lastName;

        @Pattern(regexp = "^(https?://).*", message = "Website deve começar com http:// ou https://")
        @JsonProperty("website")
        private String website;

        @JsonProperty("company")
        private String company;

        @Pattern(regexp = "^[\\d\\s-]+$", message = "Telefone deve conter apenas números, espaços ou hífens")
        @JsonProperty("phone")
        private String phone;

        @JsonProperty("custom_property")
        private String customProperty;
    }
}