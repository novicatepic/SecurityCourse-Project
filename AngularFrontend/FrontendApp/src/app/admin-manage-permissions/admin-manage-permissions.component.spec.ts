import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminManagePermissionsComponent } from './admin-manage-permissions.component';

describe('AdminManagePermissionsComponent', () => {
  let component: AdminManagePermissionsComponent;
  let fixture: ComponentFixture<AdminManagePermissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminManagePermissionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminManagePermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
