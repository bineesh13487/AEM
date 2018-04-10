# Code Guidelines

McDonald’s FE Coding Guidelines for Newfoundland release.

FE Architectural Overview

1. Include only required CSS / JS
2. Non-blocking load of CSS and JS
3. Vanilla JS / No jQuery dependency
4. Component Modularity
5. Extensibility with CSS
6. Server side rendering with HTL
7. Client side rendering with EJS compiled
8. Legacy Angular MVC code

## Include only required CSS / JS
To improve load time and reduce payload we must load only the required code when including third-party code. For example, when including bootstrap only include the required sections.

## Avoid duplication of code in clientlibs
Both mcd-us and mcd-global clientlibs are loaded for all webpages. What is already included in mcd-global must not be duplicated in mcd-us. For example, mcd-global already includes bootstrap's grid classes, therefore there is no need to include grid classes in mcd-us. 

## Ensure ESLint passes
Ensure the ESLint passes when writing code.

## Async code
Ensure that the component you write can run asynchronously and is non-blocking. 

Note:
Adobe AEM’s analytics doesn’t support async load and hence needs to be loaded synchronously. Any other library that doesn’t support async load will continue to be loaded as sync. 


## Vanilla JS and no jQuery dependency
New components JavaScript must not depend on jQuery. This will improve runtime performance of functions that access or modify DOM.

Bootstrap and Flexslide components depend on jQuery and will continue to use jQuery. Adobe AEM also needs jQuery, so removing jQuery is not an option. 

The idea is make new components run faster by minimizing the use of jQuery.

## Ensure Modularity of JS and CSS
Each component will be independent of other components in the system and will be self-contained. 

Common code will be present as utility functions as shared code. 

## Extensibility with SASS
Approved agencies (other than Sapient) must be able to extend the UI of a component just with CSS/SASS. No changes to component’s HTL will be allowed.  

HTML upload component will be considered obsolete and Sapient recommends against using the HTML component.  

Example 1: A 2/3 layout for Feature callout could be extended by an agency using just CSS code, avoiding the unnecessary use of HTML component these cases. 

Example 2: Given a component A that has caption below image, a agency could re-order the layout to have caption above image - this will ensure their designs are met. Agency will use a Flexbox layout. Screen readers software ignore the order set by Flexbox, hence, a screen-reader software will continue to read/see caption below image. 

## Server side rendering with HTL
Any component that is not a candidate for client-side MVC and doesn’t depend largely on Ajax requests must be rendered on the server side. The HTL template language will be used for the same. 


## WebPack based FE Build
We are using WebPack for building FE code. The build feature will require maintenance by the team and care must be taken to keep it up-to-date and follow the latest best practices.


## Legacy Angular MVC code
Many parts of the existing websites and components depend on AngularJS. 
- Developers and architect must decide on component by component basis on whether or not to replace the AngularJS implementation with Vanilla JS. 
 - All functionality must continue to be supported, hence, complicated MVC parts like Restaurant Locator will continue to work as AngularJS app.

## Re-styling v/s re-write
A component’s new design for desktop/mobile and it’s current implementation approach will have to be evaluated per component basis. A decision by architects will be taken on whether or not the component can be simply re-styled without the need for drastic changes to current implementation. 

To make the decision, following major considerations will be made:
1. Need for applying new design - new re-design might require a drastic change to existing implementation and hence would require a thorough re-write
1. Need for performance improvement - some implementations might be poorly written resulting in poor lead-time or run-time performance and hence will require a re-write
1. Need for simplification - some implementations will be overly complicated to understand or to make changes to without breaking existing functionality and hence will require a total re-factor. 
1. Need for documentation for current approach - some implementations might not be straight-forward to understand and may lacks developer notes/documentation and hence might require re-write


## Legacy Analytics Tracking Code

As developers make changes to code, it is important to understand how current analytics is working in a component. Developer’s must try to keep the current analytics code intact. 


## Document identified backend changes (use static HTML)
1. When you find that a component requires BE change(s), document it in a Confluence page. Continue to implement the design using static HTML in those sections.

## Design

See 04_Design.md file.

## Accessibility

See 05_Accessibility.md file.

## Right to left

See 06_Right_to_left.md file.
