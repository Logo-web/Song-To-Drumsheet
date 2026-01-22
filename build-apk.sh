#!/bin/bash

echo "🔨 Building Song-To-Drumsheet APK..."
echo ""

# Clean previous builds
echo "1️⃣ Cleaning previous builds..."
./gradlew clean

# Build debug APK
echo "2️⃣ Building debug APK..."
./gradlew assembleDebug

# Check if build was successful
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo ""
    echo "✅ Build successful!"
    echo "📱 APK location: app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "To install on connected device, run:"
    echo "   ./gradlew installDebug"
    echo ""
    echo "Or copy app-debug.apk to your phone and install manually."
else
    echo ""
    echo "❌ Build failed!"
    echo "Check the error messages above."
fi
