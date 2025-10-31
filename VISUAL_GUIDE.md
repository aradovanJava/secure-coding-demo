# 🎨 Visual Guide - Kako Izgleda Aplikacija

Ovaj dokument pruža vizualni pregled aplikacije.

---

## 🏠 Početna Stranica

```
┌────────────────────────────────────────────────────┐
│                                                    │
│          🔒 Secure Coding Demo                     │
│     XSS (Cross-Site Scripting) Demonstration      │
│                                                    │
│  ┌──────────────────────────────────────────────┐ │
│  │  ⚠️ Edukacijski Primjer                      │ │
│  │  Ova aplikacija demonstrira XSS ranjivost    │ │
│  │  u web aplikacijama. Koristi se isključivo   │ │
│  │  u edukacijske svrhe...                      │ │
│  └──────────────────────────────────────────────┘ │
│                                                    │
│  ┌─────────────────┐  ┌─────────────────────┐    │
│  │  ⚠️ RANJIVA     │  │  ✅ SIGURNA         │    │
│  │    VERZIJA      │  │     VERZIJA         │    │
│  │                 │  │                     │    │
│  │  Frontend       │  │  Backend sanitizira │    │
│  │  validacija     │  │  sve podatke        │    │
│  │  postoji, ali   │  │                     │    │
│  │  backend NE     │  │  Thymeleaf koristi  │    │
│  │  sanitizira     │  │  th:text za siguran │    │
│  │                 │  │  prikaz             │    │
│  │                 │  │                     │    │
│  │  [Testiraj]     │  │  [Vidi Sigurnu]     │    │
│  └─────────────────┘  └─────────────────────┘    │
│                                                    │
│  🎯 Kako Testirati XSS Napad:                     │
│  • Unesi: <script>alert('XSS!')</script>          │
│  • Ili: <img src=x onerror="alert('Hacked!')">    │
│  • Primijetit ćeš da se JavaScript izvršava       │
│                                                    │
└────────────────────────────────────────────────────┘
```

---

## ⚠️ Ranjiva Verzija - Forma

```
┌────────────────────────────────────────────────────┐
│  ⚠️ RANJIVA VERZIJA                                │
│  [XSS VULNERABILITY PRESENT]                       │
│                                                    │
│  ← Povratak na početnu                             │
│                                                    │
│  ┌──────────────────────────────────────────────┐ │
│  │  🔴 Aktivne Ranjivosti:                      │ │
│  │  • Backend NE sanitizira korisnički input    │ │
│  │  • Thymeleaf koristi th:utext                │ │
│  │  • NEMA Content Security Policy              │ │
│  └──────────────────────────────────────────────┘ │
│                                                    │
│  ┌─────────────  Dodaj Komentar ────────────────┐ │
│  │                                               │ │
│  │  Korisničko ime:                              │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │ Ivan                                     │ │ │
│  │  └─────────────────────────────────────────┘ │ │
│  │  * Frontend validacija: 2-50 znakova          │ │
│  │                                               │ │
│  │  Komentar:                                    │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │ <script>alert('XSS!')</script>          │ │ │
│  │  │                                          │ │ │
│  │  └─────────────────────────────────────────┘ │ │
│  │  * Frontend validacija: 5-500 znakova         │ │
│  │                                               │ │
│  │  [ Dodaj Komentar ]                           │ │
│  │                                               │ │
│  │  💉 Pokušaj XSS Napad:                        │ │
│  │  <script>alert('XSS Attack!')</script>        │ │
│  │  <img src=x onerror="alert('Image XSS!')">    │ │
│  └───────────────────────────────────────────────┘ │
│                                                    │
│  ────────────  Komentari ─────────────            │
│                                                    │
│  ┌─────────────────────────────────────────────┐  │
│  │ Ivan (#1)                                    │  │
│  │ [JavaScript se izvršio i pojavio alert!] 💥 │  │
│  └─────────────────────────────────────────────┘  │
│                                                    │
└────────────────────────────────────────────────────┘
```

---

## ✅ Sigurna Verzija - Forma

