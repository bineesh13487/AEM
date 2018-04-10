import BaseComponent from '../base-component';
import { addClass, removeClass, hasClass } from '../../assets/scripts/global/global-fn';

export default class LegalDisclaimer extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = LegalDisclaimer.init.bind(this);
    // this.hideContent = LegalDisclaimer.hideContent.bind(this);
    // this.showContent = LegalDisclaimer.showContent.bind(this);
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-legal-disclaimer').forEach(elm => {
      const instance = new LegalDisclaimer(elm);
      instance.init();
    });
  }

  static init() {
    this.elm.querySelector('.read-more')
      .addEventListener('click', LegalDisclaimer.toggleContent.bind(this), false);
  }

  // mobile
  // static hideContent() {
  // }

  // static showContent() {
  // }

  static toggleContent(e) {
    e.preventDefault();
    const content = document.querySelector('.content');
    const readMore = document.querySelector('.read-more');
    if (hasClass(content, 'open')) {
      removeClass(content, 'open');
    }
    else {
      addClass(readMore, 'active');
      addClass(content, 'open');
    }
  }
}
