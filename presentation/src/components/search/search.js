import BaseComponent from '../base-component';

import { addClass, removeClass } from './../../assets/scripts/global/global-fn';

export default class Search extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    this.componentName = 'Search';
    this.widget = $('[data-widget="accessible-autocomplete"]');
    this.input = this.widget.find('#search');
    this.init = () => {
      this.elm.querySelector('.mcd-search__input--main').addEventListener('keyup', Search.changeInput.bind(this), false);
      this.elm.querySelector('.mcd-search__input--close-btn').addEventListener('click', Search.clearSearchedText.bind(this), false);
      this.elm.querySelector('.mcd-search__suggestion--section').addEventListener('click', Search.selectPredictedText.bind(this), false);
    };
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-search').forEach(elm => {
      const instance = new Search(elm);
      instance.init();
    });
  }

  static matchItems(input) {
    const reg = new RegExp(input.split('').join('\\w*').replace(/\W/, ''), 'i');
    const items = ['Chicken McNuggets',
      'Artisan Grilled Chicken Sandwich',
      'Buttermilk Crispy Chicken Sandwich',
      'Chicken Tenders',
      'McChicken'];
    return items.filter(item => {
      if (item.match(reg)) {
        return item;
      }
      return '';
    });
  }

  static changeInput(event) {
    if ([13, 38, 40].indexOf(event.keyCode) === -1) {
      if (event.target.value.length > 0) {
        const autoCompleteResult = Search.matchItems(event.target.value);
        let predictedData = '';
        if (autoCompleteResult.length > 0) {
          addClass(this.elm.querySelector('.mcd-search__no-item'), 'hide');
          for (let i = 0; i < autoCompleteResult.length;) {
            predictedData += `<li class="mcd-search__suggestion--predicted-text autocomplete-item" role="option" aria-selected="false" tabindex="-1">${autoCompleteResult[i]}</li>`;
            i += 1;
          }
          const [backgroundInputData] = autoCompleteResult;
          this.elm.querySelector('.mcd-search__input--background').value = event.target.value + backgroundInputData.substring(event.target.value.length);
        }
        else {
          removeClass(this.elm.querySelector('.mcd-search__no-item'), 'hide');
          this.elm.querySelector('.mcd-search__input--background').value = '';
        }
        this.elm.querySelector('.mcd-search__suggestion--section').innerHTML = predictedData;
        removeClass(this.elm.querySelector('.mcd-search__input--close-btn'), 'hide');
        removeClass(this.elm.querySelector('.mcd-search__suggestion'), 'hide');
      }
      else {
        this.elm.querySelector('.mcd-search__input--background').value = '';
        addClass(this.elm.querySelector('.mcd-search__no-item'), 'hide');
        addClass(this.elm.querySelector('.mcd-search__input--close-btn'), 'hide');
        addClass(this.elm.querySelector('.mcd-search__suggestion'), 'hide');
      }
    }
    else {
      const $listItems = $('.mcd-search__suggestion--section li');
      const key = event.keyCode;
      const $selected = $listItems.filter('.selected');
      let currentElm;

      $listItems.removeClass('selected');

      if (key === 40) {
        if (!$selected.length || $selected.is(':last-child')) {
          currentElm = $listItems.eq(0);
        }
        else {
          currentElm = $selected.next();
        }
      }
      else if (key === 38) {
        if (!$selected.length || $selected.is(':first-child')) {
          currentElm = $listItems.last();
        }
        else {
          currentElm = $selected.prev();
        }
      }
      if (currentElm && currentElm.length > 0) {
        Search.clearSelected(this);
        currentElm.addClass('selected');
        currentElm.attr('aria-selected', 'true').attr('id', 'selectedOption');
        this.input.attr('aria-activedescendant', 'selectedOption');
        this.input.attr('aria-expanded', 'true');
        this.elm.querySelector('.mcd-search__input--main').value = currentElm[0].innerText;
        this.elm.querySelector('.mcd-search__input--background').value = '';
      }
      if (key === 13) {
        addClass(this.elm.querySelector('.mcd-search__suggestion'), 'hide');
      }
    }
  }

  static clearSelected(self) {
    const results = self.widget.find('#results');
    self.input.attr('aria-activedescendant', '');
    results.find('[aria-selected="true"]').attr('aria-selected', 'false').attr('id', '');
  }

  static clearSearchedText() {
    this.elm.querySelector('.mcd-search__input--main').value = '';
    this.elm.querySelector('.mcd-search__input--background').value = '';
    addClass(this.elm.querySelector('.mcd-search__no-item'), 'hide');
    addClass(this.elm.querySelector('.mcd-search__input--close-btn'), 'hide');
    addClass(this.elm.querySelector('.mcd-search__suggestion'), 'hide');
    this.input.attr('aria-expanded', 'true');
  }

  static selectPredictedText(event) {
    this.elm.querySelector('.mcd-search__input--main').value = event.target.innerText;
    this.elm.querySelector('.mcd-search__input--background').value = '';
    addClass(this.elm.querySelector('.mcd-search__suggestion'), 'hide');
    this.input.attr('aria-expanded', 'true');
  }
}
