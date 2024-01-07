import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShowForumService } from './show-forum.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { Comment } from '../model/Comment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NewCommentService } from '../new-comment/new-comment.service';
import { Room } from '../model/Room';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { UserPermissionsRoom } from '../model/UserPermissionsRoom';
import { User } from '../model/User';

@Component({
  selector: 'app-show-forum',
  templateUrl: './show-forum.component.html',
  styleUrl: './show-forum.component.css'
})
export class ShowForumComponent {
  comments: Array<Comment> = new Array();
  room?: Room;
  roomId: any;
  public firstForm: FormGroup;
  public updateForm: FormGroup;
  toUpdate?: Comment;

  permissions : UserPermissionsRoom = new UserPermissionsRoom();

  user: any;

  pageSize = 20; 
  currentPage = 1; 
  pages: number[] = [];

  constructor( 
    private formBuilder: FormBuilder,
     private router: Router,
     private service: ShowForumService,
     private commentService: NewCommentService,
     private snackBarService: SnackBarService,
     private jwtService: JwtTokenService,
     private route: ActivatedRoute) {
      this.route.paramMap.subscribe(params => {
        this.roomId = params.get('id');
      });
      this.loadRoomInfo();
      this.loadData();
      this.jwtService.getUserById().subscribe((data: any) => {
        this.user = data;

        this.service.loadPermissionsForForum(this.roomId, this.user.id).subscribe((permissionsUser) => {
          this.permissions = permissionsUser;
        },
        error => {
          this.snackBarService.triggerSnackBar("Error loading permissions!");
        });

     });
     
      

      this.firstForm = formBuilder.group({
        title: [null, [Validators.required, Validators.maxLength(45)]],
        content: [null, [Validators.required, Validators.maxLength(2000)]]
      });

      this.updateForm = formBuilder.group({
        updateTitle: [null, [Validators.required, Validators.maxLength(45)]],
        updateContent: [null, [Validators.required, Validators.maxLength(2000)]]
      });
  }

  async updatePages() {
    const pageCount = Math.ceil(this.comments.length / this.pageSize);
    this.pages = Array.from({ length: pageCount }, (_, index) => index + 1);
  }

  setPage(page: number) {
    if (page >= 1 && page <= this.pages.length) {
      this.currentPage = page;
    }
  }

  loadData() {

      this.service.loadComments(this.roomId).subscribe(async (data) => {
        this.comments = data;
        await this.updatePages();
      },
      error => {
        this.snackBarService.triggerSnackBar("Error creating user!");
      });

  }

  loadRoomInfo() {
    this.service.loadRoomInfo(this.roomId).subscribe((data) => {
      this.room = data;
    },
    error => {
      this.snackBarService.triggerSnackBar("404");
    })
  }

  getCurrentPageData(): any[] {
    const startIndex = (this.currentPage - 1) * this.pageSize;
    const endIndex = startIndex + this.pageSize;
    return this.comments.slice(startIndex, endIndex);
  }

  addComment() {
    if(this.firstForm.valid) {

      const comment = new Comment(this.firstForm.get("title")?.value, 
      this.firstForm.get("content")?.value, this.roomId, this.user.id, false, false, new Date());

      this.commentService.saveComment(comment).subscribe((data) => {
        this.snackBarService.triggerSnackBar("Saved message!");
        this.router.navigate(['/room'+this.roomId]);
      }, (err) => {
        this.snackBarService.triggerSnackBar("Error saving message!");
      })

    }
  }

  toggleUpdate(comment:Comment) {
    this.toUpdate = comment;
    this.updateForm.get("updateTitle")?.setValue(comment.title);
    this.updateForm.get("updateContent")?.setValue(comment.content);
  }

  updateComment() {
    if(this.toUpdate != undefined && this.updateForm.valid) {
      this.toUpdate.title = this.updateForm.get("updateTitle")?.value;
      this.toUpdate.content = this.updateForm.get("updateContent")?.value;
      this.toUpdate.writer = undefined;
      this.commentService.updateComment(this.toUpdate).subscribe((data) => {
        this.snackBarService.triggerSnackBar("Updated comment!");
        this.router.navigate(['/room'+this.roomId]);
      }, (err) => {
        this.snackBarService.triggerSnackBar("Error updating comment!");
      })

    }
    

  }

  deleteComment(comment: Comment) {
      this.commentService.deleteComment(comment.id, this.user.id, this.roomId).subscribe((data) => {
        this.snackBarService.triggerSnackBar("Deleted comment!");
        this.router.navigate(['/room'+this.roomId]);
      }, (err) => {
        this.snackBarService.triggerSnackBar("Error deleting comment!");
      });
  }

}
