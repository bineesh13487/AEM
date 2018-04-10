import Component from './mcd-global-footer';
import '../base-test';

describe('mcd-global-footer component', () => {
  beforeEach(() => {
    global.component = new Component();
  });
  it('should be accessible', () => {
    expect(global.component).toBeDefined();
  });
});
