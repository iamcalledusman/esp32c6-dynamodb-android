# ESP32 Sensor Monitor — Android App (AWS + DynamoDB)

An Android app that shows the **latest sensor readings from an ESP32**, stored in **AWS DynamoDB** and served through **Amazon API Gateway**. Built with Kotlin and Jetpack Compose.

> This repository contains the **Android client only**. The ESP32 firmware and AWS backend (API Gateway / Lambda / DynamoDB) are configured separately.

## How it works

```
ESP32 sensor  ──►  AWS (IoT / Lambda)  ──►  DynamoDB
                                               │
Android app  ◄──  API Gateway  GET /dev/latest ◄┘
```

1. The ESP32 publishes sensor readings to AWS, where they are stored in DynamoDB.
2. An API Gateway endpoint (`GET /dev/latest`) returns the most recent record as JSON.
3. The app calls the endpoint with OkHttp when you tap **Fetch Data**.
4. Values arrive as hexadecimal strings (`N1`–`N6`), are decoded to integers, and mapped to readable fields.
5. The UI displays the result. Temperature turns **red above 20** and **green otherwise**.

### Data mapping

| JSON field | Decoded as | Displayed as |
|------------|-----------|--------------|
| `N6` | hex → int | Temperature |
| `N2` | hex → int | Moisture |
| `N3` | hex → int | xVal |
| `N1` | hex → int | yVal |
| `N4`, `N5` | hex → int | Received, not displayed |

Example response shape:

```json
{ "N1": "1A", "N2": "2F", "N3": "0B", "N4": "37", "N5": "42", "N6": "16" }
```

## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Networking:** OkHttp 5
- **Concurrency:** Kotlin Coroutines
- **Cloud:** AWS API Gateway, DynamoDB
- **Hardware:** ESP32
- **Build:** Gradle (Kotlin DSL), version catalog

## Requirements

- Android Studio (recent version with AGP 9.x support)
- JDK 11+
- Android device or emulator, **API 24+** (Android 7.0)
- Internet access

## Getting started

```bash
git clone https://github.com/iamcalledusman/aws_dynamodb_app.git
```

1. Open the project in Android Studio and let Gradle sync.
2. In `MainActivity.kt`, set `url` to your own API Gateway endpoint that returns the latest sensor record.
3. Run the app on a device or emulator and tap **Fetch Data**.

## Project structure

```
app/src/main/java/com/example/myapplication/
├── MainActivity.kt   # Compose UI, network call, hex decoding
├── RawData.kt        # RawData and SensorDataClass models
└── ui/theme/         # Material 3 theme (colors, typography)
```

## Possible improvements

- Error handling and loading state for failed or slow requests
- Move the endpoint URL into `BuildConfig` instead of hardcoding it
- Auto-refresh / polling instead of a manual button
- Move networking out of the composable into a `ViewModel` and repository
- History chart of past readings

## License

Add a license of your choice (for example MIT) before publishing.
