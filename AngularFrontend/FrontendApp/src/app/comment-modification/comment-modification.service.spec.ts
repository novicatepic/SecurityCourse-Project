import { TestBed } from '@angular/core/testing';

import { CommentModificationService } from './comment-modification.service';

describe('CommentModificationService', () => {
  let service: CommentModificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommentModificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
