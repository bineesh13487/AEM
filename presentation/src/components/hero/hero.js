import BaseComponent from '../base-component';

export default class Hero extends BaseComponent {
  constructor() {
    super();
    this.time = new Date();
    this.componentName = 'hero';
  }
  static clickHandler() {
    console.log('hero component js is loaded');
  }
  static bootstrap() {
    document.querySelectorAll('.mcd-hero').forEach(elm => {
      const instance = new Hero(elm);
      instance.init();
    });
  }
}
