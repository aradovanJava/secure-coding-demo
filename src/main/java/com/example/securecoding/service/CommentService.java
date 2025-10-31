package com.example.securecoding.service;

import com.example.securecoding.model.Comment;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service koji demonstrira ranjive i sigurne metode za rad s komentarima
 */
@Service
public class CommentService {
    
    private final List<Comment> vulnerableComments = new ArrayList<>();
    private final List<Comment> secureComments = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();
    
    // OWASP HTML Sanitizer policy
    private final PolicyFactory policy = Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.LINKS);
    
    /**
     * RANJIVA METODA - ne sanitizira input!
     * Direktno sprema korisnički input bez ikakve provjere.
     */
    public Comment saveVulnerableComment(Comment comment) {
        comment.setId(counter.incrementAndGet());
        // ⚠️ RANJIVOST: Ne provjeravamo niti sanitiziramo sadržaj!
        // Maliciozni JavaScript će biti spremljen i izvršen u browseru
        vulnerableComments.add(comment);
        return comment;
    }
    
    /**
     * SIGURNA METODA - koristi HTML sanitizaciju
     * Sanitizira korisnički input prije spremanja.
     */
    public Comment saveSecureComment(Comment comment) {
        comment.setId(counter.incrementAndGet());
        
        // ✅ SIGURNO: Sanitiziramo sve potencijalno opasne znakove
        String sanitizedUsername = sanitizeInput(comment.getUsername());
        String sanitizedContent = sanitizeInput(comment.getContent());
        
        comment.setUsername(sanitizedUsername);
        comment.setSanitizedContent(sanitizedContent);
        
        secureComments.add(comment);
        return comment;
    }
    
    /**
     * Helper metoda za sanitizaciju input-a
     * Koristi OWASP Java HTML Sanitizer
     */
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Sanitizira HTML i JavaScript
        return policy.sanitize(input);
    }
    
    /**
     * Alternative: Encode HTML special characters
     * Ovo je još jedan način zaštite - encodiranje umjesto sanitizacije
     */
    private String encodeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("/", "&#x2F;");
    }
    
    public List<Comment> getVulnerableComments() {
        return new ArrayList<>(vulnerableComments);
    }
    
    public List<Comment> getSecureComments() {
        return new ArrayList<>(secureComments);
    }
    
    public void clearAllComments() {
        vulnerableComments.clear();
        secureComments.clear();
    }
}
