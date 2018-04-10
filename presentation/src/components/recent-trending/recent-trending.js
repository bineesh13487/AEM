import BaseComponent from '../base-component';
import { hasClass, addClass, removeClass, captureTouchEvents, getCssStyle,
  getCssStyleInNumber, isMobileViewport, getAttributeAsInt,
  getNextVisibleSibling, getPrevVisibleSibling, isElementVisible } from './../../assets/scripts/global/global-fn';

export default class RecentTrendingNews extends BaseComponent {
  constructor(_elm) {
    super();
    this.elm = _elm;
    const proto = RecentTrendingNews.prototype;
    this.init = proto.init.bind(this);
    this.attachEvents = proto.attachEvents.bind(this);
    this.scrollToSlide = proto.scrollToSlide.bind(this);
    this.handlePagination = proto.handlePagination.bind(this);
    this.handleSwipe = proto.handleSwipe.bind(this);
    this.scrollToNext = proto.scrollToNext.bind(this);
    this.scrollToPrevious = proto.scrollToPrevious.bind(this);
    this.highlightPaginationDotAt = proto.highlightPaginationDotAt.bind(this);
    this.resetToCurrentSlide = proto.resetToCurrentSlide.bind(this);
    this.updatePrevNextButtonState = proto.updatePrevNextButtonState.bind(this);
    this.handleSlideFocusIn = proto.handleSlideFocusIn.bind(this);
    this.handleSlideFocus = proto.handleSlideFocus.bind(this);
  }

  init() {
    console.log(`RecentTrendingNews component init(): + ${this.elm}`);
    this.paginationContainer = this.elm.querySelector('.mcd-recent-trending-news__pagination');
    this.slidesContainer = this.elm.querySelector('.mcd-recent-trending-news__slides');
    this.prevButton = this.elm.querySelector('.mcd-recent-trending-news__nextprev-btn--prev');
    this.nextButton = this.elm.querySelector('.mcd-recent-trending-news__nextprev-btn--next');
    this.updatePrevNextButtonState();
    this.attachEvents();
  }

  attachEvents() {
    this.paginationContainer.addEventListener('click', event => {
      this.handlePagination(event);
    });
    this.elm.querySelectorAll('.mcd-recent-trending-news__slide').forEach(slideElm => {
      captureTouchEvents(slideElm, (evt, dir, phase, swipeType) => {
        if (phase === 'end' && (swipeType === 'left' || swipeType === 'right')) {
          this.handleSwipe(swipeType);
        }
      });
    });
    this.elm.querySelectorAll('.mcd-news-item__article').forEach(articleElm => {
      articleElm.addEventListener('focusin', this.handleSlideFocusIn);
    });
    this.prevButton.addEventListener('click', this.scrollToPrevious);
    this.nextButton.addEventListener('click', this.scrollToNext);
    window.addEventListener('resize', this.resetToCurrentSlide);
    window.addEventListener('orientationchange', this.resetToCurrentSlide);
  }

  handlePagination(event) {
    if (event.target.hasAttribute('data-page-index')) {
      const index = event.target.getAttribute('data-page-index');
      const slide = this.elm.querySelector(`[data-slide-index="${index}"]`);
      if (slide) {
        this.scrollToSlide(slide);
      }
    }
  }

  handleSwipe(direction) {
    if (direction === 'left') {
      this.scrollToNext();
    }
    else {
      this.scrollToPrevious();
    }
  }

  handleSlideFocusIn(event) {
    console.log('focus');
    console.log(event.target);
    if (event.target) {
      const slide = event.target.parentElement;
      this.handleSlideFocusIn(slide);
    }
  }

  handleSlideFocus(slideElm) {
    let slide = slideElm;
    if (slide.hasAttribute('data-slide-index')) {
      let index = slide.getAttribute('data-slide-index');
      console.log(`index ${index}`);
      const page = this.elm.querySelector(`[data-page-index="${index}"]`);
      if (isElementVisible(page)
        && !hasClass(page, 'mcd-recent-trending-news__page--selected')) {
        this.scrollToSlide(slide);
      }
      else if (!isElementVisible(page)) {
        const prev = getPrevVisibleSibling(page);
        if (typeof prev !== 'undefined') {
          index = prev.getAttribute('data-page-index');
          slide = this.elm.querySelector(`[data-slide-index="${index}"]`);
          this.scrollToSlide(slide);
        }
      }
    }
  }

