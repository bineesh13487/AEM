## Code Structure
Mainly the project is an AEM project. When you checkout the root directory, you'll find folders starting with mcd-* along with a folder named *presentation*. 

1. mcd-*: these folders represent either market or websites running on AEM. 
1. presentation: this folder contains all the frontend code. 

## About *mcd-** folders
These folders mainly store HTL (or view html) for components. These folders also store CSS, JS and assets server to browser when your view the pages on your browser.

## About *presentation* folder

Presentation project is based on webpack build. It uses a folder structure as given below. Each component's View template is in HTL format and stored outside of *presentation* folder and are found in *mcd-** folders.

```pre
root
    - build (contains public generated/minified css and js files)
    - src 
      - clientlibs
        - clientlib-x.js
        - clientlib-y.js
        - clientlib-z.js
      - components 
        - component X
          - component-x.js
          - component-x.scss
        - component Y
          - component-y.js
          - component-y.scss
        - component Z
          - component-z.js
          - component-z.scss
      - templates
        - template A
          - template-a.js
          - template-a.ejs
        - template B
          - template-b.js
          - template-b.ejs
        - template C
          - template-c.js
          - template-c.ejs
      - assets
        - images
        - fonts
        - scss
          - main.scss
          - grid.scss
          - default-theme.scss (colors, fonts, global styling)
        - js
          - main.js
          - shared
            - base-component.js
    - test-e2e (no e2e tests are written yet)

```
