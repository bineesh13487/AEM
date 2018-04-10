import Component from './button';
import './../../base-test';

describe('button component', () => {
  beforeEach(() => {
    global.component = new Component();
  });

  it('should be accessible', () => {

    expect(global.component).toBeDefined();

  });

});
