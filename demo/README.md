# Demo data for screenshots

`OrderSerializer.java` — the constructor builds `sharedMapper` once
(not flagged), `serialize` builds a brand new `ObjectMapper` on every
call (flagged).

## How to get the screenshot

1. `./gradlew runIde` from `object-mapper-reuse-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `OrderSerializer.java` — a warning icon should
   appear on the `new ObjectMapper()` call in `serialize` only.
3. Screenshot with both methods visible, save into
   `object-mapper-reuse-companion/docs/screenshots/`. Close the
   sandbox.
