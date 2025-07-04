# 🎧 Halo-Themed Soundboard App

A modular Android soundboard inspired by the Halo franchise. Originally built for cosplay groups, it has evolved into a demo-ready booth system supporting ambient loops, audio playback, and interactive feedback.

---

## 📦 Initial Android Build – Audio Assets Excluded

This repository contains the complete Android source code and a fully functional APK build.

> ⚠️ **Audio Assets Notice**  
> Due to copyright restrictions, franchise audio files are excluded from this repository and APK. Playback logic utilizes both `res/raw` and the `assets/` directory.  
> **Audio files must be included at compile time**—they cannot be added dynamically at runtime due to Android's resource system limitations.

---

## 🔧 Technologies Used
- Kotlin
- Android Studio
- Fragment-based architecture
- MediaPlayer API for sound playback
- GitHub Actions CI for automated builds

---

## 📱 Features
- Real-time soundbyte playback via modular button layout  
- Ambient track looping using asset folder resources  
- Sound queues with cooldown mechanics for responsive UI  
- Fragment-based navigation for different soundboard modes  
- GitHub-safe fallback logic for missing or excluded audio

---

## 🚀 Installation

Download the latest APK from the [Releases page]([https://github.com/yourusername/yourrepo/releases](https://github.com/JScott1080/UNSC_Datapad/releases)).

> Android version: 8.0+  
> Permissions: Audio playback only (no network or storage access)  
> Note: This build uses placeholder-safe logic and does not include Halo-themed audio

---

## 🔄 CI & Build Info

This project uses GitHub Actions (`.github/workflows/android.yml`) to compile builds on push. It validates the playback system without bundling restricted assets and supports safe testing via emulator or device.

---

## 🧪 Testing Notes

You may inspect and interact with:
- Soundbyte buttons and ambiance selection  
- Sound queues and background track handling  
- UI animations triggered on click and cooldown

To experience full sound playback, clone the repository and add audio files to `res/raw` and `assets/` folders before building locally.

---

## 📄 License

The soundboard framework is provided under the MIT License.  
No audio assets are distributed with this repository. Halo franchise content is owned by its respective copyright holders.

---

## 🤝 Contact

For questions, collaboration, or event usage:
📧 [scott.joshua93@outlook.com](mailto:scott.joshua93@outlook.com)
