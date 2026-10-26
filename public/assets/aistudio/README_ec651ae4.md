# QuizEarn

An offline-first **quiz + coins** progressive web app. Play the fast **Bullet
Quiz** or a category round, build streaks, earn coins, spend them on lifelines,
and redeem rewards.

No framework and no build step — HTML, CSS and ES modules, plus the Firebase SDK
loaded from Google's CDN when you configure it.

## Run it

```bash
npm start          # http://localhost:5173
```

Then open the URL on your phone (same Wi-Fi) or in a desktop browser. Turn on
"Add to Home screen" / the install icon in Chrome to run it as a standalone app.

> The service worker only registers over `http://localhost` or `https://`.
> Opening `index.html` straight from disk works, but offline install does not.

### Scripts

| Command | What it does |
| --- | --- |
| `npm start` | dev server on port 5173 |
| `npm run package` | builds a deployable `dist/` (runtime files only) |
| `npm run preview` | serves `dist/` on port 5173 |
| `npm run check:pwa` | manifest/icon/service-worker pre-flight for the store |
| `npm run check:i18n` | every translation key exists in both languages |
| `npm run smoke` | headless jsdom test of the whole flow |
| `npm run icons` | regenerates the PNG icons |
| `npm run seed` | uploads `js/questions.js` to Firestore (needs a service account) |
| `npm run set-admin -- you@mail.com` | grants the `admin: true` custom claim |
| `npm run emulate` | Firebase emulators (auth + firestore + functions) |
| `npm run deploy:rules` | deploys `firestore.rules` + indexes |
| `npm run deploy:functions` | deploys the Cloud Functions |

## Firebase backend (accounts + server-verified coins)

The app ships with **two modes**:

| Mode | When | Coins |
| --- | --- | --- |
| Local demo | `js/firebase-config.js` still has `PASTE_` values | localStorage, not secure |
| Firebase | real config + signed-in user | Firestore, written only by Cloud Functions |

Nothing else changes — `js/backend.js` picks the provider at boot.

### 1. Paste your web config (the only file you edit)

Firebase console → **Project settings → Your apps → Web app (`</>`)** → copy the
`firebaseConfig` object into **`js/firebase-config.js`**:

```js
export const firebaseConfig = {
  apiKey: 'AIza...',
  authDomain: 'quizearn-xxxx.firebaseapp.com',
  projectId: 'quizearn-xxxx',
  storageBucket: 'quizearn-xxxx.appspot.com',
  messagingSenderId: '1234567890',
  appId: '1:1234567890:web:abcdef',
};
```

Also set `functionsRegion` to the region you deploy to (`us-central1` by default).
These keys are **public by design** — security comes from the rules and functions,
not from hiding them.

### 2. Enable sign-in methods

Authentication → **Sign-in method**:

- **Google** → enable, pick a support email.
- **Email/Password** → enable.

Authentication → **Settings → Authorized domains** must contain the domain that
serves the app (e.g. `quizearn.netlify.app`). `localhost` is there by default.

> Inside a TWA, Google sign-in falls back from popup to redirect automatically
> (`js/backend/auth.js`). The domain still has to be authorised.

### 3. Firestore + rules

Create a Firestore database (production mode), then:

```bash
npm i -g firebase-tools
firebase login
firebase use --add            # pick your project, alias "default"
npm run deploy:rules          # firestore.rules + firestore.indexes.json
```

What the rules enforce:

- a user can read only their own `users/{uid}` doc;
- the client may update only `name`, `theme`, `lang`, `haptics`, `payout*`, `photoURL`, `lastActive`;
- **`coins`, stats, lifelines, streaks are not writable by the client at all**;
- `questions` is readable (answers are not in it); `question_answers` and `rounds` are server-only;
- `redeem_requests` / `activity` are readable by their owner (or an admin);
- admins are identified by the `admin: true` custom claim.

### 4. Functions + questions

```bash
cd functions
npm install
cd ..

# service account: console → Project settings → Service accounts → Generate key
$env:GOOGLE_APPLICATION_CREDENTIALS = "C:\path\service-account.json"
$env:FIREBASE_PROJECT_ID = "quizearn-xxxx"
npm run seed

npm run deploy:functions
```

`npm run seed` writes 120 questions to `questions/{id}` and the correct option
index to `question_answers/{id}`. The answer never reaches the browser.

