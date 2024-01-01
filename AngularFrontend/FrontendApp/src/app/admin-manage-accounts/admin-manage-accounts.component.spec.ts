import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManageAccountsComponent } from './admin-manage-accounts.component';

describe('AdminManageAccountsComponent', () => {
  let component: AdminManageAccountsComponent;
  let fixture: ComponentFixture<AdminManageAccountsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminManageAccountsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminManageAccountsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
