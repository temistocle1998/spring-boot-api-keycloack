import { Component } from '@angular/core';
import { Post } from 'src/app/post';
import { PostService } from '../post.service';
import { PromiseError } from 'src/app/promise-error';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent {
  postModel = new Post
  error: any;

  public constructor(private postSrv: PostService){}

  save(){
    this.postSrv.save(this.postModel).then((data: any) => {
      this.postSrv.http.toastr.success("Le poste a été crée !")
      this.postModel = new Post
    }).catch((err: PromiseError) => {
      if (err.validationError) {
        this.error = err.data;
      }
    })
  }
}
