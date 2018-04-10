import BaseComponent from '../base-component';
import { addClass, removeClass, hasClass } from '../../assets/scripts/global/global-fn';

export default class GlobalNav extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = GlobalNav.init.bind(this);
    this.hideDesktopMenu = GlobalNav.hideDesktopMenu.bind(this);
    this.showDesktopMenu = GlobalNav.showDesktopMenu.bind(this);
    this.showMobileMenuLinks = GlobalNav.showMobileMenuLinks.bind(this);
    this.hideMobileMenuLinks = GlobalNav.hideMobileMenuLinks.bind(this);
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-global-nav').forEach(elm => {
      const instance = new GlobalNav(elm);
      instance.init();
    });
  }

  static init() {
    this.elm.querySelector('.mcd-global-nav .mobile-nav .icon-hamburger')
      .addEventListener('click', GlobalNav.openMobileNavLinks.bind(this), false);
    this.elm.querySelector('.mcd-global-nav .mobile-nav .icon-times')
      .addEventListener('click', GlobalNav.closeMobileNavLinks.bind(this), false);
    this.elm.querySelector('.mcd-global-nav .mobile-nav .mobile-menu-link')
      .addEventListener('click', GlobalNav.toggleMobileMenuLinks.bind(this), false);
    this.elm.querySelector('.mcd-global-nav .desktop-nav .toggle-desktop-menu')
      .addEventListener('click', GlobalNav.toggleDesktopMenu.bind(this), false);
  }

  // mobile
  static openMobileNavLinks() {
    const nav = this.elm.querySelector('.mcd-global-nav .mobile-nav');
    addClass(nav, 'open');
  }

  static closeMobileNavLinks() {
    const nav = this.elm.querySelector('.mcd-global-nav .mobile-nav');
    removeClass(nav, 'open');
  }

  static showMobileMenuLinks() {
    const icon = this.elm.querySelector('.mcd-global-nav .icon-plus');
    const mobileFlyoutMenu = this.elm.querySelector('.mcd-global-nav .mobile-nav .mobile-flyout-menu');
    removeClass(icon, 'icon-plus');
    addClass(icon, 'icon-minus');
    addClass(mobileFlyoutMenu, 'open');
  }

  static hideMobileMenuLinks() {
    const icon = this.elm.querySelector('.mcd-global-nav .icon-minus');
    const mobileFlyoutMenu = this.elm.querySelector('.mcd-global-nav .mobile-nav .mobile-flyout-menu');
    removeClass(icon, 'icon-minus');
    addClass(icon, 'icon-plus');
    removeClass(mobileFlyoutMenu, 'open');
  }

  static toggleMobileMenuLinks(e) {
    e.preventDefault();
    const mobileFlyoutMenu = this.elm.querySelector('.mcd-global-nav .mobile-nav .mobile-flyout-menu');
    if (hasClass(mobileFlyoutMenu, 'open')) {
      this.hideMobileMenuLinks(e);
    }
    else {
      this.showMobileMenuLinks(e);
    }
  }

  // desktop
  static hideDesktopMenu(e) {
    const flyoutMenu = document.querySelector('.mcd-global-nav .desktop-nav .flyout-menu');
    removeClass(e.target, 'active');
    removeClass(flyoutMenu, 'open');
  }

  static showDesktopMenu(e) {
    const flyoutMenu = document.querySelector('.mcd-global-nav .desktop-nav .flyout-menu');
    addClass(e.target, 'active');
    addClass(flyoutMenu, 'open');
  }

  static toggleDesktopMenu(e) {
    e.preventDefault();
    if (hasClass(e.target, 'active')) {
      this.hideDesktopMenu(e);
    }
    else {
      this.showDesktopMenu(e);
    }
  }
}
