import BaseComponent from '../base-component';
import { addClass, removeClass, hasClass } from '../../assets/scripts/global/global-fn';

export default class LanguageToggle extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = LanguageToggle.init.bind(this);
    this.hideMobileLanguageToggle = LanguageToggle.hideMobileLanguageToggle.bind(this);
    this.showMobileLanguageToggle = LanguageToggle.showMobileLanguageToggle.bind(this);
    this.hideDesktopLanguageToggle = LanguageToggle.hideDesktopLanguageToggle.bind(this);
    this.showDesktopLanguageToggle = LanguageToggle.showDesktopLanguageToggle.bind(this);
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-global-nav').forEach(elm => {
      const instance = new LanguageToggle(elm);
      instance.init();
    });
  }

  static init() {
    this.elm.querySelector('.desktop-nav .language-toggle')
      .addEventListener('click', LanguageToggle.toggleDesktopLanguages.bind(this), false);
    this.elm.querySelector('.mobile-nav .language-toggle')
      .addEventListener('click', LanguageToggle.toggleMobileLanguages.bind(this), false);
  }

  // mobile
  static hideMobileLanguageToggle() {
    const mobileLanguageToggleIcon = document.querySelector('.mobile-nav .language-toggle .icon-minus');
    const mobileLanguageToggleContainer = document.querySelector('.mobile-nav .mcd-language-toggle');
    removeClass(mobileLanguageToggleIcon, 'icon-minus');
    addClass(mobileLanguageToggleIcon, 'icon-plus');
    removeClass(mobileLanguageToggleContainer, 'open');

  }

  static showMobileLanguageToggle() {
    const mobileLanguageToggleIcon = document.querySelector('.mobile-nav .language-toggle .icon-plus');
    const mobileLanguageToggleContainer = document.querySelector('.mobile-nav .mcd-language-toggle');
    removeClass(mobileLanguageToggleIcon, 'icon-plus');
    addClass(mobileLanguageToggleIcon, 'icon-minus');
    addClass(mobileLanguageToggleContainer, 'open');
  }

  static toggleMobileLanguages(e) {
    e.preventDefault();
    const mobileLanguageToggle = document.querySelector('.mobile-nav .mcd-language-toggle');
    if (hasClass(mobileLanguageToggle, 'open')) {
      this.hideMobileLanguageToggle();
    }
    else {
      this.showMobileLanguageToggle();
    }
  }

  // desktop
  static hideDesktopLanguageToggle() {
    const languageToggleLink = document.querySelector('.desktop-nav .language-toggle');
    const languageToggleIcon = document.querySelector('.desktop-nav .language-toggle i');
    const languageToggleContainer = document.querySelector('.desktop-nav .mcd-language-toggle');
    removeClass(languageToggleIcon, 'active');
    removeClass(languageToggleLink, 'active');
    removeClass(languageToggleContainer, 'open');

  }

  static showDesktopLanguageToggle() {
    const languageToggleLink = document.querySelector('.desktop-nav .language-toggle');
    const languageToggleIcon = document.querySelector('.desktop-nav .language-toggle i');
    const languageToggleContainer = document.querySelector('.desktop-nav .mcd-language-toggle');
    addClass(languageToggleLink, 'active');
    addClass(languageToggleIcon, 'active');
    addClass(languageToggleContainer, 'open');
  }

  static toggleDesktopLanguages(e) {
    e.preventDefault();
    const languageToggle = document.querySelector('.desktop-nav .mcd-language-toggle');
    if (hasClass(languageToggle, 'open')) {
      this.hideDesktopLanguageToggle();
    }
    else {
      this.showDesktopLanguageToggle();
    }
  }
}
