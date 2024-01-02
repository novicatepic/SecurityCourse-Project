import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentModificationComponent } from './comment-modification.component';

describe('CommentModificationComponent', () => {
  let component: CommentModificationComponent;
  let fixture: ComponentFixture<CommentModificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommentModificationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommentModificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
