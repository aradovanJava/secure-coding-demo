# 🚀 Brzi Start - Secure Coding Demo

## Za prezentaciju u IntelliJ IDEA

### 1. Otvori projekt (30 sekundi)
```
1. Pokreni IntelliJ IDEA
2. File → Open
3. Odaberi 'secure-coding-demo' folder
4. Klikni OK
5. Pričekaj Maven sync (Progress bar na dnu)
```

### 2. Pokreni aplikaciju (10 sekundi)
```
1. Pronađi: src/main/java/.../SecureCodingDemoApplication.java
2. Desni klik na fajl
3. Klikni: "Run 'SecureCodingDemoApplication.main()'"
4. Ili pritisni: Shift + F10
```

### 3. Otvori u browseru
```
http://localhost:8080
```

---

## 🎯 Demo flow za prezentaciju

### Ranjiva verzija (5 min)
1. Otvori: http://localhost:8080/vulnerable
2. Kopiraj payload: `<script>alert('XSS!')</script>`
3. Zalijepi u polje "Komentar"
4. Klikni "Pošalji"
5. **Rezultat:** Pojavljuje se alert box! ⚠️

### Prikaži ranjivi kod (3 min)
**CommentController.java - linija ~45:**
```java
@PostMapping("/vulnerable")
public String submitVulnerableComment(
    @ModelAttribute Comment comment,  // ❌ BEZ @Valid
    ...) {
    vulnerableComments.add(comment);  // ❌ Direktno spremanje
    return "redirect:/vulnerable";
}
```

**vulnerable.html - linija ~65:**
```html
<p th:utext="${comment.content}"></p>  <!-- ❌ OPASNO! -->
```

### Sigurna verzija (5 min)
1. Otvori: http://localhost:8080/secure
2. Pokušaj isti payload: `<script>alert('XSS!')</script>`
3. Klikni "Pošalji"
4. **Rezultat:** Payload se prikazuje kao tekst! ✅

### Prikaži siguran kod (5 min)
**CommentController.java - linija ~95:**
```java
@PostMapping("/secure")
public String submitSecureComment(
    @Valid @ModelAttribute Comment comment,  // ✅ SA @Valid
    BindingResult bindingResult, ...) {
    
    if (bindingResult.hasErrors()) return "secure";
    
    // ✅ Sanitizacija
    String safeName = htmlSanitizer.sanitize(comment.getName());
    String safeContent = htmlSanitizer.sanitize(comment.getContent());
    
    // ✅ Dodatna provjera
    if (containsJavaScriptProtocol(...)) {
        return "secure";
    }
    ...
}
```

**secure.html - linija ~65:**
```html
<p th:text="${comment.content}"></p>  <!-- ✅ SIGURNO! -->
```

---

## 💡 Ključne točke za istaknuti

### 1. Tri sloja zaštite:
```
1️⃣ Input Validation  → @Valid + Bean Validation
2️⃣ Sanitization      → OWASP HTML Sanitizer
3️⃣ Output Escaping   → th:text (umjesto th:utext)
```

### 2. Thymeleaf razlika:
```html
th:text  → Escapea HTML (SIGURNO)  ✅
th:utext → Izvršava HTML (OPASNO) ❌
```

### 3. Whitelist pristup:
```java
// Dozvoli samo: p, br, strong, em
// SVE OSTALO je automatski blokirano!
new HtmlPolicyBuilder()
    .allowElements("p", "br", "strong", "em")
    .toFactory();
```

---

## 🎪 Bonus demo - Ekstremni primjer

Za dramatičan efekt, pokažite ovaj payload na ranjivoj verziji:

```html
<img src=x onerror="document.body.innerHTML='<h1 style=color:red>HAKIRAN!</h1>'">
```

**Rezultat:** Cijela stranica se zamijeni s crvenim tekstom "HAKIRAN!" 😱

---

## ⚡ Troubleshooting

**Port 8080 zauzet?**
```
Promijeni port u application.properties:
server.port=8081
```

**Maven ne preuzima dependencije?**
```
Terminal → mvn clean install
```

**Hot reload ne radi?**
```
File → Settings → Build → Compiler
☑ Build project automatically
```

---

## 📝 Payloadi za testiranje

Kopiraj-zalijepi direktno u formu:

```html
<!-- Alert Box -->
<script>alert('XSS')</script>

<!-- Image Attack -->
<img src=x onerror="alert('Hakiran!')">

<!-- Cookie Theft -->
<script>alert(document.cookie)</script>

<!-- Style Manipulation -->
<img src=x onerror="document.body.style.backgroundColor='red'">

<!-- Full Page Takeover -->
<img src=x onerror="document.body.innerHTML='<h1>OWNED!</h1>'">
```

---

**Spremno za prezentaciju! 🎉**

*Tip: Zatvori sve druge aplikacije i otvori IntelliJ na full screen za profesionalan dojam.*
