const hasClass = (el, className) => {
  if (el) {
    const elm = el;
    if (elm.classList) {
      return elm.classList.contains(className);
    }
    return !elm.className.match(new RegExp(`(\\s|^)${className}(\\s|$)`));
  }
  return '';
};

const addClass = (el, className) => {
  if (el) {
    const elm = el;
    if (elm.classList) {
      elm.classList.add(className);
    }
    else if (!hasClass(elm, className)) {
      elm.className += ` ${className}`;
    }
  }
};

const removeClass = (el, className) => {
  if (el) {
    const elm = el;
    if (elm.classList) {
      elm.classList.remove(className);
    }
    else if (hasClass(elm, className)) {
      const classListRegExp = new RegExp(`(\\s|^)${className}(\\s|$)`);
      elm.className = el.className.replace(classListRegExp, ' ');
    }
  }
};

const isMobileViewport = () => {
  const width = (window.screen.availWidth ? window.screen.availWidth : window.screen.width);
  return width < 768;
};

const getCssStyle = (element, styleName) => {
  if (window.getComputedStyle) {
    const styles = window.getComputedStyle(element);
    if (styles[styleName]) {
      return styles[styleName];
    }
  }
  return '';
};

const getCssStyleInNumber = (element, styleName, defaultValue = 0) => {
  const value = getCssStyle(element, styleName);
  try {
    return parseInt(value, 10);
  }
  catch (e) {
    console.error(`Error parsing styling name, ${styleName}, for numeric value in element ${element}`);
    console.error(e);
  }
  return defaultValue;
};

const getAttribute = (elm, attrName) => elm.getAttribute(attrName);

const getAttributeAsInt = (elm, attrName, defaultVal = 0) => {
  try {
    return parseInt(elm.getAttribute(attrName), 10);
  }
  catch (e) {
    console.error(e);
  }
  return defaultVal;
};

const getNextVisibleSibling = elm => {
  let nextSibling = elm.nextElementSibling;
  while (nextSibling) {
    if (window.getComputedStyle(nextSibling).visibility === 'visible' &&
    window.getComputedStyle(nextSibling).display !== 'none') {
      return nextSibling;
    }
    nextSibling = nextSibling.nextElementSibling;
  }
  return undefined;
};

const getPrevVisibleSibling = elm => {
  let prevSibling = elm.previousElementSibling;
  while (prevSibling) {
    if (window.getComputedStyle(prevSibling).visibility === 'visible' &&
    window.getComputedStyle(prevSibling).display !== 'none') {
      return prevSibling;
    }
    prevSibling = prevSibling.previousElementSibling;
  }
  return undefined;
};

const isElementVisible = elm => {
  if (window.getComputedStyle(elm).visibility === 'visible' &&
    window.getComputedStyle(elm).display !== 'none') {
    return true;
  }
  return false;
};

/*
USAGE:
captureTouchEvents(DOMelement, function callback(evt, dir, phase, swipetype, distance){
  // evt: contains original Event object
  // dir: contains "none", "left", "right", "top", or "down"
  // phase: contains "start", "move", or "end"
  // swipetype: contains "none", "left", "right", "top", or "down"
  // distance: distance traveled either horizontally or vertically, depending on dir value
  if ( phase == 'move' && (dir =='left' || dir == 'right') ) {
    console.log('You are moving the finger horizontally by ' + distance)
  }
});
*/
const captureTouchEvents = (el, callback) => {
  const touchsurface = el;
  const handletouch = callback || function noop(/* evt, dir, phase, swipetype, distance */) {};
  const threshold = 150; // required min distance traveled to be considered swipe
  const restraint = 100; // maximum distance allowed at the same time in perpendicular direction
  const allowedTime = 500; // maximum time allowed to travel that distance
  let dir;
  let swipeType;
  let startX;
  let startY;
  let distX;
  let distY;
  let elapsedTime;
  let startTime;

  touchsurface.addEventListener('touchstart', e => {
    const touchobj = e.changedTouches[0];
    dir = 'none';
    swipeType = 'none';
    startX = touchobj.pageX;
    startY = touchobj.pageY;
    startTime = new Date().getTime(); // record time when finger first makes contact with surface
    handletouch(e, 'none', 'start', swipeType, 0); // fire callback function with params dir="none", phase="start", swipetype="none" etc
    e.preventDefault();
  }, false);

  touchsurface.addEventListener('touchmove', e => {
    const touchobj = e.changedTouches[0];
    distX = touchobj.pageX - startX; // get horizontal dist traveled by finger
    distY = touchobj.pageY - startY; // get vertical dist traveled by finger
    if (Math.abs(distX) > Math.abs(distY)) {
      // if distance traveled horizontally is greater than vertically,
      // consider this a horizontal movement
      dir = (distX < 0) ? 'left' : 'right';
      // fire callback function with params dir="left|right", phase="move", swipetype="none" etc
      handletouch(e, dir, 'move', swipeType, distX);
    }
    else { // else consider this a vertical movement
      dir = (distY < 0) ? 'up' : 'down';
      // fire callback function with params dir="up|down", phase="move", swipetype="none" etc
      handletouch(e, dir, 'move', swipeType, distY);
    }
    e.preventDefault(); // prevent scrolling when inside DIV
  }, false);

  touchsurface.addEventListener('touchend', e => {
    elapsedTime = new Date().getTime() - startTime; // get time elapsed
    if (elapsedTime <= allowedTime) { // first condition for awipe met
      if (Math.abs(distX) >= threshold && Math.abs(distY) <= restraint) {
        // 2nd condition for horizontal swipe met
        swipeType = dir; // set swipeType to either "left" or "right"
      }
      else if (Math.abs(distY) >= threshold && Math.abs(distX) <= restraint) {
        // 2nd condition for vertical swipe met
        swipeType = dir; // set swipeType to either "top" or "down"
      }
    }
    // Fire callback function with params dir="left|right|up|down", phase="end", swipetype=dir etc:
    handletouch(e, dir, 'end', swipeType, (dir === 'left' || dir === 'right') ? distX : distY);
    e.preventDefault();
  }, false);
};

export { hasClass, removeClass, addClass, isMobileViewport,
  getCssStyle, getCssStyleInNumber, captureTouchEvents,
  getAttribute, getAttributeAsInt, isElementVisible,
  getNextVisibleSibling, getPrevVisibleSibling };
