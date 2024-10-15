package tech.ec2dlt.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	    public List<Post> getAllPosts() {
	        return postRepository.findAll();
	    }

	    // Get post by id
	    @GetMapping("/{id}")
	    public Optional<Post> getPostById(@PathVariable Long id) {
	        return postRepository.findById(id);
	    }

	    // Create a new post
	    @PostMapping
	    public Post createPost(@RequestBody Post post) {
	        return postRepository.save(post);
	    }

	    // Update a post
	    @PutMapping("/{id}")
	    public Post updatePost(@PathVariable Long id, @RequestBody Post updatedPost) {
	        return postRepository.findById(id)
	                .map(post -> {
	                    post.setTitle(updatedPost.getTitle());
	                    post.setDate(updatedPost.getDate());
	                    return postRepository.save(post);
	                }).orElseThrow(() -> new RuntimeException("Post not found with id " + id));
	    }

	    // Delete a post
	    @DeleteMapping("/{id}")
	    public void deletePost(@PathVariable Long id) {
	        postRepository.deleteById(id);
	    }
	}
