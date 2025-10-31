# 🎤 Skripta za Prezentaciju - Secure Coding Demo

Detaljne bilješke i scenarij za održavanje prezentacije o XSS ranjivostima.

---

## 📅 Pregled Prezentacije

**Trajanje:** 45-60 minuta  
**Publika:** Web developeri, studenti, IT sigurnost  
**Cilj:** Naučiti kako prepoznati i spriječiti XSS napade

---

## 🎯 Struktura Prezentacije

### **Dio 1: Uvod (5-7 minuta)**

#### Slide 1: Dobrodošlica
"Dobro došli! Danas ćemo govoriti o jednoj od najčešćih sigurnosnih ranjivosti u web aplikacijama - **Cross-Site Scripting** ili kratko **XSS**."

#### Slide 2: Što je XSS?
"XSS je vrsta napada gdje napadač **ubacuje maliciozni JavaScript** u web stranicu koja se zatim **izvršava u browseru drugih korisnika**."

**Prikaži statistiku:**
- XSS je u **OWASP Top 10** već godinama
- 40%+ web aplikacija ima neku vrstu XSS ranjivosti
- Može dovesti do krađe credentials, session hijacking, phishing napada

#### Slide 3: Tipovi XSS-a (kratko)
- **Stored XSS** - Maliciozni kod se sprema u bazu (najopasnije)
- **Reflected XSS** - Kod se reflektira nazad kroz URL/formu
- **DOM-based XSS** - JavaScript manipulacija DOM-a

"Mi ćemo danas demonstrirati **Stored XSS** jer je najlakši za razumijevanje."

---

### **Dio 2: Live Demo - Ranjiva Aplikacija (15 minuta)**

#### Setup
```
1. Pokreni aplikaciju (već pokrenuta)
2. Otvori browser na http://localhost:8080
3. Prikaži početnu stranicu
```

#### Govor:
"Napravio sam jednostavnu web aplikaciju za komentare - slično forumu ili društvenoj mreži. Hajmo vidjeti što je ranjivo."

**[Klik na 'Ranjiva Verzija']**

---

#### Demo 1: Normalan Input (2 min)

**Akcija:**
```
Ime: Ivan
Komentar: Ovo je normalan komentar, sve radi kako treba!
```

**Govor:**
"Vidite, aplikacija radi normalno. Frontend ima validaciju - provjerava duljinu teksta, zahtijeva da polja budu popunjena. Sve izgleda sigurno, zar ne?"

**[Submit i prikaži komentar]**

"Ali sada dolazi problem..."

---

#### Demo 2: Prvi XSS Napad (3 min)

**Akcija:**
```
Ime: Hacker
Komentar: <script>alert('XSS Attack!')</script>
```

**Govor:**
"Što ako umjesto normalnog teksta unesem JavaScript kod?"

**[Tipkaj polako, objašnjavaj što tipkaš]**

"Vidite, **frontend validacija prihvaća** ovaj input jer tehnički je unutar dozvoljene duljine. Validation provjerava samo broj znakova, ne provjerava **sadržaj**."

**[Submit]**

**[Kada se prikaži alert]**

"Boom! 💥 JavaScript se izvršio! Ovo je **XSS napad**."

**Pauza za efekt - pusti da se publika iznenadi**

---

#### Demo 3: Opasniji Primjer (5 min)

**Govor:**
"OK, alert je dobar za demonstraciju, ali što napadač stvarno može napraviti?"

**Akcija 1: Cookie Stealing**
```
Komentar: <script>alert('Cookie: ' + document.cookie)</script>
```

**Govor:**
"Napadač može pročitati cookies - što znači **session tokens, authentication data**. S ovim može **preuzeti vaš račun**."

**[Submit i pokaži alert s cookie-jima]**

---

**Akcija 2: DOM Manipulation**
```
Komentar: <script>document.body.innerHTML = '<h1 style="color:red;text-align:center;margin-top:20%">HACKED!</h1>'</script>
```

