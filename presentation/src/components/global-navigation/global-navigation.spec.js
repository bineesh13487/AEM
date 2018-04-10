import Component from './global-navigation';
import '../base-test';

describe('global navigation component', () => {
  beforeEach(() => {
    global.component = new Component();
  });

  it('should be accessible', () => {

    expect(global.component).toBeDefined();

  });

});
