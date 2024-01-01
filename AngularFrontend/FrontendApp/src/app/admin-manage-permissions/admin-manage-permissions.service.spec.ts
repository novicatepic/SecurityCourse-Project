import { TestBed } from '@angular/core/testing';

import { AdminManagePermissionsService } from './admin-manage-permissions.service';

describe('AdminManagePermissionsService', () => {
  let service: AdminManagePermissionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminManagePermissionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