> If you add a question by hand in the Firebase console, add a matching
> `question_answers/{same-id}` doc with `{ "answer": <0-based index> }`.
> `startRound` skips any question that has no valid answer key.

### 5. Make yourself admin

```bash
npm run set-admin -- you@example.com
```

Sign out and back in (or reload) so the new ID token includes the claim. Then
open `#admin` — the sheet now reads real users, activity and payouts from Firestore.

### How the anti-cheat works

| Cheat | Why it fails now |
| --- | --- |
| `localStorage.coins = 999999` | balances live in Firestore; rules reject client writes |
| Edit the answer in DevTools | answers are not sent; `submitAnswer` compares against `question_answers` |
| Pause the timer / answer late | `submitAnswer` checks server time against `currentStartedAt` (5 s bullet / 8 s category + 5 s grace) |
| Replay a correct answer | each round advances `currentIndex`; out-of-order calls are rejected |
| Request payout twice | `requestRedemption` runs in a transaction and deducts coins first |
| Approve your own payout | `decideRedemption` requires the `admin` claim |
| Farm the daily bonus | one bonus per server day, inside a transaction |

### What is still not production-ready

- **KYC / AML** and a real payment rail (bKash/Nagad merchant API) before any payout.
- **Rate limits** — add an App Check + per-user round cap for public traffic.
- **Legal review** — Google Play restricts reward/payout apps; money transmission
  is regulated in Bangladesh.
- **Unit economics** — at 5,000 coins = ৳1,000, a perfect Bullet round (200 coins) pays ৳40.

## What's included

| Area | Details |
| --- | --- |
| Bullet Quiz | 20 questions, 5 s each, flat 10 points per correct answer |
| Bullet strikes | Wrong or timed-out answers add a strike; at 3 strikes you watch a rewarded ad to clear them or forfeit the round's coins |
| Quiz engine | 120 questions, 6 categories (General, Science, Technology, Geography, Sports, Current Affairs) + the Bullet Quiz, shuffled questions **and** shuffled options |
| Scoring | Bullet: flat 10 coins per correct answer. Category: 10 coins per correct answer, +2 per answer in a streak (capped at +20), +50 perfect-round bonus |
| Timer | 5 s per Bullet question / 8 s per category question, animated bar, auto-fail on timeout |
| Feedback | Only the option you picked is marked; a wrong answer or timeout never reveals the correct option |
| Daily streak | +25 coins once per calendar day, day-streak counter (shown in Profile, not Home) |
| Lifelines | 50/50, Skip, +10 s — category rounds only; each usable from inventory or bought with coins mid-round |
| Home banner | Reserved banner-ad slot under the hero (`#banner-ad`), ready for an ad network |
| Play banner | Second reserved banner slot after the category grid on the Play screen (`#play-banner-ad`, `data-ad-slot="play-banner"`) |
| Accounts | Google sign-in + email/password (Firebase Auth), guest mode without config |
| Server truth | Coins, streaks, lifelines, strikes, revives and payouts are computed in Cloud Functions transactions |
| Reward hub | Five voucher categories, coin balance, lifetime earnings, redemption progress and payout requests |
| Reward logos | bKash, Nagad, PayPal and mobile-recharge brand marks on every reward card |
| Profile | Stats, rename, referral code, light/dark theme and language switch |
| Invite friends | Personal referral code with copy and native share/copy fallback |
| Languages | English + বাংলা — UI strings *and* all 120 questions translated |
| Redemption | Five reward categories, manual admin approval, coins held on request |
| Admin sheet | All users, per-user activity log, payout queue, CSV export, `admin` claim gate |
| PWA | Manifest, maskable icons, offline shell caching, safe-area layout |

### Bullet Quiz rules

| Rule | Value |
| --- | --- |
| Questions per round | 20 |
| Time per question | 5 seconds |
| Correct answer | +10 points |
| Wrong / timed-out answer | 0 points + 1 strike |
| Strikes allowed | 3 |
| At 3 strikes | Watch a rewarded ad to clear strikes, or be disqualified |
| Disqualified | The round's coins and its stats are forfeited |

Strikes accumulate across the whole round (a correct answer does not clear
them). Watching the ad resets strikes to 0 and restarts the question clock.
Category rounds use 20 questions at 8 seconds each with streak scoring,
and lifelines are disabled in Bullet Quiz.

