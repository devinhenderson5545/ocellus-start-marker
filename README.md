# Ocellus Start Marker

Places a small Bandos icon and optional flashing arrow in the quadrant diagonally opposite
the Bandosian Censer, before Ocellus phase two. It hides on the first detected P2
portal or a later-phase NPC. It does not require Party, move your character, or
choose movement tiles during the fight.

Settings: distance from the room centre (default 4 tiles along each axis), icon
height (32 pixels), optional tile outline and outline colour. The arrow is enabled
by default and flashes on/off once per second. It has independent visibility,
flashing, colour, size and flash-cycle settings; disable flashing for a steady
arrow. The Bandos icon can also be hidden independently. The pointer is drawn by
this overlay, so it does not replace another plugin's game hint arrow and requires
no Quest Helper installation. The supplied Bandos
PNG is bundled unchanged; it is user-provided OSRS game artwork, attributed to Jagex.
The code license does not claim ownership of that artwork.

## Test build

Extract `ocellus-start-marker-0.1.0-test.zip` and run `Start-Ocellus-Test.cmd`.
This starts a development RuneLite client; it does not install into normal RuneLite.
Use your established development-client login process. Enable **Ocellus Start Marker**.
This plugin requests no account credentials or network access.

Build with JDK 11 or later: `gradlew.bat test testerZip`. Run from source with
`gradlew.bat run`. The launcher uses normal RuneLite's bundled Java, falling back to
JAVA_HOME. No game client is opened by the tests.

## Evidence and limits

- Template region 16472 comes from Blert's TFA Location enum:
  https://github.com/blert-io/plugin/blob/main/src/main/java/io/blert/challenges/tfa/Location.java
- RuneLite's generated game values identify Bandosian Censers 62693/62694 and
  quadrant portal walls 62684/62685:
  https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/ObjectID1.java
- Intro NPCs 16671-16673 and P1 16674 allow display; P2/P3 16675-16677 suppress it.
- Centre region tile (31,41), template plane 1, was inferred from the symmetric
  saved room tile markers supplied locally by the requester. Default targets are
  (27,37), (35,37), (27,45), (35,45). **This exact placement and the portal lifecycle
  still require a live encounter check.** Passing tests do not establish that the
  proposed tile is safe or optimal for every strategy.
- Both active and inactive censers, transformed objects, scene reloads and rotated
  instances are handled. Missing/ambiguous censers or duplicate target chunks hide
  the marker instead of guessing. Requires the Ocellus room and an intro/P1 NPC.
- The marker is a pre-P2 planning aid, not an instruction to remain there during P1.

Submitted builds require Plugin Hub review before appearing in normal RuneLite.
Live encounter testing remains pending; see TESTING.md for the short live check.
