import Component from './search';
import '../base-test';

describe('Search component', () => {
  beforeEach(() => {
    global.component = new Component();
  });

  it('should be accessible', () => {

    expect(global.component).toBeDefined();

  });

});
