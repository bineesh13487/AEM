# Legacy folder

This folder is where we place the legacy JavaScript code provided to Sapient during the handover phase.

## Background
Then McDonald's web project has been in development for over three years before Sapient got involved. We are overtaking this project from HCL and are inheriting a lot of code. 

## Why keep legacy JS separately here
1. The code provided by HCL doesn't depend on dependency manager like yarn or bower. 
1. Even the code of popular framework like AngularJS has been modified by HCL.
1. We don't want to mix this code with new structured JS code we are writing for components.

## Maintaining the legacy code
1. Many views still depend on legacy code and we will continue to maintain those code
1. New functionality and bug fixes will continue to happen in code files where it makes sense


