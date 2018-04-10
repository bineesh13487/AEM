import Component from './mcd-footer-navigation';
import '../base-test';

describe('mcd-footer-navigation component', () => {
  beforeEach(() => {
    global.component = new Component();
  });
  it('should be accessible', () => {
    expect(global.component).toBeDefined();
  });
});
