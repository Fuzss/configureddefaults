# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v21.3.3-1.21.3] - 2025-01-06
### Fixed
- Fix custom `options.txt` handling leading to an invalid file due to some strings being parsed incorrectly

## [v21.3.2-1.21.3] - 2024-12-30
### Fixed
- Fix start-up crash on Fabric
- 
## [v21.3.1-1.21.3] - 2024-12-28
### Fixed
- Fix dedicated server crash on Fabric thanks to [sshcrack](https://github.com/sshcrack)

## [v21.3.0-1.21.3] - 2024-12-28
- Port to Minecraft 1.21.3
### Changed
- Add special handling for options file, missing entries in an existing options file will now be complemented
