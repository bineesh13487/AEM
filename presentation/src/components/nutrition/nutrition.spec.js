import BaseComponent from '../base-component';


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
    document.querySelectorAll('.nutrition').forEach(elm => {
      const instance = new Nutrition(elm);
      instance.init();
    });
  }

  static togglePanel(elm) {
    const panel = elm.querySelectorAll('[data-toggle]')[0];
    if (!panel.querySelector('.collapsed')) {
      console.log('make the caret point up');
    }

    if (panel.querySelector('.collapsed')) {
      console.log('make the caret point down');
    }
  }
}
