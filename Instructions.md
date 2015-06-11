#How to launch SDI

Main Application: org.onesun.smc.app.App

  * Use Environment variable: SDI\_HOME=/home/user/Path\_to\_SDI
  * Use VM arguments: -Xms512m -Xmx2048m
  * Use Program arguments:
    * -f (--features) <Connectivity|DataAccess|MetadataDiscovery>
    * -b (--browser)  <embedded|system>
    * -h (--home)     <path to SDI home>
    * -v (--version)