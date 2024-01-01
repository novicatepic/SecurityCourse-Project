import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowForumComponent } from './show-forum.component';

describe('ShowForumComponent', () => {
  let component: ShowForumComponent;
  let fixture: ComponentFixture<ShowForumComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowForumComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ShowForumComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
