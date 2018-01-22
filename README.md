# launcher-extension

Eclipse plugin adding new launch options and configurations.

## Preferred Launch Configuration

By default, it's possible to create and set launch configurations as favorites, but there's no easy way to assign a shortcut for a specific launch configuration, neither to fixate a button for it.
This solution:
- adds the 'preferred launch configuration' as a workspace setting that can be configured to be any pre-existing launch configuration in the IDE.
- adds the 'Launch Preferred Configuration' button in the toolbar. It launches the preferred launch configuration that can be selected in the button pulldown menu.
- maps the F12 key to launch the preferred configuration (just next to Eclipse's F11 run shortcut)

## Correspondent Test Launch

A 'Run Correspondent Test' button is also add in the toolbar and will launch the test correspondent to the active Java editor class being edited.
By default, the plugin searches the correspondent test matching the "{classname}Test" pattern, but it can be changed in the preference panel.

## Installation

Can be installed adding the software site: https://github.com/pedrosans/launcher-extension/raw/master/launcher-extension-updatesite

*at* Window>Preferences>Install/Update>Available Software Sites>Add...

*or at* Help>Install New Software>Add..
