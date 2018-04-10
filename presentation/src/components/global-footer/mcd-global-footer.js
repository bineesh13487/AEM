import BaseComponent from '../base-component';

export default class GlobalFooter extends BaseComponent {
  constructor() {
    super();
    this.time = new Date();
    this.componentName = 'Footer';
  }
}
