# Contributing

## Development

The project targets Java 11 and RuneLite 1.13.7. Gradle tasks `test` and `jar`
run the automated tests and build the plugin. The `run` task starts the development
client; `testerZip` packages the local test harness. No game client is opened by
the tests.

See [TESTING.md](TESTING.md) for the live encounter checklist. Automated tests
cover quadrant selection, instance rotations, object transformations, suppression
and client lifecycle handling; they do not verify actual server object timing or
the chosen starting tile.

## Detection and placement evidence

- Template region 16472 comes from
  [Blert's TFA Location enum](https://github.com/blert-io/plugin/blob/main/src/main/java/io/blert/challenges/tfa/Location.java).
- RuneLite's generated values identify Bandosian Censers 62693/62694 and quadrant
  portal walls 62684/62685 in
  [ObjectID1.java](https://github.com/runelite/runelite/blob/master/runelite-api/src/main/java/net/runelite/api/gameval/ObjectID1.java).
- Intro NPCs 16671-16673 and P1 16674 allow display; P2/P3 16675-16677 suppress it.
- Centre region tile (31,41), template plane 1, was inferred from the symmetric
  saved room markers supplied for this project. Default targets are (27,37),
  (35,37), (27,45) and (35,45). Placement and portal lifecycle still require a live
  encounter check; tests do not establish that a target is safe or optimal.
- Both active and inactive censers, transformed objects, scene reloads and rotated
  instances are handled. Missing or ambiguous censers and duplicate target chunks
  hide the marker. Display requires the Ocellus room and an intro/P1 NPC.

The bundled Bandos PNG is user-provided OSRS artwork attributed to Jagex; the code
license does not cover ownership of that artwork.
