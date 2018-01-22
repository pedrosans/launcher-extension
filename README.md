# launcher-extension

Eclipse plugin to extend the IDE launch options

## Preferred Launch Configuration

Adds the 'Launch Preferred Configuration' button to the toolbar. Contrary to favorite launch configurations (already supported by Eclipse), the preferred launch configuration can be launched right from the toolbar.

- the preferred launch configuration can be selected in the button pull-down menu
- can be launched by the F12 key (at the side of Eclipse's F11 run shortcut)

## Correspondent Test Launch

A 'Run Correspondent Test' button is also add in the toolbar and will launch the test corresponding to the active Java class being edited.
By default, the plugin searches the correspondent test matching the "{classname}Test" pattern, but it can be configured in the preference page.

- preference page in the 'Preferences' window at: Run/Debug > Launching > Launch Extension

## Automatic test run

This automatic test launch can be enabled in the plugin preference panel.
If enabled, it will automatic launch the class test in background, if detected, it reports any failure in the IDE bottom status line.

For example: while working on the class FooReader, any save + build will trigger FooReaderTest. This test will run in background (JUnit view won't be touched), and only it's progress percentage
The test run will not get the JUnit view focus, neither JUnit view will show its status. The only goal of this execution is to proactively report failures if any.

## Installation

### From Eclipse Marketplace [![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3763013 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

1) Help > Eclipse Marketplace...

2) Search for 'Launcher extensions' and proceed to the installation by clicking the 'Install' button

### From the software site

1) Window>Preferences>Install/Update>Available Software Sites>Add...

*or at* Help>Install New Software>Add..

2) add the software site: 

```
https://github.com/pedrosans/launcher-extension/raw/master/launcher-extension-updatesite
```

3) select 'Launcher Extension feature' and proceed to the installation clicking "Next >"