> **Rewarded-ad placeholder:** `watchRewardedAd()` in `js/app.js` is a 5-second
> in-app countdown so the flow is testable. Replace it with your ad SDK
> (AdMob/Unity/etc.) and call `reviveBullet()` only from the SDK's
> "reward earned" callback. Server-side, `reviveRound` clears the strikes and
> `forfeitRound` reverses the round's coins and stats.

Keyboard shortcuts on desktop: `1-4` or `a-d` to answer.

## Project layout

```
quizearn/
├── index.html              app shell (top bar, view container, tab bar)
├── css/styles.css          theme tokens + all component styles
├── js/
│   ├── app.js              routing, screens, quiz engine, Bullet strikes, lifelines
│   ├── auth-screen.js      sign-in UI + profile account card
│   ├── backend.js          picks local demo vs Firebase at boot
│   ├── backend/
│   │   ├── firebase.js     lazy Firebase SDK loader
│   │   ├── auth.js         Google + email/password helpers
│   │   ├── cloud.js        callable function wrappers
│   │   └── firebase-store.js  Firestore data layer
│   ├── firebase-config.js  ← paste your Firebase web config here
│   ├── admin.js            user data sheet, activity log, CSV export
│   ├── redeem.js           coin -> money request screen
│   ├── i18n.js             English + বাংলা dictionaries, t() helper
│   ├── questions.js        question bank (en + bn) + round builder
│   └── store.js            local demo data layer (localStorage)
├── functions/
│   ├── index.js            server-verified rounds, coins, redemptions
│   ├── package.json
│   └── scripts/
│       ├── seed.js         uploads the question bank
│       └── set-admin.js    grants the admin custom claim
├── firestore.rules         security rules (client cannot write coins)
├── firestore.indexes.json  composite indexes
├── firebase.json           functions + emulator config
├── .firebaserc             project alias
├── sw.js                   service worker (offline-first)
├── manifest.webmanifest    installable PWA metadata
├── .well-known/
│   └── assetlinks.json     digital asset link for the Android app (TWA)
├── icons/                  generated PNG icons
├── netlify.toml            hosting headers (Netlify)
├── vercel.json             hosting headers (Vercel)
├── twa-manifest.json       template for `bubblewrap init`
├── STORE.md                step-by-step Play Store playbook
├── dist/                   deploy output from `npm run package`
└── tools/
    ├── make-icons.mjs      dependency-free PNG icon generator
    ├── serve.mjs           zero-dependency dev server
    ├── package.mjs         builds dist/
    ├── check-pwa.mjs       manifest/icons/SW pre-flight
    ├── check-i18n.mjs      verifies every t() key exists in both languages
    └── smoke.mjs           headless DOM test (npm i -D jsdom)
```

## Deploying / Play Store

See **[STORE.md](./STORE.md)** for the full walkthrough: `npm run package` →
drag `dist/` to Netlify → verify with `npm run check:pwa` → `bubblewrap init` →
build the `.aab` → upload to Play Console.

`npm run check:pwa` is the gate before every deploy; it validates the manifest,
icon dimensions, precache list and flags any remaining `yourname` / `your-site`
placeholders.

## Localization

Language is detected from `navigator.language` on first run, then stored in
progress (`lang`) and switchable from **Profile → Language**.

- **UI strings** live in `js/i18n.js` as flat `'key': 'value'` maps; `t()` falls
  back to English and supports `{placeholders}`.
- **Questions** carry a parallel `bn: { q, options }`. Because the Bangla option
  array keeps the English order, the `answer` index stays valid and options can
  still be shuffled after localization (verified by `tools/check-i18n.mjs`).

Adding a third language is ~3 steps: add the dictionary, add `{ id, label, short }`
to `LANGUAGES`, and (optionally) add a `bn`-style block per question.

## Redemption + admin sheet

**Rules (configurable in `js/store.js`):**

```js
export const REWARD_OPTIONS = [
  { id: 'cash_1000',    coins: 5000, amount: 1000, currency: 'BDT', kind: 'cash' },
  { id: 'recharge_150', coins: 1000, amount: 150,  currency: 'BDT', kind: 'recharge' },
  { id: 'recharge_250', coins: 2000, amount: 250,  currency: 'BDT', kind: 'recharge' },
  { id: 'cash_500',     coins: 3000, amount: 500,  currency: 'BDT', kind: 'cash' },
  { id: 'paypal_10',    coins: 8000, amount: 10,   currency: 'USD', kind: 'paypal' },
];
```

