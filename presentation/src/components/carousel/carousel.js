import BaseComponent from '../base-component';

export default class Carousel extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = () => {
      this.elm.querySelector('.component-carousel');
    };
  }

  static bootstrap() {
    console.log('is this bootstrap working?');
    document.querySelectorAll('.component-carousel').forEach(elm => {
      const instance = new Carousel(elm);
      instance.init();
    });
  }
}
