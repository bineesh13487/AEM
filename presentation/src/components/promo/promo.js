import BaseComponent from '../base-component';

export default class Promo extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = Promo.init.bind(this);
  }

  static init() {
    console.log('column control', this.elm.querySelector('.columncontrol'));
    this.elm.querySelector('.columncontrol');
  }

  static bootstrap() {
    document.querySelectorAll('.columncontrol').forEach(elm => {
      console.log('BOOT STRAP TEST');
      const instance = new Promo(elm);
      instance.init();
    });
  }

  static getElements(elms) {
    console.log('elms', elms);
  }
}
