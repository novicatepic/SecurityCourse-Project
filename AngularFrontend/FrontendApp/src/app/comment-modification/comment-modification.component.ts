import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CommentModificationService } from './comment-modification.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { Comment } from '../model/Comment';

@Component({
  selector: 'app-comment-modification',
  templateUrl: './comment-modification.component.html',
  styleUrl: './comment-modification.component.css'
})
export class CommentModificationComponent {

  public firstForm: FormGroup;

  comment?: Comment;
  id: any;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private service: CommentModificationService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService,
    private route: ActivatedRoute) {

    this.route.paramMap.subscribe(params => {
      this.id = params.get('commentId');
    });

    this.readData();

    this.firstForm = formBuilder.group({
      title: [null, [Validators.required, Validators.maxLength(45)]],
      content: [null, [Validators.required, Validators.maxLength(2000)]]
    });

  }

  readData() {

    this.service.getComment(this.id).subscribe((data) => {
      this.comment = data;
      this.firstForm.get("title")?.setValue(this.comment?.title);
      this.firstForm.get("content")?.setValue(this.comment?.content);
    },
      error => {
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }

  updateComment(comment: any) {

    if (this.firstForm.valid) {

      comment.title = this.firstForm.get("title")?.value;
      comment.content = this.firstForm.get("content")?.value;

      comment.enabled = true;
      comment.forbidden = false;
      comment.writer = undefined;
      console.log(JSON.stringify(comment));

      this.service.allowComment(comment).subscribe((data) => {
        console.log(JSON.stringify(data));
        this.snackBarService.triggerSnackBar("Comment activated!");
      },
        error => {
          console.log("ERROR " + JSON.stringify(error));
          this.snackBarService.triggerSnackBar("Comment not activated!");
        })
    }



  }
}


