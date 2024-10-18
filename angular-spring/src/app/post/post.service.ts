import { Injectable } from '@angular/core';
import { Post } from '../post';
import { Ec2dltHttpService } from '../ec2dlt-http-service';
import { BaseService } from '../base.service';

@Injectable({
  providedIn: 'root'
})
export class PostService extends BaseService<Post> {

  constructor(public override http: Ec2dltHttpService) {
    super(http);
    this.prefix = 'posts';
  }
}
