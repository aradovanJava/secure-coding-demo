package com.example.securecoding.controller;

import com.example.securecoding.model.Comment;
import jakarta.validation.Valid;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

/**
 * Glavni kontroler koji demonstrira razliku između ranjive i sigurne
 * implementacije rukovanja korisničkim unosom.
 * 
 * VAŽNO ZA PREZENTACIJU:
 * - Ranjiva verzija NE filtrira korisnički unos
 * - Sigurna verzija koristi validaciju i sanitizaciju
 */
@Controller
public class CommentController {

    // Privremeno spremište komentara (u stvarnoj aplikaciji bi bila baza podataka)
    private final List<Comment> vulnerableComments = new ArrayList<>();
    private final List<Comment> secureComments = new ArrayList<>();

    @Autowired
    private PolicyFactory htmlSanitizer;

    /**
     * ═══════════════════════════════════════════════════════════════
     *                    POČETNA STRANICA
     * ═══════════════════════════════════════════════════════════════
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * ═══════════════════════════════════════════════════════════════
     *                    RANJIVA VERZIJA - XSS RANJIVOST!
     * ═══════════════════════════════════════════════════════════════
     * 
     * PROBLEMI:
     * 1. NE koristi @Valid - preskače validaciju
     * 2. NE sanitizira korisnički unos
     * 3. Thymeleaf th:utext (unescaped text) izvršava JavaScript!
     * 
     * NAPAD ZA DEMONSTRACIJU:
     * Ime: <script>alert('XSS Napad!')</script>
     * Komentar: <img src=x onerror="alert('Hakiran!')">
     */
    @GetMapping("/vulnerable")
    public String showVulnerableForm(Model model) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", vulnerableComments);
        return "vulnerable";
    }

    @PostMapping("/vulnerable")
    public String submitVulnerableComment(
            @ModelAttribute Comment comment,  // ❌ BEZ @Valid - nema validacije!
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // ❌ GREŠKA: Direktno spremanje bez ikakve provjere ili sanitizacije!
        vulnerableComments.add(comment);
        
        redirectAttributes.addFlashAttribute("message", 
            "Komentar dodan (ali NIJE siguran!)");
        
        return "redirect:/vulnerable";
    }

    /**
     * ═══════════════════════════════════════════════════════════════
     *                    SIGURNA VERZIJA - ZAŠTIĆENA OD XSS-a
     * ═══════════════════════════════════════════════════════════════
     * 
     * ZAŠTITE:
     * 1. Koristi @Valid - provjerava Jakarta Bean Validation anotacije
     * 2. Sanitizira HTML korištenjem OWASP HTML Sanitizer
     * 3. Thymeleaf th:text (escaped text) sprječava izvršavanje JS-a
     * 4. Server-side validacija (NIKAD ne vjeruj frontendu!)
     */
    @GetMapping("/secure")
    public String showSecureForm(Model model) {
        model.addAttribute("comment", new Comment());
        model.addAttribute("comments", secureComments);
        return "secure";
    }

    @PostMapping("/secure")
    public String submitSecureComment(
            @Valid @ModelAttribute Comment comment,  // ✅ @Valid aktivira validaciju!
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        // ✅ ZAŠTITA 1: Provjera validacijskih grešaka
        if (bindingResult.hasErrors()) {
            model.addAttribute("comments", secureComments);
            return "secure";
        }
        
        // ✅ ZAŠTITA 2: Sanitizacija HTML-a (uklanja <script>, onclick, itd.)
        String safeName = htmlSanitizer.sanitize(comment.getName());
        String safeContent = htmlSanitizer.sanitize(comment.getContent());
        
        // ✅ ZAŠTITA 3: Dodatna provjera - blokiranje JavaScript protokola
        if (containsJavaScriptProtocol(safeName) || containsJavaScriptProtocol(safeContent)) {
            model.addAttribute("error", "Nije dozvoljen JavaScript kod!");
            model.addAttribute("comments", secureComments);
            return "secure";
        }
        
        // Spremanje sanitiziranog komentara
        Comment sanitizedComment = new Comment(safeName, safeContent);
        secureComments.add(sanitizedComment);
        
        redirectAttributes.addFlashAttribute("message", 
            "✅ Komentar siguran i uspješno dodan!");
        
        return "redirect:/secure";
    }

    /**
     * Helper metoda za detekciju javascript: protokola
     */
    private boolean containsJavaScriptProtocol(String input) {
        if (input == null) return false;
        String lower = input.toLowerCase().replaceAll("\\s+", "");
        return lower.contains("javascript:") || 
               lower.contains("data:text/html") ||
               lower.contains("vbscript:");
    }

    /**
     * Endpoint za resetiranje komentara (za demo svrhe)
     */
    @PostMapping("/reset")
    public String resetComments(RedirectAttributes redirectAttributes) {
        vulnerableComments.clear();
        secureComments.clear();
        redirectAttributes.addFlashAttribute("message", "Svi komentari su obrisani!");
        return "redirect:/";
    }
}
