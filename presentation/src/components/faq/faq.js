import BaseComponent from '../base-component';

export default class FAQ extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.init = FAQ.init;
  }

  static init() {
    console.log(`FAQ component init(): + ${this.elm}`);
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-faq').forEach(elm => {
      const ins = new FAQ(elm);
      ins.init();
      console.log('FAQ');
    });
  }

}
