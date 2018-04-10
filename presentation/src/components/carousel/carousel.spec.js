import Component from './carousel';
import '../base-test';

describe('carousel component', () => {
  beforeEach(() => {
    global.component = new Component();
  });

  it('should be defined', () => {

    expect(global.component).toBeDefined();

  });

});
