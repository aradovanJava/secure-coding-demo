# 🛡️ Security Best Practices - XSS Prevention

Sveobuhvatni vodič za prevenciju XSS napada u Java Spring Boot aplikacijama.

---

## 📋 Table of Contents

1. [Input Validation](#input-validation)
2. [Output Encoding](#output-encoding)
3. [Content Security Policy](#content-security-policy)
4. [Framework Zaštite](#framework-zaštite)
5. [OWASP Preporuke](#owasp-preporuke)
6. [Code Review Checklist](#code-review-checklist)
7. [Testing Strategije](#testing-strategije)

---

## 1. Input Validation

### ✅ Što Raditi

#### Backend Validacija (OBAVEZNO!)

```java
// Bean Validation
public class Comment {
    @NotBlank(message = "Sadržaj ne može biti prazan")
    @Size(min = 5, max = 500, message = "Sadržaj mora biti 5-500 znakova")
    @Pattern(regexp = "^[a-zA-Z0-9\\s.,!?-]*$", 
             message = "Nedozvoljeni znakovi")
    private String content;
}
```

#### Whitelist Pristup

```java
// Dozvoli samo poznate znakove
public boolean isValidInput(String input) {
    // Whitelist: slova, brojevi, osnovni interpunkcija
    return input.matches("^[a-zA-Z0-9\\s.,!?-]+$");
}
```

#### Blacklist Pristup (manje siguran)

```java
// Blokiraj poznate opasne pattern-e
public boolean containsDangerousContent(String input) {
    String[] dangerous = {
        "<script", "javascript:", "onerror=", 
        "onload=", "<iframe", "eval("
    };
    
    for (String pattern : dangerous) {
        if (input.toLowerCase().contains(pattern)) {
            return true;
        }
    }
    return false;
}
```

**⚠️ Napomena:** Blacklist je lako zaobići! Whitelist je bolji pristup.

---

### ❌ Što NE Raditi

```java
// ❌ LOŠE: Samo frontend validacija
<input type="text" required minlength="5">
// User može zaobići s Developer Tools ili curl

// ❌ LOŠE: Vjerovanje client-side validaciji
if (request.getParameter("validated").equals("true")) {
    // User može poslati bilo što s "validated=true"
}

// ❌ LOŠE: Nepotpuna validacija
if (input.length() > 0) {
    // Provjerava samo duljinu, ne sadržaj!
}
```

---

## 2. Output Encoding

### ✅ HTML Context Encoding

#### Thymeleaf (Preporučeno)

```html
<!-- ✅ DOBRO: th:text automatski escape-a -->
<span th:text="${userInput}"></span>

<!-- ❌ LOŠE: th:utext ne escape-a -->
<span th:utext="${userInput}"></span>
```

#### Manual Encoding

```java
import org.apache.commons.text.StringEscapeUtils;

public String encodeForHTML(String input) {
    return StringEscapeUtils.escapeHtml4(input);
}

// Rezultat:
// "<script>" → "&lt;script&gt;"
// "&" → "&amp;"
// "'" → "&#39;"
```

---

### ✅ JavaScript Context Encoding

```html
<!-- ✅ DOBRO: Encode za JS context -->
<script>
    var userName = /*[[${@jsEncoder.encode(userName)}]]*/;
</script>
```

```java
// Custom JS Encoder
public String encodeForJavaScript(String input) {
    return input
        .replace("\\", "\\\\")
        .replace("'", "\\'")
        .replace("\"", "\\\"")
        .replace("<", "\\x3C")
        .replace(">", "\\x3E");
}
```

---

### ✅ URL Context Encoding

```java
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public String encodeForURL(String input) {
    return URLEncoder.encode(input, StandardCharsets.UTF_8);
}

// Rezultat:
// "hello world" → "hello+world"
// "<script>" → "%3Cscript%3E"
```

---

## 3. Content Security Policy

### ✅ Implementacija CSP

#### Meta Tag (Dev/Testing)

```html
<meta http-equiv="Content-Security-Policy" 
      content="default-src 'self'; 
               script-src 'self'; 
               style-src 'self' 'unsafe-inline'; 
               img-src 'self' data: https:; 
               font-src 'self'; 
               connect-src 'self'; 
               frame-ancestors 'none';">
```

#### HTTP Header (Production - Preporučeno)

**Spring Security Configuration:**

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; " +
                                    "script-src 'self'; " +
                                    "style-src 'self' 'unsafe-inline'; " +
                                    "img-src 'self' data: https:; " +
                                    "font-src 'self'; " +
                                    "connect-src 'self'; " +
                                    "frame-ancestors 'none';")
                )
            );
        return http.build();
    }
}
```

---

### 🎯 CSP Direktive Objašnjenje

| Direktiva | Opis | Primjer |
|-----------|------|---------|
| `default-src` | Fallback za sve ostalo | `default-src 'self'` |
| `script-src` | Odakle se mogu učitati skripte | `script-src 'self' cdn.example.com` |
| `style-src` | Odakle se mogu učitati style-ovi | `style-src 'self' 'unsafe-inline'` |
| `img-src` | Odakle se mogu učitati slike | `img-src 'self' data: https:` |
| `font-src` | Odakle se mogu učitati fontovi | `font-src 'self' fonts.google.com` |
| `connect-src` | AJAX/WebSocket destinacije | `connect-src 'self' api.example.com` |
| `frame-ancestors` | Ko može embedati stranicu | `frame-ancestors 'none'` |

**Vrijednosti:**
- `'self'` - Samo vlastiti origin
- `'none'` - Ništa nije dozvoljeno
- `'unsafe-inline'` - Dozvoli inline skripte/style (IZBJEGAVATI!)
- `https:` - Sve HTTPS URL-ove
- `data:` - Data URL-ove (za base64 slike)

---

## 4. Framework Zaštite

### ✅ Thymeleaf Best Practices

```html
<!-- ✅ DOBRO: Automatski escaping -->
<p th:text="${comment}"></p>

<!-- ✅ DOBRO: Escaping u atributima -->
<input type="text" th:value="${userInput}">

<!-- ✅ DOBRO: Escaping u URL -->
<a th:href="@{/search(q=${query})}">Search</a>

<!-- ❌ LOŠE: Unescaped content -->
<div th:utext="${richText}"></div>
<!-- Koristi samo za TRUSTED sadržaj! -->
```

---

### ✅ React Best Practices

```jsx
// ✅ DOBRO: React automatski escape-a
function Comment({ text }) {
    return <div>{text}</div>;
}

// ❌ LOŠE: dangerouslySetInnerHTML
function Comment({ html }) {
    return <div dangerouslySetInnerHTML={{__html: html}} />;
    // NIKAD za user input!
}

// ✅ DOBRO: Ako MORAŠ koristiti HTML
import DOMPurify from 'dompurify';

function SafeHTML({ html }) {
    const clean = DOMPurify.sanitize(html);
    return <div dangerouslySetInnerHTML={{__html: clean}} />;
}
```

---

### ✅ Angular Best Practices

```typescript
// ✅ DOBRO: Angular automatski sanitizira
@Component({
  template: `<div>{{userInput}}</div>`
})

// ✅ DOBRO: Bypass samo kad je SIGURNO
import { DomSanitizer } from '@angular/platform-browser';

constructor(private sanitizer: DomSanitizer) {}

getSafeHtml(html: string) {
  // Prvo očisti s DOMPurify ili slično
  const clean = DOMPurify.sanitize(html);
  return this.sanitizer.bypassSecurityTrustHtml(clean);
}
```

---

## 5. OWASP Preporuke

### ✅ OWASP Java HTML Sanitizer

```java
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

@Service
public class SanitizationService {
    
    // Policy za forumske komentare
    private final PolicyFactory commentPolicy = 
        Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.LINKS);
    
    // Policy za rich text (više dozvoljeno)
    private final PolicyFactory richTextPolicy = 
        Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.IMAGES)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.STYLES)
            .and(Sanitizers.TABLES);
    
    // Minimalna policy (samo plain text)
    private final PolicyFactory plainTextPolicy = 
        Sanitizers.FORMATTING;
    
    public String sanitizeComment(String input) {
        if (input == null) return "";
        return commentPolicy.sanitize(input);
    }
    
    public String sanitizeRichText(String input) {
        if (input == null) return "";
        return richTextPolicy.sanitize(input);
    }
    
    public String sanitizeToPlainText(String input) {
        if (input == null) return "";
        return plainTextPolicy.sanitize(input);
    }
}
```

---

### ✅ Custom Policy

```java
import org.owasp.html.*;

