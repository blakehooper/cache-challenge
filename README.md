Run Instructions:
Tests can be run with './gradlew test'

Notes:
I've tagged the commits of the respective stages with tags 'Basic', 'challenge', 'extension'

Assumptions:
* I tried to keep lightweight with no external libraries. Although the wiring in the builder is ugly it allows us to stay completely framework agnostic.
* My idea is that this would essentailly be a jar providing methods to create a bean to use in your application. Be it Spring / Guice whatever.

Future Work
* Provide configuration either passed into the builder or have the builder ask an endpoint for it's configuration