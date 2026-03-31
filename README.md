# TRiCYCLE

TRiCYCLE is a standalone desktop application for converting between any one of 25 different dendro data file formats. See Github Releases for installation files for Windows, MacOS, and Linux.

## Build

TRiCYCLE now targets Java 21.

- `mvn package` builds the standard application jar in `target/`
- `mvn -Pfat-jar package` builds an additional all-in-one jar with classifier `all`
- `mvn -Pjpackage verify` builds a reusable `jpackage` app image in `target/jpackage`
- `mvn -Pjpackage,jpackage-linux-deb verify` builds a Linux `.deb`
- `mvn -Pjpackage,jpackage-linux-rpm verify` builds a Linux `.rpm`
- `mvn -Pjpackage,jpackage-windows verify` builds a Windows app image on Windows
- `mvn -Pjpackage,jpackage-macos verify` builds a macOS `.app` bundle on macOS

## CI

GitHub Actions is configured in [.github/workflows/build-and-package.yml](./.github/workflows/build-and-package.yml) to:

- build the plain jar on Linux
- build the fat jar on Linux
- build Linux `.deb` and `.rpm` packages
- build Windows installers and macOS `.app` bundles on native runners
