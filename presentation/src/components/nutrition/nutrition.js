import BaseComponent from '../base-component';

import { addClass, removeClass } from '../../assets/scripts/global/global-fn';

export default class Nutrition extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = () => {
      const elms = this.elm.querySelectorAll('.panel');
      elms.forEach(elm => {
        elm.addEventListener('click', Nutrition.togglePanel.bind(this, elm));
      });
    };
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-nutrition').forEach(elm => {
      const instance = new Nutrition(elm);
      instance.init();
    });
  }

  static togglePanel(elm) {
    const clickedPanel = elm.querySelector('.panel-collapse');
    const chevronIcon = elm.querySelector('.icon-chevron-down');
    const isCollapsed = clickedPanel.classList.contains('in');

    if (isCollapsed) {
      removeClass(chevronIcon, 'active');
    }
    else {
      addClass(chevronIcon, 'active');
    }
  }
}
