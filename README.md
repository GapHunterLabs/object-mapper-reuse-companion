# Object Mapper Reuse Companion

Gutter warning icon on a Jackson mapper (`ObjectMapper`, `XmlMapper`,
`YAMLMapper`, `CBORMapper`, `CsvMapper`) built via `new` inside a
regular method body — Jackson's own javadoc states `ObjectMapper` is
"meant to be used as singleton across the lifetime of the
application... also very expensive to create". Building one inside a
method means the real construction/configuration cost is paid on
every call.

## Why it exists

`new ObjectMapper()` reads like a harmless local variable, but
Jackson's own docs are explicit that it's meant to be a singleton —
every construction pays real, avoidable cost. Nothing in the IDE flags
a mapper built the wrong way today.

## Why built this way

- **100% static text/PSI analysis** — matches the mapper class name by
  simple text, so it works whether the real Jackson jar is on the
  classpath or not. Java and Kotlin.

## v0.1 scope — stated honestly, not exhaustively

Never flags a construction inside a constructor or a field/property
initializer (legitimate "create once" locations). Doesn't trace a
mapper built once and passed around — only the construction site
itself is checked.

## Usage

Open any Java/Kotlin file using a Jackson mapper. A mapper built
inside a regular method shows a warning icon.

## Support

- **Bugs and feature requests:** [GitHub Issues](https://github.com/GapHunterLabs/object-mapper-reuse-companion/issues)
- **Questions, or custom rules for a team's codebase:** **gaphunterlabs@gmail.com**
- **Security vulnerabilities:** report privately as described in [SECURITY.md](SECURITY.md), not in a public issue.
- **Privacy and network behavior:** [PRIVACY.md](PRIVACY.md)

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
