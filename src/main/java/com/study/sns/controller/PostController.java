package com.study.sns.controller;

import com.study.sns.controller.request.PostCreateRequest;
import com.study.sns.controller.request.PostModifyRequest;
import com.study.sns.controller.response.PostResponse;
import com.study.sns.controller.response.Response;
import com.study.sns.model.Post;
import com.study.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable("postId") Long postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);
        return Response.success(PostResponse.fromPost(post));
    }

}
