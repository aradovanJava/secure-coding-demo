# 🛡️ Secure Coding Demo - XSS Zaštita u Spring Boot

## 📋 O projektu

Ova aplikacija je edukacijski primjer koji demonstrira **XSS (Cross-Site Scripting)** ranjivost i metode zaštite u Spring Boot aplikaciji. Aplikacija sadrži dvije verzije:

1. **Ranjiva verzija** - pokazuje kako NE implementirati validaciju podataka
2. **Sigurna verzija** - pokazuje pravilnu implementaciju sigurnosnih mjera

## 🎯 Cilj prezentacije

Pokazati polaznicima:
- Kako funkcionira XSS napad
- Zašto je server-side validacija kritična
- Kako implementirati višeslojnu zaštitu
- Razliku između `th:text` i `th:utext` u Thymeleafu
- Korištenje OWASP Java HTML Sanitizer biblioteke

## 🔧 Tehnologije

- **Java:** 17
- **Framework:** Spring Boot 3.2.0
- **Template Engine:** Thymeleaf
- **Validacija:** Jakarta Bean Validation
- **Sanitizacija:** OWASP Java HTML Sanitizer
- **Build Tool:** Maven
- **IDE:** IntelliJ IDEA (preporučeno)

## 📦 Struktura projekta

```
secure-coding-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/securecoding/
│   │   │       ├── SecureCodingDemoApplication.java
│   │   │       ├── config/
│   │   │       │   └── HtmlSanitizerConfig.java
│   │   │       ├── controller/
│   │   │       │   └── CommentController.java
│   │   │       └── model/
│   │   │           └── Comment.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── style.css
│   │       └── templates/
│   │           ├── index.html
│   │           ├── vulnerable.html
│   │           └── secure.html
├── pom.xml
└── README.md
```

## 🚀 Pokretanje aplikacije

### Preduvjeti
- Java 17 ili novija verzija
- Maven 3.6+ (ili koristi Maven wrapper)
- IntelliJ IDEA (preporučeno)

### Pokretanje u IntelliJ IDEA

1. **Otvori projekt:**
   - File → Open
   - Odaberi `secure-coding-demo` direktorij
   - Klikni OK

2. **Pričekaj Maven import:**
   - IntelliJ će automatski preuzeti dependencije
   - Pričekaj dok se ne završi indexiranje

3. **Pokreni aplikaciju:**
   - Pronađi `SecureCodingDemoApplication.java`
   - Desni klik → Run 'SecureCodingDemoApplication'
   - Ili pritisni `Shift + F10`

4. **Otvori browser:**
   - http://localhost:8080

### Pokretanje iz terminala

```bash
# Navigiraj u direktorij projekta
cd secure-coding-demo

# Ako koristiš Maven wrapper
./mvnw spring-boot:run

# Ili sa instaliranim Mavenom
mvn spring-boot:run
```

## 🎪 Demo scenarij za prezentaciju

### 1️⃣ Uvod (5 min)
- Otvori početnu stranicu: http://localhost:8080
- Objasni razliku između dvije verzije
- Pokaži strukturu projekta u IntelliJ-u

### 2️⃣ Demonstracija ranjive verzije (10 min)

**Otvori:** http://localhost:8080/vulnerable

**Payload primjeri za demonstraciju:**

```html
<!-- Payload 1: Osnovni alert -->
<script>alert('XSS Napad uspješan!')</script>

<!-- Payload 2: Image onerror -->
<img src=x onerror="alert('Aplikacija je hakirana!')">

<!-- Payload 3: Cookie theft (simulacija) -->
<script>alert('Cookie: ' + document.cookie)</script>

<!-- Payload 4: DOM manipulation -->
<img src=x onerror="document.body.style.backgroundColor='red'">
```

**Prikaži kod u IntelliJ-u:**

1. Otvori `CommentController.java`
2. Pokaži ranjivi `@PostMapping("/vulnerable")` metod
3. Naglasi:
   - ❌ Nema `@Valid` anotacije
   - ❌ Nema sanitizacije
   - ❌ Direktno spremanje u listu

4. Otvori `vulnerable.html`
5. Pokaži `th:utext` korištenje:
   ```html
   <p th:utext="${comment.content}"></p>
   ```
6. Objasni da `utext` = "unescaped text" izvršava HTML/JavaScript

### 3️⃣ Demonstracija sigurne verzije (15 min)

**Otvori:** http://localhost:8080/secure

