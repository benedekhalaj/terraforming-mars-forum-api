package com.codecool.terraformingmarsforum.controller;

import com.codecool.terraformingmarsforum.model.AppUser;
import com.codecool.terraformingmarsforum.model.UserPost;
import com.codecool.terraformingmarsforum.service.UserPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPostControllerTest {

    @Mock
    private UserPostService userPostService;
    private UserPostController userPostController;

    @BeforeEach
    public void init() {
        userPostController = new UserPostController(userPostService);
    }

    private List<UserPost> getUserPosts() {
        List<UserPost> userPosts = new ArrayList<>();
        userPosts.add(UserPost.builder().id(1L).build());
        userPosts.add(UserPost.builder().id(2L).build());
        return userPosts;
    }

    private UserPostController.CreateUserPostRequest getCreateUserPostRequest() {
        return UserPostController.CreateUserPostRequest
                .builder().userId(1L).description("test description").build();
    }

    private UserPostController.UpdateUserPostRequest getUpdateUserPostRequest() {
        return UserPostController.UpdateUserPostRequest
                .builder().description("test description").build();
    }

    @Test
    public void getAllUserPosts_HasAllUserPostsInRequestBody() {
        List<UserPost> expected = getUserPosts();
        when(userPostService.getAllUserPosts()).thenReturn(expected);
        List<UserPost> actual = userPostController.getAllUserPosts().getBody();
        assertEquals(expected, actual);
    }

    @Test
    public void getAllUserPosts_HasStatus200() {
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = userPostController.getAllUserPosts().getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void convertToUserPost_ConvertingCreateUserPostRequestToUserPost_CreatesUserPostWithSameFields() {
        Long userId = 1L;
        AppUser user = AppUser.builder().id(userId).build();
        String description = "lorem ipsum";
        UserPost expected = UserPost.builder().user(user).description(description).build();
        UserPostController.CreateUserPostRequest createUserPostRequest = UserPostController.CreateUserPostRequest
                .builder().
                userId(userId).
                description(description).
                build();
        UserPost actual = createUserPostRequest.convertToUserPost();
        assertEquals(expected.getUser(), actual.getUser());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    public void createUserPost_CreatingUserPost_HasStatus201() {
        UserPostController.CreateUserPostRequest createUserPostRequest = getCreateUserPostRequest();
        UserPost userPost = createUserPostRequest.convertToUserPost();

        when(userPostService.addUserPost(any(UserPost.class))).thenReturn(userPost);

        HttpStatus expected = HttpStatus.CREATED;
        HttpStatus actual = userPostController.createUserPost(createUserPostRequest).getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void createUserPost_CreatingUserPost_HasLocation() {
        Long userPostId = 1L;
        UserPostController.CreateUserPostRequest createUserPostRequest = getCreateUserPostRequest();
        UserPost userPost = createUserPostRequest.convertToUserPost();
        userPost.setId(userPostId);

        when(userPostService.addUserPost(any(UserPost.class))).thenReturn(userPost);

        URI expected = URI.create("/api/user-posts/%s".formatted(userPostId));
        URI actual = userPostController.createUserPost(createUserPostRequest).getHeaders().getLocation();
        assertEquals(expected, actual);
    }

    @Test
    public void updateUserPost_UpdatingUserPost_HasStatus204() {
        HttpStatus expected = HttpStatus.NO_CONTENT;
        HttpStatus actual = userPostController.updateUserPost(1L, getUpdateUserPostRequest()).getStatusCode();

        assertEquals(expected, actual);
    }

    @Test
    public void deleteUserPost_DeletingUserPost_HasStatus204() {
        HttpStatus expected = HttpStatus.NO_CONTENT;
        HttpStatus actual = userPostController.deleteUserPost(1L).getStatusCode();

        assertEquals(expected, actual);
    }

    @Test
    public void getUserPostById_GetUserPostById_HasUserPostInResponseBody() {
        Long id = 1L;
        UserPost expected = UserPost.builder().id(id).build();

        when(userPostService.getUserPostById(id)).thenReturn(expected);

        UserPost actual = userPostController.getUserPostById(id).getBody();

        assertEquals(expected, actual);
    }
}
