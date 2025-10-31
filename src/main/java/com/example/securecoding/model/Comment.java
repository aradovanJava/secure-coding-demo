package com.example.securecoding.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model klasa za komentar korisnika
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;

    @NotBlank(message = "Ime ne smije biti prazno")
    @Size(min = 2, max = 50, message = "Ime mora imati između 2 i 50 znakova")
    private String name;
    
    @NotBlank(message = "Komentar ne smije biti prazan")
    @Size(min = 5, max = 500, message = "Komentar mora imati između 5 i 500 znakova")
    private String content;

    @NotBlank(message = "Ime ne smije biti prazno")
    @Size(min = 2, max = 50, message = "Ime mora imati između 2 i 50 znakova")
    private String safeName;

    @NotBlank(message = "Komentar ne smije biti prazan")
    @Size(min = 5, max = 500, message = "Komentar mora imati između 5 i 500 znakova")
    private String safeContent;

    @NotBlank(message = "Ime ne smije biti prazno")
    @Size(min = 2, max = 50, message = "Ime mora imati između 2 i 50 znakova")
    private String username;

    @NotBlank(message = "Komentar ne smije biti prazan")
    @Size(min = 5, max = 500, message = "Komentar mora imati između 5 i 500 znakova")
    private String sanitizedContent;


    public Comment(String safeName, String safeContent) {
        this.safeName = safeName;
        this.safeContent = safeContent;
    }
}