```
┌────────────────────────────────────────────────────┐
│  ✅ SIGURNA VERZIJA                                │
│  [PROTECTED AGAINST XSS]                           │
│                                                    │
│  ← Povratak na početnu                             │
│                                                    │
│  ┌──────────────────────────────────────────────┐ │
│  │  🛡️ Aktivne Sigurnosne Zaštite:             │ │
│  │  ✅ Backend sanitizacija s OWASP Sanitizer   │ │
│  │  ✅ Thymeleaf koristi th:text (auto-escape)  │ │
│  │  ✅ Content Security Policy header           │ │
│  └──────────────────────────────────────────────┘ │
│                                                    │
│  ┌─────────────  Dodaj Komentar ────────────────┐ │
│  │                                               │ │
│  │  Korisničko ime:                              │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │ Hacker                                   │ │ │
│  │  └─────────────────────────────────────────┘ │ │
│  │  * Validacija: 2-50 znakova (F+B)            │ │
│  │                                               │ │
│  │  Komentar:                                    │ │
│  │  ┌─────────────────────────────────────────┐ │ │
│  │  │ <script>alert('Neće raditi!')</script>  │ │ │
│  │  │                                          │ │ │
│  │  └─────────────────────────────────────────┘ │ │
│  │  * Validacija: 5-500 znakova (F+B+S)         │ │
│  │                                               │ │
│  │  [ Dodaj Komentar ]                           │ │
│  │                                               │ │
│  │  🔐 Višeslojni Sustav Zaštite:                │ │
│  │  Layer 1: Frontend Validacija                 │ │
│  │  Layer 2: Backend Validacija                  │ │
│  │  Layer 3: Sanitizacija (OWASP)                │ │
│  │  Layer 4: Siguran Prikaz (th:text)            │ │
│  │  Layer 5: CSP Header                          │ │
│  └───────────────────────────────────────────────┘ │
│                                                    │
│  ────────────  Komentari ─────────────            │
│                                                    │
│  ┌─────────────────────────────────────────────┐  │
│  │ Hacker (#1)                                  │  │
│  │ [Tekst je prikazan kao plain text - sigurno]│  │
│  │ <script>alert('Neće raditi!')</script>      │  │
│  └─────────────────────────────────────────────┘  │
│                                                    │
│  🔍 Usporedba: Prije i Poslije Sanitizacije       │
│  ┌─────────────────────────────────────────────┐  │
│  │ Originalni input:                            │  │
│  │ <script>alert('Neće raditi!')</script>      │  │
│  │                                              │  │
│  │ Sanitizirani output:                         │  │
│  │ [prazan ili očišćen tekst]                  │  │
│  └─────────────────────────────────────────────┘  │
│                                                    │
└────────────────────────────────────────────────────┘
```

---

## 💻 IntelliJ IDEA - Code View

### Ranjivi Kod

```java
// ⚠️ CommentService.java - RANJIVA METODA
┌────────────────────────────────────────────────────┐
│ public Comment saveVulnerableComment(Comment c) {  │
│     comment.setId(counter.incrementAndGet());      │
│                                                    │
│     // ⚠️ RANJIVOST: Ne sanitiziramo!             │
│     vulnerableComments.add(comment);               │
│                                                    │
│     return comment;                                │
│ }                                                  │
└────────────────────────────────────────────────────┘
```

```html
<!-- ⚠️ vulnerable.html - RANJIVI TEMPLATE -->
┌────────────────────────────────────────────────────┐
│ <div th:each="comment : ${comments}">              │
│     <!-- ⚠️ th:utext = izvršava HTML/JS! -->      │
│     <span th:utext="${comment.content}"></span>    │
│ </div>                                             │
└────────────────────────────────────────────────────┘
```

---

### Siguran Kod

```java
// ✅ CommentService.java - SIGURNA METODA
┌────────────────────────────────────────────────────┐
│ private final PolicyFactory policy =               │
│     Sanitizers.FORMATTING                          │
│         .and(Sanitizers.BLOCKS)                    │
│         .and(Sanitizers.LINKS);                    │
│                                                    │
│ public Comment saveSecureComment(Comment c) {      │
│     comment.setId(counter.incrementAndGet());      │
│                                                    │
│     // ✅ SANITIZIRAMO sve input-e!               │
│     String clean = sanitizeInput(c.getContent());  │
│     comment.setSanitizedContent(clean);            │
│                                                    │
│     secureComments.add(comment);                   │
│     return comment;                                │
│ }                                                  │
│                                                    │
│ private String sanitizeInput(String input) {       │
│     return policy.sanitize(input);                 │
│ }                                                  │
└────────────────────────────────────────────────────┘
```

```html
<!-- ✅ secure.html - SIGURAN TEMPLATE -->
┌────────────────────────────────────────────────────┐
│ <div th:each="comment : ${comments}">              │
│     <!-- ✅ th:text = escape-a HTML znakove! -->   │
│     <span th:text="${comment.sanitized}"></span>   │
│ </div>                                             │
└────────────────────────────────────────────────────┘
```