| Reward | Points | Payout details |
| --- | ---: | --- |
| ৳1,000 bKash/Nagad voucher | 5,000 | bKash/Nagad number |
| ৳150 mobile recharge voucher | 1,000 | Mobile number |
| ৳250 mobile recharge voucher | 2,000 | Mobile number |
| ৳500 bKash/Nagad voucher | 3,000 | bKash/Nagad number |
| $10 PayPal voucher | 8,000 | PayPal email (international only) |

- A user picks a reward on the Reward page, then submits the payout details it needs.
- On request the reward's points are **held immediately** (deducted) so they cannot
  be spent twice, and a `pending` row is written to the redemption ledger.
- An admin approves (→ `paid`, points stay deducted) or rejects (→ `rejected`,
  points **refunded** automatically).

> Brand logos (bKash, Nagad, PayPal) are trademarks of their respective owners
> and are used here only to identify the voucher types.
- Every event — round, daily bonus, purchase, redemption — is appended to the
  activity log with a timestamp.

**Admin sheet** (Profile → *Admin data sheet*):

- Summary: users, coins held, pending payout value
- Redemption queue with **Approve & paid** / **Reject**
- Users table: balance, lifetime earned, rounds, accuracy, last active
- Click any row to expand that user's recent activity + payout history
- Live search by name
- CSV export: `users.csv`, `activity.csv`, `redemptions.csv`
- *Grant 5,000 coins to me* — demo tool so you can test the flow instantly

Data lives in these localStorage keys: `quizearn.users.v1`,
`quizearn.activity.v1`, `quizearn.redemptions.v1`, plus `quizearn.progress.v1`
for the current device player. With Firebase configured, the same records live in
`users`, `activity` and `redeem_requests` instead.

> ### ⚠️ Before this touches real money
>
> With Firebase configured the coin ledger is server-side, but this is still not
> a payout product. Remaining work:
>
> 1. **KYC / AML** — collect identity for payouts, keep an auditable ledger.
> 2. **Payment rails** — bKash/Nagad merchant API or a payout provider; never
>    store raw wallet numbers without encryption.
> 3. **Fraud controls** — App Check, device fingerprints, rate limits, cap per
>    user/day, manual review above a threshold.
> 4. **Legal + Play policy review** — Google Play restricts rewards/payout apps,
>    and money transmission is regulated in Bangladesh.
> 5. **Unit economics** — at 5,000 coins = ৳1,000, every correct answer pays
>    ৳2, a perfect Bullet round pays ৳40 and a 20/20 category round pays ~৳108.
>    Add revenue (ads, subscriptions, sponsored quizzes) or lower the rate
>    before scaling.


## Data layer

`js/backend.js` exposes one `api` object with async methods. `js/store.js` is the
local demo provider; `js/backend/firebase-store.js` is the Firestore provider.
`js/app.js` only imports `api`, so the UI is provider-agnostic.

| Call | Local demo | Firebase |
| --- | --- | --- |
| `loadProgress` / `saveProgress` | localStorage | Firestore `users/{uid}` |
| `startRound` / `submitAnswer` / `finishRound` | not used (client engine) | Cloud Functions |
| `reviveRound` / `forfeitRound` | not used (client engine) | Cloud Functions |
| `buyLifeline` / `useLifeline` | local state | Cloud Functions |
| `requestRedemption` / `decideRedemption` | localStorage | Cloud Functions |
| `getLeaderboard` | localStorage | Firestore `leaderboard` |

## Putting it on the Play Store

A PWA can ship to Google Play as a Trusted Web Activity:

```bash
npm i -g @bubblewrap/cli
bubblewrap init --manifest https://your-domain/manifest.webmanifest
bubblewrap build
```

You will need the site served over HTTPS with a valid `assetlinks.json`.

## Next ideas

- Daily challenge with a fixed question set
- Achievement badges, coin multipliers for perfect categories
- App Check + per-user round caps for public traffic
- Rewarded ads with server-side verification (SSV) — the Bullet revive is a placeholder today
- Sound effects and haptics toggles
- More questions / a third language