// Kreiranje custom policy-ja
PolicyFactory customPolicy = new HtmlPolicyBuilder()
    // Dozvoli samo određene tagove
    .allowElements("p", "br", "b", "i", "u", "a")
    
    // Dozvoli atribute
    .allowAttributes("href").onElements("a")
    .requireRelNofollowOnLinks()  // Dodaj rel="nofollow"
    
    // URL protokoli
    .allowUrlProtocols("https")
    
    // Styling
    .allowAttributes("class").matching(Pattern.compile("^(highlight|bold)$"))
        .onElements("span")
    
    .toFactory();
```

---

## 6. Code Review Checklist

### 🔍 Što Tražiti u Code Review-u

#### Backend

- [ ] **Svi user inputi su validirani?**
  ```java
  // Treba: @Valid, @NotBlank, @Size, custom validacija
  ```

- [ ] **User input je sanitiziran prije spremanja?**
  ```java
  // Treba: OWASP Sanitizer ili equivalent
  ```

- [ ] **Nema direktnog spajanja stringova za SQL?**
  ```java
  // ❌ LOŠE: "SELECT * FROM users WHERE id=" + userId
  // ✅ DOBRO: PreparedStatement ili JPA
  ```

- [ ] **Output encoding prema kontekstu?**
  ```java
  // HTML, JS, URL - različiti encoding!
  ```

---

#### Frontend/Templates

- [ ] **Koristi se `th:text` umjesto `th:utext`?**
  ```html
  <!-- Osim za explicitly trusted content -->
  ```

- [ ] **React: Nema `dangerouslySetInnerHTML` s user inputom?**
  ```jsx
  // Samo s DOMPurify sanitized content
  ```

- [ ] **Angular: Nema `bypassSecurityTrust*` s user inputom?**
  ```typescript
  // Samo s DOMPurify sanitized content
  ```

- [ ] **Inline event handleri su izbjegnuti?**
  ```html
  <!-- ❌ LOŠE: onclick="..." -->
  <!-- ✅ DOBRO: addEventListener -->
  ```

---

#### Headers/Configuration

- [ ] **CSP header je postavljen?**
  ```java
  // Spring Security ili manual header
  ```

- [ ] **X-XSS-Protection header?**
  ```
  X-XSS-Protection: 1; mode=block
  ```

- [ ] **X-Content-Type-Options header?**
  ```
  X-Content-Type-Options: nosniff
  ```

- [ ] **X-Frame-Options header?**
  ```
  X-Frame-Options: DENY
  ```

---

## 7. Testing Strategije

### ✅ Manual Testing

#### Basic XSS Payloads

```html
<!-- Alert tests -->
<script>alert('XSS')</script>
<img src=x onerror="alert('XSS')">
<svg onload="alert('XSS')">

