# Ocellus Start Marker

Shows a Bandos icon and optional flashing arrow in the quadrant diagonally opposite
the Bandosian Censer, to help your group plan its position before Ocellus phase two
in The Fractured Archive.

## Use

Enable **Ocellus Start Marker** in RuneLite. The marker appears in Ocellus's room
during the intro or phase one when the censer can be identified. It hides when a
phase-two portal or a later phase is detected, and when you leave the room.

The marker is a planning aid, not an instruction to remain on that tile during
phase one. Check the position against your group's strategy.

## Settings

- **Distance from centre:** 2-8 tiles along each axis into the opposite quadrant;
  defaults to 4. This is not the total diagonal distance.
- **Show Bandos icon / Icon height:** toggle the symbol and choose a height of
  16-64 pixels; defaults to 32.
- **Outline target tile / Tile colour:** toggle the tile border and set its colour
  and opacity.
- **Show arrow / Arrow size / Arrow colour:** toggle the pointer and change its
  size, colour and opacity independently of the icon.
- **Flash arrow / Flash cycle (ms):** choose a flashing or steady arrow. Flashing
  is enabled by default, with a complete on/off cycle every 1,000 milliseconds.
  The cycle can be set from 250 to 2,000 milliseconds.

The arrow does not replace other plugins' game hint arrows. Quest Helper and
RuneLite Party are not required. The plugin does not move your character, automate
game inputs, request account credentials or use a network connection.

## Limitations

The exact marker placement and portal-transition timing still require live
encounter validation. The marked tile is not guaranteed to be safe or optimal
for every strategy. Adjust **Distance from centre** to suit your group.

Missing or ambiguous censer information hides the marker rather than choosing a
quadrant. If it is absent, check that the plugin and at least one marker option
are enabled, and that Ocellus has not reached phase two.

## Credits

The Bandos symbol is OSRS game artwork attributed to Jagex. The code's BSD 2-Clause
license does not claim ownership of that artwork.
