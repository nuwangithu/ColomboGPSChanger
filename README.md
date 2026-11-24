# Colombo GPS Changer

A simple Android utility that sets a mock GPS location to Colombo, Sri Lanka.

- Latitude: 6.9271
- Longitude: 79.8612

## Build
Open this project in Android Studio, allow Gradle sync, then select **Build > Build APK(s)**. The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

A GitHub Actions workflow is included to build the debug APK automatically when code is pushed.

## Phone setup
1. Install the APK.
2. Enable Developer Options by tapping Build number seven times in Settings > About phone.
3. In Developer Options, select **Colombo GPS Changer** under **Select mock location app**.
4. Open the app and tap **SET COLOMBO LOCATION**.
5. Tap **STOP MOCK LOCATION** to stop the simulated provider.

## Notes
Android versions and phone manufacturers differ. Some apps detect or reject mock locations. Use simulated location only where permitted. Do not use it to mislead emergency services, financial services, attendance systems, or services that require your true location.
