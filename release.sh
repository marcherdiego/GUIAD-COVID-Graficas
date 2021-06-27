#!/bin/bash

./gradlew assembleRelease
zipalign -v 4 app/build/outputs/apk/release/app-release-unsigned.apk app-release-unsigned-aligned.apk
apksigner sign --ks /Users/diego/guiad_charts --out app-release.apk app-release-unsigned-aligned.apk
rm app-release.apk.idsig
rm app-release-unsigned-aligned.apk
