import BaseComponent from '../base-component';
import { addClass, removeClass, hasClass } from '../../assets/scripts/global/global-fn';

export default class MicrositeNavigation extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = MicrositeNavigation.init.bind(this);
    this.hideMobileLanguages = MicrositeNavigation.hideMobileLanguages.bind(this);
    this.showMobileLanguages = MicrositeNavigation.showMobileLanguages.bind(this);
    this.hideMobileMicroNav = MicrositeNavigation.hideMobileMicroNav.bind(this);
    this.showMobileMicroNav = MicrositeNavigation.showMobileMicroNav.bind(this);
    this.hideDesktopLanguages = MicrositeNavigation.hideDesktopLanguages.bind(this);
    this.showDesktopLanguages = MicrositeNavigation.showDesktopLanguages.bind(this);
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-microsite-navigation').forEach(elm => {
      const instance = new MicrositeNavigation(elm);
      instance.init();
    });
  }

  static init() {
    this.elm.querySelector('.mcd-microsite-navigation .desktop-nav .language-toggle')
      .addEventListener('click', MicrositeNavigation.toggleDesktopLanguages.bind(this), false);
    this.elm.querySelector('.mcd-microsite-navigation .mobile-nav .language-toggle')
      .addEventListener('click', MicrositeNavigation.toggleMobileLanguages.bind(this), false);
    this.elm.querySelector('.mcd-microsite-navigation .mobile-nav .mobile-nav__menu-icons')
      .addEventListener('click', MicrositeNavigation.toggleMobileMicroNav.bind(this), false);
  }

  // mobile
  // language toggle
  static hideMobileLanguages() {
    const mobileMicroNavIcon = document.querySelector('.mcd-microsite-navigation .mobile-nav .language-toggle .icon-minus');
    const mobileMicroNavContainer = document.querySelector('.mcd-microsite-navigation .mobile-nav .mcd-language-toggle');
    removeClass(mobileMicroNavIcon, 'icon-minus');
    addClass(mobileMicroNavIcon, 'icon-plus');
    removeClass(mobileMicroNavContainer, 'open');
  }

  static showMobileLanguages() {
    const mobileMicroNavIcon = document.querySelector('.mcd-microsite-navigation .mobile-nav .language-toggle .icon-plus');
    const mobileMicroNavContainer = document.querySelector('.mcd-microsite-navigation .mobile-nav .mcd-language-toggle');
    removeClass(mobileMicroNavIcon, 'icon-plus');
    addClass(mobileMicroNavIcon, 'icon-minus');
    addClass(mobileMicroNavContainer, 'open');
  }

  static toggleMobileLanguages(e) {
    e.preventDefault();
    const mobileLanguageToggle = document.querySelector('.mcd-microsite-navigation .mobile-nav .mcd-language-toggle');
    if (hasClass(mobileLanguageToggle, 'open')) {
      this.hideMobileLanguages();
    }
    else {
      this.showMobileLanguages();
    }
  }

  // navigation
  static hideMobileMicroNav() {
    const mobileMicroNav = document.querySelector('.mcd-microsite-navigation .mobile-nav');
    removeClass(mobileMicroNav, 'open');
  }

  static showMobileMicroNav() {
    const mobileMicroNav = document.querySelector('.mcd-microsite-navigation .mobile-nav');
    addClass(mobileMicroNav, 'open');
  }

  static toggleMobileMicroNav() {
    const mobileMicroNav = document.querySelector('.mcd-microsite-navigation .mobile-nav');
    if (hasClass(mobileMicroNav, 'open')) {
      this.hideMobileMicroNav();
    }
    else {
      this.showMobileMicroNav();
    }
  }

  // desktop
  static hideDesktopLanguages() {
    const MicroNavLink = document.querySelector('.mcd-microsite-navigation .desktop-nav .language-toggle');
    const MicroNavIcon = document.querySelector('.mcd-microsite-navigation .desktop-nav .language-toggle i');
    const MicroNavContainer = document.querySelector('.mcd-microsite-navigation .desktop-nav .mcd-language-toggle');
    removeClass(MicroNavIcon, 'active');
    removeClass(MicroNavLink, 'active');
    removeClass(MicroNavContainer, 'open');

  }

  static showDesktopLanguages() {
    const MicroNavLink = document.querySelector('.mcd-microsite-navigation .desktop-nav .language-toggle');
    const MicroNavIcon = document.querySelector('.mcd-microsite-navigation .desktop-nav .language-toggle i');
    const MicroNavContainer = document.querySelector('.mcd-microsite-navigation .desktop-nav .mcd-language-toggle');
    addClass(MicroNavLink, 'active');
    addClass(MicroNavIcon, 'active');
    addClass(MicroNavContainer, 'open');
  }

  static toggleDesktopLanguages(e) {
    e.preventDefault();
    const MicroNav = document.querySelector('.mcd-microsite-navigation .desktop-nav .mcd-language-toggle');
    if (hasClass(MicroNav, 'open')) {
      this.hideDesktopLanguages();
    }
    else {
      this.showDesktopLanguages();
    }
  }
}