---

## 🎭 Demonstracija - Prije i Poslije

### PRIJE (Ranjivo)

```
User Input:  <script>alert('XSS!')</script>
            ↓
Backend:     (NE sanitizira)
            ↓
Database:    <script>alert('XSS!')</script>
            ↓
Template:    th:utext (NE escape-a)
            ↓
Browser:     💥 JavaScript SE IZVRŠAVA!
            ↓
Result:      [Alert Box] "XSS!"
```

### POSLIJE (Sigurno)

```
User Input:  <script>alert('XSS!')</script>
            ↓
Backend:     ✅ OWASP Sanitizer
            ↓
Database:    [prazan ili očišćen text]
            ↓
Template:    ✅ th:text (auto-escape)
            ↓
Browser:     ✅ Prikazuje kao plain text
            ↓
Result:      Tekst: "<script>alert('XSS!')</script>"
             (JavaScript se NE izvršava)
```

---

## 📊 Defense in Depth Layers

```
        USER INPUT
            │
            ▼
    ┌───────────────────┐
    │  Layer 1:         │  HTML5 Validation
    │  Frontend         │  (minlength, maxlength, required)
    └────────┬──────────┘
            │
            ▼
    ┌───────────────────┐
    │  Layer 2:         │  @Valid, @NotBlank, @Size
    │  Backend          │  Spring Validation
    │  Validation       │
    └────────┬──────────┘
            │
            ▼
    ┌───────────────────┐
    │  Layer 3:         │  OWASP Java HTML Sanitizer
    │  Sanitization     │  policy.sanitize(input)
    └────────┬──────────┘
            │
            ▼
    ┌───────────────────┐
    │  Layer 4:         │  Thymeleaf th:text
    │  Output           │  Auto HTML Escaping
    │  Encoding         │
    └────────┬──────────┘
            │
            ▼
    ┌───────────────────┐
    │  Layer 5:         │  Content-Security-Policy
    │  CSP Header       │  Blocks Inline Scripts
    └────────┬──────────┘
            │
            ▼
        SAFE OUTPUT
```

---

## 🎯 Key Differences Summary

| Aspect | Ranjiva Verzija | Sigurna Verzija |
|--------|----------------|-----------------|
| Backend Sanitizacija | ❌ Nema | ✅ OWASP Sanitizer |
| Thymeleaf | ❌ th:utext | ✅ th:text |
| CSP Header | ❌ Nema | ✅ Implementiran |
| Validacija | ⚠️ Samo struktura | ✅ Struktura + Sadržaj |
| Output Encoding | ❌ Nema | ✅ Auto-escape |
| Slojevi Zaštite | 1 | 5 |
| XSS Otporan | ❌ NE | ✅ DA |

---

## 🚀 Workflow Prezentacije

```
1. Pokaži početnu stranicu
   │
   ├─→ 2a. Ranjiva verzija
   │      ├─ Normalan input (radi)
   │      ├─ XSS payload (radi! 💥)
   │      └─ Pokaži kod u IntelliJ-u
   │
   └─→ 2b. Sigurna verzija
          ├─ Isti XSS payload (ne radi ✅)
          ├─ Pokaži "prije/poslije"
          ├─ Pokaži kod u IntelliJ-u
          └─ Objasni sve slojeve zaštite
```

---

## 📱 Browser Dev Tools View

### Ranjiva Verzija - Inspected Element

```html
<div class="comment">
    <div class="comment-header">Ivan (#1)</div>
    <div class="comment-content">
        <span>
            <script>alert('XSS!')</script>
            <!-- ⚠️ Stvarni <script> tag u DOM-u! -->
        </span>
    </div>
</div>
```

### Sigurna Verzija - Inspected Element

```html
<div class="comment">
    <div class="comment-header">Hacker (#1)</div>
    <div class="comment-content">
        <span>
            &lt;script&gt;alert('XSS!')&lt;/script&gt;
            <!-- ✅ Enkodiran - plain text, ne izvršni kod -->
        </span>
    </div>
</div>
```

---

## 🎨 Color Coding

U aplikaciji:
- 🔴 **Crvena** - Ranjiva verzija (danger)
- 🟢 **Zelena** - Sigurna verzija (success)
- 🟡 **Žuta** - Upozorenja (warning)
- 🔵 **Plava** - Informacije (info)

---

**Ova vizualna dokumentacija pomaže u razumijevanju kako aplikacija izgleda i funkcionira!**
