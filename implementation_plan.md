# URL Shortener Codebase Audit — Fixes

Audited all 5 Java files. Found a critical bug, missing config, missing schema, and a few cleanup items.

## Issues Found & Fixes

---

### 1. Bug: Wrong Table Name in [URLDao](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/dao/URLDao.java#8-50)

#### [MODIFY] [URLDao.java](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/dao/URLDao.java)

[getLongUrl](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/dao/URLDao.java#32-49) queries from `urls` but [saveUrl](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/dao/URLDao.java#9-31) inserts into `short_urls`. This means lookups always return `null` — redirects will never work.

**Fix:** Change `SELECT long_url FROM urls` → `SELECT long_url FROM short_urls`

---

### 2. Missing DB Schema

#### [NEW] [schema.sql](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/schema.sql)

The `short_urls` table has never been defined anywhere. Create a `schema.sql` at the project root to document and initialize it.

```sql
CREATE TABLE IF NOT EXISTS short_urls (
    id SERIAL PRIMARY KEY,
    long_url TEXT NOT NULL,
    short_code VARCHAR(10) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);
```

---

### 3. Missing `exec-maven-plugin` in [pom.xml](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/pom.xml)

#### [MODIFY] [pom.xml](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/pom.xml)

Without this plugin, `mvn exec:java` fails. Also need to specify the `mainClass` so Maven knows the entry point.

---

### 4. [App.java](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/App.java) in Default Package + Indentation

#### [MODIFY] [App.java](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/App.java)

- Move to `com.shortener` package (move file to `com/shortener/App.java`)
- Fix inconsistent indentation in the `/:code` route handler

---

### 5. Delete [DBTest.java](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/dao/DBTest.java)

#### [DELETE] [DBTest.java](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/dao/DBTest.java)

This is a one-off debug file sitting in [main](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/App.java#5-36) source. It shouldn't be shipped.

---

### 6. Collision Check in [ShortenerService](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/service/ShortenerService.java#6-33)

#### [MODIFY] [ShortenerService.java](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/service/ShortenerService.java)

Add a retry loop in [shortenURL](file:///c:/Users/Mudit%20Garg/Desktop/Code/java_url_sortener/url-shortner/src/main/java/com/shortener/service/ShortenerService.java#10-15) — if the generated code already exists in the DB (UNIQUE constraint violation), regenerate and retry.

---

## Verification Plan

### After changes:

1. Run `mvn compile` — should compile with 0 errors
2. Push the schema to Neon by running `schema.sql` (copy-paste into Neon's SQL editor or use `psql`)
3. Run the app: `mvn exec:java -Dexec.mainClass="com.shortener.App"`
4. In a browser, POST to shorten: `curl -X POST "http://localhost:4567/shorten?url=https://google.com"`
5. Visit the returned short URL in browser — should redirect to google.com
