import { TestBed } from '@angular/core/testing';

import { ManageCommentsService } from './manage-comments.service';

describe('ManageCommentsService', () => {
  let service: ManageCommentsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ManageCommentsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
