import { TestBed } from '@angular/core/testing';

import { AdminManageSingleAccountService } from './admin-manage-single-account.service';

describe('AdminManageSingleAccountService', () => {
  let service: AdminManageSingleAccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminManageSingleAccountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
