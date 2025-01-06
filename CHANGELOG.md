# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog].

## [v8.0.4-1.20.1] - 2025-01-06
### Fixed
- Fix custom `options.txt` handling leading to an invalid file due to some strings being parsed incorrectly

## [v8.0.3-1.20.1] - 2024-12-29
### Fixed
- Attempt fixing dedicated server crash on start-up for Fabric

## [v8.0.2-1.20.1] - 2024-12-28
### Changed
- Add special handling for options file, missing entries in an existing options file will now be complemented
### Fixed
- Fix dedicated server crash on Fabric thanks to [sshcrack](https://github.com/sshcrack)

## [v8.0.1-1.20.1] - 2023-10-31
### Changed
- Simplify mod internals

## [v8.0.0-1.20.1] - 2023-10-30
- Initial release

[Keep a Changelog]: https://keepachangelog.com/en/1.0.0/