**Govor:**
"Ili još gore - može **promijeniti cijelu stranicu**. Zamislite da je ovo banking aplikacija..."

**[Submit i čekaj reakciju publike]**

---

**Akcija 3: Fake Login (Opciono ako imaš vremena)**
```
[Koristi kompleksni payload iz XSS_PAYLOADS.md]
```

**Govor:**
"Napadač može napraviti lažni login screen i **ukrasti vaš password**."

---

#### Code Walkthrough - Ranjivi Kod (5 min)

**[Prebaci se u IntelliJ IDEA]**

**Govor:**
"Hajmo pogledati zašto je ovo ranjivo. Otvaram kod..."

**[Otvori `CommentController.java`]**

**Pokazuj i objašnjavaj:**

```java
@PostMapping("/vulnerable/submit")
public String submitVulnerableComment(
        @Valid @ModelAttribute Comment comment,  // ← Samo basic validacija
        BindingResult result,
        RedirectAttributes redirectAttributes) {
    
    if (result.hasErrors()) {  // ← Provjeravamo samo @Valid anotacije
        return "redirect:/vulnerable";
    }
    
    // ⚠️ PROBLEM: Direktno spremamo bez sanitizacije!
    commentService.saveVulnerableComment(comment);
    
    return "redirect:/vulnerable";
}
```

**Govor:**
"Vidite problem? Koristimo `@Valid` što provjerava samo **strukturu** podataka - je li prazno, je li predugačko. Ali **NE provjeravamo sadržaj**!"

---

**[Otvori `CommentService.java`]**

```java
public Comment saveVulnerableComment(Comment comment) {
    // ⚠️ DIREKTNO spremamo - BEZ SANITIZACIJE!
    vulnerableComments.add(comment);
    return comment;
}
```

**Govor:**
"I ovdje u service layeru - samo spremamo. Nikakva provjera, nikakva sanitizacija. JavaScript ide direktno u memoriju."

---

**[Otvori `vulnerable.html`]**

```html
<div th:each="comment : ${comments}" class="comment">
    <!-- ⚠️ th:utext = "unescaped text" -->
    <span th:utext="${comment.content}"></span>
</div>
```

**Govor:**
"I najgore od svega - u Thymeleaf templatu koristimo `th:utext`. 

`utext` znači **unescaped text** - Thymeleaf će prikazati HTML i JavaScript **točno kako jest**, bez enkodiranja.

Ovo je **trojka smrtnih grijeha**:
1. ❌ Nema backend sanitizacije
2. ❌ Nema input validacije sadržaja
3. ❌ Koristi `th:utext` za user input

Rezultat? **XSS ranjivost!**"

---

### **Dio 3: Rješenje - Sigurna Verzija (15 minuta)**

**[Vrati se u browser]**

**Govor:**
"Dobro, vidjeli smo problem. Sada - **kako to ispraviti**?"

**[Klikni na početnu stranicu, pa 'Sigurna Verzija']**

---

#### Demo 4: Testiranje Sigurne Verzije (3 min)

**Akcija:**
```
Ime: Hacker
Komentar: <script>alert('Neće raditi!')</script>
```

**Govor:**
"Pokušat ću ISTI napad..."

**[Submit]**

**[Pokaži rezultat - tekst je enkodiran]**

"Vidite? JavaScript se **NE izvršava**! Prikazuje se kao **obični tekst**."

**[Scrollaj dolje do sekcije 'Prije i Poslije']**

"I pogledajte ovu sekciju - aplikacija **pokazuje što je blokirala**. 

Original: `<script>alert('Neće raditi!')</script>`

Sanitizirano: `[prazan string ili enkodiran tekst]`

Napad je **neutraliziran**!"

---

#### Code Walkthrough - Sigurni Kod (8 min)

**[Prebaci se u IntelliJ]**

**Govor:**
"Hajmo vidjeti kako sam to implementirao. Otvaram kod..."

**[Otvori `CommentService.java` - sigurnu metodu]**

---

**Pokazuj:**

