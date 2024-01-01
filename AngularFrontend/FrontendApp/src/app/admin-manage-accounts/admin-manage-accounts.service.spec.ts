import { TestBed } from '@angular/core/testing';

import { AdminManageAccountsService } from './admin-manage-accounts.service';

describe('AdminManageAccountsService', () => {
  let service: AdminManageAccountsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminManageAccountsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
