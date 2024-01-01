import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManageSingleAccountComponent } from './admin-manage-single-account.component';

describe('AdminManageSingleAccountComponent', () => {
  let component: AdminManageSingleAccountComponent;
  let fixture: ComponentFixture<AdminManageSingleAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminManageSingleAccountComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminManageSingleAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
