package tech.ec2dlt.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.ec2dlt.backend.ApiResponse;
import tech.ec2dlt.backend.entity.Post;
import tech.ec2dlt.backend.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
	    @Autowired
	    private PostRepository postRepository;

	    // Get all posts
	    @GetMapping
		public ResponseEntity<ApiResponse<List<Post>>> getAllPosts() {
				List<Post> posts = postRepository.findAll();

				ApiResponse<List<Post>> apiResponse = new ApiResponse<>(false, posts);

				return ResponseEntity.ok(apiResponse);
			}

	    // Get post by id
	    @GetMapping("/{id}")
	    public ResponseEntity<ApiResponse<Post>> getPostById(@PathVariable Long id) {
			Optional<Post> post = postRepository.findById(id);
			ApiResponse<?> apiResponse = new ApiResponse<>(false, post);

			return ResponseEntity.status(HttpStatus.OK).body((ApiResponse<Post>) apiResponse);
	    }

	    // Create a new post
	    @PostMapping
	    public Post createPost(@RequestBody Post post) {
	        return postRepository.save(post);
	    }

	    // Update a post
	    @PutMapping("/{id}")
	    public ResponseEntity<ApiResponse<Post>> updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
			Post post = postRepository.findById(id)
	                .map(postUpdate -> {
						postUpdate.setTitle(updatedPost.getTitle());
						postUpdate.setDescription(updatedPost.getDescription());
	                    return postRepository.save(postUpdate);
	                }).orElseThrow(() -> new RuntimeException("Post not found with id " + id));

			ApiResponse<?> apiResponse = new ApiResponse<>(false, post);

			return ResponseEntity.status(HttpStatus.OK).body((ApiResponse<Post>) apiResponse);
	    }

	    // Delete a post
	    @DeleteMapping("/{id}")
	    public ResponseEntity<ApiResponse<Post>> deletePost(@PathVariable Long id) {
			Optional<Post> post = postRepository.findById(id);
			ApiResponse<?> apiResponse = new ApiResponse<>(false, post);
			postRepository.deleteById(id);

			return ResponseEntity.status(HttpStatus.OK).body((ApiResponse<Post>) apiResponse);
	    }
	}
