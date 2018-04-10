import Component from './legal-disclaimer';
import '../base-test';

describe('legal disclaimer component', () => {
  beforeEach(() => {
    global.component = new Component();
  });

  it('should be accessible', () => {

    expect(global.component).toBeDefined();

  });

});
