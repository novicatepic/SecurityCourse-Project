import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ManageCommentsService } from './manage-comments.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { Comment } from '../model/Comment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CommentModificationService } from '../comment-modification/comment-modification.service';

@Component({
  selector: 'app-manage-comments',
  templateUrl: './manage-comments.component.html',
  styleUrl: './manage-comments.component.css'
})
export class ManageCommentsComponent {
  comments : Array<Comment> = new Array();
  toAllow?: Comment;
  userId: any;
  public firstForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private service: ManageCommentsService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService,
    private commentModificationService: CommentModificationService) {
      var temp = this.jwtService.extractTokenInfo();
      this.userId = temp.id;
      this.readData();

      this.firstForm = formBuilder.group({
        title: [null, [Validators.required, Validators.maxLength(45)]],
        content: [null, [Validators.required, Validators.maxLength(2000)]]
      });

  }

  readData() {
    this.service.getComments(this.userId).subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.comments = data;
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }



  terminateComment(comment: Comment) {

    comment.enabled=false;
    comment.forbidden=true;
    comment.writer = undefined;

    this.service.terminateComment(comment).subscribe((data) => {
      //console.log(JSON.stringify(data));
      this.snackBarService.triggerSnackBar("Comment terminated!");
      location.reload();
    },
      error => {
        //console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Error, your token is possibly not valid anymore!");
      })

  }

  updateCurrentComment(comment: Comment) {
    this.toAllow = comment;
    this.firstForm.get("title")?.setValue(comment.title);
    this.firstForm.get("content")?.setValue(comment.content);
  }

  updateComment() {
    document.getElementById("btnclose")?.click();
    if (this.firstForm.valid && this.toAllow) {

      this.toAllow.title = this.firstForm.get("title")?.value;
      this.toAllow.content = this.firstForm.get("content")?.value;

      this.toAllow.enabled = true;
      this.toAllow.forbidden = false;
      this.toAllow.writer = undefined;
      //console.log(JSON.stringify(this.toAllow));

      this.commentModificationService.allowComment(this.toAllow).subscribe((data) => {
        //console.log(JSON.stringify(data));
        this.snackBarService.triggerSnackBar("Comment activated!");
        location.reload();
      },
        error => {
          //console.log("ERROR " + JSON.stringify(error));
          this.snackBarService.triggerSnackBar("Comment not activated, your token is possibly not valid anymore!");
        })
    }



  }

}
