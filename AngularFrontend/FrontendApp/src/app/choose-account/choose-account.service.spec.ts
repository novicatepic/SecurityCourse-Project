import { TestBed } from '@angular/core/testing';

import { ChooseAccountService } from './choose-account.service';

describe('ChooseAccountService', () => {
  let service: ChooseAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChooseAccountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
