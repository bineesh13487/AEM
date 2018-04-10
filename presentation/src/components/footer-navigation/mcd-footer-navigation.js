import BaseComponent from '../base-component';
import {
  addClass,
  removeClass,
  hasClass
} from './../../assets/scripts/global/global-fn';

export default class FooterNav extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = () => {
      const elms = this.elm.querySelectorAll('.links');
      elms.forEach(elm => {
        elm.addEventListener('click', FooterNav.toggleFooterNavLinksPanel.bind(this, elm));
      });
    };
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-footer-nav').forEach(elm => {
      const instance = new FooterNav(elm);
      instance.init();
    });
  }

  static toggleFooterNavLinksPanel(elm) {
    if (hasClass(elm, 'open')) {
      removeClass(elm, 'open');
    }
    else {
      addClass(elm, 'open');
    }
  }
}
