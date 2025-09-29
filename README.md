# Planty - Android Aplikace pro Správu Úkolů

Android aplikace pro správu rutin a úkolů postavená na moderním Kotlin stacku.

## Technologie

- **Kotlin** s **Jetpack Compose** pro deklarativní UI
- **MVVM architektura** s **Dagger Hilt** dependency injection
- **Room databáze** s Entity-Relationship modelem
- **Material Design 3** a **Navigation Compose**
- **Kotlin Coroutines**, **DataStore**, **Sentry** monitoring

## Architektura

MVVM pattern s Repository vrstvou, čisté oddělení odpovědností, dependency injection a reaktivní UI s Compose.

## Klíčové Funkce

Správa objektů s obrázky, automatické generování úkolů na základě rutin, sledování pokroku se streak systémem, komplexní statistiky a lokalizace.

## Sestavení a Spuštění

```bash
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

**Požadavky**: Android SDK 26+, Kotlin 1.9.0+
