import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/post';
import { PostService } from '../post.service';
import { PromiseError } from 'src/app/promise-error';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit {
  posts: Post[] = []
  error: any
  public constructor(public postSrv: PostService) { }
  ngOnInit(): void {
    this.getAllPost()
  }

  getAllPost(): void {
    this.postSrv.findAll().then((data: any) => {
      this.posts = data
    }).catch((err: PromiseError) => {
      if (err.validationError) {
        this.error = err.data;
      }
    })
  }

  delete(post: Post) {
    this.postSrv.removeById(post.id).then((data: any) => {
      const index = this.posts.indexOf(post)
      if (index > -1) {
        this.posts.splice(index, 1);
        this.getAllPost()
        this.postSrv.http.toastr.info("Le " + post.title + " a été supprimé")
      }
    }).catch((err: PromiseError) => {
      if (err.validationError) {
        this.error = err.data;
      }
    })
  }
}
