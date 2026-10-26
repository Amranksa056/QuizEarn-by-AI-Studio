# From this folder to Google Play

Two phases: **(A) host it over HTTPS**, then **(B) wrap it as a Trusted Web
Activity (TWA)** and upload to Play.

You need: a free Netlify/Vercel account, a JDK (21), and a Google Play developer
account (**$25 one-time**). Everything else is scripted.

---

## A. Deploy the site

```powershell
npm run package      # builds dist/ with only runtime files
```

### Option 1 — Netlify drag & drop (fastest, no CLI)

1. Go to <https://app.netlify.com/drop>
2. Drag the **`dist`** folder onto the page
3. You get a URL like `https://quizearn-abc123.netlify.app`
4. Rename it under *Site configuration → Change site name* (e.g. `quizearn`)

### Option 2 — Netlify CLI

```powershell
npm i -g netlify-cli
netlify login
netlify deploy --dir=dist --prod
```

### Option 3 — Vercel

```powershell
npm i -g vercel
vercel --prod
```
(`vercel.json` already sets the headers; set *Output Directory* to `dist`.)

### Verify the deploy

Open these and confirm:

| URL | Expect |
| --- | --- |
| `https://YOUR-SITE/` | the app loads |
| `https://YOUR-SITE/manifest.webmanifest` | JSON, `application/manifest+json` |
| `https://YOUR-SITE/sw.js` | JavaScript |
| `https://YOUR-SITE/.well-known/assetlinks.json` | JSON (placeholder for now) |

In Chrome DevTools → **Application → Manifest**: no errors, icons all resolve.
**Application → Service Workers**: activated.

> Local sanity check before deploying: `npm run package && npm run preview`

### Firebase before you deploy

If you use the Firebase backend:

1. Paste the web config into `js/firebase-config.js`.
2. `npm run deploy:rules && npm run deploy:functions`.
3. Firebase console → Authentication → **Authorized domains** → add
   `YOUR-SITE.netlify.app` (and any custom domain).
4. Sign in once on the deployed site and confirm coins appear in Firestore
   `users/{uid}`.

> Google sign-in inside a TWA uses the redirect flow. If it fails, the domain is
> missing from **Authorized domains** — that is almost always the cause.

---

## B. Build the Android app (TWA)

### 1. Install the toolchain

```powershell
winget install Microsoft.OpenJDK.21
npm i -g @bubblewrap/cli
```

Bubblewrap downloads the Android SDK itself on first run (accept the prompts).

### 2. Fill in `twa-manifest.json`

Replace every placeholder:

- `packageId` → `com.yourname.quizearn` (permanent — cannot change after publish)
- `host` → `YOUR-SITE.netlify.app`
- `iconUrl`, `maskableIconUrl`, `webManifestUrl`, `fullScopeUrl` → your HTTPS URLs

### 3. Generate the project

```powershell
bubblewrap init --manifest ./twa-manifest.json
```

It will ask for a signing key. Choose **create new** and keep `android.keystore`
somewhere safe — **losing it means you can never update the app**.

### 4. Build the bundle

```powershell
bubblewrap build
```

Outputs `app-release-signed.aab` (upload this) and `app-release-signed.apk`
(sideload to test on your phone first).

### 5. Verify the digital asset link

```powershell
keytool -list -v -keystore android.keystore -alias android
```

Copy the **SHA256** fingerprint, put it in `.well-known/assetlinks.json`,
replace `com.yourname.quizearn`, then redeploy:

```powershell
npm run package
netlify deploy --dir=dist --prod
```

Check it:

```powershell
# should print the package name
curl https://YOUR-SITE/.well-known/assetlinks.json
```

Also test with Google's validator:
<https://developers.google.com/digital-asset-links/tools/generator>

If the asset link is wrong the app still installs, but it opens with a browser
address bar on top — that's the symptom to look for.

### 6. Publish

1. <https://play.google.com/console> → **Create app**
2. Upload `app-release-signed.aab` to **Internal testing** first
3. Complete *App content*: privacy policy URL, data safety form, ads declaration
4. Content rating questionnaire
5. Screenshots (phone: 2–8, min 320px), feature graphic (1024×500), icon (512×512)
6. Roll out to production

---

## ⚠️ Read this before you submit

Your app has a **coins → real money** redemption feature. Play Review treats that
as a rewards/payout product and applies extra scrutiny:

- **Monetary policy** — apps promising money for tasks are frequently rejected or
  removed. Keep redemption clearly described, term-limited, and not the headline
  claim of your listing.
- **No cash-out promises in the store listing** — avoid "earn real money" copy.
- **Privacy policy is mandatory** — it must describe payout data (phone numbers).
- **Data safety** — declare that you collect phone numbers at redemption.
- **Financial features declaration** in Play Console may apply.
- **Bangladesh regulation** — paying users from a game/app can fall under
  money-transmission rules. Get local legal advice before enabling payouts.
- **Play requires server-side integrity.** With `js/firebase-config.js` filled in,
  coins, rounds, revives and payouts are computed by Cloud Functions. The app
  still boots in **local demo mode** when the config is missing — never ship a
  payout build in that mode, because coins live in `localStorage`.
- **Rewarded ads.** The Bullet Quiz revive currently uses a 5-second in-app
  placeholder (`watchRewardedAd()` in `js/app.js`). Integrate a real rewarded-ad
  SDK and call `reviveBullet()` only from its "reward earned" callback. Complete
  Play Console's **ads declaration** once real ads are present.

**Safer launch path:** ship v1 with coins + leaderboard only (no cash payout),
build an audience, then add redemption after legal review.

---

## Checklist

- [ ] `npm run package` runs clean
- [ ] Site live on HTTPS, manifest + SW verified
- [ ] `packageId` / `host` / URLs replaced in `twa-manifest.json`
- [ ] Keystore created and backed up **off-device**
- [ ] SHA256 fingerprint in `assetlinks.json`, redeployed, validated
- [ ] APK sideloaded and tested (no browser bar, offline works)
- [ ] Play Console app created, internal testing uploaded
- [ ] Privacy policy URL live
- [ ] Content rating + data safety + ads declaration completed
- [ ] Rewarded-ad SDK integrated (Bullet revive placeholder replaced)
- [ ] Screenshots + feature graphic ready
