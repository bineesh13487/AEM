# AEM Setup

To set-up AEM, you'll need the following:

1. Download [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) for your OS. 
1. Download the [CQ folder from Box](https://lion.box.com/s/h38einhqenu6eal6blasdbwqx5df34cx)

## First Run of AEM
To run AEM for the first time and set-up admin password, take the following steps

1. Use Command Prompt or Terminal to go the the directory where you downloaded the CQ folder.
1. Run `java -jar cq-author-4503.jar`
1. Set-up will ask set-up an admin password. Use *admin* as the password.

This should get the local instance of AEM running. Now browse to [localhost:4502](http://localhost:4502/) and try to login with username *admin* and password *admin*

Once you are sure the your login works. Return to the Terminal or Command Prompt and terminate the server by pressing CTRL+C.

Now onwards, we will want to run the server using the *start* script. 

You'll note that by running the server for the first time, you now have a *crx-quickstart* folder under your CQ folder. 

## Setup your start script

Following steps are required for configuring your local AEM to run using the *start* script:

1. Stop AEM server
1. Open the *start* file for editing. You'll find the *start* file at `<AEM install dir>/crx-quickstart/bin/` folder. On Mac/Linux it will be named *start* and on Windows it will be named *start.bat*
1. For MacOS (*start*), find the line that looks like this:
`CQ_RUNMODE="${CQ_RUNMODE},crx3,crx3tar"`
and change that to:
`CQ_RUNMODE="${CQ_RUNMODE},crx3,crx3tar,local"`
1. For Windows (*start.bat*), find the line that looks like this: `CQ_RUNMODE=%CQ_RUNMODE%,crx3,crx3tar`
and change that to: 
`CQ_RUNMODE=%CQ_RUNMODE%,crx3,crx3tar,local`
1. Restart AEM - this time by running `./start` on Mac and `start.bat` on Windows

Going forward, you'll always be running the *start* script to run AEM. 

By adding "local" to the RUNMODE above, we are ensuring that the McDonald's packages will run fine in localhost environment.


## Important AEM links
1. [Welcome page](http://localhost:4502/libs/cq/core/content/welcome.html) - welcome page that links to other important section of AEM

1. [CRXDE Lite](http://localhost:4502/crx/de/) - develop applications with a web-based IDE
 
1. [Packages](http://localhost:4502/crx/packmgr/) - package and share applications
 
1. [Site Admin or Websites](http://localhost:4502/siteadmin) - List Websites and pages in heirarchical structure

1. [DAM or Digital Assets](http://localhost:4502/damadmin) - lists DAM content in heirarchical structure