```java
// OWASP HTML Sanitizer Policy
private final PolicyFactory policy = Sanitizers.FORMATTING
        .and(Sanitizers.BLOCKS)
        .and(Sanitizers.LINKS);
```

**Govor:**
"Prvo, koristim **OWASP Java HTML Sanitizer** - industrija standard za HTML čišćenje.

Definirao sam **policy** - što je dozvoljeno:
- ✅ FORMATTING - Bold, italic, itd.
- ✅ BLOCKS - Paragrafi, headeri
- ✅ LINKS - Poveznice

**Sve ostalo se BRIŠE ili ENKODIRA!**"

---

```java
public Comment saveSecureComment(Comment comment) {
    comment.setId(counter.incrementAndGet());
    
    // ✅ SANITIZACIJA - ključni dio!
    String sanitizedUsername = sanitizeInput(comment.getUsername());
    String sanitizedContent = sanitizeInput(comment.getContent());
    
    comment.setUsername(sanitizedUsername);
    comment.setSanitizedContent(sanitizedContent);
    
    secureComments.add(comment);
    return comment;
}

private String sanitizeInput(String input) {
    if (input == null) return "";
    return policy.sanitize(input);  // ← OWASP čisti input
}
```

**Govor:**
"I prije nego spremim komentar, **OBAVEZNO** ga pošaljem kroz sanitizer.

`policy.sanitize()` će:
- Ukloniti sve `<script>` tagove
- Ukloniti event handlere (onclick, onerror...)
- Enkodirati opasne znakove
- Zadržati samo sigurne HTML tagove prema mojoj policy

**Ovo je BACKEND zaštita - uvijek mora biti prisutna!**"

---

**[Otvori `secure.html`]**

```html
<div th:each="comment : ${comments}" class="comment">
    <!-- ✅ th:text = "escaped text" -->
    <span th:text="${comment.sanitizedContent}"></span>
</div>
```

**Govor:**
"I u Thymeleaf templatu - koristim `th:text` umjesto `th:utext`.

`th:text` automatski **enkodira HTML special karaktere**:
- `<` postaje `&lt;`
- `>` postaje `&gt;`
- `&` postaje `&amp;`

Čak i da sanitizer nebi uspio, ovaj drugi sloj bi zaštitio aplikaciju!"

---

**[Pokaži u browseru - Developer Tools - Inspect Element]**

**Govor:**
"I ako pogledamo u browser developer tools..."

**[Inspektiraj komentar element]**

"Vidite da je `<script>` literal tekst, NE izvršni kod. Browser ga tretira kao **plain text**, ne kao JavaScript."

---

#### Dodatne Zaštite (2 min)

**[Vrati se u `secure.html` - head sekcija]**

```html
<meta http-equiv="Content-Security-Policy" 
      content="default-src 'self'; script-src 'self'; ...">
```

**Govor:**
"I još jedna zaštita - **Content Security Policy (CSP)**.

CSP je HTTP header koji browseru govori:
- ❌ Blokiraj inline `<script>` tagove
- ❌ Blokiraj `eval()` i slične opasne funkcije
- ✅ Dozvoli samo skripte iz našeg origina

Ovo je **treći sloj zaštite** - Defense in Depth!"

---

**Rezime:**

"Dakle, sigurna verzija ima **TRI sloja obrane**:

1. **Backend Sanitizacija** - OWASP čisti input
2. **Thymeleaf th:text** - Auto-escaping
3. **Content Security Policy** - Browser level zaštita

Ovo se zove **Defense in Depth** - višeslojni pristup sigurnosti."

---

### **Dio 4: Live Coding - Ispravljanje (10 min)**

**Govor:**
"Sada ću vam pokazati kako bi to izgledalo da morate ispraviti ranjivost u postojećem kodu."

**[Otvori novi fajl ili stvori novu metodu]**

---

#### Korak 1: Dodaj Dependency

**[Otvori `pom.xml`]**

