import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShowForumService } from './show-forum.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { Comment } from '../model/Comment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NewCommentService } from '../new-comment/new-comment.service';

@Component({
  selector: 'app-show-forum',
  templateUrl: './show-forum.component.html',
  styleUrl: './show-forum.component.css'
})
export class ShowForumComponent {
  comments: Array<Comment> = new Array();
  roomId: any;
  public firstForm: FormGroup;
  public updateForm: FormGroup;
  toUpdate?: Comment;


  constructor( 
    private formBuilder: FormBuilder,
     private router: Router,
     private service: ShowForumService,
     private commentService: NewCommentService,
     private snackBarService: SnackBarService,
     private route: ActivatedRoute) {
      this.route.paramMap.subscribe(params => {
        this.roomId = params.get('id');
      });
      this.loadData();

      this.firstForm = formBuilder.group({
        title: [null, [Validators.required, Validators.maxLength(45)]],
        content: [null, [Validators.required, Validators.maxLength(2000)]]
      });

      this.updateForm = formBuilder.group({
        updateTitle: [null, [Validators.required, Validators.maxLength(45)]],
        updateContent: [null, [Validators.required, Validators.maxLength(2000)]]
      });
  }

  loadData() {

      this.service.loadData(this.roomId).subscribe((data) => {
        console.log(data);
        this.comments = data;
        console.log(this.comments);
      },
      error => {
        console.log(error);
        this.snackBarService.triggerSnackBar("Error creating user!");
      });

  }

  addComment() {
    if(this.firstForm.valid) {

      const comment = new Comment(this.firstForm.get("title")?.value, 
      this.firstForm.get("content")?.value, this.roomId, 3, false, false, new Date());

      this.commentService.saveComment(comment).subscribe((data) => {
        this.snackBarService.triggerSnackBar("Saved message!");
      }, (err) => {
        this.snackBarService.triggerSnackBar("Error saving message!");
        console.log(err);
      })

    }
  }

  toggleUpdate(comment:Comment) {
    this.updateForm.get("updateTitle")?.setValue(comment.title);
    this.updateForm.get("updateContent")?.setValue(comment.content);
    console.log("toupdt " + JSON.stringify(comment));
  }

  updateComment() {


    this.toUpdate = undefined;
  }

  deleteComment(comment: Comment) {

  }

}