**Pokušaj iste payloade:**
- Pokaži da ne rade
- Prikaži kako se payload prikazuje kao običan tekst

**Prikaži kod zaštite u IntelliJ-u:**

1. **Model validacija** - `Comment.java`:
```java
@NotBlank(message = "Ime ne smije biti prazno")
@Size(min = 2, max = 50)
private String name;
```

2. **Controller zaštita** - `CommentController.java`:
```java
@PostMapping("/secure")
public String submitSecureComment(
    @Valid @ModelAttribute Comment comment,  // ✅ @Valid
    BindingResult bindingResult) {
    
    // Provjera grešaka
    if (bindingResult.hasErrors()) {
        return "secure";
    }
    
    // HTML sanitizacija
    String safeName = htmlSanitizer.sanitize(comment.getName());
    String safeContent = htmlSanitizer.sanitize(comment.getContent());
    
    // ...
}
```

3. **HTML Sanitizer konfiguracija** - `HtmlSanitizerConfig.java`:
```java
@Bean
public PolicyFactory htmlSanitizer() {
    return new HtmlPolicyBuilder()
        .allowElements("p", "br", "strong", "em")
        .allowElements("ul", "ol", "li")
        // Whitelist pristup - sve ostalo je blokirano!
        .toFactory();
}
```

4. **Template zaštita** - `secure.html`:
```html
<!-- ✅ th:text escapea HTML -->
<p th:text="${comment.content}"></p>
```

### 4️⃣ Ključne lekcije (5 min)

Naglasi polaznicima:

1. **NIKAD ne vjeruj korisnikom unosu**
   - Frontend validacija se može zaobići
   - Uvijek validiraj na serveru

2. **Višeslojna zaštita (Defense in Depth)**
   - Input validacija (@Valid)
   - HTML sanitizacija (OWASP)
   - Output escaping (th:text)
   - Dodatne custom provjere

3. **Thymeleaf razlike:**
   - `th:text` → escapea HTML (SIGURNO)
   - `th:utext` → izvršava HTML (OPASNO)

4. **OWASP princip:**
   - Whitelist pristup (što je dozvoljeno)
   - Ne blacklist pristup (što je zabranjeno)

## 🔒 Sigurnosne mjere implementirane

### 1. Jakarta Bean Validation
```java
@NotBlank(message = "Ime ne smije biti prazno")
@Size(min = 2, max = 50, message = "Ime mora imati između 2 i 50 znakova")
private String name;
```

### 2. OWASP HTML Sanitizer
```java
String safeName = htmlSanitizer.sanitize(comment.getName());
```

### 3. Output Escaping
```html
<!-- Sigurno -->
<p th:text="${comment.content}"></p>

<!-- NESIGURNO -->
<p th:utext="${comment.content}"></p>
```

### 4. Custom Validacija
```java
private boolean containsJavaScriptProtocol(String input) {
    if (input == null) return false;
    String lower = input.toLowerCase().replaceAll("\\s+", "");
    return lower.contains("javascript:") || 
           lower.contains("data:text/html");
}
```

## 📚 Dodatni resursi

- [OWASP XSS Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [Thymeleaf Security](https://www.thymeleaf.org/doc/articles/springsecurity.html)
- [OWASP Java HTML Sanitizer](https://github.com/OWASP/java-html-sanitizer)

## 🎓 Zadaci za polaznike

Nakon prezentacije, polaznici mogu:

1. **Vježba 1:** Dodaj dodatne payloade i testiraj zaštitu
2. **Vježba 2:** Implementiraj Content Security Policy (CSP) header
3. **Vježba 3:** Dodaj rate limiting za sprječavanje spam napada
4. **Vježba 4:** Implementiraj perzistenciju s bazom podataka

## ⚠️ Važne napomene

- ❗ **Ranjiva verzija je namjerno nesigurna** - NIKAD je ne koristiti u produkciji!
- ❗ **Ova demo aplikacija je isključivo za edukacijske svrhe**
- ❗ **U produkciji UVIJEK implementirati sve sigurnosne mjere**
- ❗ **Koristiti Spring Security za dodatnu zaštitu**

## 🤝 Kontakt

Za pitanja i sugestije vezane uz prezentaciju, kontaktirajte predavača.

## 📄 Licenca

Ovaj projekt je kreiran isključivo za edukacijske svrhe.

---

**Sretno s prezentacijom! 🚀**
