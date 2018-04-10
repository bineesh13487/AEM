import BaseComponent from '../base-component';
import { addClass, hasClass, removeClass } from './../../assets/scripts/global/global-fn';

export default class Filter extends BaseComponent {
  constructor(_elm) {
    super(_elm);
    this.elm = _elm;
    if (this.elm.querySelector('.mcd-filter__carousel__item')) {
      this.filter = {
        itemCount: this.elm.querySelectorAll('.mcd-filter__carousel__item').length,
        itemWidth: this.elm.querySelector('.mcd-filter__carousel__item').offsetWidth
      };
    }
    else {
      this.filter = {
        itemCount: 0,
        itemWidth: '100px'
      };
    }
    this.init = Filter.init.bind(this);
    this.setupCarousel = Filter.setupCarousel.bind(this);
  }

  static init() {
    this.elm.querySelector('.mcd-filter__carousel')
      .addEventListener('click', Filter.carousalItemClick.bind(this), false);
    this.setupCarousel();

    window.onresize = () => {
      this.setupCarousel();
    };
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-filter').forEach(elm => {
      const instance = new Filter(elm);
      instance.init();
    });
  }

  static setupCarousel() {
    for (let i = 0; i < this.filter.itemCount; i += 1) {
      this.elm.querySelectorAll('.mcd-filter__carousel__item')[i].style.width = `calc(100% / ${this.filter.itemCount})`;
    }
    this.filter.itemWidth = this.elm.querySelector('.mcd-filter__carousel__item').offsetWidth;
    this.elm.querySelector('.mcd-filter__horizontal-line').style.width = `${(this.filter.itemWidth * this.filter.itemCount) / 16}rem`;
    this.elm.querySelector('.mcd-filter__horizontal-line--selected').style.width = `${this.filter.itemWidth / 16}rem`;
  }

  static carousalItemClick(event) {
    const selectedClass = 'mcd-filter__carousel__item--selected';
    const elm = this.elm.querySelectorAll('.mcd-filter__carousel__item');
    for (let i = 0; i < elm.length; i += 1) {
      if (hasClass(elm[i], selectedClass)) {
        removeClass(elm[i], selectedClass);
        break;
      }
    }
    addClass(event.target, selectedClass);
    const selectedClassBottom = this.elm.querySelector('.mcd-filter__horizontal-line--selected');
    selectedClassBottom.style.transform = `translateX(${(parseInt(event.target.getAttribute('key'), 10) * document.querySelector('.mcd-filter__carousel__item').offsetWidth) / 16}rem)`;
  }
}
