import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post } from 'src/app/post';
import { PostService } from '../post.service';
import { PromiseError } from 'src/app/promise-error';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css']
})
export class PostDetailComponent implements OnInit {
  post: Post
  id: number
  error: any;
  public constructor(public activatedRoute: ActivatedRoute, private postSrv: PostService) {
    this.id = this.activatedRoute.snapshot.params["id"];
  }

  ngOnInit(): void {
    this.getPostDetail(this.id)
  }

  getPostDetail(id: number) {
    this.postSrv.findOneById(id).then((data: any) => {
      this.post = data
    }).catch((err: PromiseError) => {
      if (err.validationError) {
        this.error = err.data;
      }
    })
  }

  update(){
    this.postSrv.update(this.post).then((data: any) => {
      this.post = data
      this.postSrv.http.toastr.info("le poste a été mis a jour !")
    }).catch((err: PromiseError) => {
      if (err.validationError) {
        this.error = err.data;
      }
    })
  }
}
