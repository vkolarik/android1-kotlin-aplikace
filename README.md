# Planty - Android Task Management App

## 📱 O projektu

**Planty** je moderní Android aplikace pro správu rutin a úkolů napsaná v Kotlinu. Uživatelé vytvářejí objekty (rostliny, zvířata, atd.), přiřazují k nim rutiny a sledují jejich plnění s automatickým generováním úkolů a statistikami.

## 🛠️ Technologie

- **Kotlin** + **Jetpack Compose** (moderní deklarativní UI)
- **MVVM architektura** s **Dagger Hilt** (dependency injection)
- **Room databáze** s Entity-Relationship modelem
- **Material Design 3** + **Navigation Compose**
- **Kotlin Coroutines** + **DataStore** + **Sentry** monitoring

## 🏗️ Architektura

```
├── database/     # Room databáze a DAO
├── di/          # Dagger Hilt moduly  
├── model/       # Entity-Relationship model
├── ui/          # Jetpack Compose UI
├── logic/       # Business logika
└── navigation/  # Typově bezpečná navigace
```

## ✨ Klíčové funkce

- **Správa objektů** s obrázky a popisy
- **Automatické generování úkolů** na základě rutin
- **Sledování streak** (počet dní bez vynechání)
- **Kompletní statistiky** a progress tracking
- **Lokalizace** (čeština/angličtina)

## 🎯 Proč je to dobré pro CV

- **Moderní Android stack** - nejnovější technologie a best practices
- **Čistá architektura** - MVVM, Repository pattern, DI
- **Komplexní datový model** - Room s cizími klíči a relacemi
- **Profesionální UI/UX** - Material Design 3, responsive design
- **Production-ready** - error handling, monitoring, testování

## 🚀 Spuštění

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Požadavky**: Android SDK 26+, Kotlin 1.9.0+
