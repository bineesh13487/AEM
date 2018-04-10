#! /bin/bash

mvn clean install -pl mcd-global/core,mcd-global/ui.apps,mcd-global/ui.content,mcd-wifi,mcd-wifi/core,mcd-wifi/ui.apps,mcd-wifi/ui.content -PautoInstallPackage -s "settings.xml"