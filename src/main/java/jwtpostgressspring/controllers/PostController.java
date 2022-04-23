package jwtpostgressspring.controllers;


import jwtpostgressspring.models.Post;
import jwtpostgressspring.models.User;
import jwtpostgressspring.payload.request.AddPostRequest;
import jwtpostgressspring.payload.response.MessageResponse;
import jwtpostgressspring.repository.PostRepository;
import jwtpostgressspring.repository.RoleRepository;
import jwtpostgressspring.repository.UserRepository;
import jwtpostgressspring.security.jwt.JwtUtils;
import jwtpostgressspring.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/post")
public class PostController {

    Logger logger = LoggerFactory.getLogger(PostController.class);


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    public List<Post> getAllPost() {
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    @GetMapping("/user/{id}")
    public List<Post> retriveAllUsersPosts(@PathVariable Long id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new Exception("id: " + id);
        }
        return userOptional.get().getPosts();
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@Valid @RequestBody AddPostRequest addPostRequest, Authentication authentication) {

        Long user_id = ((UserDetailsImpl) authentication.getPrincipal()).getId();

        User user = userRepository.getById(user_id);

        Post post = new Post(addPostRequest.getContent());
        post.setUser(user);

        postRepository.save(post);

        return ResponseEntity.ok(new MessageResponse("Post added successfully!"));
    }


}
