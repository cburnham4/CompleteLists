# Google Play Data Safety Responses

**App:** Complete List (`letshangllc.completelists`)  
**Version:** 3.0.0 (versionCode 100)  
**Last Updated:** November 2026  
**Target SDK:** 36  
**Play status:** Removed by Google (missing Data safety form)

This document provides responses for the Google Play Console Data Safety section.

---

## Overview

Complete List is a local checklist / shopping-list style app. List and item data is stored only in an on-device SQLite database. The app displays AdMob banners and uses Google’s User Messaging Platform (UMP) for consent. There is no account system, cloud sync, analytics SDK, or Crashlytics in this build.

---

## Data Collection Summary

| Data Type | Collected | Shared | Purpose |
|-----------|-----------|--------|---------|
| App interactions | Yes (via AdMob) | Yes (AdMob) | Advertising |
| Device or other IDs (Advertising ID) | Yes (via AdMob) | Yes (AdMob) | Advertising |
| List / item content | No (stays on device) | No | App functionality (local only) |
| Location | No | No | — |
| Personal info (name, email, etc.) | No | No | — |

---

## Detailed Data Types

### 1. App Activity — App interactions
- **Collected:** Yes, by Google Mobile Ads / AdMob when ads are requested
- **Shared:** Yes, with Google advertising partners
- **Purpose:** Advertising
- **Optional:** Subject to UMP consent in applicable regions
- **Encrypted in transit:** Yes (HTTPS; cleartext disallowed via network security config)

### 2. Device or other IDs — Advertising ID
- **Collected:** Yes, via Google AdMob
- **Shared:** Yes, with Google for advertising
- **Purpose:** Advertising
- **User control:** Device settings + UMP consent dialog (EEA/UK and other regulated regions)

### 3. App content (lists / items)
- **Collected by developer / shared:** No
- **Storage:** Local SQLite only (`DatabaseHelper`)
- **Not declared as collected** for Play Data safety (not transmitted off device)

---

## Third-Party SDKs

### Google Mobile Ads (AdMob)
- **Data:** Advertising ID, coarse app/device signals used for ads
- **Purpose:** Banner ads
- **Privacy:** https://policies.google.com/privacy

### Google User Messaging Platform (UMP)
- **Purpose:** GDPR / consent management before personalized ads
- **Data:** Consent preferences
- **Privacy:** https://policies.google.com/privacy

### Removed
- **Amazon Mobile Ads** — removed (legacy SDK; cleartext/HTTP risk)

---

## Permissions (Play declarations)

| Permission | Why | Play note |
|------------|-----|-----------|
| `INTERNET` | Load ads / consent | Standard |
| `ACCESS_NETWORK_STATE` | Ads SDK connectivity checks | Standard |
| `ACCESS_WIFI_STATE` | Legacy ads-related; retained for SDK compatibility | Standard |

No `POST_NOTIFICATIONS`, location, contacts, SMS, or storage permissions are used.

---

## Data Security

- **In transit:** TLS/HTTPS only (`network_security_config` sets `cleartextTrafficPermitted="false"`)
- **At rest:** Local SQLite; not uploaded by this app
- **Deletion:** Uninstalling the app removes local list data. Ad-related identifiers are controlled via device Ad ID / ads settings.

---

## Children’s Privacy / Families

This app is **not** directed at children under 13 (COPPA) or under 16 (GDPR).

- UMP is configured with `setTagForUnderAgeOfConsent(false)`
- If you target Families, you must additionally configure AdMob child-directed / TFUA settings and Play Families policies — **not** enabled in this modernization

---

## Regional Compliance

### GDPR (EU/EEA/UK)
- UMP consent is requested before ads when required
- Non-personalized / no-request behavior follows UMP `canRequestAds()`

### CCPA
- Users can limit ad personalization via device advertising settings

---

## Play Console Form Responses (suggested)

### Does your app collect or share any of the required user data types?
**Yes** (via AdMob)

### Is all of the user data collected by your app encrypted in transit?
**Yes**

### Do you provide a way for users to request that their data is deleted?
**Yes** — uninstall removes local data; contact developer email on the Play listing for ad-related inquiries

### Data types to declare
1. **App interactions** — Collected: Yes; Shared: Yes (AdMob); Purpose: Advertising  
2. **Device or other IDs** — Collected: Yes; Shared: Yes (AdMob); Purpose: Advertising  

Do **not** declare email, name, location, or financial info for this app version.

---

## Owner follow-ups in Play Console

1. Complete the **Data safety** form using this document (this was the removal reason).
2. Declare ads in **App content** / advertising declarations.
3. Confirm AdMob app "Complete List" (`ca-app-pub-8223005482588566~3054663130`) stays linked to package `letshangllc.completelists`.
4. Review **Families** policy if the listing is in Families — ads + UMP settings may need changes.
5. Add release signing via gitignored `keystore.properties` before uploading an AAB/APK.
