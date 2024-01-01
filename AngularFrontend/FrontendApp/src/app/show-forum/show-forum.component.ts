import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ShowForumService } from './show-forum.service';
import { SnackBarService } from '../snack-bar/snack-bar.service';
import { Comment } from '../model/Comment';

@Component({
  selector: 'app-show-forum',
  templateUrl: './show-forum.component.html',
  styleUrl: './show-forum.component.css'
})
export class ShowForumComponent {
  comments: Array<Comment> = new Array();
  roomId: any;
  constructor( 
     private router: Router,
     private service: ShowForumService,
     private snackService: SnackBarService,
     private route: ActivatedRoute) {
      this.route.paramMap.subscribe(params => {
        this.roomId = params.get('id');
      });
      this.loadData();
  }

  loadData() {

      this.service.loadData(this.roomId).subscribe((data) => {
        console.log(data);
        this.comments = data;
        console.log(this.comments);
      },
      error => {
        console.log(error);
        this.snackService.triggerSnackBar("Error creating user!");
      });

  }
}