  scrollToNext() {
    const currentPage = this.elm.querySelector('.mcd-recent-trending-news__page--selected');
    const nextPage = getNextVisibleSibling(currentPage);
    if (typeof nextPage !== 'undefined') {
      const index = getAttributeAsInt(nextPage, 'data-page-index');
      const nextSlide = this.slidesContainer.querySelector(`[data-slide-index="${index}"]`);
      console.log(`index: ${index}`);
      if (nextSlide) {
        this.scrollToSlide(nextSlide);
      }
    }
    this.updatePrevNextButtonState();
  }

  scrollToPrevious() {
    const currentPage = this.elm.querySelector('.mcd-recent-trending-news__page--selected');
    const prevPage = getPrevVisibleSibling(currentPage);
    console.log(`prevPage ${typeof prevPage}`);
    if (typeof prevPage !== 'undefined') {
      const index = getAttributeAsInt(prevPage, 'data-page-index');
      const prevSlide = this.slidesContainer.querySelector(`[data-slide-index="${index}"]`);
      console.log(`index: ${index}`);
      if (prevSlide) {
        this.scrollToSlide(prevSlide);
      }
    }
    this.updatePrevNextButtonState();
  }

  scrollToSlide(slide) {
    const offset = slide.offsetLeft;
    const parent = this.slidesContainer;
    const index = getAttributeAsInt(slide, 'data-slide-index', -1);
    const margin = isMobileViewport() ? getCssStyleInNumber(slide, index === 0 ? 'margin-left' : 'margin-right', 0) : 0;
    if (index === -1) {
      return;
    }
    this.currentIndex = index;
    parent.style.transform = `translate(-${index > 0 ? (offset - margin) : 0}px)`;
    console.log(`sliding to ${index}`);
    this.highlightPaginationDotAt(index);
  }

  autoHighlightPaginationDot() {
    const parent = this.slidesContainer;
    let translateX = 0;
    try {
      translateX = Math.abs(getCssStyle(parent, 'transform').split(',')[4]);
    }
    catch (e) {
      console.error(e);
    }
    this.elm.querySelectorAll('.mcd-recent-trending-news__slide').forEach(slide => {
      console.log(`slide: ${translateX} - ${slide.offsetLeft} < ${getCssStyleInNumber(slide, 'margin-left')} + ${getCssStyleInNumber(slide, 'margin-right')}`);
      if (translateX - slide.offsetLeft < getCssStyleInNumber(slide, 'margin-left') + getCssStyleInNumber(slide, 'margin-right')) {
        this.highlightPaginationDotAt(slide.getAttribute('data-slide-index'));
      }
    });
  }

  resetToCurrentSlide() {
    if (typeof this.currentIndex === 'undefined') {
      this.currentIndex = 0;
    }
    let page = this.elm.querySelector(`[data-page-index="${this.currentIndex}"]`);
    while (typeof page !== 'undefined' &&
      getCssStyle(page, 'display') === 'none' &&
      this.currentIndex > 0) {
      this.currentIndex = this.currentIndex - 1;
      page = this.elm.querySelector(`[data-page-index="${this.currentIndex}"]`);
    }
    const slide = this.elm.querySelector(`[data-slide-index="${this.currentIndex}"]`);
    if (slide) {
      this.scrollToSlide(slide);
    }
  }

  highlightPaginationDotAt(index) {
    console.log(`highlight at ${index}`);
    const currentPage = this.elm.querySelector('.mcd-recent-trending-news__page--selected');
    removeClass(currentPage, 'mcd-recent-trending-news__page--selected');
    const page = this.elm.querySelector(`[data-page-index="${index}"]`);
    addClass(page, 'mcd-recent-trending-news__page--selected');
  }

  updatePrevNextButtonState() {
    const currentPage = this.elm.querySelector('.mcd-recent-trending-news__page--selected');
    if (typeof getPrevVisibleSibling(currentPage) === 'undefined') {
      this.prevButton.setAttribute('disabled', 'disabled');
    }
    else {
      this.prevButton.removeAttribute('disabled');
    }
    if (typeof getNextVisibleSibling(currentPage) === 'undefined') {
      this.nextButton.setAttribute('disabled', 'disabled');
    }
    else {
      this.nextButton.removeAttribute('disabled');
    }
  }

  static bootstrap() {
    document.querySelectorAll('.mcd-recent-trending-news').forEach(elm => {
      const ins = new RecentTrendingNews(elm);
      ins.init();
    });
  }

}
