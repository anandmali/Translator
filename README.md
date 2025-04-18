# 📱 Translator App – AI-Powered Android Translation with Gemini API

🚀 An open-source Android application demonstrating the integration of Google's Generative AI (Gemini API) using the `com.google.ai.client.generativeai` SDK.

## 🧠 Tech Stack

- **Language**: Kotlin
- **Architecture**: Clean Architecture
- **UI Framework**: Jetpack Compose with Material Design
- **AI Integration**: Google's Generative AI via `com.google.ai.client.generativeai` SDK

## 📦 Getting Started

### Prerequisites

- Android Studio Flamingo or later
- Android SDK 33+
- Google AI Studio API Key for Gemini API

### Setup Instructions

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/anandmali/Translator.git
   cd Translator
   ```

2. **Obtain Gemini API Key**:

   - Visit [Google AI Studio](https://ai.google.dev/)
   - Create a new project and generate an API key

3. **Configure API Key**:

   - Create a `local.properties` file in the root directory
   - Add the following line:

     ```properties
     GEMINI_API_KEY=your_api_key_here
     ```

4. **Build and Run**:

   - Open the project in Android Studio
   - Sync Gradle and run the app on an emulator or physical device

## 🔐 Security Considerations

The `com.google.ai.client.generativeai` SDK is intended for prototyping purposes. For production applications, it's recommended to use the Vertex AI in Firebase SDK, which offers enhanced security features, including:

- **Firebase App Check**: Protects your API from abuse by unauthorized clients.
- **Cloud Storage Integration**: Handles large media files efficiently.

Learn more about transitioning to Vertex AI in Firebase:
- [Vertex AI in Firebase Documentation](https://developer.android.com/ai/vertex-ai-firebase)
- [Google AI Client SDK for Android](https://developers.google.com/learn/pathways/solution-ai-gemini-getting-started-android)

## 📚 Resources

- [Gemini API Documentation](https://ai.google.dev/gemini-api/docs)
- [Generative AI on Android Overview](https://developer.android.com/ai/generativeai)
- [Google AI Studio](https://ai.google.dev/)

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request for any enhancements or bug fixes.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.