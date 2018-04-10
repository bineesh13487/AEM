# Ensure RTl version work fine
Project will generate RTL version of CSS for every clientlib. This is achieved using  [webpack-rtl-plugin plugin](https://www.npmjs.com/package/webpack-rtl-plugin). 

Developers must test the code and make minor adjustments when required. In 99% cases, developers won't have to write RTL specific code. 


## Testing Guide
The local AEM environment can be tested by loading the Arabia site locally. If Arabia content is not available, a local only testing can be done by following the steps below:

1. Open headlibs.html under `/mcd-us/ui.apps/src/main/content/jcr_root/apps/mcd-us/components/page/base-page/` folder.
1. You will find `${!configRTL.isRtl}` and $`{configRTL.isRtl}` on this file. You can switch the condition so that RTL version of CSS is loaded on English language. To switch the condition, change `${!configRTL.isRtl}` and `${configRTL.isRtl}` to `${configRTL.isRtl}` and `${!configRTL.isRtl}` respectively. Note that, we are only adding and removing the *exclamation* mark.
1. Load the AEM pages on your browser to test the site. 
1. Once you are done with testing, revert the change made to headlibs.html file. 

## Coding Guide
Instead of authoring two sets of CSS files, one for each language direction, you only focus on  LTR version and the plugin will automatically generate the RTL counterpart for you. 

```css
.example {
  display:inline-block;
  padding:5px 10px 15px 20px;
  margin:5px 10px 15px 20px;
  border-style:dotted dashed double solid;
  border-width:1px 2px 3px 4px;
  border-color:red green blue black;
  box-shadow: -1em 0 0.4em gray, 3px 3px 30px black;
}
```

Will be transformed into:
```css
.example {
  display:inline-block;
  padding:5px 20px 15px 10px;
  margin:5px 20px 15px 10px;
  border-style:dotted solid double dashed;
  border-width:1px 4px 3px 2px;
  border-color:red black blue green;
  box-shadow: 1em 0 0.4em gray, -3px 3px 30px black;
}
```

In order to avoid transformation of a section of code in the RTL output use the following control directive:

```css
/*rtl:begin:ignore*/
.code {
  direction:ltr;
  text-align:left;
}
/*rtl:end:ignore*/

```
You can also ignore a specific property in a rule. For example, to maintain the text-align to left for elements with .code class name, you will use the rtl:ignore directive:

```css
.code {
  direction:ltr;
  /*rtl:ignore*/
  text-align:left;
}
```

In order to override any CSS rules specifically for RTL version of CSS output, use `[dir=rtl]` in your selector:

```css
.selector {
  line-height: 1.5rem;
  &[dir=rtl] & {
    line-height: 1.4rem;
  }
}
```
