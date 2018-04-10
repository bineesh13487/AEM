import Component from './microsite-navigation';
import '../base-test';

describe('microsite navigation component', () => {
  beforeEach(() => {
    global.component = new Component();
  });
  it('should be accessible', () => {
    expect(global.component).toBeDefined();
  });
});