<!-- Event handlers -->
<body onload="alert('XSS')">
<div onmouseover="alert('XSS')">

<!-- Encoding bypasses -->
<script>alert(String.fromCharCode(88,83,83))</script>
<img src="javascript:alert('XSS')">

<!-- Case variations -->
<ScRiPt>alert('XSS')</ScRiPt>
<img SRC=x onerror="alert('XSS')">
```

---

### ✅ Automated Testing

#### OWASP ZAP

```bash
# Instalacija
sudo apt-get install zaproxy

# Scan
zap-cli quick-scan --self-contained http://localhost:8080
```

---

#### Burp Suite

1. Postavi kao proxy
2. Spider aplikaciju
3. Run Active Scan
4. Provjeri XSS findings

---

#### Unit Testovi

```java
@Test
public void testXSSPrevention() {
    String malicious = "<script>alert('XSS')</script>";
    
    Comment comment = new Comment();
    comment.setContent(malicious);
    
    Comment saved = commentService.saveSecureComment(comment);
    
    // Script tag ne smije biti prisutan
    assertFalse(saved.getSanitizedContent().contains("<script>"));
    assertFalse(saved.getSanitizedContent().contains("alert"));
}

@Test
public void testEncodingIsApplied() {
    String input = "<b>Bold</b>";
    Comment comment = new Comment();
    comment.setContent(input);
    
    Comment saved = commentService.saveSecureComment(comment);
    
    // OWASP policy dozvoljava <b>, ali ne <script>
    assertTrue(saved.getSanitizedContent().contains("<b>"));
}
```

---

## 📚 Dodatni Resursi

### OWASP Dokumentacija
- [XSS Prevention Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html)
- [DOM based XSS Prevention](https://cheatsheetseries.owasp.org/cheatsheets/DOM_based_XSS_Prevention_Cheat_Sheet.html)
- [Content Security Policy](https://cheatsheetseries.owasp.org/cheatsheets/Content_Security_Policy_Cheat_Sheet.html)

### Tools
- [OWASP ZAP](https://www.zaproxy.org/)
- [Burp Suite](https://portswigger.net/burp)
- [DOMPurify](https://github.com/cure53/DOMPurify)
- [OWASP Java HTML Sanitizer](https://github.com/OWASP/java-html-sanitizer)

### Training
- [OWASP WebGoat](https://owasp.org/www-project-webgoat/)
- [HackTheBox](https://www.hackthebox.com/)
- [PortSwigger Academy](https://portswigger.net/web-security)

---

## 🎯 Quick Reference

### Three Rules of XSS Prevention

1. **NEVER trust user input** - Validate everything
2. **ALWAYS sanitize before storing** - Clean dangerous content
3. **ALWAYS encode on output** - Escape based on context

### Defense in Depth Layers

```
┌─────────────────────────────────────┐
│  Layer 1: Input Validation          │
├─────────────────────────────────────┤
│  Layer 2: Input Sanitization        │
├─────────────────────────────────────┤
│  Layer 3: Output Encoding            │
├─────────────────────────────────────┤
│  Layer 4: Content Security Policy   │
├─────────────────────────────────────┤
│  Layer 5: Framework Protection      │
└─────────────────────────────────────┘
```

---

**Remember:** Security is not a feature, it's a requirement! 🔒

---

**Last Updated:** 2025  
**Version:** 1.0
