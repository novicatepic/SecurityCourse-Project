import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ManageCommentsService } from './manage-comments.service';
import { JwtTokenService } from '../jwt-token/jwt-token.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { Comment } from '../model/Comment';

@Component({
  selector: 'app-manage-comments',
  templateUrl: './manage-comments.component.html',
  styleUrl: './manage-comments.component.css'
})
export class ManageCommentsComponent {
  comments : Array<Comment> = new Array();

  constructor(
    private router: Router,
    private service: ManageCommentsService,
    private jwtService: JwtTokenService,
    private snackBarService: SnackBarService) {

      this.readData();

  }

  readData() {
    this.service.getComments().subscribe((data) => {
      console.log(JSON.stringify(data));
      this.comments = data;
    },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Couldn't get data!");
      });
  }

  terminateComment(comment: Comment) {

    comment.enabled=false;
    comment.forbidden=true;
    comment.writer = undefined;

    this.service.terminateComment(comment).subscribe((data) => {
      console.log(JSON.stringify(data));
      this.snackBarService.triggerSnackBar("Comment terminated!");
    },
      error => {
        console.log("ERROR " + JSON.stringify(error));
        this.snackBarService.triggerSnackBar("Error!");
      })

  }

}
