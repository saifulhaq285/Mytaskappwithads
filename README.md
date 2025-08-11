# MyTask Android App with Google Mobile Ads Integration

This Android app demonstrates the integration of various Google Mobile Ads formats — Banner, Native, Rewarded, and Interstitial ads — within a Jetpack Compose UI. The project is built using Kotlin and follows MVVM architecture for clean, maintainable code.

---

## Features

- **Banner Ads:** Displayed at the bottom/top using `AdView` within Compose.
- **Native Ads:** Seamlessly integrated native ads styled to match app UI.
- **Interstitial Ads:** Full-screen ads shown at natural app breaks.
- **Rewarded Ads:** Ads that reward users upon completion.
- **Firebase Integration:** Used for user authentication and data management.
- **Jetpack Compose UI:** Modern declarative UI framework.
- **MVVM Architecture:** Separation of UI and business logic.

---

## Project Structure

- `AdManager.kt`: Handles initialization and loading/showing of all ad types.
- `BannerAd.kt`: Composable to display banner ads.
- `NativeAdLoader.kt`: Utility to load native ads.
- ViewModels and Repositories manage app data and ad state.
- UI Composables using Jetpack Compose.

---

## Setup Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/mytask-ads-integration.git
   cd mytask-ads-integration
