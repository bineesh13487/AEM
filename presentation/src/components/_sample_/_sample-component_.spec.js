import Component from './_sample-component_';
import '../base-test';

describe('sample component', () => {
  beforeEach(() => {
    global.component = new Component();
  });

  it('should be defined', () => {

    expect(global.component).toBeDefined();

  });

});