```xml
<!-- OWASP Java HTML Sanitizer -->
<dependency>
    <groupId>com.googlecode.owasp-java-html-sanitizer</groupId>
    <artifactId>owasp-java-html-sanitizer</artifactId>
    <version>20220608.1</version>
</dependency>
```

**Govor:**
"Prvo, dodajemo OWASP dependency u `pom.xml`. Maven će downloadati biblioteku."

---

#### Korak 2: Kreiraj Sanitizer

**[U Service klasi]**

**Tipkaj uživo:**

```java
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

// Na vrhu klase
private final PolicyFactory policy = Sanitizers.FORMATTING
        .and(Sanitizers.BLOCKS);
```

**Govor:**
"Kreiramo policy - što dozvoljavamo. Za komentare obično trebamo samo basic formatting."

---

#### Korak 3: Implementiraj Sanitizaciju

**Tipkaj:**

```java
private String sanitizeInput(String input) {
    if (input == null) {
        return "";
    }
    return policy.sanitize(input);
}
```

**Govor:**
"Helper metoda koja će sanitizirati string. Provjera za null, pa sanitizacija."

---

#### Korak 4: Primjeni na Input

**Tipkaj:**

```java
public Comment saveComment(Comment comment) {
    // Sanitiziraj sve user input-e!
    String cleanUsername = sanitizeInput(comment.getUsername());
    String cleanContent = sanitizeInput(comment.getContent());
    
    comment.setUsername(cleanUsername);
    comment.setContent(cleanContent);
    
    comments.add(comment);
    return comment;
}
```

**Govor:**
"Prije spremanja, **svi** user input-i idu kroz sanitizer. Ovo je obavezno!"

---

#### Korak 5: Promijeni Template

**[Otvori HTML]**

**Tipkaj promjenu:**

```html
<!-- PRIJE: -->
<span th:utext="${comment.content}"></span>

<!-- POSLIJE: -->
<span th:text="${comment.content}"></span>
```

**Govor:**
"I u templatu - **utext** mijenjamo u **text**. Jedna riječ, velika razlika!"

---

#### Korak 6: Testiraj!

**[Vrati se u browser, restart app ako treba]**

**Govor:**
"I sada testiramo... [submit XSS payload] ...i vidite, **radi!** Zaštita je aktivna."

---

### **Dio 5: Best Practices i Q&A (10-15 min)**

#### Best Practices Slide

**Govor:**
"Prije nego završimo, hajmo rezimirati **best practices** za prevenciju XSS-a:"

**Objasni svaki point:**

1. **✅ NIKAD ne vjeruj user input-u**
   - Svi inputi su potencijalno opasni
   - Uvijek validiraj I sanitiziraj

2. **✅ Koristi framework zaštite**
   - Thymeleaf `th:text`
   - React automatski escape-a
   - Angular ima DomSanitizer

3. **✅ Sanitiziraj na backendu**
   - Frontend validacija je lako zaobići (F12 Dev Tools)
   - Backend MORA sanitizirati

4. **✅ Koristi OWASP biblioteke**
   - Industrija standard
   - Testirano i provjereno

5. **✅ Implementiraj CSP**
   - Dodatni sloj zaštite
   - Moderan browser-i ga podržavaju

6. **✅ Encode prema kontekstu**
   - HTML context: `&lt;` `&gt;`
   - URL context: `%3C` `%3E`
   - JS context: `\x3C` `\x3E`

7. **✅ Minimalno privilegija**
   - Dozvoli samo ono što treba
   - Default: sve blokiraj

8. **✅ Redovni security audit**
   - OWASP ZAP
   - Burp Suite
   - Manual code review

---

#### Česte Greške

**Govor:**
"I česte greške koje programeri prave:"

**❌ Greška 1:** "Frontend validacija je dovoljna"
- **Odgovor:** NE! User može zaobići frontend (curl, Postman, Dev Tools)

**❌ Greška 2:** "Koristim HTTPS, siguran sam"
- **Odgovor:** HTTPS štiti transport, ne štiti od XSS-a

