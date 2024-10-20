package tech.ec2dlt.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.ec2dlt.backend.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
	
}