import Component from './language-toggle';
import '../base-test';

describe('language toggle component', () => {
  beforeEach(() => {
    global.component = new Component();
  });
  it('should be accessible', () => {
    expect(global.component).toBeDefined();
  });
});