**❌ Greška 3:** "Moja aplikacija je mala, nitko me neće napasti"
- **Odgovor:** Boti skeniraju SVE aplikacije, veličina nije važna

**❌ Greška 4:** "Sanitiziram samo username, content je OK"
- **Odgovor:** SVA polja moraju biti sanitizirana!

---

#### Q&A Session

**Očekivana pitanja:**

**Q: "Kada trebam koristiti `th:utext`?"**
- A: Samo kada prikazuješ **trusted content** - sadržaj koji si ti generirao ili verified admin content. NIKAD za user input.

**Q: "Je li dovoljno samo backend sanitizacija?"**
- A: Ne! Defense in Depth - trebaju ti multiple layeri. Backend + Frontend + CSP.

**Q: "Što ako trebam dozvoliti neki HTML u komentarima?"**
- A: Koristi OWASP Sanitizer s policy-jem koji dozvoljava samo sigurne tagove (b, i, u, a href). Nikad ne dozvoli script, iframe, object, embed.

**Q: "Kako testirati svoju aplikaciju na XSS?"**
- A: Koristi OWASP ZAP, Burp Suite, ili manual testing s XSS payloadima. Također code review.

**Q: "Čemu služi CSP ako imam sanitizaciju?"**
- A: Defense in Depth! Ako sanitizer ima bug, CSP te štiti. Višeslojni pristup!

**Q: "Što je s stored vs reflected XSS?"**
- A: Stored (kao naš primjer) sprema se u bazu i pogađa sve. Reflected se šalje kroz URL i pogađa jednog usera. Stored je opasniji ali obrana je ista!

---

### **Dio 6: Zaključak (3 min)**

**Govor:**
"Dakle, što smo naučili danas?"

**Rezime:**

"✅ **XSS je ozbiljna prijetnja** - može kompromitirati cijele aplikacije

✅ **Frontend validacija nije dovoljna** - lako se zaobilazi

✅ **Backend sanitizacija je obavezna** - koristite OWASP biblioteke

✅ **Framework podrška je vaš prijatelj** - Thymeleaf th:text, React auto-escape

✅ **Defense in Depth** - više slojeva zaštite je bolje

✅ **Security je continuous proces** - nije one-time fix"

---

**Završni savjet:**

"I na kraju - **security nije opcija, to je obaveza**.

Kad programirate, uvijek razmišljajte:
- 'Što ako korisnik pošalje maliciozni input?'
- 'Jesam li sanitizirao sve input-e?'
- 'Koristim li sigurne metode prikaza?'

**Think like an attacker, code like a defender!**"

---

**Call to Action:**

"Sav kod s ove prezentacije možete naći na:
- GitHub: [link]
- Dokumentacija: README.md
- XSS payloadi za vježbu: XSS_PAYLOADS.md

Testirajte sami, vježbajte, i primijenite u svojim projektima!"

---

**Završne riječi:**

"Hvala vam na pažnji! Imate li još pitanja?"

**[Q&A i networking]**

---

## 📝 Dodatne Bilješke za Prezentera

### Timeboxing
- **Ne prekoračuj 60 minuta** - respektiraj vrijeme publike
- Ako kasniš, preskoči "Live Coding" dio
- Q&A uvijek ostavi bar 10 minuta

### Engagement
- **Pitaj publiku** između dijelova: "Je li netko već vidio XSS napad?"
- **Daj im task**: "Pokušajte smisliti XSS payload dok ja pripremam sljedeći slide"
- **Napravi pauzu** nakon velikog wow momenta

### Technical Issues Backup
- Ako app ne radi: Imaš screenshotove?
- Ako nema interneta: Demo je local, nema problema
- Ako IntelliJ se crasha: Imaš kod u text editoru?

### Energy Management
- **Varriraj ton glasa** - ne monotono
- **Gestikuliraj** kad objašnjavaš importante points
- **Pokaži entuzijazam** - energija je zarazna!

---

**Sretno! 🎉🚀**
